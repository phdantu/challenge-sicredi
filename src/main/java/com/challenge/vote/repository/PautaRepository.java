package com.challenge.vote.repository;


import com.challenge.vote.entity.PautaEntity;
import com.challenge.vote.entity.enumerator.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PautaRepository extends JpaRepository<PautaEntity, Long> {

    List<PautaEntity> findByStatusAndFinishesAtLessThan(StatusEnum status, LocalDateTime finishesAt);
}
