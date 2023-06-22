package com.challenge.vote.controller;

import com.challenge.vote.dto.PautaModelDTO;
import com.challenge.vote.dto.ResultPautaDTO;
import com.challenge.vote.dto.SessionDTO;
import com.challenge.vote.dto.VoteDTO;
import com.challenge.vote.dto.VoteModelDTO;
import com.challenge.vote.external.queue.QueueConsumer;
import com.challenge.vote.external.queue.QueueSender;
import com.challenge.vote.service.PautaService;
import com.challenge.vote.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.amqp.core.Queue;

import javax.validation.Valid;
import javax.ws.rs.Consumes;

@RestController
@RequestMapping(produces = "application/json")
public class PautaController {

    @Autowired
    private PautaService pautaService;

    @Autowired
    private VoteService voteService;

    @PostMapping(value = "/{duration}/session")
    public PautaModelDTO saveNewPauta(@PathVariable("duration") Long duration){
        return pautaService.createNewPauta(duration);
    }

    @PostMapping(value = "/{pautaId}/vote", consumes = "application/json")
    public VoteModelDTO saveNewVote(@Valid @RequestBody VoteDTO data, @PathVariable("pautaId") Long pautaId) throws Exception {
        return voteService.registerNewVote(data, pautaId);
    }

    @GetMapping(value = "/{pautaId}/result")
    public ResultPautaDTO getResultPauta(@PathVariable("pautaId") Long idPauta) throws Exception {
        return pautaService.getVoteResult(idPauta);
    }
}
