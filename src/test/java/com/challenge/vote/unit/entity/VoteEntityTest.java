package com.challenge.vote.unit.entity;

import com.challenge.vote.entity.PautaEntity;
import com.challenge.vote.entity.VoteEntity;
import com.challenge.vote.entity.enumerator.StatusEnum;
import com.challenge.vote.entity.enumerator.VoteEnum;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.PersistenceException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class VoteEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private PautaEntity pauta;

    private PautaEntity savedPauta;

    private VoteEntity voteOne;

    private VoteEntity voteTwo;

    @Before
    public void setUp() {
        pauta = new PautaEntity();
        voteOne = new VoteEntity();
        voteTwo = new VoteEntity();

        savedPauta = this.entityManager.persistFlushFind(pauta);

        voteOne.setUserIdCpf("00000000010");
        voteOne.setVote(VoteEnum.SIM);
        voteOne.setCreatedAt(LocalDateTime.now());
        voteOne.setPauta(savedPauta);

        voteTwo.setUserIdCpf("00000000020");
        voteTwo.setVote(VoteEnum.NAO);
        voteTwo.setCreatedAt(LocalDateTime.now());
        voteTwo.setPauta(savedPauta);

        pauta.setStatus(StatusEnum.ABERTA);
    }

    @Test
    public void saveVoteTest(){
        VoteEntity savedVoteOne = this.entityManager.persistAndFlush(voteOne);
        VoteEntity savedVoteTwo = this.entityManager.persistAndFlush(voteTwo);
        assertThat(savedVoteOne.getUserIdCpf()).isEqualTo("00000000010");
        assertThat(savedVoteTwo.getUserIdCpf()).isEqualTo("00000000020");
        assertThat(savedVoteOne.getPauta().getId()).isEqualTo(savedPauta.getId());
    }

    @Test
    public void saveVoteWithoutCpfTest(){
        this.thrown.expect(PersistenceException.class);
        voteOne.setUserIdCpf(null);
        this.entityManager.persistAndFlush(voteOne);
    }

    @Test
    public void saveVoteWithoutPautaTest(){
        this.thrown.expect(PersistenceException.class);
        voteOne.setPauta(new PautaEntity());
        this.entityManager.persistAndFlush(voteOne);
    }
}
