package org.example.websitetechworld.Services.ClientServices.MediaDanhGiaClientService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.ClientRequest.MediaDanhGiaClientRequest.MediaDanhGiaClientRequest;
import org.example.websitetechworld.Dto.Response.ClientResponse.MediaDanhGiaClientResponse.MediaDanhGiaClientResponse;
import org.example.websitetechworld.Entity.MediaDanhGia;
import org.example.websitetechworld.Enum.DanhGiaSanPham.TrangThaiMedia;
import org.example.websitetechworld.Repository.DanhGiaSanPhamRepository;
import org.example.websitetechworld.Repository.MediaDanhGiaRepository;
import org.example.websitetechworld.Services.CloudinaryService.CloudinaryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MediaDanhGiaClientService {

    private final MediaDanhGiaRepository mediaRepo;
    private final DanhGiaSanPhamRepository danhGiaRepo;
    private final CloudinaryService cloudinaryService;



    private MediaDanhGiaClientResponse toResponse(MediaDanhGia media) {
        MediaDanhGiaClientResponse dto = new MediaDanhGiaClientResponse();
        dto.setIdMedia(media.getIdMedia());
        dto.setLoaiMedia(media.getLoaiMedia());
        dto.setUrlMedia(media.getUrlMedia());
        dto.setTenFile(media.getTenFile());
        dto.setKichThuocFile(media.getKichThuocFile());
        dto.setThoiLuongVideo(media.getThoiLuongVideo());
        dto.setThuTuHienThi(media.getThuTuHienThi());
        dto.setNgayUpload(media.getNgayUpload());
        dto.setTrangThaiMedia(media.getTrangThaiMedia());
        if (media.getDanhGiaSanPham() != null) {
            dto.setIdDanhGia(media.getDanhGiaSanPham().getIdDanhGia());
        }
        return dto;
    }

    public List<MediaDanhGiaClientResponse> getAll() {
        return mediaRepo.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

//    public List<MediaDanhGiaClientResponse> getByDanhGia(Integer idDanhGia) {
//        return mediaRepo.findByDanhGiaSanPham_IdDanhGia(idDanhGia)
//                .stream().map(this::toResponse).collect(Collectors.toList());
//    }

    public Optional<MediaDanhGiaClientResponse> getById(Integer id) {
        return mediaRepo.findById(id).map(this::toResponse);
    }


    public MediaDanhGiaClientResponse uploadFile(MultipartFile file, Integer idDanhGia) throws IOException {
        // Upload lên Cloudinary
        Map<String, Object> uploadInfo = cloudinaryService.uploadMedia(file, "media_danh_gia");

        MediaDanhGia media = new MediaDanhGia();
        media.setLoaiMedia((String) uploadInfo.get("resource_type"));
        media.setUrlMedia((String) uploadInfo.get("url"));
        media.setTenFile(file.getOriginalFilename());
        media.setKichThuocFile(BigInteger.valueOf(file.getSize()));
        media.setNgayUpload(LocalDateTime.now());
        media.setTrangThaiMedia(TrangThaiMedia.APPROVED);

        if ("VIDEO".equals(uploadInfo.get("resource_type")) && uploadInfo.get("duration") != null) {
            media.setThoiLuongVideo((Integer) uploadInfo.get("duration"));
        }

        danhGiaRepo.findById(idDanhGia).ifPresent(media::setDanhGiaSanPham);

        MediaDanhGia saved = mediaRepo.save(media);
        return toResponse(saved);
    }


    public MediaDanhGiaClientResponse update(Integer id, MediaDanhGiaClientRequest req) {
        MediaDanhGia media = mediaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy media với id: " + id));

        media.setLoaiMedia(req.getLoaiMedia());
        media.setUrlMedia(req.getUrlMedia());
        media.setTenFile(req.getTenFile());
        media.setKichThuocFile(req.getKichThuocFile());
        media.setThoiLuongVideo(req.getThoiLuongVideo());
        media.setThuTuHienThi(req.getThuTuHienThi());
        media.setTrangThaiMedia(req.getTrangThaiMedia());

        if (req.getIdDanhGia() != null) {
            danhGiaRepo.findById(req.getIdDanhGia()).ifPresent(media::setDanhGiaSanPham);
        }

        return toResponse(mediaRepo.save(media));
    }

    @Transactional
    public void delete(Integer id) {
        if (!danhGiaRepo.existsById(id)) {
            throw new RuntimeException("Đánh giá không tồn tại id: " + id);
        }
        mediaRepo.deleteByDanhGiaSanPham_IdDanhGia(id);
    }

    @Transactional
    public void deleteMediaById(Integer idMedia) {
        // Kiểm tra tồn tại trước khi xóa (optional)
        boolean exists = mediaRepo.existsById(idMedia);
        if (!exists) {
            throw new EntityNotFoundException("Media với id " + idMedia + " không tồn tại");
        }

        // Xóa media
        mediaRepo.deleteByIdMedia(idMedia);
    }
}
