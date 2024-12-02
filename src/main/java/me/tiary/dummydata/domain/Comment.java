package me.tiary.dummydata.domain;

import jakarta.persistence.*;
import lombok.*;
import me.tiary.dummydata.domain.common.Timestamp;

import java.util.UUID;

@Entity
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Comment extends Timestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "profile_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Profile profile;

    @JoinColumn(name = "til_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Til til;

    @Column(columnDefinition = "char(36)", nullable = false, unique = true)
    private String uuid;

    @Column(nullable = false)
    private String content;

    @PrePersist
    private void prePersist() {
        createUuid();
    }

    public void createUuid() {
        this.uuid = UUID.randomUUID().toString();
    }
}