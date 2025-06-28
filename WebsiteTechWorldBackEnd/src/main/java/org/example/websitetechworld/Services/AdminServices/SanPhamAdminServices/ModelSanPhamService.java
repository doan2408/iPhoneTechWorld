package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.ModelSanPhamAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.*;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Repository.*;
import org.example.websitetechworld.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ModelSanPhamService {

    private final ModelSanPhamRepository modelSanPhamRepo;
    private final SanPhamRepository sanPhamRepo;
    private final MauSacRepository mauSacRepo;
    private final RamRepository ramRepo;
    private final RomRepository romRepo;
    private final ManHinhRepository manHinhRepo;
    private final HeDieuHanhRepository heDieuHanhRepo;
    private final PinRepository pinRepo;
    private final CpuRepository cpuRepo;
    private final CameraTruocRepository cameraTruocRepo;
    private final CameraSauRepository cameraSauRepo;
    private final XuatXuRepository xuatXuRepo;
    private final LoaiRepository loaiRepo;
    private final ModelSanPhamRepository modelSanPhamRepository;

    private void mapRequestToEntity(ModelSanPhamAdminRequest req, ModelSanPham entity) {

        entity.setTenModel(req.getTenModel());
        entity.setNamRaMat(req.getNamRaMat());
        entity.setMoTa(req.getMoTa());
        entity.setTrangThai(req.getTrangThai());

//        entity.setIdSanPham(sanPhamRepo.findById(req.getIdSanPham()).orElse(null));
        entity.setIdRam(ramRepo.findById(req.getIdRam()).orElse(null));
        entity.setIdManHinh(manHinhRepo.findById(req.getIdManHinh()).orElse(null));
        entity.setIdHeDieuHanh(heDieuHanhRepo.findById(req.getIdHeDieuHanh()).orElse(null));
        entity.setIdPin(pinRepo.findById(req.getIdPin()).orElse(null));
        entity.setIdCpu(cpuRepo.findById(req.getIdCpu()).orElse(null));
        entity.setIdCameraTruoc(cameraTruocRepo.findById(req.getIdCameraTruoc()).orElse(null));
        entity.setIdCameraSau(cameraSauRepo.findById(req.getIdCameraSau()).orElse(null));
        entity.setIdXuatXu(xuatXuRepo.findById(req.getIdXuatXu()).orElse(null));
        entity.setIdLoai(loaiRepo.findById(req.getIdLoai()).orElse(null));
    }

    private ModelSanPhamAdminResponse mapEntityToResponse(ModelSanPham entity) {
        ModelSanPhamAdminResponse response = new ModelSanPhamAdminResponse();

//        entity.setMa_model_san_pham(response.getMa_model_san_pham());
//        entity.setTen_model(response.getTen_model());
//        entity.setNam_ra_mat(response.getNam_ra_mat());
//        entity.setMo_ta(response.getMo_ta());
//        entity.setTrang_thai(response.getTrang_thai());


        if (entity != null) {
            response.setIdModelSanPham(entity.getIdModelSanPham());
            response.setMaModelSanPham(entity.getMaModelSanPham());
            response.setTenModel(entity.getTenModel());
            response.setNamRaMat(entity.getNamRaMat());
            response.setMoTa(entity.getMoTa());
            response.setTrangThai(entity.getTrangThai());
        }

//        SanPham sanPham = entity.getIdSanPham();
//        if (sanPham != null) {
//            response.setMaSanPham(sanPham.getMaSanPham());
//            response.setTenSanPham(sanPham.getTenSanPham());
//            response.setThuongHieu(sanPham.getThuongHieu());
//            response.setTrangThaiSanPham(sanPham.getTrangThaiSanPham());
//
//            NhaCungCap ncc = sanPham.getIdNhaCungCap();
//            if (ncc != null) {
//                response.setTenNhaCungCap(ncc.getTenNhaCungCap());
//                response.setDiaChi(ncc.getDiaChi());
//                response.setSdt(ncc.getSdt());
//                response.setEmail(ncc.getEmail());
//            }
//        }

        Ram ram = entity.getIdRam();
        if (ram != null) {
            response.setIdRam(ram.getId());
            response.setDungLuongRam(ram.getDungLuong());
            response.setLoaiRam(ram.getLoai());
            response.setTocDoDocGhiRam(ram.getTocDoDocGhi());
            response.setNhaSanXuatRam(ram.getNhaSanXuat());
            response.setNamRaMatRam(ram.getNamRaMat());
        }

        ManHinh manHinh = entity.getIdManHinh();
        if (manHinh != null) {
            response.setIdManHinh(manHinh.getId());
            response.setTenManHinh(manHinh.getTenManHinh());
            response.setKichThuoc(manHinh.getKichThuoc());
            response.setLoaiManHinh(manHinh.getLoaiManHinh());
            response.setDoPhanGiaiManHinh(manHinh.getDoPhanGiai());
            response.setTanSoQuet(manHinh.getTanSoQuet());
            response.setDoSang(manHinh.getDoSang());
            response.setChatLieuKinh(manHinh.getChatLieuKinh());
        }

        HeDieuHanh heDieuHanh = entity.getIdHeDieuHanh();
        if (heDieuHanh != null) {
            response.setIdHeDieuHanh(heDieuHanh.getId());
            response.setPhienBanHeDieuHanh(heDieuHanh.getPhienBan());
            response.setNhaPhatTrien(heDieuHanh.getNhaPhatTrien());
            response.setGiaoDienNguoiDung(heDieuHanh.getGiaoDienNguoiDung());

        }

        Pin pin = entity.getIdPin();
        if (pin != null) {
            response.setIdPin(pin.getId());
            response.setPhienBanPin(pin.getPhienBan());
            response.setCongSuatSac(pin.getCongSuatSac());
            response.setThoiGianSuDung(pin.getThoiGianSuDung());
            response.setSoLanSacToiDa(pin.getSoLanSacToiDa());
        }

        Cpu cpu = entity.getIdCpu();
        if (cpu != null) {
            response.setIdCpu(cpu.getId());
            response.setHangSanXuat(cpu.getHangSanXuat());
            response.setChipXuLy(cpu.getChipXuLy());
            response.setSoNhan(cpu.getSoNhan());
            response.setXungNhip(cpu.getXungNhip());
            response.setCongNgheSanXuat(cpu.getCongNgheSanXuat());
            response.setBoNhoDem(cpu.getBoNhoDem());
            response.setTieuThuDienNang(cpu.getTieuThuDienNang());
            response.setNamRaMatCpu(cpu.getNamRaMat());
        }

        // Xử lý Camera Trước
        CameraTruoc cameraTruoc = entity.getIdCameraTruoc();
        if (cameraTruoc != null) {
            response.setIdCameraTruoc(cameraTruoc.getId());
            response.setLoaiCameraTruoc(cameraTruoc.getLoaiCamera());
            response.setDoPhanGiaiCameraTruoc(cameraTruoc.getDoPhanGiai());
            response.setKhauDoCameraTruoc(cameraTruoc.getKhauDo());
            response.setLoaiZoomCameraTruoc(cameraTruoc.getLoaiZoom());
            response.setCheDoChupCameraTruoc(cameraTruoc.getCheDoChup());
        }

        CameraSau cameraSau = entity.getIdCameraSau();
        if (cameraSau != null) {
            response.setIdCameraSau(cameraSau.getId());
            response.setLoaiCameraSau(cameraSau.getLoaiCamera());
            response.setDoPhanGiaiCameraSau(cameraSau.getDoPhanGiai());
            response.setKhauDoCameraSau(cameraSau.getKhauDo());
            response.setLoaiZoomCameraSau(cameraSau.getLoaiZoom());
            response.setCheDoChupCameraSau(cameraSau.getCheDoChup());
        }

        XuatXu xuatXu = entity.getIdXuatXu();
        if (xuatXu != null) {
            response.setIdXuatXu(xuatXu.getId());
            response.setMaXuatXu(xuatXu.getMaXuatXu());
            response.setTenQuocGia(xuatXu.getTenQuocGia());

        }

        Loai loai = entity.getIdLoai();
        if (loai != null) {
            response.setIdLoai(loai.getId());
            response.setTenLoai(loai.getTenLoai());
        }

        return response;
    }


    private List<ModelSanPhamAdminResponse> mapEntityToResponseList(List<ModelSanPham> entity) {
        return entity.stream()
                .map(this::mapEntityToResponse)
                .toList();
    }



    public List<ModelSanPhamAdminResponse> getAllListModelSanPhamAdmin() {
        List<ModelSanPham> list = modelSanPhamRepo.findAll();

        return mapEntityToResponseList(list);
    }

    public Page<ModelSanPhamHienThiAdminResponse> getAllPageModelSanPhamAdmin(int page , int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Object[]> modelSanPhamsPage = modelSanPhamRepo.getAllPage(pageable);

        List<ModelSanPhamHienThiAdminResponse> modelSanPham = new ArrayList<>();
        for (Object[] object : modelSanPhamsPage) {
            ModelSanPhamHienThiAdminResponse msp = new ModelSanPhamHienThiAdminResponse();
            msp.setIdModelSanPham((Integer) object[0]);
            msp.setMaModelSanPham((String) object[1]);
            msp.setTenModel((String) object[2]);
            msp.setIdLoai((Integer) object[3]);
            msp.setIdRam((Integer) object[4]);
            msp.setIdXuatXu((Integer) object[5]);
            msp.setNamRaMat(((java.sql.Date) object[6]).toLocalDate());
            msp.setTrangThai((String) object[7]);
            modelSanPham.add(msp);
        }
        return new PageImpl<>(modelSanPham, pageable, modelSanPhamsPage.getTotalElements());
    }

    @Transactional
    public ModelSanPhamAdminResponse createModelSanPham(ModelSanPhamAdminRequest request) {
        ModelSanPham model = new ModelSanPham();
        mapRequestToEntity(request, model);
        ModelSanPham savedModel = modelSanPhamRepository.save(model);
        return mapEntityToResponse(savedModel);
    }

    @Transactional
    public ModelSanPhamAdminResponse updateModelSanPham(Integer id, ModelSanPhamAdminRequest request) {
        ModelSanPham model = modelSanPhamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Model sản phẩm không tồn tại"));
        mapRequestToEntity(request, model);
        ModelSanPham updatedModel = modelSanPhamRepository.save(model);
        return mapEntityToResponse(updatedModel);
    }

    @Transactional
    public void deleteModelSanPham(Integer id) {
        ModelSanPham model = modelSanPhamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Model sản phẩm không tồn tại"));
        modelSanPhamRepository.delete(model);
    }

    @Transactional
    public ModelSanPhamAdminResponse findByIdModelSanPham(Integer id) {
        ModelSanPham model = modelSanPhamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Model sản phẩm không tồn tại"));
        return mapEntityToResponse(model);
    }


}
