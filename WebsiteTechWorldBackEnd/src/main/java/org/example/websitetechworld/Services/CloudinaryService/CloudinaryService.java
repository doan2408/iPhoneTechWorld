package org.example.websitetechworld.Services.CloudinaryService;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService {
    @Autowired
    private Cloudinary cloudinary;

    public Map<String, String> upload(MultipartFile file) throws IOException {
        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

        Map<String, String> result = new HashMap<>();
        result.put("url", uploadResult.get("secure_url").toString());
        result.put("public_id", uploadResult.get("public_id").toString());
        return result;
    }

    public void delete(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }


    public Map<String, Object> uploadMedia(MultipartFile file, String folder) throws IOException {
        boolean isVideo = file.getContentType() != null && file.getContentType().startsWith("video");

        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "folder", folder,
                        "resource_type", isVideo ? "video" : "image"
                ));

        Map<String, Object> result = new HashMap<>();
        result.put("url", uploadResult.get("secure_url").toString());
        result.put("public_id", uploadResult.get("public_id").toString());
        result.put("resource_type", isVideo ? "VIDEO" : "IMAGE");

        if (isVideo && uploadResult.get("duration") != null) {
            result.put("duration", ((Number) uploadResult.get("duration")).intValue());
        }
        return result;
    }


}
