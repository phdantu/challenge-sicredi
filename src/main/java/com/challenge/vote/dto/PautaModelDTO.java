package com.challenge.vote.dto;

import com.challenge.vote.entity.enumerator.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PautaModelDTO {

    private Long id;
    private StatusEnum status;
    private LocalDateTime finishesAt;
    private LocalDateTime createdAt;
}
