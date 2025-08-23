package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.ModelSanPhamAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.*;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Enum.SanPham.TrangThaiSanPhamModel;
import org.example.websitetechworld.Repository.*;
import org.example.websitetechworld.exception.BusinessException;
import org.example.websitetechworld.exception.NotFoundException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModelSanPhamService {

    private final ModelSanPhamRepository modelSanPhamRepo;
    private final RamRepository ramRepo;
    private final ManHinhRepository manHinhRepo;
    private final HeDieuHanhRepository heDieuHanhRepo;
    private final PinRepository pinRepo;
    private final CpuRepository cpuRepo;
    private final CameraTruocRepository cameraTruocRepo;
    private final CameraSauRepository cameraSauRepo;
    private final XuatXuRepository xuatXuRepo;
    private final LoaiRepository loaiRepo;
    private final ModelSanPhamRepository modelSanPhamRepository;
    private final ModelCameraSauRepository modelCameraSauRepository;
    private final ImeiDaBanRepository imeiDaBanRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public static String formatTenModel(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        input = input.trim();
        // Chèn khoảng cách giữa 'iphone' và số, ví dụ "iphone14" → "iphone 14"
        input = input.replaceAll("(?i)(iphone)(\\d)", "$1 $2");

        // Chèn khoảng cách giữa số và chữ (ví dụ: "13Mini" → "13 Mini")
        input = input.replaceAll("(\\d)([A-Za-z])", "$1 $2");

        String[] word = input.split("\\s+");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < word.length; i++) {
            if (i == 0 && word[i].equalsIgnoreCase("iphone")) {
                sb.append("iPhone");
            } else {
                sb.append(word[i]);
            }

            if (i < word.length - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private void mapRequestToEntity(ModelSanPhamAdminRequest req, ModelSanPham entity) {
        String tenModel = req.getTenModel();
        String tenFormat = formatTenModel(tenModel);

        entity.setTenModel(tenFormat);

        entity.setMaModelSanPham(req.getMaModelSanPham());
        entity.setNamRaMat(req.getNamRaMat());
        entity.setTrangThaiSanPhamModel(req.getTrangThaiSanPhamModel());
        entity.setIdRam(ramRepo.findById(req.getIdRam()).orElse(null));
        entity.setIdManHinh(manHinhRepo.findById(req.getIdManHinh()).orElse(null));
        entity.setIdHeDieuHanh(heDieuHanhRepo.findById(req.getIdHeDieuHanh()).orElse(null));
        entity.setIdPin(pinRepo.findById(req.getIdPin()).orElse(null));
        entity.setIdCpu(cpuRepo.findById(req.getIdCpu()).orElse(null));
        entity.setIdCameraTruoc(cameraTruocRepo.findById(req.getIdCameraTruoc()).orElse(null));
//        entity.setIdCameraSau(cameraSauRepo.findById(req.getIdCameraSau()).orElse(null));
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

        List<CameraSauAdminResponse> camList = Optional.ofNullable(entity.getCameraSaus())
                .orElse(Collections.emptyList())
                .stream()
                .map(modelCam -> {
                    CameraSauAdminResponse dto = new CameraSauAdminResponse();
                    CameraSau cam = modelCam.getCameraSau();
                    dto.setId(cam.getId());
                    dto.setLoaiCamera(cam.getLoaiCamera());
                    dto.setDoPhanGiai(cam.getDoPhanGiai());
                    dto.setKhauDo(cam.getKhauDo());
                    dto.setLoaiZoom(cam.getLoaiZoom());
                    dto.setCheDoChup(cam.getCheDoChup());
                    dto.setIsChinh(modelCam.getIsChinh());
                    return dto;
                }).collect(Collectors.toList());

        response.setCameraSaus(camList);

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

    public Page<ModelSanPhamHienThiAdminResponse> getAllPageModelSanPhamAdmin(int page, int size) {
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

        String tenFormat = formatTenModel(request.getTenModel());

        //  1. Kiểm tra trùng cấu hình (trừ camera sau)
        if (modelSanPhamRepository.existsModelWithSameConfig(
                tenFormat,
//                request.getIdRam(),
                request.getIdXuatXu(),
                request.getIdLoai()
        )) {
            throw new BusinessException("model.duplicate.config");
        }

        //  2. Tạo entity ModelSanPham
        ModelSanPham model = new ModelSanPham();
        mapRequestToEntity(request, model);

        //  3. Xử lý danh sách camera sau
        List<ModelCameraSau> cameraSaus = new ArrayList<>();
        for (ModelSanPhamAdminRequest.CameraSauRequest cam : request.getCameraSaus()) {
            CameraSau entityCam = cameraSauRepo.findById(cam.getId())
                    .orElseThrow(() -> new BusinessException("camera_sau.not_found"));

            ModelCameraSau mcs = new ModelCameraSau();
            mcs.setModel(model);
            mcs.setCameraSau(entityCam);
            mcs.setIsChinh(cam.getIsChinh());

            ModelCameraSauKey key = new ModelCameraSauKey();
            // modelId sẽ tạm null, sẽ cập nhật sau khi model được lưu
            key.setCameraSauId(cam.getId());
            mcs.setId(key);

            cameraSaus.add(mcs);
        }

        //  4. Gắn vào model
        model.setCameraSaus(cameraSaus);

        //  5. Lưu model (sẽ cascade luôn modelCameraSau nếu bạn dùng Cascade.ALL)
        ModelSanPham savedModel = modelSanPhamRepository.save(model);

        //  6. Đồng bộ để đảm bảo lấy đúng ID (vì ID được tự động sinh)
        entityManager.flush();
        entityManager.refresh(savedModel);

        //  7. Gán lại modelId cho từng ModelCameraSauKey (vì giờ đã có id thật)
        for (ModelCameraSau mcs : savedModel.getCameraSaus()) {
            mcs.getId().setModelId(savedModel.getIdModelSanPham());
        }

        //  8. Lưu lại cameraSaus nếu cần (để cập nhật key đúng)
        modelCameraSauRepository.saveAll(savedModel.getCameraSaus());

        //  9. Trả về response
        return mapEntityToResponse(savedModel);
    }


    @Transactional
    public ModelSanPhamAdminResponse updateModelSanPham(Integer id, ModelSanPhamAdminRequest request) {
        ModelSanPham model = modelSanPhamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Model sản phẩm không tồn tại"));

        String tenFormat = formatTenModel(request.getTenModel());

        // check changed of tenModel, idXuatXu, idLoai
        boolean isChanged = !Objects.equals(tenFormat, model.getTenModel().trim()) ||
                !Objects.equals(request.getIdXuatXu(), model.getIdXuatXu().getId()) ||
                !Objects.equals(request.getIdLoai(), model.getIdLoai().getId());

        if (isChanged) {
            if (modelSanPhamRepository.existsModelWithSameConfig(
                    tenFormat,
                    request.getIdXuatXu(),
                    request.getIdLoai()
            )) {
                throw new BusinessException("model.duplicate.config");
            }
        }

        // 1. Gán dữ liệu cơ bản
        mapRequestToEntity(request, model);

        // 2. Xóa danh sách camera sau cũ
        model.getCameraSaus().clear();

        // 3. Tạo danh sách camera sau mới
        for (ModelSanPhamAdminRequest.CameraSauRequest cam : request.getCameraSaus()) {
            CameraSau entityCam = cameraSauRepo.findById(cam.getId())
                    .orElseThrow(() -> new BusinessException("camera_sau.not_found"));

            ModelCameraSau mcs = new ModelCameraSau();
            mcs.setModel(model);
            mcs.setCameraSau(entityCam);
            mcs.setIsChinh(cam.getIsChinh());

            // Composite key
            ModelCameraSauKey key = new ModelCameraSauKey();
            key.setModelId(model.getIdModelSanPham()); // Đã có vì đây là update
            key.setCameraSauId(cam.getId());
            mcs.setId(key);

            model.getCameraSaus().add(mcs); //  Thêm từng phần tử vào list cũ
        }

        // 4. Lưu lại
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


    public List<ModelSanPhamAdminResponse> importExcelFileUpload(MultipartFile file) throws IOException {
        List<ModelSanPham> sanPhams = new ArrayList<>();

        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Bỏ qua tiêu đề
                }

                ModelSanPham sanPham = new ModelSanPham();

                // Cột 1: Tên model
                String tenModel = getCellValue(row.getCell(0));
                if (tenModel == null || tenModel.trim().isEmpty()) {
                    throw new IllegalArgumentException("Tên model không được để trống tại dòng: " + (row.getRowNum() + 1));
                }
                String tenFormat = formatTenModel(tenModel);
                sanPham.setTenModel(tenFormat);

                // Cột 2: Năm ra mắt
                String namRaMatStr = getCellValue(row.getCell(1));
                if (namRaMatStr == null || namRaMatStr.trim().isEmpty()) {
                    throw new IllegalArgumentException("Năm ra mắt không được để trống tại dòng: " + (row.getRowNum() + 1));
                }
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[yyyy/MM/dd][d/M/yyyy]");
                    sanPham.setNamRaMat(LocalDate.parse(namRaMatStr, formatter));
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Năm ra mắt không hợp lệ tại dòng: " + (row.getRowNum() + 1) +
                            ". Định dạng yêu cầu: yyyy/MM/dd hoặc d/M/yyyy");
                }

                // Cột 3: Trạng thái sản phẩm
                String trangThai = getCellValue(row.getCell(2));
                if (trangThai == null || trangThai.trim().isEmpty()) {
                    throw new IllegalArgumentException("Trạng thái sản phẩm không được để trống tại dòng: " + (row.getRowNum() + 1));
                }
                try {
                    sanPham.setTrangThaiSanPhamModel(TrangThaiSanPhamModel.valueOf(trangThai));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Trạng thái sản phẩm không hợp lệ tại dòng: " + (row.getRowNum() + 1));
                }

                // Cột 4: RAM
                String ramDungLuong = getCellValue(row.getCell(3));
                String loaiRam = getCellValue(row.getCell(4));
                if (ramDungLuong == null || ramDungLuong.trim().isEmpty()) {
                    throw new IllegalArgumentException(
                            "Dữ liệu dung lượng RAM bị trống ở dòng " + (row.getRowNum() + 1) +
                                    ". Vui lòng nhập dung lượng RAM."
                    );
                }
                Ram ram = ramRepo.findByDungLuongAndLoai(ramDungLuong, loaiRam)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Không tìm thấy RAM với dung lượng '" + ramDungLuong +
                                        "' và loại '" + (loaiRam != null ? loaiRam : "") +
                                        "' ở dòng " + (row.getRowNum() + 1) +
                                        ". Vui lòng kiểm tra dữ liệu RAM hoặc thêm RAM vào hệ thống."
                        ));
                sanPham.setIdRam(ram);


                // Cột 5: Màn hình
                String manHinhKichThuoc = getCellValue(row.getCell(5));
                String tenManHinh = getCellValue(row.getCell(6));
                String loaiManHinh = getCellValue(row.getCell(7));

                if (manHinhKichThuoc == null || manHinhKichThuoc.trim().isEmpty()) {
                    throw new IllegalArgumentException(
                            "Dữ liệu kích thước màn hình bị trống ở dòng " + (row.getRowNum() + 1) +
                                    ". Vui lòng nhập kích thước màn hình."
                    );
                }

                if (tenManHinh == null || tenManHinh.trim().isEmpty()) {
                    throw new IllegalArgumentException(
                            "Dữ liệu tên màn hình bị trống ở dòng " + (row.getRowNum() + 1) +
                                    ". Vui lòng nhập tên màn hình."
                    );
                }

                if (loaiManHinh == null || loaiManHinh.trim().isEmpty()) {
                    throw new IllegalArgumentException(
                            "Dữ liệu loại màn hình bị trống ở dòng " + (row.getRowNum() + 1) +
                                    ". Vui lòng nhập loại màn hình."
                    );
                }

                ManHinh manHinh = manHinhRepo
                        .findByKichThuocAndTenManHinhAndLoaiManHinh(manHinhKichThuoc, tenManHinh, loaiManHinh)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Không tìm thấy màn hình với kích thước '" + manHinhKichThuoc +
                                        "', tên '" + tenManHinh +
                                        "', loại '" + loaiManHinh +
                                        "' ở dòng " + (row.getRowNum() + 1) +
                                        ". Vui lòng kiểm tra dữ liệu màn hình hoặc thêm màn hình vào hệ thống."
                        ));

                sanPham.setIdManHinh(manHinh);


                // Cột 6: Hệ điều hành
                String hdhPhienBan = getCellValue(row.getCell(8));
                if (hdhPhienBan == null || hdhPhienBan.trim().isEmpty()) {
                    throw new IllegalArgumentException("Hệ điều hành phiên bản không được để trống tại dòng: " + (row.getRowNum() + 1));
                }
                HeDieuHanh heDieuHanh = heDieuHanhRepo.findByPhienBan(hdhPhienBan)
                        .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy HĐH phiên bản: " + hdhPhienBan + " tại dòng: " + (row.getRowNum() + 1)));
                sanPham.setIdHeDieuHanh(heDieuHanh);

                // Cột 7: Pin
                String pinPhienBan = getCellValue(row.getCell(9));
                if (pinPhienBan == null || pinPhienBan.trim().isEmpty()) {
                    throw new IllegalArgumentException("Pin phiên bản không được để trống tại dòng: " + (row.getRowNum() + 1));
                }
                Pin pin = pinRepo.findByPhienBan(pinPhienBan)
                        .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy pin phiên bản: " + pinPhienBan + " tại dòng: " + (row.getRowNum() + 1)));
                sanPham.setIdPin(pin);

                // Cột 8: CPU
                String cpuChipXuLy = getCellValue(row.getCell(10));
                if (cpuChipXuLy == null || cpuChipXuLy.trim().isEmpty()) {
                    throw new IllegalArgumentException("CPU chip xử lý không được để trống tại dòng: " + (row.getRowNum() + 1));
                }
                Cpu cpu = cpuRepo.findByChipXuLy(cpuChipXuLy)
                        .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy CPU chip xử lý: " + cpuChipXuLy + " tại dòng: " + (row.getRowNum() + 1)));
                sanPham.setIdCpu(cpu);

                // Cột 9: Camera trước
                String cameraTruocLoai = getCellValue(row.getCell(11));
                String khauDoCamTruoc = getCellValue(row.getCell(12));
                String doPhanGiaiCamTruoc = getCellValue(row.getCell(13));

                if (cameraTruocLoai == null || cameraTruocLoai.trim().isEmpty()) {
                    throw new IllegalArgumentException(
                            "Dữ liệu loại camera trước bị trống ở dòng " + (row.getRowNum() + 1) +
                                    ". Vui lòng nhập loại camera trước."
                    );
                }

                if (khauDoCamTruoc == null || khauDoCamTruoc.trim().isEmpty()) {
                    throw new IllegalArgumentException(
                            "Dữ liệu khẩu độ camera trước bị trống ở dòng " + (row.getRowNum() + 1) +
                                    ". Vui lòng nhập khẩu độ camera trước."
                    );
                }

                if (doPhanGiaiCamTruoc == null || doPhanGiaiCamTruoc.trim().isEmpty()) {
                    throw new IllegalArgumentException(
                            "Dữ liệu độ phân giải camera trước bị trống ở dòng " + (row.getRowNum() + 1) +
                                    ". Vui lòng nhập độ phân giải camera trước."
                    );
                }

                CameraTruoc cameraTruoc = cameraTruocRepo
                        .findByLoaiCameraAndKhauDoAndDoPhanGiai(cameraTruocLoai, khauDoCamTruoc, doPhanGiaiCamTruoc)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Không tìm thấy camera trước với loại '" + cameraTruocLoai +
                                        "', khẩu độ '" + khauDoCamTruoc +
                                        "', độ phân giải '" + doPhanGiaiCamTruoc +
                                        "' ở dòng " + (row.getRowNum() + 1) +
                                        ". Vui lòng kiểm tra dữ liệu hoặc thêm camera trước vào hệ thống."
                        ));

                sanPham.setIdCameraTruoc(cameraTruoc);


                // Cột 10: Xuất xứ
                String xuatXuMa = getCellValue(row.getCell(14));
                if (xuatXuMa == null || xuatXuMa.trim().isEmpty()) {
                    throw new IllegalArgumentException("Xuất xứ mã không được để trống tại dòng: " + (row.getRowNum() + 1));
                }
                XuatXu xuatXu = xuatXuRepo.findByMaXuatXu(xuatXuMa)
                        .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy xuất xứ mã 11 : " + xuatXuMa + " tại dòng: " + (row.getRowNum() + 1)));
                sanPham.setIdXuatXu(xuatXu);

                // Cột 11: Loại
                String loaiTen = getCellValue(row.getCell(15));
                if (loaiTen == null || loaiTen.trim().isEmpty()) {
                    throw new IllegalArgumentException("Loại tên không được để trống tại dòng: " + (row.getRowNum() + 1));
                }
                Loai loai = loaiRepo.findByTenLoai(loaiTen)
                        .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy loại tên: " + loaiTen + " tại dòng: " + (row.getRowNum() + 1)));
                sanPham.setIdLoai(loai);

                // Kiểm tra trùng lặp
                if (modelSanPhamRepository.existsModelWithSameConfig(tenFormat, xuatXu.getId(), loai.getId())) {
                    throw new BusinessException("model.duplicate.config tại dòng: " + (row.getRowNum() + 1));
                }

                // Cột 12: Camera sau
                List<ModelCameraSau> cameraSaus = new ArrayList<>();

                String cameraLoaiStr = getCellValue(row.getCell(16));
                String cameraDoPhanGiaiStr = getCellValue(row.getCell(17));
                String cameraKhauDoStr = getCellValue(row.getCell(18));

                if (cameraLoaiStr != null && !cameraLoaiStr.trim().isEmpty()) {
                    List<String> cameraLoaiList = Arrays.asList(cameraLoaiStr.split("\\s*,\\s*"));
                    List<String> cameraDoPhanGiaiList = cameraDoPhanGiaiStr != null
                            ? Arrays.asList(cameraDoPhanGiaiStr.split("\\s*,\\s*"))
                            : new ArrayList<>();
                    List<String> cameraKhauDoList = cameraKhauDoStr != null
                            ? Arrays.asList(cameraKhauDoStr.split("\\s*,\\s*"))
                            : new ArrayList<>();

                    for (int i = 0; i < cameraLoaiList.size(); i++) {
                        String loaiCamSau = cameraLoaiList.get(i).trim();
                        String doPhanGiaiCamSau = (i < cameraDoPhanGiaiList.size()) ? cameraDoPhanGiaiList.get(i).trim() : null;
                        String khauDoCamSau = (i < cameraKhauDoList.size()) ? cameraKhauDoList.get(i).trim() : null;

                        if (loaiCamSau.isEmpty()) continue;

                        CameraSau cameraSau = cameraSauRepo
                                .findByLoaiCameraAndDoPhanGiaiAndKhauDo(loaiCamSau, doPhanGiaiCamSau, khauDoCamSau)
                                .orElseThrow(() -> new IllegalArgumentException(
                                        "Không tìm thấy Camera Sau với loại '" + loaiCamSau +
                                                "', độ phân giải '" + (doPhanGiaiCamSau != null ? doPhanGiaiCamSau : "") +
                                                "', khẩu độ '" + (khauDoCamSau != null ? khauDoCamSau : "") +
                                                "' tại dòng: " + (row.getRowNum() + 1) +
                                                ". Vui lòng kiểm tra dữ liệu hoặc thêm Camera Sau vào hệ thống."
                                ));

                        ModelCameraSau modelCameraSau = new ModelCameraSau();
                        modelCameraSau.setCameraSau(cameraSau);
                        modelCameraSau.setIsChinh(i == 0); // camera đầu tiên là chính
                        cameraSaus.add(modelCameraSau);
                    }
                }

                sanPham.setCameraSaus(cameraSaus);


                // Không lưu vào DB, chỉ add vào list
                sanPhams.add(sanPham);
            }
        }

        return mapEntityToResponseList(sanPhams);
    }


    private String getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                }
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return null;
        }
    }

}
