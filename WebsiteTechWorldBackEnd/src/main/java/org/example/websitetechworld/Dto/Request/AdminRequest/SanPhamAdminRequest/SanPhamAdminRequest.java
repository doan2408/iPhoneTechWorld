package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Enum.SanPham.TrangThaiSanPham;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SanPhamAdminRequest {
    private Integer id;

    private String maSanPham;

    @NotBlank(message = "{sanpham.tenSanPham.required}")
    @Size(min = 3, max = 50, message = "{sanpham.tenSanPham.size}")
    private String tenSanPham;

    @NotBlank(message = "{sanpham.thuongHieu.required}")
    @Size(min = 3, max = 50, message = "{sanpham.thuongHieu.size}")
    private String thuongHieu;

    @NotNull(message = "{sanpham.trangThai.required}")
    @Enumerated(EnumType.STRING) // Chỉ giữ lại nếu đây là Entity, nếu là DTO thì bỏ
    private TrangThaiSanPham trangThaiSanPham;

    @NotNull(message = "{sanpham.idNhaCungCap.required}")
    private List<Integer> idNhaCungCaps;

    @NotNull(message = "{sanpham.idModel.required}")
    private Integer idModelSanPham;

    @NotEmpty(message = "{sanpham.chiTiet.required}")
    @Valid
    private Set<SanPhamChiTietAdminRepuest> sanPhamChiTiets;


}
