package com.challenge.vote.repository;


import com.challenge.vote.entity.PautaEntity;
import com.challenge.vote.entity.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<VoteEntity, Long> {

    Optional<VoteEntity> findByPautaAndUserIdCpf(PautaEntity pauta, String userIdCpf);

    List<VoteEntity> findAllByPautaId(Long pauta);
}
