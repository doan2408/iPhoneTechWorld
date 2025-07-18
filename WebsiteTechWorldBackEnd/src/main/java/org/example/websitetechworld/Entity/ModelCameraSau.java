package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelCameraSau {
    @EmbeddedId
    private ModelCameraSauKey id;

    @ManyToOne
    @MapsId("modelId")
    @JoinColumn(name = "id_model_san_pham")
    private ModelSanPham model;

    @ManyToOne
    @MapsId("cameraSauId")
    @JoinColumn(name = "id_camera_sau")
    private CameraSau cameraSau;

    private Boolean isChinh;
}
