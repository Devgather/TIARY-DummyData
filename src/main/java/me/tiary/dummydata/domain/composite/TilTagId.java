package me.tiary.dummydata.domain.composite;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TilTagId implements Serializable {
    @Serial
    private static final long serialVersionUID = 9017788639195921594L;

    @Column
    private Long tilId;

    @Column
    private Long tagId;
}