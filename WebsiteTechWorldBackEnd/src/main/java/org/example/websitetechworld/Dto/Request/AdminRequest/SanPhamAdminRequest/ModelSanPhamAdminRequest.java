package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Enum.SanPham.TrangThaiSanPhamModel;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelSanPhamAdminRequest {

    private Integer idModelSanPham;


    private String maModelSanPham;

    @NotBlank(message = "model.name.required")
    @Size(max = 100, message = "model.name.maxlength")
    @Size(min = 3, message = "model.name.minlength")
    @Pattern(regexp = "^[\\p{L}\\d\\s]+$", message = "model.name.invalidchars")
    private String tenModel;

    @NotNull(message = "model.namRaMat.required")
    private LocalDate namRaMat;


    @NotNull(message = "model.trangThai.required")
    @Enumerated(EnumType.STRING)
    private TrangThaiSanPhamModel trangThaiSanPhamModel;

    @NotNull(message = "model.idRam.required")
    private Integer idRam;

    @NotNull(message = "model.idManHinh.required")
    private Integer idManHinh;

    @NotNull(message = "model.idHeDieuHanh.required")
    private Integer idHeDieuHanh;

    @NotNull(message = "model.idPin.required")
    private Integer idPin;

    @NotNull(message = "model.idCpu.required")
    private Integer idCpu;

    @NotNull(message = "model.idCameraTruoc.required")
    private Integer idCameraTruoc;

    @NotNull(message = "model.idCameraSau.required")
    private Integer idCameraSau;

    @NotNull(message = "model.idXuatXu.required")
    private Integer idXuatXu;

    @NotNull(message = "model.idLoai.required")
    private Integer idLoai;

}
