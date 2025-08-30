package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.ModelSanPhamAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.ModelSanPhamAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.ModelSanPhamHienThiAdminResponse;
import org.example.websitetechworld.Entity.ModelSanPham;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.ModelSanPhamService;
import org.example.websitetechworld.exception.BusinessException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/modelSanPham")
public class ModelSanPhamAdminController {
    private final ModelSanPhamService modelSanPhamService;


    @GetMapping("/listModelSanPham")
    public ResponseEntity<List<ModelSanPhamAdminResponse>> getAlllistResponse() {
        List<ModelSanPhamAdminResponse> list = modelSanPhamService.getAllListModelSanPhamAdmin();
        return ResponseEntity.ok(list);
    }

    @GetMapping
    public ResponseEntity<Page<ModelSanPhamHienThiAdminResponse>> getAllPage(
           @RequestParam(defaultValue = "0") int page,
           @RequestParam(defaultValue = "5") int size
            ) {
        Page<ModelSanPhamHienThiAdminResponse> responses = modelSanPhamService.getAllPageModelSanPhamAdmin(page,size);

        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ModelSanPhamAdminResponse> createModelSanPham(@Valid @RequestBody ModelSanPhamAdminRequest request) {
        ModelSanPhamAdminResponse response = modelSanPhamService.createModelSanPham(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ModelSanPhamAdminResponse> updateModelSanPham(@PathVariable Integer id,@RequestBody ModelSanPhamAdminRequest request) {
        ModelSanPhamAdminResponse response = modelSanPhamService.updateModelSanPham(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModelSanPham(@PathVariable Integer id) {
        modelSanPhamService.deleteModelSanPham(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModelSanPhamAdminResponse> findByIdModelSanPham(@PathVariable Integer id) {
        ModelSanPhamAdminResponse response =  modelSanPhamService.findByIdModelSanPham(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<ModelSanPhamAdminResponse>> getAllPageModelSanPham(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer idLoai,
            @RequestParam(required = false) Integer idRam,
            @RequestParam(required = false) Integer idXuatXu) {
        Page<ModelSanPhamAdminResponse> result = modelSanPhamService.searchModelSanPham(page, size, search, idLoai, idRam, idXuatXu);
        return ResponseEntity.ok(result);
    }


    @PostMapping("/import-excel/upload")
    public ResponseEntity<List<ModelSanPhamAdminResponse>> importExcelUpload(@RequestParam("file") MultipartFile file) throws IOException {
        List<ModelSanPhamAdminResponse> responses = modelSanPhamService.importExcelFileUpload(file);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/download-template")
    public ResponseEntity<byte[]> downloadTemplate() {
        try {
            // Đọc file template có sẵn trong resources
            ClassPathResource resource = new ClassPathResource("templates/ModelSanPhamTemplate.xlsx");

            byte[] data = Files.readAllBytes(resource.getFile().toPath());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "ModelSanPhamTemplate.xlsx");

            return new ResponseEntity<>(data, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
