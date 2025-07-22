package org.example.websitetechworld.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Enum.DanhGiaSanPham.TrangThaiMedia;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "media_danh_gia")
public class MediaDanhGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_media")
    private Integer idMedia;

    @Column(name = "loai_media", nullable = false)
    private String loaiMedia;

    @Column(name = "url_media", nullable = false, length = 500)
    private String urlMedia;

    @Column(name = "ten_file")
    private String tenFile;

    @Column(name = "kich_thuoc_file")
    private BigInteger kichThuocFile;

    @Column(name = "thoi_luong_video")
    private Integer thoiLuongVideo;

    @Column(name = "thu_tu_hien_thi")
    private Integer thuTuHienThi;

    @Column(name = "ngay_upload")
    private LocalDateTime ngayUpload;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai_media_danh_gia")
    private TrangThaiMedia trangThaiMedia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_danh_gia", insertable = false, updatable = false)
    private DanhGiaSanPham danhGiaSanPham;

}
