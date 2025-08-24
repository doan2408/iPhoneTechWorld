package org.example.websitetechworld.Dto.Response.AdminResponse.BaoHanhAdminResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.websitetechworld.Enum.BaoHanh.TrangThaiBaoHanh;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaoHanhOfProductResponse {
    private Integer idBaoHanh;
    private Integer idLoaiBaoHanh;
    private String  tenLoaiBaoHanh;
    private Integer thoiGianThang;
    private Date ngayHetHan;
    private TrangThaiBaoHanh trangThaiBaoHanh;
}
