package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Response.ClientResponse.SanPhamClientResponse.BaoHanhAdminResponse;
import org.example.websitetechworld.Dto.Response.ClientResponse.SanPhamClientResponse.ImeiAdminResponse;
import org.example.websitetechworld.Dto.Response.ClientResponse.SanPhamClientResponse.SanPhamChiTietResponse;
import org.example.websitetechworld.Entity.SanPhamChiTiet;
import org.example.websitetechworld.Repository.SanPhamChiTietRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SanPhamChiTietAdminService {

    private final SanPhamChiTietRepository sanPhamChiTietRepo;

    private final ModelMapper modelMapper;

    public SanPhamChiTiet getChiTietById(Integer id) {
        return sanPhamChiTietRepo.findFullById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết sản phẩm với ID: " + id));
    }

    public SanPhamChiTietResponse getSanPhamChiTiet(SanPhamChiTiet entity) {
        // Ánh xạ các thuộc tính cơ bản
        SanPhamChiTietResponse response = modelMapper.map(entity, SanPhamChiTietResponse.class);

        // Ánh xạ các collections phức tạp như Set<BaoHanh> thành List<BaoHanhDTO>
        List<BaoHanhAdminResponse> baoHanhs = modelMapper.map(entity.getBaoHanhs(), new TypeToken<List<BaoHanhAdminResponse>>(){}.getType());
        response.setBaoHanhs(baoHanhs);

        // Ánh xạ các collection khác tương tự
        List<ImeiAdminResponse> imeiList = modelMapper.map(entity.getImeis(), new TypeToken<List<ImeiAdminResponse>>() {}.getType());
        response.setImeiList(imeiList);

        // Ánh xạ Set<HinhAnh> thành List<String> nếu cần
        List<String> hinhAnhUrls = modelMapper.map(entity.getHinhAnhs(), new TypeToken<List<String>>() {}.getType());
        response.setHinhAnhUrls(hinhAnhUrls);

        return response;
    }
}
