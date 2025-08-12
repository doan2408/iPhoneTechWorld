package org.example.websitetechworld.Dto.Response.ClientResponse.MediaDanhGiaClientResponse;


import lombok.Data;
import org.example.websitetechworld.Enum.DanhGiaSanPham.TrangThaiMedia;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
public class MediaDanhGiaClientResponse {
    private Integer idMedia;
    private String loaiMedia;
    private String urlMedia;
    private String tenFile;
    private BigInteger kichThuocFile;
    private Integer thoiLuongVideo;
    private Integer thuTuHienThi;
    private LocalDateTime ngayUpload;
    private TrangThaiMedia trangThaiMedia;
    private Integer idDanhGia;
}
