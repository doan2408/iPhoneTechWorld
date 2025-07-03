package org.example.websitetechworld.Controller.AdminController.HoaDonAdminController;

import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.PaymentMethodResponse;
import org.example.websitetechworld.Enum.HoaDon.LoaiHinhThuc;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/payment-methods")
public class PaymentMethodController {

    @GetMapping
    public ResponseEntity<List<PaymentMethodResponse>> getAllPaymentMethods() {
        List<PaymentMethodResponse> paymentMethods = Arrays.stream(LoaiHinhThuc.values())
                .map(loaiHinhThuc -> new PaymentMethodResponse(
                        loaiHinhThuc.name(),
                        loaiHinhThuc.getDisplayName()   // Lấy tên hiển thị (Tiền mặt, Ngân hàng)
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(paymentMethods);
    }
}
