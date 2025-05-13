package org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices;

import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.HoaDonAdminResponse;
import org.example.websitetechworld.Repository.HoaDonRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HoaDonAdminService {
    private final HoaDonRepository hoaDonRepository;

    public HoaDonAdminService(HoaDonRepository hoaDonRepository) {
        this.hoaDonRepository = hoaDonRepository;
    }

    public List<HoaDonAdminResponse> getAllHoaDon(){
        return hoaDonRepository.findAll().stream().map(HoaDonAdminResponse::convertDto).collect(Collectors.toList());
    }

    public List<HoaDonAdminResponse> getPageHoaDon(Integer pageNo, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        System.out.println("So phan tu: " + pageable.getPageNumber());
        return hoaDonRepository.findAll(pageable).stream().map(HoaDonAdminResponse::convertDto).toList();

    }


}
