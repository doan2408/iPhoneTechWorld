package org.example.websitetechworld.Controller.ClientController.HoaDonClientController;

import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.PaymentMethodResponse;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.PaymentMethod.PaymentMethodServices;
import org.example.websitetechworld.Services.CommonSerivces.ThanhToanCommonServices.VnpayService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payment-methods")
public class PaymentMethodClientController {

    private final PaymentMethodServices paymentMethodServices;
    private final VnpayService vnpayService;

    public PaymentMethodClientController(PaymentMethodServices paymentMethodServices, VnpayService vnpayService) {
        this.paymentMethodServices = paymentMethodServices;
        this.vnpayService = vnpayService;
    }

    @GetMapping
    public ResponseEntity<List<PaymentMethodResponse>> getAllPaymentMethods() {
        List<PaymentMethodResponse> paymentMethods = paymentMethodServices.getPaymentMethoOnline();
        System.out.println(paymentMethods);
        return ResponseEntity.ok(paymentMethods);
    }

    @PostMapping("/vnpay-ipn")
    public ResponseEntity<String> vnpayIPN(@RequestParam Map<String, String> params) throws Exception {
        boolean valid = vnpayService.verifyPayment(params);
        String responseCode = params.get("vnp_ResponseCode");
        String txnRef = params.get("vnp_TxnRef");

        if (!valid) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }

        if ("00".equals(responseCode)) {
            // Cập nhật trạng thái đơn hàng theo txnRef = đã thanh toán thành công
            // ... code cập nhật DB ...
            return ResponseEntity.ok("OK");
        } else {
            // Giao dịch thất bại hoặc bị hủy, có thể cập nhật trạng thái tương ứng
            return ResponseEntity.ok("FAILED");
        }
    }
}
