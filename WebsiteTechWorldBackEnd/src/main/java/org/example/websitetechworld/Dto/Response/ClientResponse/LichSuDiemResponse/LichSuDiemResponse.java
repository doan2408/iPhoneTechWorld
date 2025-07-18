package org.example.websitetechworld.Dto.Response.ClientResponse.LichSuDiemResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LichSuDiemResponse {
    private Integer idLichSuDiem;
    private String loaiDiem;
    private BigDecimal soDiem;
    private String ghiChu;
    private OffsetDateTime thoiGian;
    private OffsetDateTime hanSuDung;
}
