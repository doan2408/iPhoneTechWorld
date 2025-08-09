package org.example.websitetechworld.Controller.ClientController.HoaDonClientController;

import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.PaymentMethodResponse;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.PaymentMethod.PaymentMethodServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/payment-methods")
public class PaymentMethodClientController {

    private final PaymentMethodServices paymentMethodServices;

    public PaymentMethodClientController(PaymentMethodServices paymentMethodServices) {
        this.paymentMethodServices = paymentMethodServices;
    }

    @GetMapping
    public ResponseEntity<List<PaymentMethodResponse>> getAllPaymentMethods() {
        List<PaymentMethodResponse> paymentMethods = paymentMethodServices.getPaymentMethoOnline();
        System.out.println(paymentMethods);
        return ResponseEntity.ok(paymentMethods);
    }
}
