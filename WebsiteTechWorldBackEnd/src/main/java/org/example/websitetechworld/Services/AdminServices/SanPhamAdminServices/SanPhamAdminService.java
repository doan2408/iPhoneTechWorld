package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;

import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.HinhAnhAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.ImeiAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.SanPhamAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.SanPhamChiTietAdminRepuest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.*;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Enum.Imei.TrangThaiImei;
import org.example.websitetechworld.Enum.KhuyenMai.DoiTuongApDung;
import org.example.websitetechworld.Enum.KhuyenMai.TrangThaiKhuyenMai;
import org.example.websitetechworld.Enum.SanPham.TrangThaiSanPham;
import org.example.websitetechworld.Repository.*;
import org.example.websitetechworld.Services.AdminServices.KhuyenMaiAdminService.KhuyenMaiAdminService;
import org.example.websitetechworld.exception.BusinessException;
import org.example.websitetechworld.exception.NotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SanPhamAdminService {
    private final SanPhamRepository sanPhamRepo;
    private final ModelSanPhamRepository modelSanPhamRepository;
    private final NhaCungCapRepository nhaCungCapRepository;
    private final MauSacRepository mauSacRepository;
    private final RomRepository romRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;
    private final HinhAnhRepository hinhAnhRepository;
    private final ImeiReposiory imeiReposiory;
    private final SanPhamRepository sanPhamRepository;
    private final ModelMapper modelMapper;
    private final SanPhamChiTietAdminService sanPhamChiTietAdminService;
    private final NhaCungCapSpRepository nhaCungCapSpRepository;
    private final HoaDonRepository hoaDonRepository;
    private final KhachHangRepository khachHangRepository;
    private final KhuyenMaiAdminService khuyenMaiAdminService;
    private final LoaiRepository loaiRepository;

    private SanPhamChiTietResponse mapToChiTietResponse(SanPhamChiTiet chiTiet) {
        SanPhamChiTietResponse response = new SanPhamChiTietResponse();

        response.setId(chiTiet.getId());
        response.setMaSanPhamChiTiet(chiTiet.getMaSanPhamChiTiet());
        response.setSoLuongSPCT(chiTiet.getSoLuong());
        response.setGiaBan(chiTiet.getGiaBan());

        if (chiTiet.getIdMau() != null) {
            response.setTenMau(chiTiet.getIdMau().getTenMau());
            response.setMaMau(chiTiet.getIdMau().getMaMau());
        }

        if (chiTiet.getIdRom() != null) {
            response.setLoaiRom(chiTiet.getIdRom().getLoai());
            response.setDungLuongRom(chiTiet.getIdRom().getDungLuong());
            response.setTocDoDocGhiRom(chiTiet.getIdRom().getTocDoDocGhi());
            response.setNhaSanXuatRom(chiTiet.getIdRom().getNhaSanXuat());
            response.setNamRaMatRom(chiTiet.getIdRom().getNamRaMat());
        }

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

    public Page<SanPhamHienThiAdminResponse> getAllSanPham(String keyword, Integer idLoai, String trangThais, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> allHienThi = sanPhamRepository.getAllHienThi( keyword ,idLoai ,trangThais, pageable );

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
            sp.setMaXuatXu((String) hienThi[7]);
            sanPhamHienThi.add(sp);

        }

        return new PageImpl<>(sanPhamHienThi,pageable, allHienThi.getTotalElements());
    }

    //detail update sanpham
    public SanPhamAdminUpdateResponse getSanPhamById(Integer id) {
        SanPham sanPham = sanPhamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm"));

        SanPhamAdminUpdateResponse response = new SanPhamAdminUpdateResponse();
        response.setId(sanPham.getId());
        response.setTenSanPham(sanPham.getTenSanPham());
        response.setThuongHieu(sanPham.getThuongHieu());

        List<Integer> idNhaCungCaps = nhaCungCapSpRepository.findBySanPham_Id(sanPham.getId()).stream()
                .map(nhaCungCapSp -> nhaCungCapSp.getNhaCungCap().getId())
                .collect(Collectors.toList());
        response.setIdNhaCungCaps(idNhaCungCaps);

        response.setTrangThaiSanPham(sanPham.getTrangThaiSanPham());
        response.setIdModelSanPham(sanPham.getIdModelSanPham().getIdModelSanPham());
        // Filter product details that have at least one AVAILABLE imei
        Set<SanPhamChiTietAdminDetailResponse> chiTietList = sanPham.getSanPhamChiTiets().stream()
//                .filter(ct -> ct.getImeis() !=null && ct.getImeis().stream()
//                        .anyMatch(imei -> imei.getTrangThaiImei() == TrangThaiImei.AVAILABLE)
//                )
                .map(ct -> {
                    // only select imei AVAILABLE
                    Set<Imei> imeiAvailable = ct.getImeis().stream()
                            .filter(imei -> imei.getTrangThaiImei() == TrangThaiImei.AVAILABLE)
                            .collect(Collectors.toSet());
            SanPhamChiTietAdminDetailResponse dto = new SanPhamChiTietAdminDetailResponse();
            dto.setId(ct.getId());
//            dto.setSoLuong(ct.getSoLuong());
            dto.setSoLuong(imeiAvailable.size());
            dto.setGiaBan(ct.getGiaBan());
            dto.setMaSanPhamChiTiet(ct.getMaSanPhamChiTiet());
            dto.setIdMau(ct.getIdMau() != null ? ct.getIdMau().getId() : null);
            dto.setIdRom(ct.getIdRom() != null ? ct.getIdRom().getId() : null);
            // imeis
            if (ct.getImeis() != null) {
                dto.setImeisInput(imeiAvailable.stream()
                        .map(Imei::getSoImei)
                        .collect(Collectors.joining(", ")));
                dto.setImeis(imeiAvailable.stream()
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
        response.setMaSanPham(sanPham.getMaSanPham());
        response.setThuongHieu(sanPham.getThuongHieu());
        response.setTrangThaiSanPham(sanPham.getTrangThaiSanPham());

        List<NhaCungCapSp> nhaCungCapSpList = nhaCungCapSpRepository.findBySanPham_Id(sanPham.getId());
        List<NhaCungCapAdminResponse> nhaCungCaps = nhaCungCapSpList.stream()
                .map(nhaCungCapSp -> {
                    NhaCungCapAdminResponse nhaCungCapDto = new NhaCungCapAdminResponse();
                    nhaCungCapDto.setId(nhaCungCapSp.getNhaCungCap().getId());
                    nhaCungCapDto.setTenNhaCungCap(nhaCungCapSp.getNhaCungCap().getTenNhaCungCap());
                    nhaCungCapDto.setDiaChi(nhaCungCapSp.getNhaCungCap().getDiaChi());
                    nhaCungCapDto.setSdt(nhaCungCapSp.getNhaCungCap().getSdt());
                    nhaCungCapDto.setEmail(nhaCungCapSp.getNhaCungCap().getEmail());
                    return nhaCungCapDto;
                })
                .collect(Collectors.toList());
        response.setNhaCungCaps(nhaCungCaps);

        if (sanPham.getIdModelSanPham() != null) {
            ModelSanPhamAdminResponse modelSanPhamAdminResponse = new ModelSanPhamAdminResponse();
            modelSanPhamAdminResponse.setIdModelSanPham(sanPham.getIdModelSanPham().getIdModelSanPham());
            modelSanPhamAdminResponse.setMaModelSanPham(sanPham.getIdModelSanPham().getMaModelSanPham());
            modelSanPhamAdminResponse.setTenModel(sanPham.getIdModelSanPham().getTenModel());
            modelSanPhamAdminResponse.setNamRaMat(sanPham.getIdModelSanPham().getNamRaMat());
            modelSanPhamAdminResponse.setTrangThaiSanPhamModel(sanPham.getIdModelSanPham().getTrangThaiSanPhamModel());

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

            if (sanPham.getIdModelSanPham().getIdXuatXu() != null) {
                modelSanPhamAdminResponse.setIdXuatXu(sanPham.getIdModelSanPham().getIdXuatXu().getId());
                modelSanPhamAdminResponse.setMaXuatXu(sanPham.getIdModelSanPham().getIdXuatXu().getMaXuatXu());
                modelSanPhamAdminResponse.setTenQuocGia(sanPham.getIdModelSanPham().getIdXuatXu().getTenQuocGia());
            }

            if (sanPham.getIdModelSanPham().getIdLoai() != null) {
                modelSanPhamAdminResponse.setIdLoai(sanPham.getIdModelSanPham().getIdLoai().getId());
                modelSanPhamAdminResponse.setTenLoai(sanPham.getIdModelSanPham().getIdLoai().getTenLoai());
            }

            if (sanPham.getIdModelSanPham().getCameraSaus() != null && !sanPham.getIdModelSanPham().getCameraSaus().isEmpty()) {
                List<CameraSauAdminResponse> cameraSauList = sanPham.getIdModelSanPham().getCameraSaus().stream()
                        .map(camera -> {
                            CameraSauAdminResponse cameraDto = new CameraSauAdminResponse();
                            cameraDto.setId(camera.getId().getCameraSauId());
                            cameraDto.setLoaiCamera(camera.getCameraSau().getLoaiCamera());
                            cameraDto.setDoPhanGiai(camera.getCameraSau().getDoPhanGiai());
                            cameraDto.setKhauDo(camera.getCameraSau().getKhauDo());
                            cameraDto.setLoaiZoom(camera.getCameraSau().getLoaiZoom());
                            cameraDto.setCheDoChup(camera.getCameraSau().getCheDoChup());
                            cameraDto.setIsChinh(camera.getIsChinh());
                            return cameraDto;
                        })
                        .collect(Collectors.toList());
                modelSanPhamAdminResponse.setCameraSaus(cameraSauList);
            } else {
                modelSanPhamAdminResponse.setCameraSaus(new ArrayList<>());
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

        // === Bước 1: Kiểm tra dữ liệu đầu vào ===
        if (sanPhamAdminRequest.getIdModelSanPham() == null) {
            throw new BusinessException("Model sản phẩm không được để trống.");
        }

        ModelSanPham modelSanPham = modelSanPhamRepository.findById(sanPhamAdminRequest.getIdModelSanPham())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy model sản phẩm với ID: " + sanPhamAdminRequest.getIdModelSanPham()));

        // === Bước 2: Tìm hoặc tạo mới SanPham ===
        List<SanPham> sanPhamList = sanPhamRepo.findAllByIdModelSanPham_IdModelSanPham(sanPhamAdminRequest.getIdModelSanPham());

        // Sử dụng findFirst() để lấy 1 kết quả duy nhất, tránh lỗi NonUniqueResultException
        SanPham sanPham = sanPhamList.stream()
                .filter(sp -> sp.getTrangThaiSanPham() == TrangThaiSanPham.ACTIVE)
                .filter(sp -> sp.getTenSanPham().equalsIgnoreCase(sanPhamAdminRequest.getTenSanPham()))
                .findFirst()
                .orElse(null);

        // Nếu không tìm thấy sản phẩm phù hợp, tạo mới
        if (sanPham == null) {
            sanPham = new SanPham();
            sanPham.setTenSanPham(sanPhamAdminRequest.getTenSanPham());
            sanPham.setThuongHieu(sanPhamAdminRequest.getThuongHieu());
            sanPham.setTrangThaiSanPham(Optional.ofNullable(sanPhamAdminRequest.getTrangThaiSanPham())
                    .orElse(TrangThaiSanPham.ACTIVE));
            sanPham.setIdModelSanPham(modelSanPham);
            sanPham = sanPhamRepo.save(sanPham);
        }

        // === Bước 3: Xử lý quan hệ với nhà cung cấp ===
        if (sanPhamAdminRequest.getIdNhaCungCaps() != null && !sanPhamAdminRequest.getIdNhaCungCaps().isEmpty()) {
            for (Integer idNhaCungCap : sanPhamAdminRequest.getIdNhaCungCaps()) {
                NhaCungCap nhaCungCap = nhaCungCapRepository.findById(idNhaCungCap)
                        .orElseThrow(() -> new NotFoundException("Không tìm thấy nhà cung cấp với ID: " + idNhaCungCap));

                // Kiểm tra xem quan hệ đã tồn tại chưa
                if (!nhaCungCapSpRepository.existsByNhaCungCapAndSanPham(nhaCungCap, sanPham)) {
                    NhaCungCapSp nhaCungCapSp = new NhaCungCapSp();
                    nhaCungCapSp.setNhaCungCap(nhaCungCap);
                    nhaCungCapSp.setSanPham(sanPham);
                    nhaCungCapSpRepository.save(nhaCungCapSp);
                }
            }
        }

        // === Bước 4: Xử lý danh sách chi tiết sản phẩm ===
        Set<SanPhamChiTiet> chiTietSet = new HashSet<>();
        Map<Integer, Boolean> mauDaCoAnh = new HashMap<>();
        if (sanPhamAdminRequest.getSanPhamChiTiets() != null && !sanPhamAdminRequest.getSanPhamChiTiets().isEmpty()) {
            for (SanPhamChiTietAdminRepuest rq : sanPhamAdminRequest.getSanPhamChiTiets()) {

                // Tìm SanPhamChiTiet hiện có - sử dụng findFirst() để tránh lỗi NonUniqueResult
                SanPhamChiTiet chiTiet = null;

                // Nếu sanPham đã có chi tiết, tìm trong danh sách hiện có
                if (sanPham.getSanPhamChiTiets() != null) {
                    chiTiet = sanPham.getSanPhamChiTiets().stream()
                            .filter(ct -> Objects.equals(ct.getIdMau() != null ? ct.getIdMau().getId() : null, rq.getIdMau()))
                            .filter(ct -> Objects.equals(ct.getIdRom() != null ? ct.getIdRom().getId() : null, rq.getIdRom()))
                            .findFirst()
                            .orElse(null);
                }

                // Nếu không tìm thấy trong collection hiện có, tìm trong database
                if (chiTiet == null) {
                    List<SanPhamChiTiet> existingChiTiets = sanPhamChiTietRepository
                            .findBySanPhamAndMauAndRom(sanPham.getId(), rq.getIdMau(), rq.getIdRom());
                    chiTiet = existingChiTiets.stream().findFirst().orElse(null);
                }

                if (chiTiet == null) {
                    // Tạo mới SanPhamChiTiet nếu không tìm thấy
                    chiTiet = new SanPhamChiTiet();
                    chiTiet.setMaSanPhamChiTiet(rq.getMaSanPhamChiTiet());
                    chiTiet.setGiaBan(rq.getGiaBan());
                    chiTiet.setIdSanPham(sanPham);

                    if (rq.getIdMau() != null) {
                        MauSac mauSac = mauSacRepository.findById(rq.getIdMau())
                                .orElseThrow(() -> new NotFoundException("Không tìm thấy màu sắc với ID: " + rq.getIdMau()));
                        chiTiet.setIdMau(mauSac);
                    }

                    if (rq.getIdRom() != null) {
                        Rom rom = romRepository.findById(rq.getIdRom())
                                .orElseThrow(() -> new NotFoundException("Không tìm thấy ROM với ID: " + rq.getIdRom()));
                        chiTiet.setIdRom(rom);
                    }
                } else {
                    // Cập nhật giá bán nếu giá mới cao hơn
                    if (rq.getGiaBan() != null && chiTiet.getGiaBan() != null &&
                            chiTiet.getGiaBan().compareTo(rq.getGiaBan()) < 0) {
                        chiTiet.setGiaBan(rq.getGiaBan());
                    }
                    // Cập nhật maSanPhamChiTiet nếu cần
                    if (rq.getMaSanPhamChiTiet() != null && !rq.getMaSanPhamChiTiet().trim().isEmpty()) {
                        chiTiet.setMaSanPhamChiTiet(rq.getMaSanPhamChiTiet());
                    }
                }

                // Lưu SanPhamChiTiet
                SanPhamChiTiet chiTietSaved = sanPhamChiTietRepository.save(chiTiet);

                Integer idMau = rq.getIdMau();
                String tenMau = mauSacRepository.findById(rq.getIdMau()).map(MauSac::getTenMau).orElse("color");

                System.out.println("id san pham: " + sanPhamAdminRequest.getIdModelSanPham());
                System.out.println("id color: " + rq.getIdMau());
                System.out.println("anh: " + rq.getHinhAnhs());
                // Nếu chưa xử lý màu này
                if (!mauDaCoAnh.containsKey(idMau)) {
                    Integer soAnh = hinhAnhRepository.anhQuantityOfSp(sanPhamAdminRequest.getIdModelSanPham(), idMau);
                    boolean daCoAnh = soAnh > 0 || (rq.getHinhAnhs() != null && !rq.getHinhAnhs().isEmpty());
                    mauDaCoAnh.put(idMau, daCoAnh);
                }

                if (!Boolean.TRUE.equals(mauDaCoAnh.get(idMau))) {
                    throw new BusinessException("Màu " + tenMau + " cần có ít nhất một ảnh.");
                }

                // Xử lý hình ảnh
                if (rq.getHinhAnhs() != null && !rq.getHinhAnhs().isEmpty()) {
                    // Lấy danh sách URL hiện có
                    Set<String> existingImageUrls = hinhAnhRepository.findByIdSanPhamChiTiet_Id(chiTietSaved.getId()).stream()
                            .map(HinhAnh::getUrl)
                            .collect(Collectors.toSet());

                    // Lọc các hình ảnh mới (không trùng với hiện có)
                    List<HinhAnh> hinhAnhs = rq.getHinhAnhs().stream()
                            .filter(ha -> ha.getUrl() != null && !ha.getUrl().trim().isEmpty())
                            .filter(ha -> !existingImageUrls.contains(ha.getUrl()))
                            .map(ha -> {
                                HinhAnh hinhAnh = new HinhAnh();
                                hinhAnh.setIdSanPhamChiTiet(chiTietSaved);
                                hinhAnh.setUrl(ha.getUrl());
                                hinhAnh.setImagePublicId(ha.getImagePublicId());
                                return hinhAnh;
                            }).toList();

                    if (!hinhAnhs.isEmpty()) {
                        hinhAnhRepository.saveAll(hinhAnhs);
                    }
                }

                // Xử lý IMEI
                if (rq.getImeis() != null && !rq.getImeis().isEmpty()) {
                    List<String> soImeis = rq.getImeis().stream()
                            .filter(imeiDto -> imeiDto.getSoImei() != null && !imeiDto.getSoImei().trim().isEmpty())
                            .map(imeiDto -> imeiDto.getSoImei().trim())
                            .distinct() // Loại bỏ IMEI trùng lặp trong request
                            .toList();

                    // Kiểm tra IMEI trùng lặp trong cơ sở dữ liệu
                    Set<String> imeiDaTonTai = imeiReposiory.findAllBySoImeiIn(soImeis).stream()
                            .map(Imei::getSoImei)
                            .collect(Collectors.toSet());

                    if (!imeiDaTonTai.isEmpty()) {
                        throw new BusinessException("Các số IMEI đã tồn tại: " + String.join(", ", imeiDaTonTai));
                    }

                    // Thêm IMEI mới
                    List<Imei> imeis = soImeis.stream()
                            .map(soImei -> {
                                Imei imei = new Imei();
                                imei.setSoImei(soImei);
                                imei.setTrangThaiImei(TrangThaiImei.AVAILABLE);
                                imei.setIdSanPhamChiTiet(chiTietSaved);
                                return imei;
                            }).toList();
                    imeiReposiory.saveAll(imeis);

                    // Cập nhật số lượng dựa trên số IMEI
                    int soLuongThucTe = imeiReposiory.countByIdSanPhamChiTiet(chiTietSaved);
                    chiTietSaved.setSoLuong(soLuongThucTe);
                    sanPhamChiTietRepository.save(chiTietSaved);
                }

                chiTietSet.add(chiTietSaved);
            }
        } else {
            throw new BusinessException("Danh sách chi tiết sản phẩm không được để trống.");
        }

        // Cập nhật danh sách chi tiết sản phẩm
        if (sanPham.getSanPhamChiTiets() == null) {
            sanPham.setSanPhamChiTiets(new HashSet<>());
        }
        sanPham.getSanPhamChiTiets().addAll(chiTietSet);
        sanPham = sanPhamRepo.save(sanPham);

        // === Bước 5: Tạo response ===
        SanPhamAdminResponse response = new SanPhamAdminResponse();
        response.setId(sanPham.getId());
        response.setMaSanPham(sanPham.getMaSanPham());
        response.setTenSanPham(sanPham.getTenSanPham());
        response.setTrangThaiSanPham(sanPham.getTrangThaiSanPham());
        response.setThuongHieu(sanPham.getThuongHieu());

        // Xử lý thông tin nhà cung cấp - sử dụng List để tránh lỗi NonUniqueResult
        List<NhaCungCapSp> nhaCungCapSpList = nhaCungCapSpRepository.findAllBySanPham(sanPham);

        if (!nhaCungCapSpList.isEmpty()) {
            // Lấy nhà cung cấp đầu tiên nếu có nhiều
            NhaCungCapSp nhaCungCapSp = nhaCungCapSpList.get(0);
            if (nhaCungCapSp.getNhaCungCap() != null) {
                NhaCungCap nha = nhaCungCapSp.getNhaCungCap();
                NhaCungCapAdminResponse nccRes = new NhaCungCapAdminResponse();
                nccRes.setTenNhaCungCap(nha.getTenNhaCungCap());
                nccRes.setSdt(nha.getSdt());
                nccRes.setEmail(nha.getEmail());
                nccRes.setDiaChi(nha.getDiaChi());
//                response.setNhaCungCapAdminResponse(nccRes);
            }
        }

        if (sanPham.getIdModelSanPham() != null) {
            ModelSanPham m = sanPham.getIdModelSanPham();
            ModelSanPhamAdminResponse modelRes = new ModelSanPhamAdminResponse();
            modelRes.setMaModelSanPham(m.getMaModelSanPham());
            modelRes.setTenModel(m.getTenModel());
            modelRes.setNamRaMat(m.getNamRaMat());
            response.setModelSanPhamAdminResponse(modelRes);
        }

        if (sanPham.getSanPhamChiTiets() != null) {
            Set<SanPhamChiTietResponse> chiTietResponses = sanPham.getSanPhamChiTiets().stream()
                    .map(this::mapToChiTietResponse)
                    .collect(Collectors.toSet());
            response.setSanPhamChiTiets(chiTietResponses);
        }

        return response;
    }

//    @Transactional(rollbackFor = Exception.class)
//    public SanPhamAdminResponse createSanPhamAdmin(SanPhamAdminRequest sanPhamAdminRequest) {
//        // === Bước 1: Kiểm tra model tồn tại ===
//        if (sanPhamAdminRequest.getIdModelSanPham() == null) {
//            throw new BusinessException("Model sản phẩm không được để trống.");
//        }
//
//        ModelSanPham modelSanPham = modelSanPhamRepository.findById(sanPhamAdminRequest.getIdModelSanPham())
//                .orElseThrow(() -> new NotFoundException("Không tìm thấy model sản phẩm"));
//
//        // === Bước 2: Tìm hoặc tạo mới SanPham tương ứng với Model ===
//        List<SanPham> sanPhamList = sanPhamRepo.findAllByIdModelSanPham_IdModelSanPham(sanPhamAdminRequest.getIdModelSanPham());
//        SanPham sanPham;
//
//        if (!sanPhamList.isEmpty()) {
//            sanPham = sanPhamList.get(0); // Lấy sản phẩm đầu tiên (có thể cần logic lọc thêm)
//        } else {
//            sanPham = new SanPham();
//            sanPham.setTenSanPham(sanPhamAdminRequest.getTenSanPham());
//            sanPham.setThuongHieu(sanPhamAdminRequest.getThuongHieu());
//            sanPham.setTrangThaiSanPham(Optional.ofNullable(sanPhamAdminRequest.getTrangThaiSanPham())
//                    .orElse(TrangThaiSanPham.ACTIVE));
//            if (sanPhamAdminRequest.getIdNhaCungCap() != null) {
//                NhaCungCap nhaCungCap = nhaCungCapRepository.findById(sanPhamAdminRequest.getIdNhaCungCap())
//                        .orElseThrow(() -> new NotFoundException("Không tìm thấy nhà cung cấp"));
//                sanPham.setIdNhaCungCap(nhaCungCap);
//            }
//            sanPham.setIdModelSanPham(modelSanPham);
//            sanPham = sanPhamRepo.save(sanPham);
//        }
//
//        Set<SanPhamChiTiet> chiTietSet = new HashSet<>();
//
//        // === Bước 3: Xử lý danh sách chi tiết sản phẩm ===
//        if (sanPhamAdminRequest.getSanPhamChiTiets() != null && !sanPhamAdminRequest.getSanPhamChiTiets().isEmpty()) {
//            for (SanPhamChiTietAdminRepuest rq : sanPhamAdminRequest.getSanPhamChiTiets()) {
//                // Kiểm tra xem SanPhamChiTiet có tồn tại với idMau và idRom không
//                List<SanPhamChiTiet> existingChiTiets = sanPhamChiTietRepository
//                        .findByIdSanPhamAndIdMau_IdAndIdRom_Id(sanPham, rq.getIdMau(), rq.getIdRom());
//
//                SanPhamChiTiet chiTiet;
//                if (!existingChiTiets.isEmpty()) {
//                    // Lấy bản ghi đầu tiên (hoặc áp dụng logic để chọn hoặc hợp nhất)
//                    chiTiet = existingChiTiets.get(0);
//                    chiTiet.setSoLuong(chiTiet.getSoLuong() + rq.getSoLuong());
//                    chiTiet.setGiaBan(rq.getGiaBan()); // Cập nhật giá bán nếu cần
//                } else {
//                    // Nếu không tồn tại, tạo mới SanPhamChiTiet
//                    chiTiet = new SanPhamChiTiet();
//                    chiTiet.setMaSanPhamChiTiet(rq.getMaSanPhamChiTiet());
//                    chiTiet.setSoLuong(rq.getSoLuong());
//                    chiTiet.setGiaBan(rq.getGiaBan());
//                    chiTiet.setIdSanPham(sanPham);
//
//                    // Kiểm tra khóa ngoại
//                    if (rq.getIdMau() != null) {
//                        MauSac mauSac = mauSacRepository.findById(rq.getIdMau())
//                                .orElseThrow(() -> new NotFoundException("Không tìm thấy màu sắc với ID: " + rq.getIdMau()));
//                        chiTiet.setIdMau(mauSac);
//                    }
//
//                    if (rq.getIdRom() != null) {
//                        Rom rom = romRepository.findById(rq.getIdRom())
//                                .orElseThrow(() -> new NotFoundException("Không tìm thấy ROM với ID: " + rq.getIdRom()));
//                        chiTiet.setIdRom(rom);
//                    }
//                }
//
//// Lưu chi tiết sản phẩm
//                SanPhamChiTiet chiTietSaved = sanPhamChiTietRepository.save(chiTiet);
//
//                // Lưu hình ảnh (nếu có và chỉ khi tạo mới SanPhamChiTiet)
//                if (existingChiTiets == null && rq.getHinhAnhs() != null && !rq.getHinhAnhs().isEmpty()) {
//                    List<HinhAnh> hinhAnhs = rq.getHinhAnhs().stream()
//                            .filter(ha -> ha.getUrl() != null)
//                            .map(ha -> {
//                                HinhAnh hinhAnh = new HinhAnh();
//                                hinhAnh.setIdSanPhamChiTiet(chiTietSaved);
//                                hinhAnh.setUrl(ha.getUrl());
//                                hinhAnh.setImagePublicId(ha.getImagePublicId());
//                                return hinhAnh;
//                            }).toList();
//                    hinhAnhRepository.saveAll(hinhAnhs);
//                }
//
//                // Lưu danh sách IMEI (nếu có)
//                if (rq.getImeis() != null && !rq.getImeis().isEmpty()) {
//                    List<Imei> imeis = rq.getImeis().stream()
//                            .filter(imeiDto -> imeiDto.getSoImei() != null && !imeiDto.getSoImei().trim().isEmpty())
//                            .map(imeiDto -> {
//                                String soImei = imeiDto.getSoImei().trim();
//                                if (imeiReposiory.existsBySoImei(soImei)) {
//                                    throw new BusinessException("Số IMEI đã tồn tại: " + soImei);
//                                }
//                                Imei imei = new Imei();
//                                imei.setSoImei(soImei);
//                                imei.setTrangThaiImei(TrangThaiImei.AVAILABLE);
//                                imei.setIdSanPhamChiTiet(chiTietSaved);
//                                return imei;
//                            }).toList();
//                    imeiReposiory.saveAll(imeis);
//                }
//
//                chiTietSet.add(chiTietSaved);
//            }
//        }
//
//        // === Bước 4: Gán chi tiết sản phẩm vào SanPham ===
//        sanPham.getSanPhamChiTiets().addAll(chiTietSet);
//        sanPham = sanPhamRepo.save(sanPham);
//
//        // === Bước 5: Tạo response ===
//        SanPhamAdminResponse response = new SanPhamAdminResponse();
//        response.setId(sanPham.getId());
//        response.setMaSanPham(sanPham.getMaSanPham());
//        response.setTenSanPham(sanPham.getTenSanPham());
//        response.setTrangThaiSanPham(sanPham.getTrangThaiSanPham());
//        response.setThuongHieu(sanPham.getThuongHieu());
//
//        if (sanPham.getIdNhaCungCap() != null) {
//            NhaCungCap nha = sanPham.getIdNhaCungCap();
//            NhaCungCapAdminResponse nccRes = new NhaCungCapAdminResponse();
//            nccRes.setTenNhaCungCap(nha.getTenNhaCungCap());
//            nccRes.setSdt(nha.getSdt());
//            nccRes.setEmail(nha.getEmail());
//            nccRes.setDiaChi(nha.getDiaChi());
//            response.setNhaCungCapAdminResponse(nccRes);
//        }
//
//        if (sanPham.getIdModelSanPham() != null) {
//            ModelSanPham m = sanPham.getIdModelSanPham();
//            ModelSanPhamAdminResponse modelRes = new ModelSanPhamAdminResponse();
//            modelRes.setMaModelSanPham(m.getMaModelSanPham());
//            modelRes.setTenModel(m.getTenModel());
//            modelRes.setNamRaMat(m.getNamRaMat());
//            response.setModelSanPhamAdminResponse(modelRes);
//        }
//
//        if (sanPham.getSanPhamChiTiets() != null) {
//            Set<SanPhamChiTietResponse> chiTietResponses = sanPham.getSanPhamChiTiets().stream()
//                    .map(this::mapToChiTietResponse)
//                    .collect(Collectors.toSet());
//            response.setSanPhamChiTiets(chiTietResponses);
//        }
//
//        return response;
//    }


//    @Transactional(rollbackFor = Exception.class)
//    public SanPhamAdminResponse updateSanPhamAdmin(Integer id, SanPhamAdminRequest sanPhamAdminRequest) {
//
//        // === Bước 1: Kiểm tra dữ liệu đầu vào ===
////        validateSanPhamRequest(sanPhamAdminRequest);
//
//        // === Bước 2: Tìm sản phẩm theo ID ===
//        SanPham sanPham = sanPhamRepo.findById(id)
//                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm với ID: " + id));
//
//        // === Bước 3: Cập nhật thông tin chung cho sản phẩm ===
//        sanPham.setId(sanPhamAdminRequest.getId());
//        sanPham.setTenSanPham(sanPhamAdminRequest.getTenSanPham());
//        sanPham.setThuongHieu(sanPhamAdminRequest.getThuongHieu());
//        sanPham.setTrangThaiSanPham(Optional.ofNullable(sanPhamAdminRequest.getTrangThaiSanPham())
//                .orElse(TrangThaiSanPham.ACTIVE));
//
//        nhaCungCapSpRepository.deleteBySanPham_Id(sanPham.getId());
//        if (sanPhamAdminRequest.getIdNhaCungCaps() != null && !sanPhamAdminRequest.getIdNhaCungCaps().isEmpty()) {
//            for (Integer idNhaCungCap : sanPhamAdminRequest.getIdNhaCungCaps()) {
//                NhaCungCap nhaCungCap = nhaCungCapRepository.findById(idNhaCungCap)
//                        .orElseThrow(() -> new NotFoundException("Không tìm thấy nhà cung cấp với ID: " + idNhaCungCap));
//                NhaCungCapSp nhaCungCapSp = new NhaCungCapSp();
//                nhaCungCapSp.setNhaCungCap(nhaCungCap);
//                nhaCungCapSp.setSanPham(sanPham);
//                nhaCungCapSpRepository.save(nhaCungCapSp);
//            }
//        }
//
//        // Cập nhật model sản phẩm nếu có
//        if (sanPhamAdminRequest.getIdModelSanPham() != null) {
//            ModelSanPham modelSanPham = modelSanPhamRepository.findById(sanPhamAdminRequest.getIdModelSanPham())
//                    .orElseThrow(() -> new NotFoundException("Không tìm thấy model sản phẩm với ID: " + sanPhamAdminRequest.getIdModelSanPham()));
//            sanPham.setIdModelSanPham(modelSanPham);
//        }
//
//        // === Bước 4: Kiểm tra trùng lặp biến thể ===
//        if (sanPhamAdminRequest.getSanPhamChiTiets() != null && !sanPhamAdminRequest.getSanPhamChiTiets().isEmpty()) {
//            Set<String> variantKeys = new HashSet<>();
//            for (SanPhamChiTietAdminRepuest rq : sanPhamAdminRequest.getSanPhamChiTiets()) {
//                String variantKey = (rq.getIdMau() != null ? rq.getIdMau() : "null") + "-" + (rq.getIdRom() != null ? rq.getIdRom() : "null");
//                if (!variantKeys.add(variantKey)) {
//                    throw new BusinessException("Tổ hợp màu sắc và ROM trùng lặp: Màu ID " + rq.getIdMau() + ", ROM ID " + rq.getIdRom());
//                }
//            }
//        }
//
//        // === Bước 5: Xử lý chi tiết sản phẩm ===
//        Set<SanPhamChiTiet> updatedChiTietSet = new HashSet<>();
//        if (sanPhamAdminRequest.getSanPhamChiTiets() != null && !sanPhamAdminRequest.getSanPhamChiTiets().isEmpty()) {
//            for (SanPhamChiTietAdminRepuest rq : sanPhamAdminRequest.getSanPhamChiTiets()) {
//                SanPhamChiTiet chiTiet;
//                if (rq.getId() != null) {
//                    chiTiet = sanPhamChiTietRepository.findById(rq.getId())
//                            .orElseThrow(() -> new NotFoundException("Không tìm thấy chi tiết sản phẩm ID: " + rq.getId()));
//                } else {
//                    chiTiet = new SanPhamChiTiet();
//                    chiTiet.setIdSanPham(sanPham);
//                }
//
//                // Cập nhật thông tin cơ bản
//                chiTiet.setMaSanPhamChiTiet(rq.getMaSanPhamChiTiet());
//                chiTiet.setGiaBan(rq.getGiaBan());
//
//                // Cập nhật màu sắc và ROM
//                chiTiet.setIdMau(rq.getIdMau() != null
//                        ? mauSacRepository.findById(rq.getIdMau()).orElseThrow(() -> new NotFoundException("Không tìm thấy màu sắc với ID: " + rq.getIdMau()))
//                        : null);
//                chiTiet.setIdRom(rq.getIdRom() != null
//                        ? romRepository.findById(rq.getIdRom()).orElseThrow(() -> new NotFoundException("Không tìm thấy ROM với ID: " + rq.getIdRom()))
//                        : null);
//
//                // Lưu chi tiết sản phẩm
//                SanPhamChiTiet chiTietSaved = sanPhamChiTietRepository.save(chiTiet);
//
//                // === Xử lý hình ảnh ===
//                if (rq.getHinhAnhs() != null && !rq.getHinhAnhs().isEmpty()) {
//                    // Lấy danh sách URL mới
//                    Set<String> newImageUrls = rq.getHinhAnhs().stream()
//                            .filter(ha -> ha.getUrl() != null && !ha.getUrl().trim().isEmpty())
//                            .map(HinhAnhAdminRequest::getUrl)
//                            .collect(Collectors.toSet());
//
//                    // Xóa hình ảnh cũ không còn trong danh sách mới
//                    List<HinhAnh> oldImages = hinhAnhRepository.findByIdSanPhamChiTiet_Id(chiTietSaved.getId());
//                    oldImages.removeIf(img -> newImageUrls.contains(img.getUrl()));
//                    hinhAnhRepository.deleteAll(oldImages);
//
//                    // Thêm hình ảnh mới
//                    List<HinhAnh> newImages = rq.getHinhAnhs().stream()
//                            .filter(ha -> ha.getUrl() != null && !ha.getUrl().trim().isEmpty())
////                            .filter(ha -> !hinhAnhRepository.existsByIdSanPhamChiTietAndUrl(chiTietSaved.getId(), ha.getUrl()))
//                            .map(ha -> {
//                                HinhAnh hinhAnh = new HinhAnh();
//                                hinhAnh.setIdSanPhamChiTiet(chiTietSaved);
//                                hinhAnh.setUrl(ha.getUrl());
//                                hinhAnh.setImagePublicId(ha.getImagePublicId());
//                                return hinhAnh;
//                            }).toList();
//
//                    if (newImages.isEmpty() && newImageUrls.isEmpty()) {
//                        throw new BusinessException("Danh sách hình ảnh không hợp lệ");
//                    }
//
//                    hinhAnhRepository.saveAll(newImages);
//                } else {
//                    // Xóa tất cả hình ảnh nếu không có danh sách mới
//                    hinhAnhRepository.deleteByIdSanPhamChiTiet(chiTietSaved.getId());
//                }
//
//                // === Xử lý IMEI ===
//                if (rq.getImeis() != null && !rq.getImeis().isEmpty()) {
//                    // Lấy danh sách IMEI mới
//                    List<String> imeisFromFE = rq.getImeis().stream()
//                            .map(ImeiAdminRequest::getSoImei)
//                            .filter(Objects::nonNull)
//                            .map(String::trim)
//                            .filter(s -> !s.isEmpty())
//                            .toList();
//
//                    // Kiểm tra IMEI trùng lặp với các biến thể khác
//                    Set<String> imeiDaTonTai = imeiReposiory.findAllBySoImeiIn(imeisFromFE).stream()
//                            .filter(imei -> !imei.getIdSanPhamChiTiet().getId().equals(chiTietSaved.getId()))
//                            .map(Imei::getSoImei)
//                            .collect(Collectors.toSet());
//
//                    if (!imeiDaTonTai.isEmpty()) {
//                        throw new BusinessException("Các IMEI đã tồn tại ở biến thể khác: " + String.join(", ", imeiDaTonTai));
//                    }
//
//                    // Xóa IMEI cũ có trạng thái AVAILABLE
//                    List<Imei> oldImeis = imeiReposiory.findByIdSanPhamChiTiet_Id(chiTietSaved.getId());
//                    oldImeis.removeIf(imei -> imei.getTrangThaiImei() != TrangThaiImei.AVAILABLE);
//                    imeiReposiory.deleteAll(oldImeis);
//
//                    // Thêm IMEI mới
//                    List<Imei> imeiMoi = imeisFromFE.stream()
//                            .filter(imei -> !imeiDaTonTai.contains(imei))
//                            .map(soImei -> {
//                                Imei imei = new Imei();
//                                imei.setSoImei(soImei);
//                                imei.setTrangThaiImei(TrangThaiImei.AVAILABLE);
//                                imei.setIdSanPhamChiTiet(chiTietSaved);
//                                return imei;
//                            }).toList();
//
//                    imeiReposiory.saveAll(imeiMoi);
//
//                    // Cập nhật số lượng dựa trên số IMEI
//                    int soLuongThucTe = imeiReposiory.soLuongImei(TrangThaiImei.AVAILABLE, chiTietSaved.getId());
//                    chiTietSaved.setSoLuong(soLuongThucTe);
//                    sanPhamChiTietRepository.save(chiTietSaved);
//                } else {
//                    // Nếu không có IMEI, đặt số lượng về 0
//                    chiTietSaved.setSoLuong(0);
//                    sanPhamChiTietRepository.save(chiTietSaved);
//                }
//
//                updatedChiTietSet.add(chiTietSaved);
//            }
//        }
//
//        // === Bước 6: Xóa các chi tiết sản phẩm cũ không còn trong request ===
//        Set<SanPhamChiTiet> chiTietCanXoa = sanPham.getSanPhamChiTiets().stream()
//                .filter(oldChiTiet -> updatedChiTietSet.stream()
//                        .noneMatch(newChiTiet -> newChiTiet.getId() != null && newChiTiet.getId().equals(oldChiTiet.getId())))
//                .collect(Collectors.toSet());
//
//        chiTietCanXoa.forEach(chiTiet -> {
//            // Kiểm tra xem biến thể có IMEI đang sử dụng không
//            long usedImeiCount = imeiReposiory.countByIdSanPhamChiTietIdAndTrangThaiImeiNot(chiTiet.getId(), TrangThaiImei.AVAILABLE);
//            if (usedImeiCount > 0) {
//                throw new BusinessException("Không thể xóa biến thể " + chiTiet.getMaSanPhamChiTiet() + " vì có " + usedImeiCount + " IMEI đang được sử dụng");
//            }
//            hinhAnhRepository.deleteByIdSanPhamChiTiet(chiTiet.getId());
//            imeiReposiory.deleteByIdSanPhamChiTietId(chiTiet.getId());
//        });
//        sanPhamChiTietRepository.deleteAll(chiTietCanXoa);
//
//        // Cập nhật danh sách chi tiết sản phẩm
//        sanPham.getSanPhamChiTiets().clear();
//        sanPham.getSanPhamChiTiets().addAll(updatedChiTietSet);
//
//        // Lưu sản phẩm
//        sanPham = sanPhamRepo.save(sanPham);
//
//        // === Bước 7: Tạo response ===
//        SanPhamAdminResponse response = new SanPhamAdminResponse();
//        response.setId(sanPham.getId());
//        response.setMaSanPham(sanPham.getMaSanPham());
//        response.setTenSanPham(sanPham.getTenSanPham());
//        response.setThuongHieu(sanPham.getThuongHieu());
//        response.setTrangThaiSanPham(sanPham.getTrangThaiSanPham());
//
//        List<NhaCungCapSp> nhaCungCapSpList = nhaCungCapSpRepository.findBySanPham_Id(sanPham.getId());
//        List<NhaCungCapAdminResponse> nhaCungCaps = nhaCungCapSpList.stream()
//                .map(nhaCungCapSp -> {
//                    NhaCungCapAdminResponse nhaCungCapDto = new NhaCungCapAdminResponse();
//                    nhaCungCapDto.setId(nhaCungCapSp.getNhaCungCap().getId()); // ĐÃ SỬA: Thêm ID nhà cung cấp
//                    nhaCungCapDto.setTenNhaCungCap(nhaCungCapSp.getNhaCungCap().getTenNhaCungCap());
//                    nhaCungCapDto.setDiaChi(nhaCungCapSp.getNhaCungCap().getDiaChi());
//                    nhaCungCapDto.setSdt(nhaCungCapSp.getNhaCungCap().getSdt());
//                    nhaCungCapDto.setEmail(nhaCungCapSp.getNhaCungCap().getEmail());
//                    return nhaCungCapDto;
//                })
//                .collect(Collectors.toList());
//        response.setNhaCungCaps(nhaCungCaps);
//
//        if (sanPham.getIdModelSanPham() != null) {
//            ModelSanPham m = sanPham.getIdModelSanPham();
//            ModelSanPhamAdminResponse modelRes = new ModelSanPhamAdminResponse();
//            modelRes.setMaModelSanPham(m.getMaModelSanPham());
//            modelRes.setTenModel(m.getTenModel());
//            modelRes.setNamRaMat(m.getNamRaMat());
//            response.setModelSanPhamAdminResponse(modelRes);
//        }
//
//        if (sanPham.getSanPhamChiTiets() != null) {
//            Set<SanPhamChiTietResponse> chiTietResponses = sanPham.getSanPhamChiTiets().stream()
//                    .map(this::mapToChiTietResponse)
//                    .collect(Collectors.toSet());
//            response.setSanPhamChiTiets(chiTietResponses);
//        }
//
//        return response;
//    }

    @Transactional(rollbackFor = Exception.class)
    public SanPhamAdminResponse updateSanPhamAdmin(Integer id, SanPhamAdminRequest sanPhamAdminRequest) {

        // === Bước 1: Kiểm tra dữ liệu đầu vào ===
        // validateSanPhamRequest(sanPhamAdminRequest); // Bỏ comment nếu cần sử dụng

        // === Bước 2: Tìm sản phẩm theo ID ===
        SanPham sanPham = sanPhamRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm với ID: " + id));

        // === Bước 3: Cập nhật thông tin chung cho sản phẩm ===
        sanPham.setId(sanPhamAdminRequest.getId());
        sanPham.setTenSanPham(sanPhamAdminRequest.getTenSanPham());
        sanPham.setThuongHieu(sanPhamAdminRequest.getThuongHieu());
        sanPham.setTrangThaiSanPham(Optional.ofNullable(sanPhamAdminRequest.getTrangThaiSanPham())
                .orElse(TrangThaiSanPham.ACTIVE));

        nhaCungCapSpRepository.deleteBySanPham_Id(sanPham.getId());
        if (sanPhamAdminRequest.getIdNhaCungCaps() != null && !sanPhamAdminRequest.getIdNhaCungCaps().isEmpty()) {
            for (Integer idNhaCungCap : sanPhamAdminRequest.getIdNhaCungCaps()) {
                NhaCungCap nhaCungCap = nhaCungCapRepository.findById(idNhaCungCap)
                        .orElseThrow(() -> new NotFoundException("Không tìm thấy nhà cung cấp với ID: " + idNhaCungCap));
                NhaCungCapSp nhaCungCapSp = new NhaCungCapSp();
                nhaCungCapSp.setNhaCungCap(nhaCungCap);
                nhaCungCapSp.setSanPham(sanPham);
                nhaCungCapSpRepository.save(nhaCungCapSp);
            }
        }

        // Cập nhật model sản phẩm nếu có
        if (sanPhamAdminRequest.getIdModelSanPham() != null) {
            ModelSanPham modelSanPham = modelSanPhamRepository.findById(sanPhamAdminRequest.getIdModelSanPham())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy model sản phẩm với ID: " + sanPhamAdminRequest.getIdModelSanPham()));
            sanPham.setIdModelSanPham(modelSanPham);
        }

        // === Bước 4: Kiểm tra trùng lặp biến thể ===
        if (sanPhamAdminRequest.getSanPhamChiTiets() != null && !sanPhamAdminRequest.getSanPhamChiTiets().isEmpty()) {
            Set<String> variantKeys = new HashSet<>();
            for (SanPhamChiTietAdminRepuest rq : sanPhamAdminRequest.getSanPhamChiTiets()) {
                String variantKey = (rq.getIdMau() != null ? rq.getIdMau() : "null") + "-" + (rq.getIdRom() != null ? rq.getIdRom() : "null");
                if (!variantKeys.add(variantKey)) {
                    throw new BusinessException("Tổ hợp màu sắc và ROM trùng lặp: Màu ID " + rq.getIdMau() + ", ROM ID " + rq.getIdRom());
                }
            }
        }

        // === Bước 5: Xử lý chi tiết sản phẩm ===
        Set<SanPhamChiTiet> updatedChiTietSet = new HashSet<>();
        if (sanPhamAdminRequest.getSanPhamChiTiets() != null && !sanPhamAdminRequest.getSanPhamChiTiets().isEmpty()) {
            for (SanPhamChiTietAdminRepuest rq : sanPhamAdminRequest.getSanPhamChiTiets()) {
                SanPhamChiTiet chiTiet;
                if (rq.getId() != null) {
                    chiTiet = sanPhamChiTietRepository.findById(rq.getId())
                            .orElseThrow(() -> new NotFoundException("Không tìm thấy chi tiết sản phẩm ID: " + rq.getId()));
                } else {
                    chiTiet = new SanPhamChiTiet();
                    chiTiet.setIdSanPham(sanPham);
                }

                // Cập nhật thông tin cơ bản
                chiTiet.setMaSanPhamChiTiet(rq.getMaSanPhamChiTiet());
                chiTiet.setGiaBan(rq.getGiaBan());

                // Cập nhật màu sắc và ROM
                chiTiet.setIdMau(rq.getIdMau() != null
                        ? mauSacRepository.findById(rq.getIdMau()).orElseThrow(() -> new NotFoundException("Không tìm thấy màu sắc với ID: " + rq.getIdMau()))
                        : null);
                chiTiet.setIdRom(rq.getIdRom() != null
                        ? romRepository.findById(rq.getIdRom()).orElseThrow(() -> new NotFoundException("Không tìm thấy ROM với ID: " + rq.getIdRom()))
                        : null);

                // Lưu chi tiết sản phẩm
                SanPhamChiTiet chiTietSaved = sanPhamChiTietRepository.save(chiTiet);

                // === Xử lý hình ảnh ===
                // === Xử lý IMEI ===
                if (rq.getHinhAnhs() != null && !rq.getHinhAnhs().isEmpty()) {
                    // Lấy danh sách URL mới
                    Set<String> newImageUrls = rq.getHinhAnhs().stream()
                            .filter(ha -> ha.getUrl() != null && !ha.getUrl().trim().isEmpty())
                            .map(HinhAnhAdminRequest::getUrl)
                            .collect(Collectors.toSet());

                    // Xóa hình ảnh cũ không còn trong danh sách mới
                    List<HinhAnh> oldImages = hinhAnhRepository.findByIdSanPhamChiTiet_Id(chiTietSaved.getId());
                    oldImages.removeIf(img -> newImageUrls.contains(img.getUrl()));
                    hinhAnhRepository.deleteAll(oldImages);

                    // Thêm hình ảnh mới
                    List<HinhAnh> newImages = rq.getHinhAnhs().stream()
                            .filter(ha -> ha.getUrl() != null && !ha.getUrl().trim().isEmpty())
                            .map(ha -> {
                                HinhAnh hinhAnh = new HinhAnh();
                                hinhAnh.setIdSanPhamChiTiet(chiTietSaved);
                                hinhAnh.setUrl(ha.getUrl());
                                hinhAnh.setImagePublicId(ha.getImagePublicId());
                                return hinhAnh;
                            }).toList();

                    if (newImages.isEmpty() && newImageUrls.isEmpty()) {
                        throw new BusinessException("Danh sách hình ảnh không hợp lệ");
                    }

                    hinhAnhRepository.saveAll(newImages);
                } else {
                    // Xóa tất cả hình ảnh nếu không có danh sách mới
                    hinhAnhRepository.deleteByIdSanPhamChiTiet(chiTietSaved.getId());
                }

                // === Xử lý IMEI ===
                if (rq.getImeis() != null && !rq.getImeis().isEmpty()) {
                    // Lấy danh sách IMEI mới
                    List<String> imeisFromFE = rq.getImeis().stream()
                            .map(ImeiAdminRequest::getSoImei)
                            .filter(Objects::nonNull)
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .toList();

                    // Lấy danh sách IMEI hiện tại của biến thể này
                    List<Imei> currentImeis = imeiReposiory.findByIdSanPhamChiTiet_Id(chiTietSaved.getId());
                    List<String> currentImeiStrings = currentImeis.stream()
                            .map(Imei::getSoImei)
                            .collect(Collectors.toList());

                    // Xóa tất cả IMEI cũ có trạng thái AVAILABLE
                    List<Imei> oldImeis = currentImeis.stream()
                            .filter(imei -> imei.getTrangThaiImei() == TrangThaiImei.AVAILABLE)
                            .collect(Collectors.toList());
                    imeiReposiory.deleteAll(oldImeis);

                    // Thêm hoặc cập nhật IMEI
                    List<Imei> imeiMoi = new ArrayList<>();
                    for (String soImei : imeisFromFE) {
                        // Kiểm tra xem IMEI có tồn tại trong cơ sở dữ liệu không
                        Optional<Imei> existingImei = Optional.ofNullable(imeiReposiory.findBySoImei(soImei));
                        Imei imei;

                        if (existingImei.isPresent()) {
                            imei = existingImei.get();
                            // Nếu IMEI đã thuộc biến thể hiện tại, giữ nguyên
                            if (imei.getIdSanPhamChiTiet().getId().equals(chiTietSaved.getId())) {
                                imei.setTrangThaiImei(TrangThaiImei.AVAILABLE);
                            } else {
                                // Chuyển IMEI sang biến thể hiện tại nếu trạng thái là AVAILABLE
                                if (imei.getTrangThaiImei() == TrangThaiImei.AVAILABLE) {
                                    imei.setIdSanPhamChiTiet(chiTietSaved);
                                    imei.setTrangThaiImei(TrangThaiImei.AVAILABLE);
                                } else {
                                    throw new BusinessException("IMEI " + soImei + " đã được sử dụng ở biến thể khác và không thể chuyển vì trạng thái không phải AVAILABLE");
                                }
                            }
                        } else {
                            // Tạo IMEI mới
                            imei = new Imei();
                            imei.setSoImei(soImei);
                            imei.setTrangThaiImei(TrangThaiImei.AVAILABLE);
                            imei.setIdSanPhamChiTiet(chiTietSaved);
                        }
                        imeiMoi.add(imei);
                    }

                    imeiReposiory.saveAll(imeiMoi);

                    // Cập nhật số lượng dựa trên số IMEI
                    int soLuongThucTe = imeisFromFE.size();
                    chiTietSaved.setSoLuong(soLuongThucTe);
                    sanPhamChiTietRepository.save(chiTietSaved);
                } else {
                    // Nếu không có IMEI, xóa tất cả IMEI cũ và đặt số lượng về 0
                    imeiReposiory.deleteByIdSanPhamChiTietId(chiTietSaved.getId());
                    chiTietSaved.setSoLuong(0);
                    sanPhamChiTietRepository.save(chiTietSaved);
                }

                updatedChiTietSet.add(chiTietSaved);
            }
        }

        // === Bước 6: Xóa các chi tiết sản phẩm cũ không còn trong request ===
        Set<SanPhamChiTiet> chiTietCanXoa = sanPham.getSanPhamChiTiets().stream()
                .filter(oldChiTiet -> updatedChiTietSet.stream()
                        .noneMatch(newChiTiet -> newChiTiet.getId() != null && newChiTiet.getId().equals(oldChiTiet.getId())))
                .collect(Collectors.toSet());

        chiTietCanXoa.forEach(chiTiet -> {
            // Kiểm tra xem biến thể có IMEI đang sử dụng không
            long usedImeiCount = imeiReposiory.countByIdSanPhamChiTietIdAndTrangThaiImeiNot(chiTiet.getId(), TrangThaiImei.AVAILABLE);
            if (usedImeiCount > 0) {
                throw new BusinessException("Không thể xóa biến thể " + chiTiet.getMaSanPhamChiTiet() + " vì có " + usedImeiCount + " IMEI đang được sử dụng");
            }
            hinhAnhRepository.deleteByIdSanPhamChiTiet(chiTiet.getId());
            imeiReposiory.deleteByIdSanPhamChiTietId(chiTiet.getId());
        });
        sanPhamChiTietRepository.deleteAll(chiTietCanXoa);

        // Cập nhật danh sách chi tiết sản phẩm
        sanPham.getSanPhamChiTiets().clear();
        sanPham.getSanPhamChiTiets().addAll(updatedChiTietSet);

        // Lưu sản phẩm
        sanPham = sanPhamRepo.save(sanPham);

        // === Bước 7: Tạo response ===
        SanPhamAdminResponse response = new SanPhamAdminResponse();
        response.setId(sanPham.getId());
        response.setMaSanPham(sanPham.getMaSanPham());
        response.setTenSanPham(sanPham.getTenSanPham());
        response.setThuongHieu(sanPham.getThuongHieu());
        response.setTrangThaiSanPham(sanPham.getTrangThaiSanPham());

        List<NhaCungCapSp> nhaCungCapSpList = nhaCungCapSpRepository.findBySanPham_Id(sanPham.getId());
        List<NhaCungCapAdminResponse> nhaCungCaps = nhaCungCapSpList.stream()
                .map(nhaCungCapSp -> {
                    NhaCungCapAdminResponse nhaCungCapDto = new NhaCungCapAdminResponse();
                    nhaCungCapDto.setId(nhaCungCapSp.getNhaCungCap().getId());
                    nhaCungCapDto.setTenNhaCungCap(nhaCungCapSp.getNhaCungCap().getTenNhaCungCap());
                    nhaCungCapDto.setDiaChi(nhaCungCapSp.getNhaCungCap().getDiaChi());
                    nhaCungCapDto.setSdt(nhaCungCapSp.getNhaCungCap().getSdt());
                    nhaCungCapDto.setEmail(nhaCungCapSp.getNhaCungCap().getEmail());
                    return nhaCungCapDto;
                })
                .collect(Collectors.toList());
        response.setNhaCungCaps(nhaCungCaps);

        if (sanPham.getIdModelSanPham() != null) {
            ModelSanPham m = sanPham.getIdModelSanPham();
            ModelSanPhamAdminResponse modelRes = new ModelSanPhamAdminResponse();
            modelRes.setMaModelSanPham(m.getMaModelSanPham());
            modelRes.setTenModel(m.getTenModel());
            modelRes.setNamRaMat(m.getNamRaMat());
            response.setModelSanPhamAdminResponse(modelRes);
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
    public void deleteSanPhamAdmin(Integer id) {
        SanPham sanPham = sanPhamRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm với ID: " + id));
        sanPham.setTrangThaiSanPham(TrangThaiSanPham.ACTIVE);
        sanPhamRepo.save(sanPham);
    }
    public Page<SanPhamBanHangAdminResponse> getProductNames(String tenSanPham, int page, int size, Integer selectedIdKhachHang,
                                                             Integer loaiSanPham, BigDecimal giaTu,BigDecimal giaDen,
                                                             Integer soLuongTu, Integer soLuongDen,
                                                             String maSpct, Integer dungLuong, Integer tenMau) {

        String keyword = (tenSanPham == null || tenSanPham.trim().isEmpty() || tenSanPham.trim().equalsIgnoreCase("all"))
                ? ""
                : tenSanPham.trim();
        String maSpctKeyword = (maSpct == null || maSpct.trim().isEmpty()) ? "" : maSpct.trim();

        Pageable pageable = PageRequest.of(page, size);
        Page<SanPhamChiTiet> chiTietPage = sanPhamChiTietRepository.findByTenSanPhamAndTrangThai(keyword, TrangThaiSanPham.ACTIVE,
                loaiSanPham,giaTu,giaDen,soLuongTu,soLuongDen,maSpctKeyword,dungLuong,tenMau, pageable);

        return chiTietPage.map(spct -> {
            SanPhamBanHangAdminResponse response = SanPhamBanHangAdminResponse.converDto(spct);
            response.setGiaBan(khuyenMaiAdminService.tinhGiaSauKhuyenMai(spct, selectedIdKhachHang));
            return response;
        });
    }

    public Page<SanPhamBanHangAdminResponse> getProductMas(String maSanPham, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SanPhamChiTiet> chiTietPage = sanPhamChiTietRepository.findByIdSanPham_MaSanPhamContainingAndIdSanPham_TrangThaiSanPham(maSanPham, TrangThaiSanPham.ACTIVE, pageable);

        return chiTietPage.map(SanPhamBanHangAdminResponse::converDto);
    }

    public Page<String> getProductNameCategory(int pageNo, int pageSize){
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("ten_dong_san_pham").ascending());
        return sanPhamRepo.findTenDongSanPham(pageable);
    }

    private void validateSanPhamRequest(SanPhamAdminRequest request, Boolean isUpdate) {
        Set<String> variantKeySet = new HashSet<>();
        Set<String> allImeis = new HashSet<>();

        ModelSanPham model = modelSanPhamRepository
                .findById(request.getIdModelSanPham())
                .orElseThrow(() -> new BusinessException("model.notFound"));

        Integer idLoai = model.getIdLoai().getId();

        if (isUpdate) {
            sanPhamChiTietAdminService.validateKhongTrungBienTheTheoLoai_Update(idLoai, request.getSanPhamChiTiets());
        } else {
            sanPhamChiTietAdminService.validateKhongTrungBienTheTheoLoai(idLoai, request.getSanPhamChiTiets());
        }

        for (SanPhamChiTietAdminRepuest rq : request.getSanPhamChiTiets()) {
            String key = rq.getIdMau() + "-" + rq.getIdRom();

            if (!variantKeySet.add(key)) {
                throw new BusinessException("chitiet.variant.duplicate", "Biến thể trùng tổ hợp màu - ROM: " + key);
            }

            List<String> imeis = rq.getImeis().stream()
                    .map(i -> i.getSoImei().trim())
                    .filter(s -> !s.isEmpty())
                    .toList();

            // 1. IMEI trùng trong cùng biến thể
            if (new HashSet<>(imeis).size() < imeis.size()) {
                throw new BusinessException("chitiet.imei.duplicate.self", "IMEI bị trùng trong cùng biến thể");
            }

            for (String imei : imeis) {
                // 2. IMEI trùng giữa các biến thể
                if (!allImeis.add(imei)) {
                    throw new BusinessException("chitiet.imei.duplicate.cross", "IMEI trùng giữa các biến thể: " + imei);
                }

                // 3. IMEI đã tồn tại trong DB (trừ chính biến thể)
                boolean exists;
                if (isUpdate && rq.getId() != null) {
                    exists = imeiReposiory.existsBySoImeiExceptChiTietId(imei, rq.getId()) == 1;
                } else {
                    exists = imeiReposiory.existsBySoImei(imei);
                }

                if (exists) {
                    throw new BusinessException("chitiet.imei.exists", "IMEI đã tồn tại: " + imei);
                }
            }

            // 4. So sánh số lượng IMEI và số lượng sản phẩm
            if (!Objects.equals(rq.getSoLuong(), imeis.size())) {
                throw new BusinessException("IMEI phải đúng với số lượng: " + rq.getSoLuong());
            }
        }
    }

    public Map<String,List<?>> fillDataForPulldown(){
        Map<String,List<?>> map = new HashMap<>();
        map.put("loai",loaiRepository.findAll());
        map.put("mauSac",mauSacRepository.findAll());
        map.put("dungLuong",romRepository.findAll());
        return map;
    }
}


