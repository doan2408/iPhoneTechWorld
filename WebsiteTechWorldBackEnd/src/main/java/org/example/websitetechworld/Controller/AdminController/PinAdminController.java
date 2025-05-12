package org.example.websitetechworld.Controller.AdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Entity.Pin;
import org.example.websitetechworld.Services.AdminServices.PinAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/pin")
public class PinAdminController {
    private final PinAdminService pinAdminService;

    @GetMapping
    public ResponseEntity<Page<Pin>> getAllPin(
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<Pin> pins = pinAdminService.getAllPin(pageable);

        return ResponseEntity.ok(pins);
    }
}
