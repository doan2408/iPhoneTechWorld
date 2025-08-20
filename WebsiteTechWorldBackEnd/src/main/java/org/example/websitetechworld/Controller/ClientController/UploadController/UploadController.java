package org.example.websitetechworld.Controller.ClientController.UploadController;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.example.websitetechworld.Entity.XuLySauBanHang;
import org.example.websitetechworld.Repository.XuLySauBanHangRepository;
import org.example.websitetechworld.Services.CloudinaryService.MediaTamService.MediaTamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private XuLySauBanHangRepository xuLyRepo;

    @Autowired
    private MediaTamService mediaTamService;

    @PostMapping
    public ResponseEntity<?> uploadFiles(
            @RequestParam("idXuLy") Integer idXuLy,
            @RequestParam("files") MultipartFile[] files) {

        List<String> uploadedUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                String resourceType = file.getContentType().startsWith("video") ? "video" : "image";

                Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                        "resource_type", resourceType,
                        "folder", "imei-returns"
                ));

                String url = (String) uploadResult.get("secure_url");
                uploadedUrls.add(url);

                XuLySauBanHang xuLy = xuLyRepo.findById(idXuLy).orElse(null);
                if (xuLy != null) {
                    if (resourceType.equals("image")) {
                        xuLy.setUrlHinh(url);
                    } else {
                        xuLy.setUrlVideo(url);
                    }
                    xuLyRepo.save(xuLy);
                }

            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Lỗi upload: " + e.getMessage());
            }
        }

        return ResponseEntity.ok(uploadedUrls);
    }
    @PostMapping("/upload-imei")
    public ResponseEntity<?> uploadMedia(
            @RequestParam("idImei") Integer idImei,
            @RequestParam("file") MultipartFile file) {

        try {
            String type = file.getContentType().startsWith("video") ? "video" : "image";

            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "resource_type", type,
                    "folder", "imei-returns"
            ));

            String url = (String) uploadResult.get("secure_url");
            mediaTamService.addMedia(idImei, url, type);

            return ResponseEntity.ok("Upload thành công");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi upload: " + e.getMessage());
        }
    }
}
