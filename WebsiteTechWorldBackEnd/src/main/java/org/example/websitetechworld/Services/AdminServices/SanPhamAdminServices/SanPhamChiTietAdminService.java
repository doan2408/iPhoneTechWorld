package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.SanPhamChiTietAdminRepuest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.BaoHanhAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.ImeiAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamChiTietResponse;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Repository.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SanPhamChiTietAdminService {

    private final SanPhamChiTietRepository sanPhamChiTietRepo;
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


    private final ModelMapper modelMapper;


    public SanPhamChiTiet getChiTietById(Integer id) {
        return sanPhamChiTietRepo.findFullById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết sản phẩm với ID: " + id));
    }


    //detail
    public SanPhamChiTietResponse getSanPhamChiTiet(SanPhamChiTiet entity) {

        SanPhamChiTietResponse response = new SanPhamChiTietResponse();

        if (entity != null) {
            response.setId(entity.getId());
            response.setMaSanPhamChiTiet(entity.getMaSanPhamChiTiet());
            response.setSoLuongSPCT(entity.getSoLuong());
            response.setGiaBan(entity.getGiaBan());
        }

        SanPham sanPham = entity.getIdSanPham();
        if (sanPham != null) {
            response.setMaSanPham(sanPham.getMaSanPham());
            response.setTenSanPham(sanPham.getTenSanPham());
            response.setThuongHieu(sanPham.getThuongHieu());
            response.setSoLuongTonKho(sanPham.getSoLuongTonKho());

            NhaCungCap ncc = sanPham.getIdNhaCungCap();
            if (ncc != null) {
                response.setTenNhaCungCap(ncc.getTenNhaCungCap());
                response.setDiaChi(ncc.getDiaChi());
                response.setSdt(ncc.getSdt());
                response.setEmail(ncc.getEmail());
            }
        }

        MauSac mauSac = entity.getIdMau();
        response.setTenMau(mauSac.getTenMau());

        Ram ram = entity.getIdRam();
        if (ram != null) {
            response.setDungLuongRam(ram.getDungLuong());
            response.setLoaiRam(ram.getLoai());
            response.setTocDoDocGhiRam(ram.getTocDoDocGhi());
            response.setNhaSanXuatRam(ram.getNhaSanXuat());
            response.setNamRaMatRam(ram.getNamRaMat());
        }

        Rom rom = entity.getIdRom();
        if (rom != null) {
            response.setDungLuongRom(rom.getDungLuong());
            response.setLoaiRom(rom.getLoai());
            response.setTocDoDocGhiRom(rom.getTocDoDocGhi());
            response.setNhaSanXuatRom(rom.getNhaSanXuat());
            response.setNamRaMatRom(rom.getNamRaMat());
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
            response.setSoNhan(cpu.getSoNhan());
            response.setSoLuongCpu(cpu.getSoLuong());
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
            response.setXuatXu(xuatXu.getMaXuatXu());
            response.setTenQuocGia(xuatXu.getTenQuocGia());

        }

        Loai loai = entity.getIdLoai();
        if (loai != null) {
            response.setTenLoai(loai.getTenLoai());
        }

        if (entity.getImeis() != null && !entity.getImeis().isEmpty()) {
            Imei firstImei = entity.getImeis().iterator().next();
            response.setImei(firstImei.getSoImei()); // sửa theo tên field thực tế
        }

        if (entity.getHinhAnhs() != null && !entity.getHinhAnhs().isEmpty()) {
            HinhAnh firstImage = entity.getHinhAnhs().iterator().next();
            response.setUrl(firstImage.getUrl()); // sửa theo tên field thực tế
        }

        return response;
    }


    @Transactional
    public SanPhamChiTietResponse createSanPhamChiTiet(SanPhamChiTietAdminRepuest req) {
        SanPhamChiTiet spct = new SanPhamChiTiet();

        spct.setMaSanPhamChiTiet(req.getMaSanPhamChiTiet());
        spct.setSoLuong(req.getSoLuong());
        spct.setGiaBan(req.getGiaBan());

        spct.setIdSanPham(sanPhamRepo.findById(req.getIdSanPham()).orElse(null));
        spct.setIdMau(mauSacRepo.findById(req.getIdMau()).orElse(null));
        spct.setIdRam(ramRepo.findById(req.getIdRam()).orElse(null));
        spct.setIdRom(romRepo.findById(req.getIdRom()).orElse(null));
        spct.setIdManHinh(manHinhRepo.findById(req.getIdManHinh()).orElse(null));
        spct.setIdHeDieuHanh(heDieuHanhRepo.findById(req.getIdHeDieuHanh()).orElse(null));
        spct.setIdPin(pinRepo.findById(req.getIdPin()).orElse(null));
        spct.setIdCpu(cpuRepo.findById(req.getIdCpu()).orElse(null));
        spct.setIdCameraTruoc(cameraTruocRepo.findById(req.getIdCameraTruoc()).orElse(null));
        spct.setIdCameraSau(cameraSauRepo.findById(req.getIdCameraSau()).orElse(null));
        spct.setIdXuatXu(xuatXuRepo.findById(req.getIdXuatXu()).orElse(null));
        spct.setIdLoai(loaiRepo.findById(req.getIdLoai()).orElse(null));


        SanPhamChiTiet save = sanPhamChiTietRepo.save(spct);

        SanPhamChiTietResponse response = new SanPhamChiTietResponse();

        response.setId(save.getId());
        response.setMaSanPhamChiTiet(save.getMaSanPhamChiTiet());
        response.setSoLuongSPCT(save.getSoLuong());
        response.setGiaBan(save.getGiaBan());

        SanPham sanPham = save.getIdSanPham();
        if (sanPham != null) {
            response.setMaSanPham(sanPham.getMaSanPham());
            response.setTenSanPham(sanPham.getTenSanPham());
            response.setThuongHieu(sanPham.getThuongHieu());
            response.setSoLuongTonKho(sanPham.getSoLuongTonKho());

            NhaCungCap ncc = sanPham.getIdNhaCungCap();
            if (ncc != null) {
                response.setTenNhaCungCap(ncc.getTenNhaCungCap());
                response.setDiaChi(ncc.getDiaChi());
                response.setSdt(ncc.getSdt());
                response.setEmail(ncc.getEmail());
            }
        }

        MauSac mauSac = save.getIdMau();
        response.setTenMau(mauSac.getTenMau());

        Ram ram = save.getIdRam();
        if (ram != null) {
            response.setDungLuongRam(ram.getDungLuong());
            response.setLoaiRam(ram.getLoai());
            response.setTocDoDocGhiRam(ram.getTocDoDocGhi());
            response.setNhaSanXuatRam(ram.getNhaSanXuat());
            response.setNamRaMatRam(ram.getNamRaMat());
        }

        Rom rom = save.getIdRom();
        if (rom != null) {
            response.setDungLuongRom(rom.getDungLuong());
            response.setLoaiRom(rom.getLoai());
            response.setTocDoDocGhiRom(rom.getTocDoDocGhi());
            response.setNhaSanXuatRom(rom.getNhaSanXuat());
            response.setNamRaMatRom(rom.getNamRaMat());
        }

        ManHinh manHinh = save.getIdManHinh();
        if (manHinh != null) {
            response.setTenManHinh(manHinh.getTenManHinh());
            response.setKichThuoc(manHinh.getKichThuoc());
            response.setLoaiManHinh(manHinh.getLoaiManHinh());
            response.setDoPhanGiaiManHinh(manHinh.getDoPhanGiai());
            response.setTanSoQuet(manHinh.getTanSoQuet());
            response.setDoSang(manHinh.getDoSang());
            response.setChatLieuKinh(manHinh.getChatLieuKinh());
        }

        HeDieuHanh heDieuHanh = save.getIdHeDieuHanh();
        if (heDieuHanh != null) {
            response.setPhienBanHeDieuHanh(heDieuHanh.getPhienBan());
            response.setNhaPhatTrien(heDieuHanh.getNhaPhatTrien());
            response.setGiaoDienNguoiDung(heDieuHanh.getGiaoDienNguoiDung());

        }

        Pin pin = save.getIdPin();
        if (pin != null) {
            response.setPhienBanPin(pin.getPhienBan());
            response.setCongSuatSac(pin.getCongSuatSac());
            response.setThoiGianSuDung(pin.getThoiGianSuDung());
            response.setSoLanSacToiDa(pin.getSoLanSacToiDa());
        }

        Cpu cpu = save.getIdCpu();
        if (cpu != null) {
            response.setHangSanXuat(cpu.getHangSanXuat());
            response.setSoNhan(cpu.getSoNhan());
            response.setSoLuongCpu(cpu.getSoLuong());
            response.setXungNhip(cpu.getXungNhip());
            response.setCongNgheSanXuat(cpu.getCongNgheSanXuat());
            response.setBoNhoDem(cpu.getBoNhoDem());
            response.setTieuThuDienNang(cpu.getTieuThuDienNang());
            response.setNamRaMat(cpu.getNamRaMat());
        }

        // Xử lý Camera Trước
        CameraTruoc cameraTruoc = save.getIdCameraTruoc();
        if (cameraTruoc != null) {
            response.setLoaiCameraTruoc(cameraTruoc.getLoaiCamera());
            response.setDoPhanGiaiCameraTruoc(cameraTruoc.getDoPhanGiai());
            response.setKhauDoCameraTruoc(cameraTruoc.getKhauDo());
            response.setLoaiZoomCameraTruoc(cameraTruoc.getLoaiZoom());
            response.setCheDoChupCameraTruoc(cameraTruoc.getCheDoChup());
        }

        CameraSau cameraSau = save.getIdCameraSau();
        if (cameraSau != null) {
            response.setLoaiCameraSau(cameraSau.getLoaiCamera());
            response.setDoPhanGiaiCameraSau(cameraSau.getDoPhanGiai());
            response.setKhauDoCameraSau(cameraSau.getKhauDo());
            response.setLoaiZoomCameraSau(cameraSau.getLoaiZoom());
            response.setCheDoChupCameraSau(cameraSau.getCheDoChup());
        }

        XuatXu xuatXu = save.getIdXuatXu();
        if (xuatXu != null) {
            response.setXuatXu(xuatXu.getMaXuatXu());
            response.setTenQuocGia(xuatXu.getTenQuocGia());

        }

        Loai loai = save.getIdLoai();
        if (loai != null) {
            response.setTenLoai(loai.getTenLoai());
        }


        if (spct.getImeis() != null && !spct.getImeis().isEmpty()) {
            Imei firstImei = spct.getImeis().iterator().next();
            response.setImei(firstImei.getSoImei()); // sửa theo tên field thực tế
        }

        if (spct.getHinhAnhs() != null && !spct.getHinhAnhs().isEmpty()) {
            HinhAnh firstImage = spct.getHinhAnhs().iterator().next();
            response.setUrl(firstImage.getUrl()); // sửa theo tên field thực tế
        }

        return response;
    }
}
