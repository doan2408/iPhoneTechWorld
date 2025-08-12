package org.example.websitetechworld.Services.CommonSerivces.ThanhToanCommonServices;

import org.example.websitetechworld.Enum.HoaDon.TenHinhThuc;
import org.example.websitetechworld.Services.AdminServices.ThanhToanAdminServices.ChuyenKhoanStrategy;
import org.example.websitetechworld.Services.AdminServices.ThanhToanAdminServices.TienMatStrategy;
import org.example.websitetechworld.Services.ClientServices.ThanhToanClientService.CodStrategy;
import org.example.websitetechworld.Services.ClientServices.ThanhToanClientService.NganHangStrategy;
import org.example.websitetechworld.Services.ClientServices.ThanhToanClientService.VnpayStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ThanhToanFactory {
    private final Map<String, ThanhToanStrategy> strategies = new HashMap<>();

    @Autowired
    public ThanhToanFactory(List<ThanhToanStrategy> strategyList) {
        for (ThanhToanStrategy strategy : strategyList) {
            if (strategy instanceof TienMatStrategy) {
                strategies.put(TenHinhThuc.TIEN_MAT.name(), strategy);
            } else if (strategy instanceof ChuyenKhoanStrategy) {
                strategies.put(TenHinhThuc.CHUYEN_KHOAN.name(), strategy);
            } else if (strategy instanceof NganHangStrategy) {
                strategies.put(TenHinhThuc.NGAN_HANG.name(),strategy);
            } else if (strategy instanceof CodStrategy) {
                strategies.put(TenHinhThuc.COD.name(),strategy);
            }else if (strategy instanceof VnpayStrategy) {
                strategies.put(TenHinhThuc.VNPAY.name(), strategy);
            }

        }
    }

    public ThanhToanStrategy getStrategy(String method) {
        ThanhToanStrategy strategy = strategies.get(method);
        if (strategy == null) {
            throw new IllegalArgumentException("Phương thức thanh toán không hợp lệ: " + method);
        }
        return strategy;
    }

}
