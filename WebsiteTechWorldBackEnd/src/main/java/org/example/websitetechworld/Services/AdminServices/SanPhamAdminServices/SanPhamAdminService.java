package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;

import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.SanPhamAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.SanPhamChiTietAdminRepuest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.*;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Enum.Imei.TrangThaiImei;
import org.example.websitetechworld.Enum.SanPham.TrangThaiSanPham;
import org.example.websitetechworld.Repository.*;
import org.example.websitetechworld.exception.BusinessException;
import org.example.websitetechworld.exception.NotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SanPhamAdminService {
    private final SanPhamRepository sanPhamRepo;
    private final ModelSanPhamRepository modelSanPhamRepository;
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
    private final SanPhamRepository sanPhamRepository;


    private final ModelMapper modelMapper;


    private SanPhamChiTietResponse mapToChiTietResponse(SanPhamChiTiet chiTiet) {
        SanPhamChiTietResponse response = new SanPhamChiTietResponse();

        response.setId(chiTiet.getId());
        response.setMaSanPhamChiTiet(chiTiet.getMaSanPhamChiTiet());
        response.setSoLuongSPCT(chiTiet.getSoLuong());
        response.setGiaBan(chiTiet.getGiaBan());

//        if (chiTiet.getIdSanPham() != null) {
//            response.setMaSanPham(chiTiet.getIdSanPham().getMaSanPham());
//            response.setTenSanPham(chiTiet.getIdSanPham().getTenSanPham());
//            response.setThuongHieu(chiTiet.getIdSanPham().getThuongHieu());
//            response.setTrangThaiSanPham(chiTiet.getIdSanPham().getTrangThaiSanPham());
//
//            NhaCungCap cungCap = chiTiet.getIdSanPham().getIdNhaCungCap();
//            if (cungCap != null) {
//                response.setTenNhaCungCap(cungCap.getTenNhaCungCap());
//                response.setDiaChi(cungCap.getDiaChi());
//                response.setSdt(cungCap.getSdt());
//                response.setEmail(cungCap.getEmail());
//            }
//        }

        if (chiTiet.getIdMau() != null) {
            response.setTenMau(chiTiet.getIdMau().getTenMau());
            response.setMaMau(chiTiet.getIdMau().getMaMau());
        }
//        if (chiTiet.getIdRam() != null) {
//            response.setLoaiRam(chiTiet.getIdRam().getLoai());
//            response.setDungLuongRam(chiTiet.getIdRam().getDungLuong());
//            response.setTocDoDocGhiRam(chiTiet.getIdRam().getTocDoDocGhi());
//            response.setNhaSanXuatRam(chiTiet.getIdRam().getNhaSanXuat());
//            response.setNamRaMatRam(chiTiet.getIdRam().getNamRaMat());
//        }
        if (chiTiet.getIdRom() != null) {
            response.setLoaiRom(chiTiet.getIdRom().getLoai());
            response.setDungLuongRom(chiTiet.getIdRom().getDungLuong());
            response.setTocDoDocGhiRom(chiTiet.getIdRom().getTocDoDocGhi());
            response.setNhaSanXuatRom(chiTiet.getIdRom().getNhaSanXuat());
            response.setNamRaMatRom(chiTiet.getIdRom().getNamRaMat());
        }
//        if (chiTiet.getIdManHinh() != null) {
//            response.setTenManHinh(chiTiet.getIdManHinh().getTenManHinh());
//            response.setKichThuoc(chiTiet.getIdManHinh().getKichThuoc());
//            response.setLoaiManHinh(chiTiet.getIdManHinh().getLoaiManHinh());
//            response.setDoPhanGiaiManHinh(chiTiet.getIdManHinh().getDoPhanGiai());
//            response.setTanSoQuet(chiTiet.getIdManHinh().getTanSoQuet());
//            response.setDoSang(chiTiet.getIdManHinh().getDoSang());
//            response.setChatLieuKinh(chiTiet.getIdManHinh().getChatLieuKinh());
//        }
//        if (chiTiet.getIdHeDieuHanh() != null) {
//            response.setPhienBanHeDieuHanh(chiTiet.getIdHeDieuHanh().getPhienBan());
//            response.setNhaPhatTrien(chiTiet.getIdHeDieuHanh().getNhaPhatTrien());
//            response.setGiaoDienNguoiDung(chiTiet.getIdHeDieuHanh().getGiaoDienNguoiDung());
//        }
//        if (chiTiet.getIdPin() != null) {
//            response.setPhienBanPin(chiTiet.getIdPin().getPhienBan());
//            response.setCongSuatSac(chiTiet.getIdPin().getCongSuatSac());
//            response.setThoiGianSuDung(chiTiet.getIdPin().getThoiGianSuDung());
//            response.setSoLanSacToiDa(chiTiet.getIdPin().getSoLanSacToiDa());
//        }
//        if (chiTiet.getIdCpu() != null) {
//            response.setHangSanXuat(chiTiet.getIdCpu().getHangSanXuat());
//            response.setSoNhan(chiTiet.getIdCpu().getSoNhan());
//            response.setChipXuLy(chiTiet.getIdCpu().getChipXuLy());
//            response.setXungNhip(chiTiet.getIdCpu().getXungNhip());
//            response.setCongNgheSanXuat(chiTiet.getIdCpu().getCongNgheSanXuat());
//            response.setBoNhoDem(chiTiet.getIdCpu().getBoNhoDem());
//            response.setTieuThuDienNang(chiTiet.getIdCpu().getTieuThuDienNang());
//            response.setNamRaMat(chiTiet.getIdCpu().getNamRaMat());
//        }
//        if (chiTiet.getIdCameraSau() != null) {
//            response.setLoaiCameraSau(chiTiet.getIdCameraSau().getLoaiCamera());
//            response.setDoPhanGiaiCameraSau(chiTiet.getIdCameraSau().getDoPhanGiai());
//            response.setKhauDoCameraSau(chiTiet.getIdCameraSau().getKhauDo());
//            response.setLoaiZoomCameraSau(chiTiet.getIdCameraSau().getLoaiZoom());
//            response.setCheDoChupCameraSau(chiTiet.getIdCameraSau().getCheDoChup());
//        }
//        if (chiTiet.getIdCameraTruoc() != null) {
//            response.setLoaiCameraTruoc(chiTiet.getIdCameraTruoc().getLoaiCamera());
//            response.setDoPhanGiaiCameraTruoc(chiTiet.getIdCameraTruoc().getDoPhanGiai());
//            response.setKhauDoCameraTruoc(chiTiet.getIdCameraTruoc().getKhauDo());
//            response.setLoaiZoomCameraTruoc(chiTiet.getIdCameraTruoc().getLoaiZoom());
//            response.setCheDoChupCameraTruoc(chiTiet.getIdCameraTruoc().getCheDoChup());
//        }
//        if (chiTiet.getIdXuatXu() != null) {
//            response.setMaXuatXu(chiTiet.getIdXuatXu().getMaXuatXu());
//            response.setTenQuocGia(chiTiet.getIdXuatXu().getTenQuocGia());
//        }
//        if (chiTiet.getIdLoai() != null) {
//            response.setTenLoai(chiTiet.getIdLoai().getTenLoai());
//        }

        // map danh sách hình ảnh
        if (chiTiet.getImeis() != null && !chiTiet.getImeis().isEmpty()) {
            Imei firstImei = chiTiet.getImeis().iterator().next();
            ImeiAdminResponse imeiResponse = new ImeiAdminResponse();
            imeiResponse.setSoImei(firstImei.getSoImei());
            imeiResponse.setTrangThaiImei(firstImei.getTrangThaiImei());
            response.setImeis(new LinkedHashSet<>(Collections.singletonList(imeiResponse)));
        } else {
            response.setImeis(new LinkedHashSet<>());
        }

        // Ánh xạ hinhAnhs (chỉ lấy phần tử đầu tiên)
        if (chiTiet.getHinhAnhs() != null && !chiTiet.getHinhAnhs().isEmpty()) {
            HinhAnh firstImage = chiTiet.getHinhAnhs().iterator().next();
            HinhAnhAdminResponse hinhAnhResponse = new HinhAnhAdminResponse();
            hinhAnhResponse.setUrl(firstImage.getUrl());
            hinhAnhResponse.setImagePublicId(firstImage.getImagePublicId());
            response.setHinhAnhs(new LinkedHashSet<>(Collections.singletonList(hinhAnhResponse)));
        } else {
            response.setHinhAnhs(new LinkedHashSet<>());
        }

        return response;
    }


    private SanPhamAdminResponse mapToSanPhamAdminResponse(SanPham sanPham) {
        SanPhamAdminResponse response = new SanPhamAdminResponse();

        response.setId(sanPham.getId());
        response.setMaSanPham(sanPham.getMaSanPham());
        response.setTenSanPham(sanPham.getTenSanPham());
        response.setThuongHieu(sanPham.getThuongHieu());
        response.setTrangThaiSanPham(sanPham.getTrangThaiSanPham());
        // map các trường khác tương tự

//        if (sanPham.getIdNhaCungCap() != null) {
//            response.setIdNhaCungCap(sanPham.getIdNhaCungCap().getId());
//        }

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
        Page<Object[]> allHienThi = sanPhamRepository.getAllHienThi(pageable);

        List<SanPhamHienThiAdminResponse> sanPhamHienThi = new ArrayList<>();
        for (Object[] hienThi : allHienThi) {
            SanPhamHienThiAdminResponse sp = new SanPhamHienThiAdminResponse();
            sp.setId((Integer) hienThi[0]);
            sp.setMaSanPham((String) hienThi[1]);
            sp.setTenSanPham((String) hienThi[2]);
            String trangThaiStr = (String) hienThi[3];
            TrangThaiSanPham trangThai = TrangThaiSanPham.valueOf(trangThaiStr);
            sp.setTrangThaiSanPham(trangThai);
//            sp.setGiaBan((BigDecimal) hienThi[4]);
            sp.setTenLoai((String) hienThi[4]);
            sp.setSoLuong((Integer) hienThi[5]);
            sp.setUrl((String) hienThi[6]);
            sanPhamHienThi.add(sp);

        }

        return new PageImpl<>(sanPhamHienThi,pageable, allHienThi.getTotalElements());
    }

//    public Page<SanPhamHienThiAdminResponse> getAllSanPham(int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        return sanPhamRepo.getAllHienThi(pageable);
//    }

//    public SanPhamAdminResponse detailSanPhamAdmin(Integer id) {
//        Optional<SanPham> sanPham = sanPhamRepo.findById(id);
//        if (sanPham.isPresent()) {
//            return mapToSanPhamAdminResponse(sanPham.get());
//        } else {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "khong tim thay san pham hop le:" + id);
//        }
//    }


    //detai update sanpham
    public SanPhamAdminUpdateResponse getSanPhamById(Integer id) {
        SanPham sanPham = sanPhamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm"));

        SanPhamAdminUpdateResponse response = new SanPhamAdminUpdateResponse();
        response.setId(sanPham.getId());
        response.setTenSanPham(sanPham.getTenSanPham());
        response.setThuongHieu(sanPham.getThuongHieu());
        response.setIdNhaCungCap(sanPham.getIdNhaCungCap().getId());
        response.setTrangThaiSanPham(sanPham.getTrangThaiSanPham());

        Set<SanPhamChiTietAdminDetailResponse> chiTietList = sanPham.getSanPhamChiTiets().stream().map(ct -> {
            SanPhamChiTietAdminDetailResponse dto = new SanPhamChiTietAdminDetailResponse();
            dto.setId(ct.getId());
            dto.setSoLuong(ct.getSoLuong());
            dto.setGiaBan(ct.getGiaBan());
            dto.setMaSanPhamChiTiet(ct.getMaSanPhamChiTiet());
            dto.setIdMau(ct.getIdMau() != null ? ct.getIdMau().getId() : null);
//            dto.setIdRam(ct.getIdRam() != null ? ct.getIdRam().getId() : null);
            dto.setIdRom(ct.getIdRom() != null ? ct.getIdRom().getId() : null);
//            dto.setIdManHinh(ct.getIdManHinh() != null ? ct.getIdManHinh().getId() : null);
//            dto.setIdHeDieuHanh(ct.getIdHeDieuHanh() != null ? ct.getIdHeDieuHanh().getId() : null);
//            dto.setIdPin(ct.getIdPin() != null ? ct.getIdPin().getId() : null);
//            dto.setIdCpu(ct.getIdCpu() != null ? ct.getIdCpu().getId() : null);
//            dto.setIdCameraTruoc(ct.getIdCameraTruoc() != null ? ct.getIdCameraTruoc().getId() : null);
//            dto.setIdCameraSau(ct.getIdCameraSau() != null ? ct.getIdCameraSau().getId() : null);
//            dto.setIdXuatXu(ct.getIdXuatXu() != null ? ct.getIdXuatXu().getId() : null);
//            dto.setIdLoai(ct.getIdLoai() != null ? ct.getIdLoai().getId() : null);

            // imeis
            if (ct.getImeis() != null) {
                dto.setImeisInput(ct.getImeis().stream()
                        .map(Imei::getSoImei)
                        .collect(Collectors.joining(", ")));
                dto.setImeis(ct.getImeis().stream()
                        .map(i -> {
                            ImeiAdminResponse imeiDto = new ImeiAdminResponse();
                            imeiDto.setSoImei(i.getSoImei());
                            return imeiDto;
                        }).collect(Collectors.toSet()));
            }

            // hinhAnhs
            if (ct.getHinhAnhs() != null) {
                dto.setHinhAnhs(ct.getHinhAnhs().stream()
                        .map(img -> {
                            HinhAnhAdminResponse imgDto = new HinhAnhAdminResponse();
                            imgDto.setUrl(img.getUrl());
                            imgDto.setImagePublicId(img.getImagePublicId());
                            return imgDto;
                        }).collect(Collectors.toSet()));
            }

            return dto;
        }).collect(Collectors.toSet());

        response.setSanPhamChiTiets(chiTietList);
        return response;
    }

    public SanPhamAdminResponse getViewSanPham(Integer id) {
        // Tìm sản phẩm theo ID, ném ngoại lệ nếu không tìm thấy
        SanPham sanPham = sanPhamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm với ID: " + id));

        // Khởi tạo đối tượng phản hồi
        SanPhamAdminResponse response = new SanPhamAdminResponse();

        // Gán thông tin cơ bản của sản phẩm
        response.setId(sanPham.getId());
        response.setTenSanPham(sanPham.getTenSanPham());
        response.setThuongHieu(sanPham.getThuongHieu());
        response.setTrangThaiSanPham(sanPham.getTrangThaiSanPham());
        if (sanPham.getIdNhaCungCap() != null) {
            NhaCungCapAdminResponse nhaCungCapDto = new NhaCungCapAdminResponse();
            nhaCungCapDto.setTenNhaCungCap(sanPham.getIdNhaCungCap().getTenNhaCungCap());
            nhaCungCapDto.setDiaChi(sanPham.getIdNhaCungCap().getDiaChi());
            nhaCungCapDto.setSdt(sanPham.getIdNhaCungCap().getSdt());
            nhaCungCapDto.setEmail(sanPham.getIdNhaCungCap().getEmail());
            response.setNhaCungCapAdminResponse(nhaCungCapDto);
        }

        if (sanPham.getIdModelSanPham() != null) {
            ModelSanPhamAdminResponse modelSanPhamAdminResponse = new ModelSanPhamAdminResponse();
            modelSanPhamAdminResponse.setIdModelSanPham(sanPham.getIdModelSanPham().getIdModelSanPham());
            modelSanPhamAdminResponse.setMaModelSanPham(sanPham.getIdModelSanPham().getMaModelSanPham());
            modelSanPhamAdminResponse.setTenModel(sanPham.getIdModelSanPham().getTenModel());
            modelSanPhamAdminResponse.setNamRaMat(sanPham.getIdModelSanPham().getNamRaMat());
            modelSanPhamAdminResponse.setMoTa(sanPham.getIdModelSanPham().getMoTa());
            modelSanPhamAdminResponse.setTrangThai(sanPham.getIdModelSanPham().getTrangThai());

            if (sanPham.getIdModelSanPham().getIdRam() != null) {
                modelSanPhamAdminResponse.setIdRam(sanPham.getIdModelSanPham().getIdRam().getId());
                modelSanPhamAdminResponse.setDungLuongRam(sanPham.getIdModelSanPham().getIdRam().getDungLuong());
                modelSanPhamAdminResponse.setLoaiRam(sanPham.getIdModelSanPham().getIdRam().getLoai());
                modelSanPhamAdminResponse.setTocDoDocGhiRam(sanPham.getIdModelSanPham().getIdRam().getTocDoDocGhi());
                modelSanPhamAdminResponse.setNhaSanXuatRam(sanPham.getIdModelSanPham().getIdRam().getNhaSanXuat());
                modelSanPhamAdminResponse.setNamRaMatRam(sanPham.getIdModelSanPham().getIdRam().getNamRaMat());
            }

            if (sanPham.getIdModelSanPham().getIdManHinh() != null) {
                modelSanPhamAdminResponse.setIdManHinh(sanPham.getIdModelSanPham().getIdManHinh().getId());
                modelSanPhamAdminResponse.setTenManHinh(sanPham.getIdModelSanPham().getIdManHinh().getTenManHinh());
                modelSanPhamAdminResponse.setKichThuoc(sanPham.getIdModelSanPham().getIdManHinh().getKichThuoc());
                modelSanPhamAdminResponse.setLoaiManHinh(sanPham.getIdModelSanPham().getIdManHinh().getLoaiManHinh());
                modelSanPhamAdminResponse.setDoPhanGiaiManHinh(sanPham.getIdModelSanPham().getIdManHinh().getDoPhanGiai());
                modelSanPhamAdminResponse.setTanSoQuet(sanPham.getIdModelSanPham().getIdManHinh().getTanSoQuet());
                modelSanPhamAdminResponse.setDoSang(sanPham.getIdModelSanPham().getIdManHinh().getDoSang());
                modelSanPhamAdminResponse.setChatLieuKinh(sanPham.getIdModelSanPham().getIdManHinh().getChatLieuKinh());
            }

            if (sanPham.getIdModelSanPham().getIdHeDieuHanh() != null) {
                modelSanPhamAdminResponse.setIdHeDieuHanh(sanPham.getIdModelSanPham().getIdHeDieuHanh().getId());
                modelSanPhamAdminResponse.setPhienBanHeDieuHanh(sanPham.getIdModelSanPham().getIdHeDieuHanh().getPhienBan());
                modelSanPhamAdminResponse.setNhaPhatTrien(sanPham.getIdModelSanPham().getIdHeDieuHanh().getNhaPhatTrien());
                modelSanPhamAdminResponse.setGiaoDienNguoiDung(sanPham.getIdModelSanPham().getIdHeDieuHanh().getGiaoDienNguoiDung());
            }

            if (sanPham.getIdModelSanPham().getIdPin() != null) {
                modelSanPhamAdminResponse.setIdPin(sanPham.getIdModelSanPham().getIdPin().getId());
                modelSanPhamAdminResponse.setPhienBanPin(sanPham.getIdModelSanPham().getIdPin().getPhienBan());
                modelSanPhamAdminResponse.setCongSuatSac(sanPham.getIdModelSanPham().getIdPin().getCongSuatSac());
                modelSanPhamAdminResponse.setThoiGianSuDung(sanPham.getIdModelSanPham().getIdPin().getThoiGianSuDung());
                modelSanPhamAdminResponse.setSoLanSacToiDa(sanPham.getIdModelSanPham().getIdPin().getSoLanSacToiDa());
            }

            if (sanPham.getIdModelSanPham().getIdCpu() != null) {
                modelSanPhamAdminResponse.setIdCpu(sanPham.getIdModelSanPham().getIdCpu().getId());
                modelSanPhamAdminResponse.setHangSanXuat(sanPham.getIdModelSanPham().getIdCpu().getHangSanXuat());
                modelSanPhamAdminResponse.setSoNhan(sanPham.getIdModelSanPham().getIdCpu().getSoNhan());
                modelSanPhamAdminResponse.setChipXuLy(sanPham.getIdModelSanPham().getIdCpu().getChipXuLy());
                modelSanPhamAdminResponse.setXungNhip(sanPham.getIdModelSanPham().getIdCpu().getXungNhip());
                modelSanPhamAdminResponse.setCongNgheSanXuat(sanPham.getIdModelSanPham().getIdCpu().getCongNgheSanXuat());
                modelSanPhamAdminResponse.setBoNhoDem(sanPham.getIdModelSanPham().getIdCpu().getBoNhoDem());
                modelSanPhamAdminResponse.setTieuThuDienNang(sanPham.getIdModelSanPham().getIdCpu().getTieuThuDienNang());
                modelSanPhamAdminResponse.setNamRaMatCpu(sanPham.getIdModelSanPham().getIdCpu().getNamRaMat());
            }

            if (sanPham.getIdModelSanPham().getIdCameraTruoc() != null) {
                modelSanPhamAdminResponse.setIdCameraTruoc(sanPham.getIdModelSanPham().getIdCameraTruoc().getId());
                modelSanPhamAdminResponse.setLoaiCameraTruoc(sanPham.getIdModelSanPham().getIdCameraTruoc().getLoaiCamera());
                modelSanPhamAdminResponse.setDoPhanGiaiCameraTruoc(sanPham.getIdModelSanPham().getIdCameraTruoc().getDoPhanGiai());
                modelSanPhamAdminResponse.setKhauDoCameraTruoc(sanPham.getIdModelSanPham().getIdCameraTruoc().getKhauDo());
                modelSanPhamAdminResponse.setLoaiZoomCameraTruoc(sanPham.getIdModelSanPham().getIdCameraTruoc().getLoaiZoom());
                modelSanPhamAdminResponse.setCheDoChupCameraTruoc(sanPham.getIdModelSanPham().getIdCameraTruoc().getCheDoChup());
            }

            if (sanPham.getIdModelSanPham().getIdCameraSau() != null) {
                modelSanPhamAdminResponse.setIdCameraSau(sanPham.getIdModelSanPham().getIdCameraSau().getId());
                modelSanPhamAdminResponse.setLoaiCameraSau(sanPham.getIdModelSanPham().getIdCameraSau().getLoaiCamera());
                modelSanPhamAdminResponse.setDoPhanGiaiCameraSau(sanPham.getIdModelSanPham().getIdCameraSau().getDoPhanGiai());
                modelSanPhamAdminResponse.setKhauDoCameraSau(sanPham.getIdModelSanPham().getIdCameraSau().getKhauDo());
                modelSanPhamAdminResponse.setLoaiZoomCameraSau(sanPham.getIdModelSanPham().getIdCameraSau().getLoaiZoom());
                modelSanPhamAdminResponse.setCheDoChupCameraSau(sanPham.getIdModelSanPham().getIdCameraSau().getCheDoChup());
            }

            if (sanPham.getIdModelSanPham().getIdXuatXu() != null) {
                modelSanPhamAdminResponse.setIdXuatXu(sanPham.getIdModelSanPham().getIdXuatXu().getId());
                modelSanPhamAdminResponse.setMaXuatXu(sanPham.getIdModelSanPham().getIdXuatXu().getMaXuatXu());
                modelSanPhamAdminResponse.setTenQuocGia(sanPham.getIdModelSanPham().getIdXuatXu().getTenQuocGia());
            }

            if (sanPham.getIdModelSanPham().getIdLoai() != null) {
                modelSanPhamAdminResponse.setIdLoai(sanPham.getIdModelSanPham().getIdLoai().getId());
                modelSanPhamAdminResponse.setTenLoai(sanPham.getIdModelSanPham().getIdLoai().getTenLoai());
            }

            response.setModelSanPhamAdminResponse(modelSanPhamAdminResponse);
        }


        // Chuyển đổi danh sách chi tiết sản phẩm
        Set<SanPhamChiTietResponse> chiTietList = (sanPham.getSanPhamChiTiets() != null)
                ? sanPham.getSanPhamChiTiets().stream().map(ct -> {
            SanPhamChiTietResponse dto = new SanPhamChiTietResponse();

            // Thông tin cơ bản của sản phẩm chi tiết
            dto.setId(ct.getId());
            dto.setMaSanPhamChiTiet(ct.getMaSanPhamChiTiet());
            dto.setSoLuongSPCT(ct.getSoLuong());
            dto.setGiaBan(ct.getGiaBan());

            // Thông tin màu sắc
            if (ct.getIdMau() != null) {
                dto.setTenMau(ct.getIdMau().getTenMau());
                dto.setMaMau(ct.getIdMau().getMaMau());
            }

            // Thông tin ROM
            if (ct.getIdRom() != null) {
                dto.setDungLuongRom(ct.getIdRom().getDungLuong());
                dto.setLoaiRom(ct.getIdRom().getLoai());
                dto.setTocDoDocGhiRom(ct.getIdRom().getTocDoDocGhi());
                dto.setNhaSanXuatRom(ct.getIdRom().getNhaSanXuat());
                dto.setNamRaMatRom(ct.getIdRom().getNamRaMat());
            }


            // Xử lý danh sách IMEI
            if (ct.getImeis() != null && !ct.getImeis().isEmpty()) {
                dto.setImeis(ct.getImeis().stream()
                        .map(i -> {
                            ImeiAdminResponse imeiDto = new ImeiAdminResponse();
                            imeiDto.setSoImei(i.getSoImei());
                            return imeiDto;
                        }).collect(Collectors.toSet()));
            } else {
                dto.setImeis(new HashSet<>());
            }

            // Xử lý danh sách hình ảnh
            if (ct.getHinhAnhs() != null && !ct.getHinhAnhs().isEmpty()) {
                dto.setHinhAnhs(ct.getHinhAnhs().stream()
                        .map(img -> {
                            HinhAnhAdminResponse imgDto = new HinhAnhAdminResponse();
                            imgDto.setUrl(img.getUrl());
                            imgDto.setImagePublicId(img.getImagePublicId());
                            return imgDto;
                        }).collect(Collectors.toSet()));
            } else {
                dto.setHinhAnhs(new HashSet<>());
            }

            return dto;
        }).collect(Collectors.toSet())
                : new HashSet<>();

        // Gán danh sách chi tiết sản phẩm vào phản hồi
        response.setSanPhamChiTiets(chiTietList);

        return response;
    }



    @Transactional(rollbackFor = Exception.class)
    public SanPhamAdminResponse createSanPhamAdmin(SanPhamAdminRequest sanPhamAdminRequest) {

        Set<String> variantKeySet = new HashSet<>();
        Set<String> allImeis = new HashSet<>();
        for (SanPhamChiTietAdminRepuest rq : sanPhamAdminRequest.getSanPhamChiTiets()) {
            List<String> imeiList = rq.getImeis().stream()
                    .map(i -> i.getSoImei().trim())
                    .filter(s -> !s.isEmpty())
                    .toList();

            Set<String> imeiSet = new HashSet<>(imeiList);
            if (imeiSet.size() < imeiList.size()) {
                throw new BusinessException("Chi tiết sản phẩm có IMEI bị trùng trong chính nó.");
            }

            for (String imei : imeiList) {
                if (!allImeis.add(imei)) {
                    throw new BusinessException("IMEI bị trùng giữa các biến thể: " + imei);
                }

                // Check trùng với DB
                if (imeiReposiory.existsBySoImei(imei)) {
                    throw new BusinessException("IMEI đã tồn tại trong hệ thống: " + imei);
                }
            }

            String variantKey = String.format("%d-%d",
                    rq.getIdMau(), rq.getIdRom());
            if (!variantKeySet.add(variantKey)) {
                throw new BusinessException("Biến thể với tổ hợp thuộc tính trùng lặp trong yêu cầu: " + variantKey);
            }
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

        if (sanPhamAdminRequest.getIdModelSanPham() != null)  {
            ModelSanPham modelSanPham = modelSanPhamRepository.findById(sanPhamAdminRequest.getIdModelSanPham()).orElseThrow();
            sanPham.setIdModelSanPham(modelSanPham);
        }

        sanPham = sanPhamRepo.save(sanPham);

        Set<SanPhamChiTiet> chiTietSet = new HashSet<>();

        // Bước 3: Lưu chi tiết sản phẩm
        if (sanPhamAdminRequest.getSanPhamChiTiets() != null && !sanPhamAdminRequest.getSanPhamChiTiets().isEmpty()) {
            for (SanPhamChiTietAdminRepuest rq : sanPhamAdminRequest.getSanPhamChiTiets()) {

                // Tạo chi tiết sản phẩm
                SanPhamChiTiet chiTiet = new SanPhamChiTiet();
                chiTiet.setMaSanPhamChiTiet(rq.getMaSanPhamChiTiet());
                chiTiet.setSoLuong(rq.getSoLuong());
                chiTiet.setGiaBan(rq.getGiaBan());
                chiTiet.setIdSanPham(sanPham);

                // Validate foreign keys
                if (rq.getIdMau() != null) {
                    chiTiet.setIdMau(mauSacRepository.findById(rq.getIdMau())
                            .orElseThrow(() -> new NotFoundException("Không tìm thấy màu sắc với ID: " + rq.getIdMau())));
                }

                if (rq.getIdRom() != null) {
                    chiTiet.setIdRom(romRepository.findById(rq.getIdRom())
                            .orElseThrow(() -> new NotFoundException("Không tìm thấy ROM với ID: " + rq.getIdRom())));
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
                                if (imeiReposiory.existsBySoImei(imeiDto.getSoImei().trim())) {
                                    throw new BusinessException("Số IMEI đã tồn tại: " + imeiDto.getSoImei());
                                }
                                Imei newImei = new Imei();
                                newImei.setSoImei(imeiDto.getSoImei().trim());
//                                newImei.setTrangThaiImei(imeiDto.getTrangThaiImei());
                                newImei.setTrangThaiImei(TrangThaiImei.AVAILABLE);
                                newImei.setIdSanPhamChiTiet(chiTietSaved);
                                return newImei;
                            })
                            .toList());
                }

                chiTietSet.add(chiTietSaved);
            }
        }


        // Gán lại chi tiết vào sản phẩm
        sanPham.getSanPhamChiTiets().clear();
        sanPham.getSanPhamChiTiets().addAll(chiTietSet);
        sanPham = sanPhamRepo.save(sanPham);


        // Bước 4: Tạo response
        SanPhamAdminResponse response = new SanPhamAdminResponse();
        response.setId(sanPham.getId());
        response.setMaSanPham(sanPham.getMaSanPham());
        response.setTenSanPham(sanPham.getTenSanPham());
        response.setTrangThaiSanPham(sanPham.getTrangThaiSanPham());
        response.setThuongHieu(sanPham.getThuongHieu());

        if (sanPham.getIdNhaCungCap() != null) {
            NhaCungCapAdminResponse nhaCungCapAdminResponse = new NhaCungCapAdminResponse();
            nhaCungCapAdminResponse.setTenNhaCungCap(sanPham.getIdNhaCungCap().getTenNhaCungCap());
            nhaCungCapAdminResponse.setSdt(sanPham.getIdNhaCungCap().getSdt());
            nhaCungCapAdminResponse.setEmail(sanPham.getIdNhaCungCap().getEmail());
            nhaCungCapAdminResponse.setDiaChi(sanPham.getIdNhaCungCap().getDiaChi());
            response.setNhaCungCapAdminResponse(nhaCungCapAdminResponse);
        }

        if (sanPham.getIdModelSanPham() != null) {
            ModelSanPhamAdminResponse modelSanPhamAdminResponse = new ModelSanPhamAdminResponse();
            modelSanPhamAdminResponse.setMaModelSanPham(sanPham.getIdModelSanPham().getMaModelSanPham());
            modelSanPhamAdminResponse.setTenModel(sanPham.getIdModelSanPham().getTenModel());
            modelSanPhamAdminResponse.setNamRaMat(sanPham.getIdModelSanPham().getNamRaMat());
            modelSanPhamAdminResponse.setMoTa(sanPham.getIdModelSanPham().getMoTa());
            modelSanPhamAdminResponse.setTrangThai(sanPham.getIdModelSanPham().getTrangThai());
        }

        if (sanPham.getSanPhamChiTiets() != null) {
            Set<SanPhamChiTietResponse> chiTietResponses = sanPham.getSanPhamChiTiets().stream()
                    .map(this::mapToChiTietResponse)
                    .collect(Collectors.toSet());
            response.setSanPhamChiTiets(chiTietResponses);
        }

        return response;
    }


    @Transactional(rollbackFor = Exception.class)
    public SanPhamAdminResponse updateSanPhamAdmin(Integer id, SanPhamAdminRequest sanPhamAdminRequest) {

        // Kiểm tra biến thể trùng lặp
        Set<String> variantKeySet = new HashSet<>();
        for (SanPhamChiTietAdminRepuest rq : sanPhamAdminRequest.getSanPhamChiTiets()) {
            String variantKey = String.format("%d-%d",
                    rq.getIdMau(), rq.getIdRom());
            if (!variantKeySet.add(variantKey)) {
                throw new BusinessException("Biến thể với tổ hợp thuộc tính trùng lặp trong yêu cầu: " + variantKey);
            }
        }

        // Tìm sản phẩm cần cập nhật
        SanPham sanPham = sanPhamRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm với ID: " + id));

        // Cập nhật thông tin sản phẩm
        sanPham.setTenSanPham(sanPhamAdminRequest.getTenSanPham());
        sanPham.setThuongHieu(sanPhamAdminRequest.getThuongHieu());
        sanPham.setTrangThaiSanPham(
                Optional.ofNullable(sanPhamAdminRequest.getTrangThaiSanPham())
                        .orElse(TrangThaiSanPham.COMING_SOON)
        );

        if (sanPhamAdminRequest.getIdNhaCungCap() != null) {
            NhaCungCap nhaCungCap = nhaCungCapRepository.findById(sanPhamAdminRequest.getIdNhaCungCap())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy nhà cung cấp với ID: " + sanPhamAdminRequest.getIdNhaCungCap()));
            sanPham.setIdNhaCungCap(nhaCungCap);
        } else {
            sanPham.setIdNhaCungCap(null);
        }

        // Xử lý chi tiết sản phẩm
//        Set<SanPhamChiTiet> existingChiTietSet = sanPham.getSanPhamChiTiets();
        Set<SanPhamChiTiet> updatedChiTietSet = new HashSet<>();

        if (sanPhamAdminRequest.getSanPhamChiTiets() != null && !sanPhamAdminRequest.getSanPhamChiTiets().isEmpty()) {
            for (SanPhamChiTietAdminRepuest rq : sanPhamAdminRequest.getSanPhamChiTiets()) {

                // Tìm hoặc tạo mới chi tiết sản phẩm
                SanPhamChiTiet chiTiet;
                if (rq.getId() != null && sanPhamChiTietRepository.existsById(rq.getId())) {
                    chiTiet = sanPhamChiTietRepository.findById(rq.getId())
                            .orElseThrow(() -> new NotFoundException("Không tìm thấy chi tiết sản phẩm với ID: " + rq.getId()));
                } else {
                    chiTiet = new SanPhamChiTiet();
                    chiTiet.setIdSanPham(sanPham);
                }

                chiTiet.setMaSanPhamChiTiet(rq.getMaSanPhamChiTiet());
                chiTiet.setSoLuong(rq.getSoLuong());
                chiTiet.setGiaBan(rq.getGiaBan());

                // Validate và cập nhật foreign keys
                if (rq.getIdMau() != null) {
                    chiTiet.setIdMau(mauSacRepository.findById(rq.getIdMau())
                            .orElseThrow(() -> new NotFoundException("Không tìm thấy màu sắc với ID: " + rq.getIdMau())));
                } else {
                    chiTiet.setIdMau(null);
                }
//                if (rq.getIdRam() != null) {
//                    chiTiet.setIdRam(ramRepository.findById(rq.getIdRam())
//                            .orElseThrow(() -> new NotFoundException("Không tìm thấy RAM với ID: " + rq.getIdRam())));
//                } else {
//                    chiTiet.setIdRam(null);
//                }
                if (rq.getIdRom() != null) {
                    chiTiet.setIdRom(romRepository.findById(rq.getIdRom())
                            .orElseThrow(() -> new NotFoundException("Không tìm thấy ROM với ID: " + rq.getIdRom())));
                } else {
                    chiTiet.setIdRom(null);
                }
//                if (rq.getIdManHinh() != null) {
//                    chiTiet.setIdManHinh(manHinhRepository.findById(rq.getIdManHinh())
//                            .orElseThrow(() -> new NotFoundException("Không tìm thấy màn hình với ID: " + rq.getIdManHinh())));
//                } else {
//                    chiTiet.setIdManHinh(null);
//                }
//                if (rq.getIdHeDieuHanh() != null) {
//                    chiTiet.setIdHeDieuHanh(heDieuHanhRepository.findById(rq.getIdHeDieuHanh())
//                            .orElseThrow(() -> new NotFoundException("Không tìm thấy hệ điều hành với ID: " + rq.getIdHeDieuHanh())));
//                } else {
//                    chiTiet.setIdHeDieuHanh(null);
//                }
//                if (rq.getIdPin() != null) {
//                    chiTiet.setIdPin(pinRepository.findById(rq.getIdPin())
//                            .orElseThrow(() -> new NotFoundException("Không tìm thấy pin với ID: " + rq.getIdPin())));
//                } else {
//                    chiTiet.setIdPin(null);
//                }
//                if (rq.getIdCpu() != null) {
//                    chiTiet.setIdCpu(cpuRepository.findById(rq.getIdCpu())
//                            .orElseThrow(() -> new NotFoundException("Không tìm thấy CPU với ID: " + rq.getIdCpu())));
//                } else {
//                    chiTiet.setIdCpu(null);
//                }
//                if (rq.getIdCameraTruoc() != null) {
//                    chiTiet.setIdCameraTruoc(cameraTruocRepository.findById(rq.getIdCameraTruoc())
//                            .orElseThrow(() -> new NotFoundException("Không tìm thấy camera trước với ID: " + rq.getIdCameraTruoc())));
//                } else {
//                    chiTiet.setIdCameraTruoc(null);
//                }
//                if (rq.getIdCameraSau() != null) {
//                    chiTiet.setIdCameraSau(cameraSauRepository.findById(rq.getIdCameraSau())
//                            .orElseThrow(() -> new NotFoundException("Không tìm thấy camera sau với ID: " + rq.getIdCameraSau())));
//                } else {
//                    chiTiet.setIdCameraSau(null);
//                }
//                if (rq.getIdXuatXu() != null) {
//                    chiTiet.setIdXuatXu(xuatXuRepository.findById(rq.getIdXuatXu())
//                            .orElseThrow(() -> new NotFoundException("Không tìm thấy xuất xứ với ID: " + rq.getIdXuatXu())));
//                } else {
//                    chiTiet.setIdXuatXu(null);
//                }
//                if (rq.getIdLoai() != null) {
//                    chiTiet.setIdLoai(loaiRepository.findById(rq.getIdLoai())
//                            .orElseThrow(() -> new NotFoundException("Không tìm thấy loại với ID: " + rq.getIdLoai())));
//                } else {
//                    chiTiet.setIdLoai(null);
//                }

                // Lưu chi tiết sản phẩm
                SanPhamChiTiet chiTietSaved = sanPhamChiTietRepository.save(chiTiet);

                // Cập nhật hình ảnh
                if (rq.getHinhAnhs() != null && !rq.getHinhAnhs().isEmpty()) {
                    // Xóa hình ảnh cũ
                    hinhAnhRepository.deleteByIdSanPhamChiTiet(chiTietSaved.getId());
                    // Lưu hình ảnh mới
                    List<HinhAnh> newImages = rq.getHinhAnhs().stream()
                            .filter(ha -> ha.getUrl() != null && !ha.getUrl().trim().isEmpty())
                            .map(ha -> {
                                HinhAnh hinhAnh = new HinhAnh();
                                hinhAnh.setIdSanPhamChiTiet(chiTietSaved);
                                hinhAnh.setUrl(ha.getUrl());
                                hinhAnh.setImagePublicId(ha.getImagePublicId());
                                return hinhAnh;
                            })
                            .toList();
                    if (!newImages.isEmpty()) {
                        hinhAnhRepository.saveAll(newImages);
                    } else {
                        throw new BusinessException("Danh sách hình ảnh mới không chứa URL hợp lệ cho chi tiết sản phẩm ID: " + chiTietSaved.getId());
                    }
                } else {
                    // Nếu không có hình ảnh mới, ném lỗi để yêu cầu client cung cấp hình ảnh
                    throw new BusinessException("Không có danh sách hình ảnh để cập nhật cho chi tiết sản phẩm ID: " + chiTietSaved.getId());
                }

                // Cập nhật IMEI
                if (rq.getImeis() != null && !rq.getImeis().isEmpty()) {
                    // Xóa IMEI cũ
                    imeiReposiory.deleteByIdSanPhamChiTiet(chiTietSaved);
                    // Lưu IMEI mới
                    imeiReposiory.saveAll(rq.getImeis().stream()
                            .filter(imeiDto -> imeiDto.getSoImei() != null && !imeiDto.getSoImei().trim().isEmpty())
                            .map(imeiDto -> {
                                Imei newImei = new Imei();
                                newImei.setSoImei(imeiDto.getSoImei().trim());
                                newImei.setTrangThaiImei(imeiDto.getTrangThaiImei());
                                newImei.setIdSanPhamChiTiet(chiTietSaved);
                                return newImei;
                            })
                            .toList());
                }

                updatedChiTietSet.add(chiTietSaved);
            }
        }

        // Xóa các chi tiết không còn trong request
        Set<SanPhamChiTiet> chiTietCanXoa = sanPham.getSanPhamChiTiets().stream()
                .filter(oldChiTiet -> updatedChiTietSet.stream()
                        .noneMatch(newChiTiet -> newChiTiet.getId() != null && newChiTiet.getId().equals(oldChiTiet.getId())))
                .collect(Collectors.toSet());

        sanPham.getSanPhamChiTiets().removeAll(chiTietCanXoa);

        // Xóa trong DB nếu cần
        sanPhamChiTietRepository.deleteAll(chiTietCanXoa);

        // Thêm hoặc cập nhật các chi tiết còn lại
        for (SanPhamChiTiet chiTietMoi : updatedChiTietSet) {
            // Thiết lập quan hệ 2 chiều nếu chưa có
            chiTietMoi.setIdSanPham(sanPham);

            // Nếu đã có thì update, nếu chưa thì thêm
            if (sanPham.getSanPhamChiTiets().stream()
                    .noneMatch(old -> old.getId() != null && old.getId().equals(chiTietMoi.getId()))) {
                sanPham.getSanPhamChiTiets().add(chiTietMoi);
            }
        }

        sanPham = sanPhamRepo.save(sanPham);

        // Tạo response
        SanPhamAdminResponse response = new SanPhamAdminResponse();
        response.setId(sanPham.getId());
        response.setMaSanPham(sanPham.getMaSanPham());
        response.setTenSanPham(sanPham.getTenSanPham());
        response.setThuongHieu(sanPham.getThuongHieu());
//        if (sanPham.getIdNhaCungCap() != null) {
//            response.setIdNhaCungCap(Integer.valueOf(sanPham.getIdNhaCungCap().getId()));
//        }

        if (sanPham.getSanPhamChiTiets() != null) {
            Set<SanPhamChiTietResponse> chiTietResponses = sanPham.getSanPhamChiTiets().stream()
                    .map(this::mapToChiTietResponse)
                    .collect(Collectors.toSet());
            response.setSanPhamChiTiets(chiTietResponses);
        }

        return response;
    }

    @Transactional
    public SanPhamAdminResponse deleteSanPhamAdmin(Integer id) {

        SanPham sanPham = sanPhamRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm với ID: " + id));

        SanPhamAdminResponse sanPhamAdminResponse = new SanPhamAdminResponse();
        sanPhamAdminResponse.setId(sanPham.getId());
        sanPhamAdminResponse.setTenSanPham(sanPham.getTenSanPham());
        sanPhamAdminResponse.setThuongHieu(sanPham.getThuongHieu());

        sanPhamRepo.deleteById(id);

        return sanPhamAdminResponse;
    }


    public Page<SanPhamBanHangAdminResponse> getProductNames(String tenSanPham, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SanPhamChiTiet> chiTietPage = sanPhamChiTietRepository.findByIdSanPham_TenSanPhamContainingAndIdSanPham_TrangThaiSanPham(tenSanPham, TrangThaiSanPham.ACTIVE, pageable);

        return chiTietPage.map(SanPhamBanHangAdminResponse::converDto);
    }

    public Page<String> getProductNameCategory(int pageNo, int pageSize){
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("ten_dong_san_pham").ascending());
        return sanPhamRepo.findTenDongSanPham(pageable);
    }


}


