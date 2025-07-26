package org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.PaymentMethod;

import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.PaymentMethodResponse;
import org.example.websitetechworld.Entity.PhuongThucThanhToan;
import org.example.websitetechworld.Enum.HoaDon.TenHinhThuc;
import org.example.websitetechworld.Repository.PhuongThucThanhToanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentMethodServices {
    private final PhuongThucThanhToanRepository phuongThucThanhToanRepository;

    public PaymentMethodServices(PhuongThucThanhToanRepository phuongThucThanhToanRepository) {
        this.phuongThucThanhToanRepository = phuongThucThanhToanRepository;
    }

    @Transactional
    public List<PaymentMethodResponse> getPaymentMethoOffine(){
        List<PhuongThucThanhToan> paymentMethods = phuongThucThanhToanRepository.findByLoaiHinhThuc("OFFLINE");
        List<PaymentMethodResponse> lstPaymentMethod =  paymentMethods.stream()
                .map(method -> new PaymentMethodResponse(
                        method.getTenPhuongThuc().name(),
                        method.getTenPhuongThuc().getDisplayName()
                ))
                .collect(Collectors.toList());
        return lstPaymentMethod;
    }
    @Transactional
    public List<PaymentMethodResponse> getPaymentMethoOnline(){
        List<PhuongThucThanhToan> paymentMethods = phuongThucThanhToanRepository.findByLoaiHinhThuc("ONLINE");
        List<PaymentMethodResponse> lstPaymentMethod =  paymentMethods.stream()
                .map(method -> new PaymentMethodResponse(
                        method.getTenPhuongThuc().name(),
                        method.getTenPhuongThuc().getDisplayName()
                ))
                .collect(Collectors.toList());
        return lstPaymentMethod;
    }
}
