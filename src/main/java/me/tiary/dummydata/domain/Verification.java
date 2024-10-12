package me.tiary.dummydata.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import me.tiary.dummydata.domain.common.Timestamp;
import me.tiary.dummydata.utility.StringUtility;

import java.util.UUID;

@Entity
@Table(name = "verification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Verification extends Timestamp {
    public static final int CODE_MAX_LENGTH = 6;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "char(36)", nullable = false, unique = true)
    private String uuid;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = CODE_MAX_LENGTH, nullable = false)
    private String code;

    @Column(nullable = false)
    private Boolean state;

    @PrePersist
    private void prePersist() {
        createUuid();

        if (code == null && Boolean.FALSE.equals(state)) {
            refreshCode();
        }
    }

    public void createUuid() {
        this.uuid = UUID.randomUUID().toString();
    }

    public void refreshCode() {
        if (Boolean.TRUE.equals(state)) {
            throw new IllegalStateException();
        }

        this.code = StringUtility.generateRandomString(CODE_MAX_LENGTH).toUpperCase();
    }
}