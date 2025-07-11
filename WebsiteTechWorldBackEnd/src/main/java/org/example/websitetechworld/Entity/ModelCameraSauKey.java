package org.example.websitetechworld.Entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;


@Data
@Embeddable
public class ModelCameraSauKey implements Serializable {

    private Integer modelId;

    private Integer cameraSauId;

}
