package com.challenge.vote.controller.integration;

import com.challenge.vote.dto.SessionDTO;
import com.challenge.vote.dto.VoteDTO;
import com.challenge.vote.entity.PautaEntity;
import com.challenge.vote.entity.enumerator.StatusEnum;
import com.challenge.vote.entity.enumerator.VoteEnum;
import com.challenge.vote.repository.PautaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import javax.ws.rs.NotFoundException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
public class PautaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PautaRepository pautaRepository;

    @Test
    void whenTryOpenSessionWithInvalidPaudaId() throws Exception {
        var json = SessionDTO.builder().duration(5L).build();

        assertThrows(NotFoundException.class, () ->{
            try {
                mockMvc.perform(put("/{pautaId}/session", 999L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(json))
                        .accept(MediaType.ALL)).andDo(print()).andReturn();
            }catch (NestedServletException e) {
                throw (Exception) e.getCause();}
        });
    }

    @Test
    void whenTryToVoteWithInvalidPautaId() throws Exception {
        var json = VoteDTO.builder().vote(VoteEnum.SIM).cpf("93670170065").build();

        assertThrows(NotFoundException.class, () ->{
            try {
                mockMvc.perform(post("/{pautaId}/vote", 999L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(json))
                        .accept(MediaType.ALL)).andDo(print()).andReturn();
            }catch (NestedServletException e) {
                throw (Exception) e.getCause();}
        });
    }

    @Test
    void whenTryToVoteWithInvalidCpfAssertErrorMessage() throws Exception {
        var json = VoteDTO.builder().vote(VoteEnum.SIM).cpf("s").build();

        var entity = pautaRepository.save(PautaEntity.builder()
                .finishesAt(LocalDateTime.now().plusMinutes(10L))
                .status(StatusEnum.ABERTA).build());

        String message = null;

        try {
            mockMvc.perform(post("/{pautaId}/vote", entity.getId())
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(json))
                    .accept(MediaType.ALL)).andDo(print()).andReturn();
        }catch (NestedServletException e) {
            message = (String) e.getMessage();
        }

        assertTrue(message.contains("CPF Inválido!"));

        pautaRepository.delete(entity);
    }

    @Test
    void whenTryToGetResultWithInvalidPautaId() throws Exception {
        assertThrows(NotFoundException.class, () ->{
            try {
                mockMvc.perform(get("/{pautaId}/result", 999L)
                        .contentType("application/json")
                        .accept(MediaType.ALL)).andDo(print()).andReturn();
            }catch (NestedServletException e) {
                throw (Exception) e.getCause();}
        });
    }

    @Test
    void whenTryToVoteWhenSessionIsClosed() throws Exception {
        var json = VoteDTO.builder().vote(VoteEnum.SIM).cpf("93670170065").build();

        var entity = pautaRepository.save(PautaEntity.builder()
                .status(StatusEnum.ENCERRADA).build());

        String message = null;

        try {
            mockMvc.perform(post("/{pautaId}/vote", 1L)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(json))
                    .accept(MediaType.ALL)).andDo(print()).andReturn();
        }catch (NestedServletException e) {
            message = (String) e.getMessage();
        }

        assertTrue(message.contains("Sessão já encerrada!"));

        pautaRepository.delete(entity);
    }

    @Test
    void testIfIsClosingTheSession() throws Exception {
        var json = VoteDTO.builder().vote(VoteEnum.SIM).cpf("93670170065").build();

        var entity = pautaRepository.save(PautaEntity.builder()
                .finishesAt(LocalDateTime.now().minusMinutes(5L))
                .status(StatusEnum.ABERTA).build());

        String message = null;

        try {
            mockMvc.perform(post("/{pautaId}/vote", 1L)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(json))
                    .accept(MediaType.ALL)).andDo(print()).andReturn();
        }catch (NestedServletException e) {
            message = (String) e.getMessage();
        }

        assertTrue(message.contains("Sessão já encerrada!"));

        pautaRepository.delete(entity);
    }
}
