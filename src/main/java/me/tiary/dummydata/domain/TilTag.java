package me.tiary.dummydata.domain;

import jakarta.persistence.*;
import lombok.*;
import me.tiary.dummydata.domain.common.Timestamp;
import me.tiary.dummydata.domain.composite.TilTagId;

import java.util.UUID;

@Entity
@Table(name = "til_tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class TilTag extends Timestamp {
    @EmbeddedId
    @Builder.Default
    private TilTagId id = new TilTagId();

    @JoinColumn(name = "til_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tilId")
    private Til til;

    @JoinColumn(name = "tag_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tagId")
    private Tag tag;

    @Column(columnDefinition = "char(36)", nullable = false, unique = true)
    private String uuid;

    @PrePersist
    private void prePersist() {
        createId();
        createUuid();
    }

    public void createId() {
        this.id = TilTagId.builder()
                .tilId((til == null) ? (null) : (til.getId()))
                .tagId((tag == null) ? (null) : (tag.getId()))
                .build();
    }

    public void createUuid() {
        this.uuid = UUID.randomUUID().toString();
    }
}