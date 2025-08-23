package org.example.websitetechworld.Dto.Request.AdminRequest.BaoHanhAdminRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Entity.LoaiBaoHanh;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.websitetechworld.Entity.ModelSanPham;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoaiBaoHanhRequest {

    private Integer idLoaiBaoHanh;

    @NotBlank(message = "Tên loại bảo hành không được để trống")
    private String tenLoaiBaoHanh;

    @NotNull(message = "Thời gian (tháng) không được để trống")
    @Positive(message = "Thời gian (tháng) phải lớn hơn 0")
    private Integer thoiGianThang;

    private String moTa;

    @NotNull(message = "Trạng thái không được để trống")
    private Boolean trangThai;

    @NotNull(message = "Model sản phẩm không được để trống")
    private Integer idModelSanPham;

    public static LoaiBaoHanh convertDto(LoaiBaoHanhRequest loaiBaoHanhRequest) {
        LoaiBaoHanh loaiBaoHanh = new LoaiBaoHanh();
        loaiBaoHanh.setId(loaiBaoHanhRequest.getIdLoaiBaoHanh());
        loaiBaoHanh.setTenLoaiBaoHanh(loaiBaoHanhRequest.getTenLoaiBaoHanh());
        loaiBaoHanh.setThoiGianThang(loaiBaoHanhRequest.getThoiGianThang());
        loaiBaoHanh.setMoTa(loaiBaoHanhRequest.getMoTa());// Model sản phẩm
        loaiBaoHanh.setTrangThai(loaiBaoHanhRequest.getTrangThai());
        if (loaiBaoHanhRequest.getIdModelSanPham() != null) {
            ModelSanPham model = new ModelSanPham();
            model.setIdModelSanPham(loaiBaoHanhRequest.getIdModelSanPham());
            loaiBaoHanh.setIdModelSanPham(model);
        }
        return loaiBaoHanh;
    }
}

