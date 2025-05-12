package org.example.websitetechworld.Controller.AdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Response.ImeiAdminResponse;
import org.example.websitetechworld.Services.AdminServices.ImeiAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/imei")
public class ImeiAdminController {
    private final ImeiAdminService imeiAdminService;

    @GetMapping
    public ResponseEntity<Page<ImeiAdminResponse>> getAllImei(
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<ImeiAdminResponse> imeis = imeiAdminService.getAllImei(pageable);

        return ResponseEntity.ok(imeis);
    }
}
