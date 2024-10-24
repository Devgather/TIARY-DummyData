package me.tiary.dummydata.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import me.tiary.dummydata.domain.common.Timestamp;

import java.util.UUID;

@Entity
@Table(name = "oauth")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuth extends Timestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "profile_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Profile profile;

    @Column(columnDefinition = "char(36)", nullable = false, unique = true)
    private String uuid;

    @Column(nullable = false, unique = true)
    private String identifier;

    @Column(nullable = false)
    private String provider;

    @Builder
    public OAuth(final Long id, final Profile profile, final String uuid, final String identifier, final String provider) {
        setProfile(profile);

        this.id = id;
        this.uuid = uuid;
        this.identifier = identifier;
        this.provider = provider;
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
            this.profile.getOAuths().remove(this);
        }

        this.profile = profile;

        if (profile != null) {
            profile.getOAuths().add(this);
        }
    }
}