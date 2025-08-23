package org.example.websitetechworld.Dto.Response.AdminResponse.BaoHanhAdminResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.websitetechworld.Enum.BaoHanh.TrangThaiLichSuBaoHanh;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaoHanhHistoryAdminResponse {
    private Integer idLsbh;
    private TrangThaiLichSuBaoHanh status;
    private String loaiBaoHanh;
    private String moTa;
    private Date ngayYeuCau;
    private Date ngayHoanThanh;
}
