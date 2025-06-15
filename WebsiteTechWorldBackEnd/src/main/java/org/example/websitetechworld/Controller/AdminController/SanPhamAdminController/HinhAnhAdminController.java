package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.HinhAnhAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.ImeiAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.HinhAnhAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.ImeiAdminResponse;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.HinhAnhAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/hinhAnh")
public class HinhAnhAdminController {
    private final HinhAnhAdminService hinhAnhAdminService;
    private final Cloudinary cloudinary;

    @GetMapping
    public ResponseEntity<Page<HinhAnhAdminResponse>> getAllHinhAnh(
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<HinhAnhAdminResponse> hinhAnhs = hinhAnhAdminService.getAllHinhAnh(pageable);
        return ResponseEntity.ok(hinhAnhs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HinhAnhAdminResponse> detailHinhAnh(@PathVariable Integer id) {
        HinhAnhAdminResponse response = hinhAnhAdminService.detailHinhAnh(id);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", "File rỗng. Vui lòng chọn file ảnh."));
        }

        try {
            // Thay vì upload mặc định, bạn chỉ định folder Cloudinary tại đây
            Map<String, Object> options = ObjectUtils.asMap(
                    "folder", "anh_san_pham" // <-- đổi tên thư mục theo ý bạn
            );

            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), options);

            String imageUrl = (String) uploadResult.get("secure_url");

            return ResponseEntity.ok(Map.of(
                    "message", "Upload thành công",
                    "url", imageUrl
            ));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Lỗi khi upload lên Cloudinary: " + e.getMessage()));
        }
    }


//    @PostMapping
//    public ResponseEntity<HinhAnhAdminResponse> createHinhAnh(@RequestBody HinhAnhAdminRequest hinhAnhAdminRequest) {
//        HinhAnhAdminResponse response = hinhAnhAdminService.createHinhAnh(hinhAnhAdminRequest);
//
//        return ResponseEntity.ok(response);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<HinhAnhAdminResponse> updateHinhAnh(@PathVariable Integer id, @RequestBody HinhAnhAdminRequest hinhAnhAdminRequest) {
//        HinhAnhAdminResponse response = hinhAnhAdminService.updateHinhAnh(id, hinhAnhAdminRequest);
//
//        return ResponseEntity.ok(response);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HinhAnhAdminResponse> deleteHinhAnh(@PathVariable Integer id) {
        HinhAnhAdminResponse response = hinhAnhAdminService.deleteHinhAnh(id);

        return ResponseEntity.ok(response);
    }
}
