package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.ModelSanPhamAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.HinhAnhAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.ImeiAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.ModelSanPhamAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamChiTietResponse;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Repository.*;
import org.springframework.stereotype.Service;

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

    private void mapRequestToEntity(ModelSanPhamAdminRequest req, ModelSanPham entity) {

        entity.setMa_model_san_pham(req.getMa_model_san_pham());
        entity.setTen_model(req.getTen_model());
        entity.setNam_ra_mat(req.getNam_ra_mat());
        entity.setMo_ta(req.getMo_ta());
        entity.setTrang_thai(req.getTrang_thai());

        entity.setIdSanPham(sanPhamRepo.findById(req.getIdSanPham()).orElse(null));
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

        entity.setMa_model_san_pham(response.getMa_model_san_pham());
        entity.setTen_model(response.getTen_model());
        entity.setNam_ra_mat(response.getNam_ra_mat());
        entity.setMo_ta(response.getMo_ta());
        entity.setTrang_thai(response.getTrang_thai());


        if (entity != null) {
            response.setId_model_san_pham(entity.getId_model_san_pham());
            response.setMa_model_san_pham(entity.getMa_model_san_pham());
            response.setTen_model(entity.getTen_model());
            response.setNam_ra_mat(entity.getNam_ra_mat());
            response.setMo_ta(entity.getMo_ta());
            response.setTrang_thai(entity.getTrang_thai());
        }

        SanPham sanPham = entity.getIdSanPham();
        if (sanPham != null) {
            response.setMaSanPham(sanPham.getMaSanPham());
            response.setTenSanPham(sanPham.getTenSanPham());
            response.setThuongHieu(sanPham.getThuongHieu());

            NhaCungCap ncc = sanPham.getIdNhaCungCap();
            if (ncc != null) {
                response.setTenNhaCungCap(ncc.getTenNhaCungCap());
                response.setDiaChi(ncc.getDiaChi());
                response.setSdt(ncc.getSdt());
                response.setEmail(ncc.getEmail());
            }
        }

        Ram ram = entity.getIdRam();
        if (ram != null) {
            response.setDungLuongRam(ram.getDungLuong());
            response.setLoaiRam(ram.getLoai());
            response.setTocDoDocGhiRam(ram.getTocDoDocGhi());
            response.setNhaSanXuatRam(ram.getNhaSanXuat());
            response.setNamRaMatRam(ram.getNamRaMat());
        }

        ManHinh manHinh = entity.getIdManHinh();
        if (manHinh != null) {
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
            response.setPhienBanHeDieuHanh(heDieuHanh.getPhienBan());
            response.setNhaPhatTrien(heDieuHanh.getNhaPhatTrien());
            response.setGiaoDienNguoiDung(heDieuHanh.getGiaoDienNguoiDung());

        }

        Pin pin = entity.getIdPin();
        if (pin != null) {
            response.setPhienBanPin(pin.getPhienBan());
            response.setCongSuatSac(pin.getCongSuatSac());
            response.setThoiGianSuDung(pin.getThoiGianSuDung());
            response.setSoLanSacToiDa(pin.getSoLanSacToiDa());
        }

        Cpu cpu = entity.getIdCpu();
        if (cpu != null) {
            response.setHangSanXuat(cpu.getHangSanXuat());
            response.setChipXuLy(cpu.getChipXuLy());
            response.setSoNhan(cpu.getSoNhan());
            response.setXungNhip(cpu.getXungNhip());
            response.setCongNgheSanXuat(cpu.getCongNgheSanXuat());
            response.setBoNhoDem(cpu.getBoNhoDem());
            response.setTieuThuDienNang(cpu.getTieuThuDienNang());
            response.setNamRaMat(cpu.getNamRaMat());
        }

        // Xử lý Camera Trước
        CameraTruoc cameraTruoc = entity.getIdCameraTruoc();
        if (cameraTruoc != null) {
            response.setLoaiCameraTruoc(cameraTruoc.getLoaiCamera());
            response.setDoPhanGiaiCameraTruoc(cameraTruoc.getDoPhanGiai());
            response.setKhauDoCameraTruoc(cameraTruoc.getKhauDo());
            response.setLoaiZoomCameraTruoc(cameraTruoc.getLoaiZoom());
            response.setCheDoChupCameraTruoc(cameraTruoc.getCheDoChup());
        }

        CameraSau cameraSau = entity.getIdCameraSau();
        if (cameraSau != null) {
            response.setLoaiCameraSau(cameraSau.getLoaiCamera());
            response.setDoPhanGiaiCameraSau(cameraSau.getDoPhanGiai());
            response.setKhauDoCameraSau(cameraSau.getKhauDo());
            response.setLoaiZoomCameraSau(cameraSau.getLoaiZoom());
            response.setCheDoChupCameraSau(cameraSau.getCheDoChup());
        }

        XuatXu xuatXu = entity.getIdXuatXu();
        if (xuatXu != null) {
            response.setMaXuatXu(xuatXu.getMaXuatXu());
            response.setTenQuocGia(xuatXu.getTenQuocGia());

        }

        Loai loai = entity.getIdLoai();
        if (loai != null) {
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


}
