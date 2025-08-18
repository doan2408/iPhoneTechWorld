package org.example.websitetechworld.Entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class ModelCameraSauKey implements Serializable {

    private Integer modelId;

    private Integer cameraSauId;

}
