package com.challenge.vote.entity;

import com.challenge.vote.entity.enumerator.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pauta")
public class PautaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status", columnDefinition = "ENUM('ABERTA', 'ENCERRADA')")
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Column(name = "finishes_at")
    private LocalDateTime finishesAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ToString.Exclude
    @OneToMany(mappedBy = "pauta", fetch = FetchType.LAZY)
    private List<VoteEntity> votes;

    @PrePersist
    public void createdAt(){
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void updatedAt(){
        updatedAt = LocalDateTime.now();
    }
}
