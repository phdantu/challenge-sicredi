package com.challenge.vote.unit.repository;

import com.challenge.vote.entity.PautaEntity;
import com.challenge.vote.entity.enumerator.StatusEnum;
import com.challenge.vote.repository.PautaRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PautaRepositoryTest {

    private PautaEntity pauta;

    @Autowired
    private PautaRepository pautaRepository;

    @Before
    public void setUp() {
        pauta = new PautaEntity();
        pauta.setStatus(StatusEnum.ABERTA);
    }

    @Test
    public void savePautaTest(){
        PautaEntity savedPauta = pautaRepository.save(pauta);
        PautaEntity persistedPauta = pautaRepository.findById(savedPauta.getId()).get();
        assertThat(persistedPauta).isEqualTo((savedPauta));
    }
}
