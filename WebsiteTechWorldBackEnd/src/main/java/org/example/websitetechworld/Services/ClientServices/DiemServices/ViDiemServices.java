package org.example.websitetechworld.Services.ClientServices.DiemServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.PhieuGiamGiaAdminRequest.PhieuGiamGiaAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.PhieuGiamGiaAdminResponse.PhieuGiamGiaAdminResponse;
import org.example.websitetechworld.Dto.Response.ClientResponse.DiemResponse.ViDiemClientResponse;
import org.example.websitetechworld.Entity.PhieuGiamGia;
import org.example.websitetechworld.Entity.ViDiem;
import org.example.websitetechworld.Repository.ViDiemRepository;
import org.example.websitetechworld.Services.LoginServices.CustomUserDetails;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ViDiemServices {
    private final ViDiemRepository viDiemRepository;
    private final ModelMapper modelMapper;

    // get diemKhaDung by idKhachHang
    public ViDiemClientResponse getDiemKhaDung() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer idKhachHang = null;
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            idKhachHang = userDetails.getId();
        }
        Optional<ViDiem> optionalViDiem = viDiemRepository.findByIdKhachHang(idKhachHang);
        ViDiem viDiem = optionalViDiem.get();

        return modelMapper.map(viDiem, ViDiemClientResponse.class);
    }

}
