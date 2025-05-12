package org.example.websitetechworld.Controller.AdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Entity.MauSac;
import org.example.websitetechworld.Services.AdminServices.MauSacAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/mauSac")
public class MauSacAdminController {
    private final MauSacAdminService mauSacService;

    @GetMapping
    public ResponseEntity<Page<MauSac>> getAllMauSac(
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<MauSac> mauSacs = mauSacService.getAllMauSac(pageable);

        return ResponseEntity.ok(mauSacs);
    }
}
