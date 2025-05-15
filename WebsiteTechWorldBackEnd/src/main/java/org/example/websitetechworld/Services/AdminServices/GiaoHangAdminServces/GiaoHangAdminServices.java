package org.example.websitetechworld.Services.AdminServices.GiaoHangAdminServces;

import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseGiaoHang.GetAllGiaoHangResponseAdmin;
import org.example.websitetechworld.Repository.GiaoHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GiaoHangAdminServices {
    private final GiaoHangRepository giaoHangRepository;

    public GiaoHangAdminServices(GiaoHangRepository giaoHangRepository) {
        this.giaoHangRepository = giaoHangRepository;
    }

    public List<GetAllGiaoHangResponseAdmin> getPageGiaoHang(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        return giaoHangRepository.findAll(pageable).stream()
                .map(GetAllGiaoHangResponseAdmin::convertDto).toList();
    }


}
