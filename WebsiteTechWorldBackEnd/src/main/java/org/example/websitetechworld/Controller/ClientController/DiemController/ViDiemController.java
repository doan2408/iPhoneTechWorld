package org.example.websitetechworld.Controller.ClientController.DiemController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Response.ClientResponse.DiemResponse.ViDiemClientResponse;
import org.example.websitetechworld.Services.ClientServices.DiemServices.ViDiemServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/client/viDiem")
@RequiredArgsConstructor
public class ViDiemController {
    private final ViDiemServices viDiemServices;

    @GetMapping
    public ResponseEntity<?> getViDiem() {
        ViDiemClientResponse viDiem = viDiemServices.getDiemKhaDung();
        if (viDiem != null) {
            return ResponseEntity.ok().body(viDiem);
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "status", 404,
                            "message", "Did not found point wallet for this client"
                    ));
        }
    }
}
