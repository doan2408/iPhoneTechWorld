package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.ModelSanPhamAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.*;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Enum.SanPham.TrangThaiSanPham;
import org.example.websitetechworld.Enum.SanPham.TrangThaiSanPhamModel;
import org.example.websitetechworld.Repository.*;
import org.example.websitetechworld.exception.NotFoundException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    @PersistenceContext
    private EntityManager entityManager;

    private void mapRequestToEntity(ModelSanPhamAdminRequest req, ModelSanPham entity) {

        entity.setTenModel(req.getTenModel());
        entity.setMaModelSanPham(req.getModelSanPham());
        entity.setNamRaMat(req.getNamRaMat());
        entity.setTrangThaiSanPhamModel(req.getTrangThaiSanPhamModel());
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

        if (entity != null) {
            response.setIdModelSanPham(entity.getIdModelSanPham());
            response.setMaModelSanPham(entity.getMaModelSanPham());
            response.setTenModel(entity.getTenModel());
            response.setNamRaMat(entity.getNamRaMat());
            response.setTrangThaiSanPhamModel(entity.getTrangThaiSanPhamModel());
        }

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
            String trangThaiModel = (String) object[7];
            TrangThaiSanPhamModel setTrangThai = TrangThaiSanPhamModel.valueOf(trangThaiModel);
            msp.setTrangThaiSanPhamModel(setTrangThai);
            modelSanPham.add(msp);
        }
        return new PageImpl<>(modelSanPham, pageable, modelSanPhamsPage.getTotalElements());
    }

    @Transactional
    public ModelSanPhamAdminResponse createModelSanPham(ModelSanPhamAdminRequest request) {
        System.out.println("Request nhận vào: " + request); // Yêu cầu ModelSanPhamAdminRequest phải override toString()

        ModelSanPham model = new ModelSanPham();
        mapRequestToEntity(request, model);
        ModelSanPham savedModel = modelSanPhamRepository.save(model);

        entityManager.flush();
        entityManager.refresh(savedModel);

        ModelSanPhamAdminResponse response = mapEntityToResponse(savedModel);
        System.out.println("Đối tượng sau khi lưu: " + savedModel);
        System.out.println("Response trả về: " + response);
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
        model.setTrangThaiSanPhamModel(TrangThaiSanPhamModel.DELETED);
        modelSanPhamRepository.save(model);
    }

    @Transactional
    public ModelSanPhamAdminResponse findByIdModelSanPham(Integer id) {
        ModelSanPham model = modelSanPhamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Model sản phẩm không tồn tại"));
        return mapEntityToResponse(model);
    }

    public Page<ModelSanPhamAdminResponse> searchModelSanPham(int page, int size, String search, Integer idLoai, Integer idRam, Integer idXuatXu) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ModelSanPham> modelPage = modelSanPhamRepository.findByFiltersNative(search, idLoai, idRam, idXuatXu, pageable);
        return modelPage.map(this::mapEntityToResponse);
    }

}
