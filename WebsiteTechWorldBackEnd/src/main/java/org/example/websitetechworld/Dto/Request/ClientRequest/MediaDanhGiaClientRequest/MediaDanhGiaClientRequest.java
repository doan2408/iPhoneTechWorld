package org.example.websitetechworld.Dto.Request.ClientRequest.MediaDanhGiaClientRequest;

import lombok.Data;
import org.example.websitetechworld.Enum.DanhGiaSanPham.TrangThaiMedia;

import java.math.BigInteger;

@Data
public class MediaDanhGiaClientRequest {
    private String loaiMedia;
    private String urlMedia;
    private String tenFile;
    private BigInteger kichThuocFile;
    private Integer thoiLuongVideo;
    private Integer thuTuHienThi;
    private TrangThaiMedia trangThaiMedia;
    private Integer idDanhGia; // để liên kết với đánh giá sản phẩm
}
