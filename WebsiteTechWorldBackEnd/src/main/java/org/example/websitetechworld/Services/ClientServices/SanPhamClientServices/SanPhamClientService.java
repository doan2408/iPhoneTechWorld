package org.example.websitetechworld.Services.ClientServices.SanPhamClientServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Response.ClientResponse.SanPhamClientResponse.*;
import org.example.websitetechworld.Entity.SanPham;
import org.example.websitetechworld.Entity.SanPhamChiTiet;
import org.example.websitetechworld.Repository.LoaiRepository;
import org.example.websitetechworld.Repository.MauSacRepository;
import org.example.websitetechworld.Repository.RomRepository;
import org.example.websitetechworld.Repository.SanPhamRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SanPhamClientService {
    private final SanPhamRepository sanPhamRepo;
    private final MauSacRepository mauSacRepository;
    private final RomRepository romRepository;
    private final ModelMapper modelMapper;
    private final LoaiRepository loaiRepository;

    //hien thi san pham len trang chủ
    public Page<ClientProductResponse> getAllSanPhamHome(int page, int size, String tenSanPham, Integer idLoai, BigDecimal giaMin, BigDecimal giaMax, String sort) {
        Page<ClientProductResponse> homePage;

        Pageable pageable = PageRequest.of(page, size);


        if (tenSanPham != null) {
            tenSanPham = tenSanPham.trim();
            if (tenSanPham.isEmpty()) {
                tenSanPham = null;
            }
        }

        homePage = sanPhamRepo.getSanPhamHome(tenSanPham, idLoai, giaMin, giaMax, sort, pageable);
        return homePage;
    }

    //click 1 sp trang chủ -> chon bien the de mua
    public ClientProductDetailResponse getSanPhamDetail(int idSanPham) {
        SanPham sanPham = sanPhamRepo.findById(idSanPham).orElse(null);
        if (sanPham == null) return null;

        List<ClientProductDetailResponse.ThuocTinh> listMau = new ArrayList<>();
        for (Object[] row : mauSacRepository.getMauByIdSanPham(idSanPham)) {
            Integer idMauSac = (Integer) row[0];
            String tenMau = (String) row[1];
            listMau.add(new ClientProductDetailResponse.ThuocTinh(idMauSac, tenMau));
        }

        List<ClientProductDetailResponse.ThuocTinh> listRom = new ArrayList<>();
        for (Object[] row : romRepository.getRomByIdSanPham(idSanPham)) {
            Integer idRom = (Integer) row[0];
            String tenRom = (String) row[1];
            listRom.add(new ClientProductDetailResponse.ThuocTinh(idRom, tenRom));
        }
        System.out.println("list rom: " + listRom.getFirst().getTen());

        ClientProductDetailResponse response = new ClientProductDetailResponse();
        response.setIdSpct(sanPham.getId());
        response.setTenSanPham(sanPham.getTenSanPham());
        List<String> hinhAnh = sanPhamRepo.getFirstAnh(idSanPham);
        response.setHinhAnh(hinhAnh);
        BigDecimal giaBan = sanPhamRepo.giaThapNhat(idSanPham);
        response.setGiaBan(giaBan);
        response.setMau(listMau);
        response.setRom(listRom);
        Integer tongSoLuong = sanPhamRepo.tongSoLuong(idSanPham);
        response.setTongSoLuong(tongSoLuong);
        return response;
    }

    //hien thi thong so tuong ung voi rom
    public ThongSoResponse getThongSo(Integer idSp, Integer idRom) {
        ThongSoResponse response = sanPhamRepo.getThongSoByIdSpct(idSp, idRom);
        return response !=null ? response : new ThongSoResponse();
    }

    public ThongSoCompareResponse getThongSoLimitRomMin(Integer idSp) {
        ThongSoCompareResponse response = sanPhamRepo.getThongSoByIdSp(idSp);
        return response !=null ? response : new ThongSoCompareResponse();
    }

    //khi chon mau + rom
    public ClientProductDetailResponse getChiTietBienThe(Integer idSanPham, Integer idMau, Integer idRom) {

        Optional<SanPhamChiTiet> chiTietOtp = sanPhamRepo.getSpctByMauAndRom(idSanPham, idMau, idRom);
        if (chiTietOtp.isEmpty()) {
            throw new RuntimeException("Không tim thấy sản phẩm với màu và rom tương ứng");
        }

        SanPhamChiTiet spct = chiTietOtp.get();
        SanPham sp = spct.getIdSanPham();

        ClientProductDetailResponse response = new ClientProductDetailResponse();
        response.setIdSpct(spct.getId());
        response.setTenSanPham(sp.getTenSanPham());
        List<String> hinhAnh = sanPhamRepo.getListAnhByMau(idSanPham, idMau);
        response.setHinhAnh(hinhAnh);
        response.setGiaBan(spct.getGiaBan());

        List<ClientProductDetailResponse.ThuocTinh> listRom = sp.getSanPhamChiTiets().stream()
                .map(SanPhamChiTiet::getIdRom)
                .distinct()
                .map(r -> new ClientProductDetailResponse.ThuocTinh(r.getId(), r.getDungLuong()))
                .toList();
        response.setRom(listRom);

        List<ClientProductDetailResponse.ThuocTinh> listMau = sp.getSanPhamChiTiets().stream()
                .map(SanPhamChiTiet::getIdMau)
                .distinct()
                .map(m -> new ClientProductDetailResponse.ThuocTinh(m.getId(), m.getTenMau()))
                .toList();
        response.setMau(listMau);

        response.setSoLuong(spct.getSoLuong());
        response.setTongSoLuong(sp.getSanPhamChiTiets().stream()
                .mapToInt(SanPhamChiTiet::getSoLuong).sum()
        );

        return response;
    }

    public List<String> getListAnhByMau(Integer idSp, Integer idMau) {
        return sanPhamRepo.getListAnhByMau(idSp, idMau);
    }

    public List<LoaiClientResponse> getLoaiClientResponses() {
        return loaiRepository.getLoaiClientResponse();
    }

}
