package me.tiary.dummydata.domain;

import jakarta.persistence.*;
import lombok.*;
import me.tiary.dummydata.domain.common.Timestamp;

import java.util.UUID;

@Entity
@Table(name = "tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Tag extends Timestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "char(36)", nullable = false, unique = true)
    private String uuid;

    @Column(nullable = false, unique = true)
    private String name;

    @PrePersist
    private void prePersist() {
        createUuid();
    }

    public void createUuid() {
        this.uuid = UUID.randomUUID().toString();
    }
}