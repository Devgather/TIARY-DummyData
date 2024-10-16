package me.tiary.dummydata.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import me.tiary.dummydata.domain.common.Timestamp;

import java.util.UUID;

@Entity
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    public Comment(final Long id, final Profile profile, final Til til, final String uuid, final String content) {
        setProfile(profile);
        setTil(til);

        this.id = id;
        this.uuid = uuid;
        this.content = content;
    }

    @PrePersist
    private void prePersist() {
        createUuid();
    }

    public void createUuid() {
        this.uuid = UUID.randomUUID().toString();
    }

    void setProfile(final Profile profile) {
        if (this.profile != null) {
            this.profile.getComments().remove(this);
        }

        this.profile = profile;

        if (profile != null) {
            profile.getComments().add(this);
        }
    }

    void setTil(final Til til) {
        if (this.til != null) {
            this.til.getComments().remove(this);
        }

        this.til = til;

        if (til != null) {
            til.getComments().add(this);
        }
    }
}