package com.challenge.vote.service;

import com.challenge.vote.dto.PautaModelDTO;
import com.challenge.vote.dto.ResultPautaDTO;
import com.challenge.vote.dto.SessionDTO;
import com.challenge.vote.entity.PautaEntity;
import com.challenge.vote.entity.enumerator.StatusEnum;
import com.challenge.vote.entity.enumerator.VoteEnum;
import com.challenge.vote.external.queue.QueueSender;
import com.challenge.vote.repository.PautaRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PautaService {
    
    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private VoteService voteService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private QueueSender queueSender;

    public PautaModelDTO createNewPauta(Long duration) {
        duration = (duration != null && duration >= 1L) ? duration : 1L;
        return toPautaModelDTO(pautaRepository.save(PautaEntity.builder()
                .status(StatusEnum.ABERTA)
                .finishesAt(LocalDateTime.now().plusMinutes(duration))
                .build()));
    }

    public PautaEntity findPauta(Long pautaId) {
        return pautaRepository.findById(pautaId).orElseThrow(() -> new NotFoundException("Pauta não encontrada."));
    }

    public PautaModelDTO openNewSession(SessionDTO duration, Long pautaId) throws Exception {

        if(duration.getDuration() == null || duration.getDuration()  < 1){
            duration.setDuration(1L);
        }

        var pauta = findPauta(pautaId);
        pauta.setStatus(StatusEnum.ABERTA);
        pauta.setFinishesAt(LocalDateTime.now().plusMinutes(duration.getDuration()));

        var entity = pautaRepository.save(pauta);

        return toPautaModelDTO(entity);
    }

    public PautaEntity checkIfSessionIsAvailable(Long pautaId) throws Exception {
        var pauta = findPauta(pautaId);

        if(pauta.getStatus().equals(StatusEnum.ENCERRADA)){
            throw new Exception("Sessão já encerrada!");
        }

        return pauta;
    }

    public PautaEntity changeStatus(StatusEnum status, PautaEntity session){
        session.setStatus(status);
        return pautaRepository.save(session);
    }

    public ResultPautaDTO getVoteResult(Long pautaId) throws Exception {
        return ResultPautaDTO.builder()
                .result(getResultOfVoting(pautaId))
                .build();
    }

    private String getResultOfVoting(Long pautaId) {
        var votes = voteService.getAllVotesByPauta(pautaId);
        var simVotes = new ArrayList<>();
        var naoVotes = new ArrayList<>();
        votes.forEach(vote -> {
            if(vote.getVote().equals(VoteEnum.SIM)){
                simVotes.add(vote);
            }else{
                naoVotes.add(vote);
            }
        });
        return new StringBuilder()
                .append("Pauta: " + pautaId)
                .append(" ")
                .append(VoteEnum.SIM).append(": " + simVotes.size())
                .append(" ")
                .append(VoteEnum.NAO).append(": " + naoVotes.size())
                .toString();
    }

    @Scheduled(fixedDelay = 1000)
    public void verifyOpenSession() {
        log.info("Verify for open Pautas");
        List<PautaEntity> pautas = pautaRepository.findByStatusAndFinishesAtLessThan(StatusEnum.ABERTA, LocalDateTime.now());
        if(!pautas.isEmpty()){
            pautas.forEach( pautaEntity -> {
                        try {
                            log.info("Closing Pautas and Sending the result for the Queue");
                            queueSender.send(getVoteResult(pautaEntity.getId()).getResult());
                            //pautaEntity.setStatus(StatusEnum.ENCERRADA);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
            pautaRepository.saveAll(pautas);
        }
        log.info("End of Verifications.");
    }

    private PautaModelDTO toPautaModelDTO(PautaEntity entity){
        return modelMapper.map(entity, PautaModelDTO.class);
    }

}
