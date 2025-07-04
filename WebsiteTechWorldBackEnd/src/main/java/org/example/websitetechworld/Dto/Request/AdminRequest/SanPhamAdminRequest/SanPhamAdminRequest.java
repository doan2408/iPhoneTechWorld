package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size( min = 3, max = 50, message = "Tên sản phẩm phải từ 3 đến 50 ký tự")
    private String tenSanPham;

    @NotBlank(message = "Thương hiệu không được để trống")
    @Size(min = 3, max = 50, message = "Tên thương hiệu phải từ 3 đến 50 ký tự")
    private String thuongHieu;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Trạng thái sản phẩm không được để trống")
    private TrangThaiSanPham trangThaiSanPham;

    @NotNull(message = "Nhà cung cấp không được để trống")
    private Integer idNhaCungCap;

    @NotNull(message = "id_model_san_pham không được để trống")
    private Integer idModelSanPham;

    @Size(min = 1, message = "Vui lòng tạo ít nhất 1 biến thể sản phẩm")
    @Valid
    private Set<SanPhamChiTietAdminRepuest> sanPhamChiTiets;

}
