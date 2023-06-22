package com.challenge.vote.unit.entity;


import com.challenge.vote.entity.PautaEntity;
import com.challenge.vote.entity.VoteEntity;
import com.challenge.vote.entity.enumerator.StatusEnum;
import com.challenge.vote.entity.enumerator.VoteEnum;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PautaEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private PautaEntity pauta;

    private VoteEntity voteOne;

    private VoteEntity voteTwo;

    @Before
    public void setUp() {
        pauta = new PautaEntity();
        voteOne = new VoteEntity();
        voteTwo = new VoteEntity();

        voteOne.setUserIdCpf("00000000010");
        voteOne.setVote(VoteEnum.SIM);
        voteOne.setCreatedAt(LocalDateTime.now());

        voteTwo.setUserIdCpf("00000000020");
        voteTwo.setVote(VoteEnum.NAO);
        voteTwo.setCreatedAt(LocalDateTime.now());

        pauta.setStatus(StatusEnum.ABERTA);
    }

    @Test
    public void savePautaTest(){
        PautaEntity savedPauta = this.entityManager.persistAndFlush(pauta);
        assertThat(savedPauta.getStatus()).isEqualTo(StatusEnum.ABERTA);
    }

    @Test
    public void savePautaVotesTest() {

        PautaEntity savedPauta = this.entityManager.persistFlushFind(pauta);

        voteOne.setPauta(savedPauta);
        voteTwo.setPauta(savedPauta);

        VoteEntity savedVoteOne = this.entityManager.persistFlushFind(voteOne);
        VoteEntity savedVoteTwo = this.entityManager.persistFlushFind(voteTwo);

        assertThat(savedPauta.getId()).isNotNull();
        assertThat(savedPauta.getStatus()).isEqualTo(StatusEnum.ABERTA);
        assertThat(savedPauta.getVotes().size()).isEqualTo(2);
    }
}
