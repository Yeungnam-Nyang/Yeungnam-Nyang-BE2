package com.example.YNN.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.Objects;
import java.io.Serializable;
@Embeddable

public class CatMapId implements Serializable {
    private Long postId;
    private Long locationId;

    public CatMapId() {}

    public CatMapId(Long postId, Long locationId) {
        this.postId = postId;
        this.locationId = locationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CatMapId catMapId = (CatMapId) o;
        return Objects.equals(postId, catMapId.postId) && Objects.equals(locationId, catMapId.locationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, locationId);
    }
}
