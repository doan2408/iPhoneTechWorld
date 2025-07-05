package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.example.websitetechworld.Enum.Imei.TrangThaiImei;

import java.util.List;

@Data
public class SaveImeiRequest {
    private String soImei;

    @Enumerated(EnumType.STRING)
    private TrangThaiImei trangThaiImei;
    private Integer idSanPhamChiTiet;
}
