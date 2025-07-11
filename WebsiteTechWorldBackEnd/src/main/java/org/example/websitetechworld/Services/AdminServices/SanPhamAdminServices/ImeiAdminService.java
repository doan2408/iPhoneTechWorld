package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.ImeiAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.SaveImeiRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.ImeiAdminResponse;
import org.example.websitetechworld.Entity.Imei;
import org.example.websitetechworld.Entity.SanPham;
import org.example.websitetechworld.Entity.SanPhamChiTiet;
import org.example.websitetechworld.Enum.Imei.TrangThaiImei;
import org.example.websitetechworld.Repository.ImeiReposiory;
import org.example.websitetechworld.Repository.SanPhamChiTietRepository;
import org.example.websitetechworld.Repository.SanPhamRepository;
import org.example.websitetechworld.exception.BusinessException;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.example.websitetechworld.exception.ValidationException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ImeiAdminService {
    private final ImeiReposiory imeiReposiory;
    private final SanPhamChiTietRepository sanPhamChiTietRepo;

    private final ModelMapper modelMapper;
    private final SanPhamRepository sanPhamRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;

    public ImeiAdminResponse convert(Imei imei) {
        return modelMapper.map(imei, ImeiAdminResponse.class);
    }

    public void checkDuplicateImei(ImeiAdminRequest request) {
        Integer count1 = imeiReposiory.countSoImei(request.getSoImei());
//        Integer count2 = imeiReposiory.countSoImei2(request.getSoImei2());

        List<Map<String, String>> errors = new ArrayList<>();

        if (count1 != null && count1 > 0) {
            errors.add(Map.of("field", "soImei", "message", "IMEI đã tồn tại"));
        }
//        if (count2 != null && count2 > 0) {
//            errors.add(Map.of("field", "soImei2", "message", "IMEI 2 đã tồn tại"));
//        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public Page<ImeiAdminResponse> getAllImei(Pageable pageable) {
        Page<Imei> imeiList = imeiReposiory.findAll(pageable);
        return imeiList.map(this::convert);
    }

    @Transactional(readOnly = true)
    public List<String> checkImeiDuplicates(List<String> imeis) {
        if (imeis == null || imeis.isEmpty()) {
            throw new BusinessException("Danh sách IMEI không được để trống");
        }

        // Lọc các IMEI hợp lệ (15 chữ số)
        List<String> validImeis = imeis.stream()
                .filter(imei -> imei != null && imei.matches("^\\d{15}$"))
                .collect(Collectors.toList());

        if (validImeis.isEmpty()) {
            throw new BusinessException("Không có IMEI hợp lệ trong danh sách");
        }

        // Kiểm tra trùng lặp
        return imeiReposiory.findBySoImeiIn(validImeis).stream()
                .map(Imei::getSoImei)
                .collect(Collectors.toList());
    }

    public Page<ImeiAdminResponse> getAllImei(Pageable pageable, String keyword) {
        Page<Imei> pageResult;
        if(keyword != null && !keyword.isEmpty()) {
            pageResult = imeiReposiory.findByKeyword(keyword, pageable);
        }
        else {
            pageResult = imeiReposiory.findAll(pageable);
        }
        return pageResult.map(this::convert);
    }

    @Transactional
    public ImeiAdminResponse createImei(ImeiAdminRequest req) {
        List<Map<String, String>> errors = new ArrayList<>();
        //validate
        checkDuplicateImei(req);

        Imei imei = imeiReposiory.save(modelMapper.map(req, Imei.class));
        return convert(imei);
    }

    @Transactional
    public ImeiAdminResponse updateImei(Integer id, ImeiAdminRequest req) {
        Imei imeiOld = imeiReposiory.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Imei ID: " + id));
        List<Map<String, String>> errors = new ArrayList<>();


        boolean unchangeImei1 =
                Objects.equals(req.getSoImei(), imeiOld.getSoImei());

//        boolean unchangeImei2 =
//                Objects.equals(req.getSoImei2(), imeiOld.getSoImei2());

        // Nếu cả hai đều không thay đổi → không cần kiểm tra trùng lặp → trả về luôn
//        if (unchangeImei1 && unchangeImei2) {
//            return convert(imeiOld);
//        }

        //imei 1 k thay đổi -> k kiểm tra trùng -> trả về dữ liệu
        if (unchangeImei1) {
            return convert(imeiOld);
        }

        // Nếu có thay đổi ít nhất một trong hai → kiểm tra trùng lặp tương ứng
        if (!unchangeImei1) {
            Integer count1 = imeiReposiory.countSoImei(req.getSoImei());
            if (count1 != null && count1 > 0) {
                errors.add(Map.of("field", "soImei", "message", "IMEI 1 đã tồn tại"));
            }
        }

//        if (!unchangeImei2) {
//            Integer count2 = imeiReposiory.countSoImei2(req.getSoImei2());
//            if (count2 != null && count2 > 0) {
//                errors.add(Map.of("field", "soImei2", "message", "IMEI 2 đã tồn tại"));
//            }
//        }

        // Nếu có lỗi → ném ngoại lệ
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        imeiOld.setSoImei(req.getSoImei());
//        imeiOld.setSoImei2(req.getSoImei2());
        imeiOld.setTrangThaiImei(req.getTrangThaiImei());
        imeiOld.setIdSanPhamChiTiet(sanPhamChiTietRepo.findById(req.getIdSanPhamChiTiet()).orElse(null));

        imeiReposiory.save(modelMapper.map(imeiOld, Imei.class));

        return convert(imeiOld);
    }

    @Transactional
    public ImeiAdminResponse deleteImei(Integer id) {
        Imei imei = imeiReposiory.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Imei ID: " + id));
        imeiReposiory.deleteById(id);

        return convert(imei);
    }

    public ImeiAdminResponse detailImei(Integer id) {
        Imei imei = imeiReposiory.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Imei ID: " + id));
        return convert(imei);
    }


    public void importImeiFromExcel(MultipartFile file, Integer idSanPhamChiTiet) {
        try {
            List<SaveImeiRequest> imeiDtos = readExcelToDto(file.getInputStream());

            SanPhamChiTiet spct = sanPhamChiTietRepository.findById(idSanPhamChiTiet)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm chi tiết"));

            List<Imei> imeis = imeiDtos.stream().map(dto -> {
                Imei imei = new Imei();
                imei.setSoImei(dto.getSoImei());
                imei.setTrangThaiImei(TrangThaiImei.AVAILABLE);
                imei.setIdSanPhamChiTiet(spct); //  dùng chung ID
                return imei;
            }).collect(Collectors.toList());

            imeiReposiory.saveAll(imeis);

        } catch (IOException e) {
            throw new RuntimeException("Lỗi đọc file Excel");
        }
    }

    /**
     * Hàm đọc file Excel và convert thành danh sách DTO
     */
    private List<SaveImeiRequest> readExcelToDto(InputStream inputStream) throws IOException {
        List<SaveImeiRequest> imeiList = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null || row.getCell(0) == null) continue;

            String soImei = getCellStringValue(row.getCell(0));
            SaveImeiRequest dto = new SaveImeiRequest();
            dto.setSoImei(soImei);
            imeiList.add(dto);
        }

        workbook.close();
        return imeiList;
    }




    private String getCellStringValue(Cell cell) {
        if (cell == null) return null;

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf(new java.math.BigDecimal(cell.getNumericCellValue()).toBigInteger()); // để không bị mất số 0 ở đầu
            default -> throw new IllegalArgumentException("Không thể đọc giá trị IMEI từ ô Excel.");
        };
    }


    public List<String> getImeisBySanPhamChiTietId(Integer sanPhamChiTietId) {
        return imeiReposiory.findByIdSanPhamChiTiet_Id(sanPhamChiTietId)
                .stream()
                .map(Imei::getSoImei)
                .collect(Collectors.toList());
    }
}
