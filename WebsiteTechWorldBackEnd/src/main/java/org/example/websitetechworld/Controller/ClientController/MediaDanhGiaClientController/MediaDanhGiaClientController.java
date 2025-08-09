package org.example.websitetechworld.Controller.ClientController.MediaDanhGiaClientController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.ClientRequest.MediaDanhGiaClientRequest.MediaDanhGiaClientRequest;
import org.example.websitetechworld.Dto.Response.ClientResponse.MediaDanhGiaClientResponse.MediaDanhGiaClientResponse;
import org.example.websitetechworld.Services.ClientServices.MediaDanhGiaClientService.MediaDanhGiaClientService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/client/media-danh-gia")
@RequiredArgsConstructor
public class MediaDanhGiaClientController {
    private final MediaDanhGiaClientService mediaService;
    private final MediaDanhGiaClientService mediaDanhGiaClientService;

    @GetMapping
    public ResponseEntity<List<MediaDanhGiaClientResponse>> getAll() {
        return ResponseEntity.ok(mediaService.getAll());
    }

//    @GetMapping("/danh-gia/{idDanhGia}")
//    public ResponseEntity<List<MediaDanhGiaClientResponse>> getByDanhGia(@PathVariable Integer idDanhGia) {
//        return ResponseEntity.ok(mediaService.getByDanhGia(idDanhGia));
//    }

    @GetMapping("/{id}")
    public ResponseEntity<MediaDanhGiaClientResponse> getById(@PathVariable Integer id) {
        return mediaService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadMedia(
            @RequestParam("file") MultipartFile file,
            @RequestParam("idDanhGia") Integer idDanhGia) throws IOException {
        
        return ResponseEntity.ok(mediaService.uploadFile(file, idDanhGia));
    }


    @PutMapping("/{id}")
    public ResponseEntity<MediaDanhGiaClientResponse> update(@PathVariable Integer id,
                                                             @RequestBody MediaDanhGiaClientRequest req) {
        return ResponseEntity.ok(mediaService.update(id, req));
    }


    //delete media theo idDanhGia
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        mediaService.delete(id);
        return ResponseEntity.ok("Thành công");
    }

    @DeleteMapping("/delete/{idMedia}")
    public ResponseEntity<String> deleteIdMedia(@PathVariable Integer idMedia) {
        mediaDanhGiaClientService.deleteMediaById(idMedia);
        return ResponseEntity.ok("Xóa media thành công");
    }

}
