package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;

import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.HinhAnhAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.ImeiAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.SanPhamAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.SanPhamChiTietAdminRepuest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.*;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Enum.SanPham.TrangThaiSanPham;
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

        // Bước 1: Validate request
        if (sanPhamAdminRequest == null || sanPhamAdminRequest.getTenSanPham() == null) {
            throw new IllegalArgumentException("Dữ liệu sản phẩm không hợp lệ");
        }
        if (sanPhamAdminRequest.getIdNhaCungCap() != null && !nhaCungCapRepository.existsById(sanPhamAdminRequest.getIdNhaCungCap())) {
            throw new ResourceNotFoundException("Không tìm thấy nhà cung cấp với ID: " + sanPhamAdminRequest.getIdNhaCungCap());
        }

        // Bước 2: Tạo SanPham
        SanPham sanPham = new SanPham();
        sanPham.setTenSanPham(sanPhamAdminRequest.getTenSanPham());
        sanPham.setThuongHieu(sanPhamAdminRequest.getThuongHieu());
        sanPham.setTrangThaiSanPham(
                Optional.ofNullable(sanPhamAdminRequest.getTrangThaiSanPham())
                        .orElse(TrangThaiSanPham.COMING_SOON)
        );

        if (sanPhamAdminRequest.getIdNhaCungCap() != null) {
            NhaCungCap nhaCungCap = nhaCungCapRepository.findById(sanPhamAdminRequest.getIdNhaCungCap()).orElseThrow();
            sanPham.setIdNhaCungCap(nhaCungCap);
        }

        sanPham = sanPhamRepo.save(sanPham);

        Set<SanPhamChiTiet> chiTietSet = new HashSet<>();

        // Bước 3: Lưu chi tiết sản phẩm
        if (sanPhamAdminRequest.getSanPhamChiTiets() != null && !sanPhamAdminRequest.getSanPhamChiTiets().isEmpty()) {
            for (SanPhamChiTietAdminRepuest rq : sanPhamAdminRequest.getSanPhamChiTiets()) {
                // Validate chi tiết
                if (rq.getSoLuong() == null || rq.getSoLuong() < 0) {
                    throw new IllegalArgumentException("Số lượng không hợp lệ");
                }
                if (rq.getGiaBan() == null || rq.getGiaBan().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new IllegalArgumentException("Giá bán phải lớn hơn 0");
                }
//                if (rq.getMaSanPhamChiTiet() != null && sanPhamChiTietRepository.existsByMaSanPhamChiTiet(rq.getMaSanPhamChiTiet())) {
//                    throw new IllegalArgumentException("Mã sản phẩm chi tiết đã tồn tại: " + rq.getMaSanPhamChiTiet());
//                }

                // Tạo chi tiết sản phẩm
                SanPhamChiTiet chiTiet = new SanPhamChiTiet();
                chiTiet.setMaSanPhamChiTiet(rq.getMaSanPhamChiTiet());
                chiTiet.setSoLuong(rq.getSoLuong());
                chiTiet.setGiaBan(rq.getGiaBan());
                chiTiet.setIdSanPham(sanPham);

                // Validate foreign keys
                if (rq.getIdMau() != null) {
                    chiTiet.setIdMau(mauSacRepository.findById(rq.getIdMau())
                            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy màu sắc với ID: " + rq.getIdMau())));
                }
                if (rq.getIdRam() != null) {
                    chiTiet.setIdRam(ramRepository.findById(rq.getIdRam())
                            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy RAM với ID: " + rq.getIdRam())));
                }
                if (rq.getIdRom() != null) {
                    chiTiet.setIdRom(romRepository.findById(rq.getIdRom())
                            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ROM với ID: " + rq.getIdRom())));
                }
                if (rq.getIdManHinh() != null) {
                    chiTiet.setIdManHinh(manHinhRepository.findById(rq.getIdManHinh())
                            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy màn hình với ID: " + rq.getIdManHinh())));
                }
                if (rq.getIdHeDieuHanh() != null) {
                    chiTiet.setIdHeDieuHanh(heDieuHanhRepository.findById(rq.getIdHeDieuHanh())
                            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hệ điều hành với ID: " + rq.getIdHeDieuHanh())));
                }
                if (rq.getIdPin() != null) {
                    chiTiet.setIdPin(pinRepository.findById(rq.getIdPin())
                            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy pin với ID: " + rq.getIdPin())));
                }
                if (rq.getIdCpu() != null) {
                    chiTiet.setIdCpu(cpuRepository.findById(rq.getIdCpu())
                            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy CPU với ID: " + rq.getIdCpu())));
                }
                if (rq.getIdCameraTruoc() != null) {
                    chiTiet.setIdCameraTruoc(cameraTruocRepository.findById(rq.getIdCameraTruoc())
                            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy camera trước với ID: " + rq.getIdCameraTruoc())));
                }
                if (rq.getIdCameraSau() != null) {
                    chiTiet.setIdCameraSau(cameraSauRepository.findById(rq.getIdCameraSau())
                            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy camera sau với ID: " + rq.getIdCameraSau())));
                }
                if (rq.getIdXuatXu() != null) {
                    chiTiet.setIdXuatXu(xuatXuRepository.findById(rq.getIdXuatXu())
                            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xuất xứ với ID: " + rq.getIdXuatXu())));
                }
                if (rq.getIdLoai() != null) {
                    chiTiet.setIdLoai(loaiRepository.findById(rq.getIdLoai())
                            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy loại với ID: " + rq.getIdLoai())));
                }

                // Lưu chi tiết
                SanPhamChiTiet chiTietSaved = sanPhamChiTietRepository.save(chiTiet);

                // Lưu hình ảnh
                if (rq.getHinhAnhs() != null && !rq.getHinhAnhs().isEmpty()) {
                    hinhAnhRepository.saveAll(rq.getHinhAnhs().stream()
                            .filter(ha -> ha.getUrl() != null)
                            .map(ha -> {
                                HinhAnh hinhAnh = new HinhAnh();
                                hinhAnh.setIdSanPhamChiTiet(chiTietSaved);
                                hinhAnh.setUrl(ha.getUrl());
                                hinhAnh.setImagePublicId(ha.getImagePublicId());
                                return hinhAnh;
                            })
                            .toList());
                }

                // Lưu IMEI
                if (rq.getImeis() != null && !rq.getImeis().isEmpty()) {
                    imeiReposiory.saveAll(rq.getImeis().stream()
                            .filter(imeiDto -> imeiDto.getSoImei() != null && !imeiDto.getSoImei().trim().isEmpty())
                            .map(imeiDto -> {
//                                if (imeiReposiory.existsBySoImei(imeiDto.getSoImei().trim())) {
//                                    throw new IllegalArgumentException("Số IMEI đã tồn tại: " + imeiDto.getSoImei());
//                                }
                                Imei newImei = new Imei();
                                newImei.setSoImei(imeiDto.getSoImei().trim());
                                newImei.setTrangThaiImei(imeiDto.getTrangThaiImei());
                                newImei.setNhaMang(imeiDto.getNhaMang());
                                newImei.setIdSanPhamChiTiet(chiTietSaved);
                                return newImei;
                            })
                            .toList());
                }

                chiTietSet.add(chiTietSaved);
            }
        }

        // Gán lại chi tiết vào sản phẩm
        sanPham.setSanPhamChiTiets(chiTietSet);

        // Bước 4: Tạo response
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
//        dto.setSoLuongTonKho(updatedSanPham.getSoLuongTonKho());

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


