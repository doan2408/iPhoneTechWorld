package org.example.websitetechworld.Services.ClientServices.DiemServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Response.ClientResponse.DiemResponse.HangClientResponse;
import org.example.websitetechworld.Repository.HangThanhVienRepository;
import org.example.websitetechworld.Services.LoginServices.CustomUserDetails;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HangServices {
    private final HangThanhVienRepository hangRepo;
    private final ModelMapper modelMapper;

    public String tenHang() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer idKhachHang = null;
        if(authentication != null && authentication.getPrincipal() instanceof CustomUserDetails){
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            idKhachHang = userDetails.getId();
        }
        return hangRepo.tenHangThanhVien(idKhachHang);
    }

    public List<HangClientResponse> getAllHang() {
        return hangRepo.getListHangThanhVien().stream()
                .map(h -> modelMapper.map(h, HangClientResponse.class))
                .collect(Collectors.toList());
    }
}
