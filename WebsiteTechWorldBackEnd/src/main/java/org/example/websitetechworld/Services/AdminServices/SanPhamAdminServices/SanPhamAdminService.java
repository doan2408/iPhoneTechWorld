package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;

import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.HinhAnhAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.ImeiAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.SanPhamAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.SanPhamChiTietAdminRepuest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.*;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Repository.*;
import org.example.websitetechworld.exception.ResourceNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SanPhamAdminService {
    private final SanPhamRepository sanPhamRepo;
    private final NhaCungCapRepository nhaCungCapRepository;
    private final MauSacRepository mauSacRepository;
    private final RamRepository ramRepository;
    private final RomRepository romRepository;
    private final ManHinhRepository manHinhRepository;
    private final HeDieuHanhRepository heDieuHanhRepository;
    private final PinRepository pinRepository;
    private final CpuRepository cpuRepository;
    private final CameraTruocRepository cameraTruocRepository;
    private final CameraSauRepository cameraSauRepository;
    private final XuatXuRepository xuatXuRepository;
    private final LoaiRepository loaiRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;
    private final HinhAnhRepository hinhAnhRepository;
    private final ImeiReposiory imeiReposiory;

    private final ModelMapper modelMapper;


    private SanPhamChiTietResponse mapToChiTietResponse(SanPhamChiTiet chiTiet) {
        SanPhamChiTietResponse response = new SanPhamChiTietResponse();

        response.setId(chiTiet.getId());
        response.setSoLuongSPCT(chiTiet.getSoLuong());
        response.setGiaBan(chiTiet.getGiaBan());

        // map các trường liên quan nếu cần (ví dụ tên màu, ram, rom...)
        if (chiTiet.getIdMau() != null) {
            response.setTenMau(chiTiet.getIdMau().getTenMau());
        }
        if (chiTiet.getIdRam() != null) {
            response.setLoaiRam(chiTiet.getIdRam().getLoai());
        }
        if (chiTiet.getIdRom() != null) {
            response.setLoaiRom(chiTiet.getIdRom().getLoai());
        }
        // tương tự cho các thuộc tính liên quan khác...

        // map danh sách hình ảnh
        if (chiTiet.getHinhAnhs() != null) {
            Set<HinhAnhAdminResponse> hinhAnhResponses = chiTiet.getHinhAnhs().stream()
                    .map(ha -> {
                        HinhAnhAdminResponse haRes = new HinhAnhAdminResponse();
                        haRes.setId(ha.getId());
                        haRes.setUrl(ha.getUrl());
                        haRes.setImagePublicId(ha.getImagePublicId());
                        // thêm các trường khác nếu có
                        return haRes;
                    }).collect(Collectors.toSet());
            response.setUrl(hinhAnhResponses.toString());
        }

        // map danh sách imei
        if (chiTiet.getImeis() != null) {
            Set<ImeiAdminResponse> imeiResponses = chiTiet.getImeis().stream()
                    .map(imei -> {
                        ImeiAdminResponse imeiRes = new ImeiAdminResponse();
                        imeiRes.setId(imei.getId());
                        imeiRes.setSoImei(imei.getSoImei());
                        // thêm các trường khác nếu có
                        return imeiRes;
                    }).collect(Collectors.toSet());
            response.setImei(imeiResponses.toString());
        }

        return response;
    }


    private SanPhamAdminResponse mapToSanPhamAdminResponse(SanPham sanPham) {
        SanPhamAdminResponse response = new SanPhamAdminResponse();

        response.setId(sanPham.getId());
        response.setTenSanPham(sanPham.getTenSanPham());
        response.setThuongHieu(sanPham.getThuongHieu());
        // map các trường khác tương tự

        if (sanPham.getIdNhaCungCap() != null) {
            response.setTenNhaCungCap(sanPham.getIdNhaCungCap().getTenNhaCungCap());
            // hoặc set id nhà cung cấp nếu có
            response.setTenNhaCungCap(sanPham.getIdNhaCungCap().getTenNhaCungCap());
        }

        // map danh sách chi tiết sản phẩm
        if (sanPham.getSanPhamChiTiets() != null) {
            Set<SanPhamChiTietResponse> chiTietResponses = sanPham.getSanPhamChiTiets().stream()
                    .map(this::mapToChiTietResponse)
                    .collect(Collectors.toSet());
            response.setSanPhamChiTiets(chiTietResponses);
        }

        return response;
    }




    //this đại diện cho instance (thể hiện) của lớp hiện tại
    // – tức là class chứa phương thức getAllSanPham() và convert().
    public Page<SanPhamHienThiAdminResponse> getAllSanPham(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return sanPhamRepo.getAllHienThi(pageable);
    }

    public SanPhamAdminResponse detailSanPhamAdmin(Integer id) {
        Optional<SanPham> sanPham = sanPhamRepo.findById(id);
        if (sanPham.isPresent()) {
            return mapToSanPhamAdminResponse(sanPham.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "khong tim thay san pham hop le:" + id);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public SanPhamAdminResponse createSanPhamAdmin(SanPhamAdminRequest sanPhamAdminRequest) {
        // Bước 1: Tạo đối tượng SanPham từ request thủ công
        SanPham sanPham = new SanPham();
        sanPham.setMaSanPham(sanPhamAdminRequest.getMaSanPham());
        sanPham.setTenSanPham(sanPhamAdminRequest.getTenSanPham());
        sanPham.setThuongHieu(sanPhamAdminRequest.getThuongHieu());

        // Gán nhà cung cấp nếu có
        if (sanPhamAdminRequest.getIdNhaCungCap() != null) {
            NhaCungCap nhaCungCap = nhaCungCapRepository.findById(sanPhamAdminRequest.getIdNhaCungCap())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhà cung cấp với ID: " + sanPhamAdminRequest.getIdNhaCungCap()));
            sanPham.setIdNhaCungCap(nhaCungCap);
        }

        // Lưu sản phẩm để có ID
        sanPham = sanPhamRepo.save(sanPham);

        Set<SanPhamChiTiet> chiTietSet = new HashSet<>();

        // Bước 2: Lưu danh sách chi tiết sản phẩm (nếu có)
        if (sanPhamAdminRequest.getSanPhamChiTiets() != null && !sanPhamAdminRequest.getSanPhamChiTiets().isEmpty()) {
            for (SanPhamChiTietAdminRepuest rq : sanPhamAdminRequest.getSanPhamChiTiets()) {
                if (rq.getSoLuong() < 0) {
                    throw new IllegalArgumentException("Số lượng không hợp lệ");
                }
                if (rq.getGiaBan() == null || rq.getGiaBan().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new IllegalArgumentException("Giá bán phải lớn hơn 0");
                }

                // Map thủ công chi tiết sản phẩm
                SanPhamChiTiet chiTiet = new SanPhamChiTiet();
                chiTiet.setSoLuong(rq.getSoLuong());
                chiTiet.setGiaBan(rq.getGiaBan());
                chiTiet.setIdSanPham(sanPham);

                chiTiet.setIdMau(mauSacRepository.findById(rq.getIdMau())
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy màu sắc với ID: " + rq.getIdMau())));
                chiTiet.setIdRam(ramRepository.findById(rq.getIdRam())
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy RAM với ID: " + rq.getIdRam())));
                chiTiet.setIdRom(romRepository.findById(rq.getIdRom())
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ROM với ID: " + rq.getIdRom())));
                chiTiet.setIdManHinh(manHinhRepository.findById(rq.getIdManHinh())
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy màn hình với ID: " + rq.getIdManHinh())));
                chiTiet.setIdHeDieuHanh(heDieuHanhRepository.findById(rq.getIdHeDieuHanh())
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hệ điều hành với ID: " + rq.getIdHeDieuHanh())));
                chiTiet.setIdPin(pinRepository.findById(rq.getIdPin())
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy pin với ID: " + rq.getIdPin())));
                chiTiet.setIdCpu(cpuRepository.findById(rq.getIdCpu())
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy CPU với ID: " + rq.getIdCpu())));
                chiTiet.setIdCameraTruoc(cameraTruocRepository.findById(rq.getIdCameraTruoc())
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy camera trước với ID: " + rq.getIdCameraTruoc())));
                chiTiet.setIdCameraSau(cameraSauRepository.findById(rq.getIdCameraSau())
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy camera sau với ID: " + rq.getIdCameraSau())));
                chiTiet.setIdXuatXu(xuatXuRepository.findById(rq.getIdXuatXu())
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xuất xứ với ID: " + rq.getIdXuatXu())));
                chiTiet.setIdLoai(loaiRepository.findById(rq.getIdLoai())
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy loại với ID: " + rq.getIdLoai())));

                // Lưu chi tiết sản phẩm
                SanPhamChiTiet chiTietSaved = sanPhamChiTietRepository.save(chiTiet);

                // Bước 3: Gán hình ảnh
                if (rq.getHinhAnhs() != null && !rq.getHinhAnhs().isEmpty()) {
                    for (HinhAnhAdminRequest ha : rq.getHinhAnhs()) {
                        HinhAnh hinhAnh = new HinhAnh();
                        hinhAnh.setIdSanPhamChiTiet(chiTietSaved);
                        hinhAnh.setUrl(ha.getUrl());
                        hinhAnh.setImagePublicId(ha.getImagePublicId());
                        hinhAnhRepository.save(hinhAnh);
                    }
                }

                // Bước 4: Gán IMEI
                if (rq.getImeis() != null && !rq.getImeis().isEmpty()) {
                    for (ImeiAdminRequest imeiDto : rq.getImeis()) {
                        if (imeiDto.getSoImei() == null || imeiDto.getSoImei().trim().isEmpty()) {
                            throw new IllegalArgumentException("Số IMEI không được để trống");
                        }

                        Imei newImei = new Imei();
                        newImei.setSoImei(imeiDto.getSoImei().trim());
                        newImei.setTrangThaiImei(imeiDto.getTrangThaiImei());
                        newImei.setNhaMang(imeiDto.getNhaMang());
                        newImei.setIdSanPhamChiTiet(chiTietSaved);
                        imeiReposiory.save(newImei);
                    }

                }

                System.out.println("IMEIs của chi tiết sp: " + rq.getImeis());
                chiTietSet.add(chiTietSaved);
            }
        }

        // Gán lại danh sách chi tiết vào sản phẩm
        sanPham.setSanPhamChiTiets(chiTietSet);

        // Tạo thủ công response
        SanPhamAdminResponse response = new SanPhamAdminResponse();
        response.setId(sanPham.getId());
        response.setMaSanPham(sanPham.getMaSanPham());
        response.setTenSanPham(sanPham.getTenSanPham());
        response.setThuongHieu(sanPham.getThuongHieu());
        if (sanPham.getIdNhaCungCap() != null) {
            response.setTenNhaCungCap(sanPham.getIdNhaCungCap().getTenNhaCungCap());
        }

        if (sanPham.getSanPhamChiTiets() != null) {
            Set<SanPhamChiTietResponse> chiTietResponses = sanPham.getSanPhamChiTiets().stream()
                    .map(this::mapToChiTietResponse)
                    .collect(Collectors.toSet());
            response.setSanPhamChiTiets(chiTietResponses);
        }

        return response;
    }



    @Transactional
    public SanPhamAdminResponse updateSanPhamAdmin(Integer id, SanPhamAdminRequest sanPhamAdminRequest) {

        SanPham sanPham = sanPhamRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + id));

        sanPham.setTenSanPham(sanPhamAdminRequest.getTenSanPham());
        sanPham.setThuongHieu(sanPhamAdminRequest.getThuongHieu());

        if (sanPhamAdminRequest.getIdNhaCungCap() != null) {
            NhaCungCap nhaCungCap = nhaCungCapRepository.findById(sanPhamAdminRequest.getIdNhaCungCap())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhà cung cấp với ID: " + sanPhamAdminRequest.getIdNhaCungCap()));

            sanPham.setIdNhaCungCap(nhaCungCap);
        }

        SanPham updatedSanPham = sanPhamRepo.save(sanPham);

        SanPhamAdminResponse dto = new SanPhamAdminResponse();
        dto.setId(updatedSanPham.getId());
        dto.setTenSanPham(updatedSanPham.getTenSanPham());
        dto.setThuongHieu(updatedSanPham.getThuongHieu());
        dto.setSoLuongTonKho(updatedSanPham.getSoLuongTonKho());

        if (updatedSanPham.getIdNhaCungCap() != null) {
            dto.setTenNhaCungCap(updatedSanPham.getIdNhaCungCap().getTenNhaCungCap());
        }

        return dto;
    }


    @Transactional
    public SanPhamAdminResponse deleteSanPhamAdmin(Integer id) {

        SanPham sanPham = sanPhamRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + id));

        SanPhamAdminResponse sanPhamAdminResponse = new SanPhamAdminResponse();
        sanPhamAdminResponse.setId(sanPham.getId());
        sanPhamAdminResponse.setTenSanPham(sanPham.getTenSanPham());
        sanPhamAdminResponse.setThuongHieu(sanPham.getThuongHieu());

        sanPhamRepo.deleteById(id);

        return sanPhamAdminResponse;
    }

}


