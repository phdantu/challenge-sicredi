package com.challenge.vote.dto;

import com.challenge.vote.entity.enumerator.VoteEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteModelDTO {

    private String userIdCpf;
    private VoteEnum vote;
    private LocalDateTime createdAt;
}
