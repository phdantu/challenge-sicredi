package com.challenge.vote.entity;

import com.challenge.vote.entity.enumerator.VoteEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vote")
public class VoteEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id_cpf")
    private String userIdCpf;

    @Column(name = "vote", columnDefinition = "ENUM('SIM', 'NAO')")
    @Enumerated(EnumType.STRING)
    private VoteEnum vote;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "pauta_id")
    @JsonIgnore
    private PautaEntity pauta;

    @PrePersist
    public void createdAt(){
        createdAt = LocalDateTime.now();
    }

}
