package org.example.websitetechworld.Dto.Response.ClientResponse.MediaDanhGiaClientResponse;

import lombok.Data;
import org.example.websitetechworld.Enum.DanhGiaSanPham.TrangThaiMedia;

import java.time.LocalDateTime;

@Data
public class MediaReviewClientResponse {
    private Integer idMedia;
    private String loaiMedia;
    private String urlMedia;
    private LocalDateTime ngayUpload;
    private TrangThaiMedia trangThaiMedia;
    private Integer idDanhGia;
}
