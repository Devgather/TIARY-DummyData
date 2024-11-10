package me.tiary.dummydata.domain;

import jakarta.persistence.*;
import lombok.*;
import me.tiary.dummydata.domain.common.Timestamp;

import java.util.UUID;

@Entity
@Table(name = "profile")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Profile extends Timestamp {
    public static final int NICKNAME_MAX_LENGTH = 20;

    public static final String BASIC_PICTURE = "/common/profile/picture.png";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "char(36)", nullable = false, unique = true)
    private String uuid;

    @Column(length = NICKNAME_MAX_LENGTH, nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String picture;

    @PrePersist
    private void prePersist() {
        createUuid();
    }

    public void createUuid() {
        this.uuid = UUID.randomUUID().toString();
    }
}