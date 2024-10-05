package me.tiary.dummydata.domain.composite;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class TilTagId implements Serializable {
    private static final long serialVersionUID = 9017788639195921594L;

    @Column
    private Long tilId;

    @Column
    private Long tagId;
}