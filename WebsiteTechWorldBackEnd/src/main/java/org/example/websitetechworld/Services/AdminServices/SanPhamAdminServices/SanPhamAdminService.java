package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;

import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.ImeiAdminRequest;
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
    private final RomRepository romRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;
    private final HinhAnhRepository hinhAnhRepository;
    private final ImeiReposiory imeiReposiory;
    private final SanPhamRepository sanPhamRepository;
    private final ModelMapper modelMapper;
    private final SanPhamChiTietAdminService sanPhamChiTietAdminService;

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
        Page<Object[]> allHienThi = sanPhamRepository.getAllHienThi( keyword ,idLoai ,trangThais ,pageable );

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
        response.setIdModelSanPham(sanPham.getIdModelSanPham().getIdModelSanPham());

        Set<SanPhamChiTietAdminDetailResponse> chiTietList = sanPham.getSanPhamChiTiets().stream().map(ct -> {
            SanPhamChiTietAdminDetailResponse dto = new SanPhamChiTietAdminDetailResponse();
            dto.setId(ct.getId());
            dto.setSoLuong(ct.getSoLuong());
            dto.setGiaBan(ct.getGiaBan());
            dto.setMaSanPhamChiTiet(ct.getMaSanPhamChiTiet());
            dto.setIdMau(ct.getIdMau() != null ? ct.getIdMau().getId() : null);
            dto.setIdRom(ct.getIdRom() != null ? ct.getIdRom().getId() : null);
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
        response.setMaSanPham(sanPham.getMaSanPham());
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

//        validateSanPhamRequest(sanPhamAdminRequest, false);

        // === Bước 1: Kiểm tra model tồn tại ===
        if (sanPhamAdminRequest.getIdModelSanPham() == null) {
            throw new BusinessException("Model sản phẩm không được để trống.");
        }

        ModelSanPham modelSanPham = modelSanPhamRepository.findById(sanPhamAdminRequest.getIdModelSanPham())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy model sản phẩm"));

        // === Bước 2: Tìm hoặc tạo mới SanPham tương ứng với Model ===
        List<SanPham> sanPhamList = sanPhamRepo.findAllByIdModelSanPham_IdModelSanPham(sanPhamAdminRequest.getIdModelSanPham());

        SanPham sanPham;

        if (!sanPhamList.isEmpty()) {
            sanPham = sanPhamList.get(0); // hoặc có thể lọc kỹ hơn nếu có nhiều sản phẩm
        } else {
            sanPham = new SanPham();
            sanPham.setTenSanPham(sanPhamAdminRequest.getTenSanPham());
            sanPham.setThuongHieu(sanPhamAdminRequest.getThuongHieu());
            sanPham.setTrangThaiSanPham(Optional.ofNullable(sanPhamAdminRequest.getTrangThaiSanPham())
                    .orElse(TrangThaiSanPham.ACTIVE));
            if (sanPhamAdminRequest.getIdNhaCungCap() != null) {
                NhaCungCap nhaCungCap = nhaCungCapRepository.findById(sanPhamAdminRequest.getIdNhaCungCap())
                        .orElseThrow(() -> new NotFoundException("Không tìm thấy nhà cung cấp"));
                sanPham.setIdNhaCungCap(nhaCungCap);
            }
            sanPham.setIdModelSanPham(modelSanPham);
            sanPham = sanPhamRepo.save(sanPham);
        }

        Set<SanPhamChiTiet> chiTietSet = new HashSet<>();

        // === Bước 3: Xử lý danh sách chi tiết sản phẩm ===
        if (sanPhamAdminRequest.getSanPhamChiTiets() != null && !sanPhamAdminRequest.getSanPhamChiTiets().isEmpty()) {
            for (SanPhamChiTietAdminRepuest rq : sanPhamAdminRequest.getSanPhamChiTiets()) {

                // Tạo chi tiết sản phẩm
                SanPhamChiTiet chiTiet = new SanPhamChiTiet();
                chiTiet.setMaSanPhamChiTiet(rq.getMaSanPhamChiTiet());
                chiTiet.setSoLuong(rq.getSoLuong());
                chiTiet.setGiaBan(rq.getGiaBan());
                chiTiet.setIdSanPham(sanPham);

                // Kiểm tra khóa ngoại
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

                // Lưu chi tiết sản phẩm
                SanPhamChiTiet chiTietSaved = sanPhamChiTietRepository.save(chiTiet);

                // Lưu hình ảnh (nếu có)
                if (rq.getHinhAnhs() != null && !rq.getHinhAnhs().isEmpty()) {
                    List<HinhAnh> hinhAnhs = rq.getHinhAnhs().stream()
                            .filter(ha -> ha.getUrl() != null)
                            .map(ha -> {
                                HinhAnh hinhAnh = new HinhAnh();
                                hinhAnh.setIdSanPhamChiTiet(chiTietSaved);
                                hinhAnh.setUrl(ha.getUrl());
                                hinhAnh.setImagePublicId(ha.getImagePublicId());
                                return hinhAnh;
                            }).toList();
                    hinhAnhRepository.saveAll(hinhAnhs);
                }

                // Lưu danh sách IMEI (nếu có)
                if (rq.getImeis() != null && !rq.getImeis().isEmpty()) {
                    List<Imei> imeis = rq.getImeis().stream()
                            .filter(imeiDto -> imeiDto.getSoImei() != null && !imeiDto.getSoImei().trim().isEmpty())
                            .map(imeiDto -> {
                                String soImei = imeiDto.getSoImei().trim();
                                if (imeiReposiory.existsBySoImei(soImei)) {
                                    throw new BusinessException("Số IMEI đã tồn tại: " + soImei);
                                }
                                Imei imei = new Imei();
                                imei.setSoImei(soImei);
                                imei.setTrangThaiImei(TrangThaiImei.AVAILABLE);
                                imei.setIdSanPhamChiTiet(chiTietSaved);
                                return imei;
                            }).toList();
                    imeiReposiory.saveAll(imeis);
                }

                chiTietSet.add(chiTietSaved);
            }
        }

        // === Bước 4: Gán chi tiết sản phẩm vào SanPham ===
        sanPham.getSanPhamChiTiets().addAll(chiTietSet);
        sanPham = sanPhamRepo.save(sanPham); // cập nhật lại nếu cần

        // === Bước 5: Tạo response ===
        SanPhamAdminResponse response = new SanPhamAdminResponse();
        response.setId(sanPham.getId());
        response.setMaSanPham(sanPham.getMaSanPham());
        response.setTenSanPham(sanPham.getTenSanPham());
        response.setTrangThaiSanPham(sanPham.getTrangThaiSanPham());
        response.setThuongHieu(sanPham.getThuongHieu());

        if (sanPham.getIdNhaCungCap() != null) {
            NhaCungCap nha = sanPham.getIdNhaCungCap();
            NhaCungCapAdminResponse nccRes = new NhaCungCapAdminResponse();
            nccRes.setTenNhaCungCap(nha.getTenNhaCungCap());
            nccRes.setSdt(nha.getSdt());
            nccRes.setEmail(nha.getEmail());
            nccRes.setDiaChi(nha.getDiaChi());
            response.setNhaCungCapAdminResponse(nccRes);
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


    @Transactional(rollbackFor = Exception.class)
    public SanPhamAdminResponse updateSanPhamAdmin(Integer id, SanPhamAdminRequest sanPhamAdminRequest) {

        validateSanPhamRequest(sanPhamAdminRequest, true);
        //  Kiểm tra trùng lặp biến thể theo tổ hợp (màu, rom)
//        Set<String> variantKeySet = new HashSet<>();
//        for (SanPhamChiTietAdminRepuest rq : sanPhamAdminRequest.getSanPhamChiTiets()) {
//            String variantKey = String.format("%d-%d", rq.getIdMau(), rq.getIdRom());
//            if (!variantKeySet.add(variantKey)) {
//                throw new BusinessException("Biến thể trùng lặp: " + variantKey);
//            }
//        }

        // Tìm sản phẩm theo ID
        SanPham sanPham = sanPhamRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm với ID: " + id));

        //  Cập nhật thông tin chung cho sản phẩm
        sanPham.setTenSanPham(sanPhamAdminRequest.getTenSanPham());
        sanPham.setThuongHieu(sanPhamAdminRequest.getThuongHieu());
        sanPham.setTrangThaiSanPham(Optional.ofNullable(sanPhamAdminRequest.getTrangThaiSanPham())
                .orElse(TrangThaiSanPham.ACTIVE));

        //  Cập nhật nhà cung cấp nếu có
        if (sanPhamAdminRequest.getIdNhaCungCap() != null) {
            NhaCungCap nhaCungCap = nhaCungCapRepository.findById(sanPhamAdminRequest.getIdNhaCungCap())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy nhà cung cấp với ID: " + sanPhamAdminRequest.getIdNhaCungCap()));
            sanPham.setIdNhaCungCap(nhaCungCap);
        } else {
            sanPham.setIdNhaCungCap(null); // có thể bỏ chọn nhà cung cấp
        }

        //  Tập hợp các chi tiết sẽ cập nhật
        Set<SanPhamChiTiet> updatedChiTietSet = new HashSet<>();

        //  Xử lý từng biến thể (chi tiết sản phẩm)
        if (sanPhamAdminRequest.getSanPhamChiTiets() != null && !sanPhamAdminRequest.getSanPhamChiTiets().isEmpty()) {
            for (SanPhamChiTietAdminRepuest rq : sanPhamAdminRequest.getSanPhamChiTiets()) {

                //  Nếu đã tồn tại → lấy ra để cập nhật
                SanPhamChiTiet chiTiet;
                if (rq.getId() != null ) {
                    chiTiet = sanPhamChiTietRepository.findById(rq.getId())
                            .orElseThrow(() -> new NotFoundException("Không tìm thấy chi tiết sản phẩm ID: " + rq.getId()));
                } else {
                    //  Nếu chưa có → tạo mới
                    chiTiet = new SanPhamChiTiet();
                    chiTiet.setIdSanPham(sanPham);
                }

                // Cập nhật các thông tin cơ bản
                chiTiet.setMaSanPhamChiTiet(rq.getMaSanPhamChiTiet());
                chiTiet.setGiaBan(rq.getGiaBan());

                //  Chi tiết này hiện đang lấy số lượng từ FE (sẽ sửa phía dưới)
                // chiTiet.setSoLuong(rq.getSoLuong());

                // Cập nhật màu sắc & ROM (nếu có)
                chiTiet.setIdMau(rq.getIdMau() != null
                        ? mauSacRepository.findById(rq.getIdMau()).orElseThrow(() -> new NotFoundException("Không tìm thấy màu"))
                        : null);

                chiTiet.setIdRom(rq.getIdRom() != null
                        ? romRepository.findById(rq.getIdRom()).orElseThrow(() -> new NotFoundException("Không tìm thấy ROM"))
                        : null);

                //  Lưu chi tiết sản phẩm (tạm thời)
                SanPhamChiTiet chiTietSaved = sanPhamChiTietRepository.save(chiTiet);

                //  Cập nhật hình ảnh
                if (rq.getHinhAnhs() != null && !rq.getHinhAnhs().isEmpty()) {
                    hinhAnhRepository.deleteByIdSanPhamChiTiet(chiTietSaved.getId()); // Xoá cũ

                    List<HinhAnh> newImages = rq.getHinhAnhs().stream()
                            .filter(ha -> ha.getUrl() != null && !ha.getUrl().trim().isEmpty())
                            .map(ha -> {
                                HinhAnh hinhAnh = new HinhAnh();
                                hinhAnh.setIdSanPhamChiTiet(chiTietSaved);
                                hinhAnh.setUrl(ha.getUrl());
                                hinhAnh.setImagePublicId(ha.getImagePublicId());
                                return hinhAnh;
                            }).toList();

                    if (newImages.isEmpty()) {
                        throw new BusinessException("Danh sách hình ảnh không hợp lệ");
                    }

                    hinhAnhRepository.saveAll(newImages);
                } else {
                    throw new BusinessException("Không có hình ảnh để cập nhật");
                }

                if (rq.getImeis() != null && !rq.getImeis().isEmpty()) {
                    List<String> imeisFromFE = rq.getImeis().stream()
                            .map(ImeiAdminRequest::getSoImei)
                            .filter(Objects::nonNull)
                            .map(String::trim)
                            .toList();

                    // ✅ XÓA TẤT CẢ IMEI CŨ CỦA BIẾN THỂ TRƯỚC KHI GHI LẠI
                    imeiReposiory.deleteByIdSanPhamChiTietId(chiTietSaved.getId());

                    Set<String> imeiDaTonTai = imeiReposiory.findAllBySoImeiIn(imeisFromFE).stream()
                            .map(Imei::getSoImei)
                            .collect(Collectors.toSet());

                    List<Imei> imeiMoi = rq.getImeis().stream()
                            .filter(dto -> dto.getSoImei() != null && !imeiDaTonTai.contains(dto.getSoImei()))
                            .map(dto -> {
                                Imei imei = new Imei();
                                imei.setSoImei(dto.getSoImei().trim());
                                imei.setTrangThaiImei(TrangThaiImei.AVAILABLE);
                                imei.setIdSanPhamChiTiet(chiTietSaved);
                                return imei;
                            })
                            .toList();

                    imeiReposiory.saveAll(imeiMoi);
                }


                //  Cập nhật số lượng thực tế dựa vào số IMEI hiện có
                int soLuongThucTe = imeiReposiory.countByIdSanPhamChiTiet(chiTietSaved);
                chiTietSaved.setSoLuong(soLuongThucTe);
                sanPhamChiTietRepository.save(chiTietSaved);

                updatedChiTietSet.add(chiTietSaved); // Thêm vào danh sách cập nhật
            }
        }

        //  Tìm các biến thể cũ không còn trong request → xoá
        Set<SanPhamChiTiet> chiTietCanXoa = sanPham.getSanPhamChiTiets().stream()
                .filter(oldChiTiet -> updatedChiTietSet.stream()
                        .noneMatch(newChiTiet -> newChiTiet.getId() != null && newChiTiet.getId().equals(oldChiTiet.getId())))
                .collect(Collectors.toSet());

        sanPham.getSanPhamChiTiets().removeAll(chiTietCanXoa);
        sanPhamChiTietRepository.deleteAll(chiTietCanXoa);

        // Gắn lại các biến thể mới vào sản phẩm
        for (SanPhamChiTiet chiTietMoi : updatedChiTietSet) {
            chiTietMoi.setIdSanPham(sanPham);
            boolean isNotExist = sanPham.getSanPhamChiTiets().stream()
                    .noneMatch(old -> old.getId().equals(chiTietMoi.getId()));
            if (isNotExist) {
                sanPham.getSanPhamChiTiets().add(chiTietMoi);
            }
        }

        //  Lưu sản phẩm sau khi cập nhật
        sanPham = sanPhamRepo.save(sanPham);

        SanPhamAdminResponse response = new SanPhamAdminResponse();
        response.setId(sanPham.getId());
        response.setMaSanPham(sanPham.getMaSanPham());
        response.setTenSanPham(sanPham.getTenSanPham());
        response.setThuongHieu(sanPham.getThuongHieu());

        if (sanPham.getIdNhaCungCap() != null) {
            NhaCungCapAdminResponse nhaCungCapAdminResponse = new NhaCungCapAdminResponse();
            nhaCungCapAdminResponse.setTenNhaCungCap(sanPham.getIdNhaCungCap().getTenNhaCungCap());
            nhaCungCapAdminResponse.setSdt(sanPham.getIdNhaCungCap().getSdt());
            nhaCungCapAdminResponse.setEmail(sanPham.getIdNhaCungCap().getEmail());
            nhaCungCapAdminResponse.setDiaChi(sanPham.getIdNhaCungCap().getDiaChi());
            response.setNhaCungCapAdminResponse(nhaCungCapAdminResponse);
        }

        //  Gán danh sách chi tiết sản phẩm
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
        sanPham.setTrangThaiSanPham(TrangThaiSanPham.TEMPORARILY_UNAVAILABLE);
        sanPhamRepo.save(sanPham);
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


}


