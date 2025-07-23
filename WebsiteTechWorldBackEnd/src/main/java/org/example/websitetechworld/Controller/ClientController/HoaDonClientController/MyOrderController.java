package org.example.websitetechworld.Controller.ClientController.HoaDonClientController;

import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.HoaDonAdminResponse;
import org.example.websitetechworld.Dto.Response.ClientResponse.HoaDonClientResponse.MyOrderClientResponse;
import org.example.websitetechworld.Services.ClientServices.HoaDonClientServices.MyOrderClientServices;
import org.example.websitetechworld.Services.LoginServices.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/client/my-order")
public class MyOrderController {
    private final MyOrderClientServices myOrderClientServices;

    public MyOrderController(MyOrderClientServices myOrderClientServices) {
        this.myOrderClientServices = myOrderClientServices;
    }

    @GetMapping()
    public List<MyOrderClientResponse> myOrder() {
        List<MyOrderClientResponse> myOrderClientResponseList = new ArrayList<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails customUserDetails){
            Integer userLoginId = customUserDetails.getId();
            myOrderClientResponseList =myOrderClientServices.getOrderByUserLogin(userLoginId);
        }
        return myOrderClientResponseList;

        }

    @GetMapping("/mvd/{maVanDon}")
    public List<Integer> findIdHoaDonByMVD(@PathVariable String maVanDon) {
        return myOrderClientServices.findIdHoaDonByMVD(maVanDon);
    }

    @GetMapping("/{idHoaDon}")
    public HoaDonAdminResponse findById(@PathVariable("idHoaDon") int idHoaDon) {
        return myOrderClientServices.findById(idHoaDon);
    }
}
