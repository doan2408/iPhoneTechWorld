package org.example.websitetechworld.Controller.ClientController.PhanHoiDanhGiaClientController;


import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Response.ClientResponse.PhanHoiDanhGiaClientResponse.PhanHoiDanhGiaClientResponse;
import org.example.websitetechworld.Services.ClientServices.PhanHoiDanhGiaClientService.PhanHoiDanhGiaClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/client/phan-hoi")
public class PhanHoiDanhGiaClientController {
    private final PhanHoiDanhGiaClientService phanHoiDanhGiaClientService;

    @GetMapping("/{idDanhGia}/phan-hoi")
    public ResponseEntity<?> getPhanHoiTheoDanhGia(@PathVariable Integer idDanhGia) {
        List<PhanHoiDanhGiaClientResponse> responses = phanHoiDanhGiaClientService.getPhanHoiDanhTheoDanhGia(idDanhGia);
        return ResponseEntity.ok(responses);
    }


}
