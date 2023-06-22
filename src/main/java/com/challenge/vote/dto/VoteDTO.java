package com.challenge.vote.dto;

import com.challenge.vote.entity.enumerator.VoteEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteDTO {

    @NotBlank private String cpf;
    @NotNull private VoteEnum vote;
}
