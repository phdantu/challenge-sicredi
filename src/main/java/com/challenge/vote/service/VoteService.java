package com.challenge.vote.service;

import com.challenge.vote.dto.VoteDTO;
import com.challenge.vote.dto.VoteModelDTO;
import com.challenge.vote.entity.PautaEntity;
import com.challenge.vote.entity.VoteEntity;
import com.challenge.vote.repository.VoteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteService {

    private final String UNABLE_TO_VOTE = "UNABLE_TO_VOTE";
    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private PautaService pautaService;

    @Autowired
    private ModelMapper modelMapper;

    public VoteModelDTO registerNewVote(VoteDTO data, Long pautaId) throws Exception {
        var pauta = pautaService.checkIfSessionIsAvailable(pautaId);

        verifyIfHaventVotedYet(pauta, data.getCpf());

        var entity = voteRepository.save(VoteEntity.builder()
                                    .vote(data.getVote())
                                    .pauta(pauta)
                                    .userIdCpf(data.getCpf())
                                    .build());

        return toVoteModelDTO(entity);
    }

    private void verifyIfHaventVotedYet(PautaEntity pauta, String cpf) throws Exception {
        var vote = voteRepository.findByPautaAndUserIdCpf(pauta, cpf);

        if(vote.isPresent()){
            throw new Exception("Este associado já votou nesta sessão!");
        }
    }

    public List<VoteEntity> getAllVotesByPauta(Long pauta){
        return voteRepository.findAllByPautaId(pauta);
    }

    private VoteModelDTO toVoteModelDTO(VoteEntity entity){
        return modelMapper.map(entity, VoteModelDTO.class);
    }

}
