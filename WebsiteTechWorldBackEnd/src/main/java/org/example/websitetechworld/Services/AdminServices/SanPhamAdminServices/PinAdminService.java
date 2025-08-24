package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.PinAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.PinQuicKCreateAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.PinAdminResponse;
import org.example.websitetechworld.Entity.Pin;
import org.example.websitetechworld.Repository.PinRepository;
import org.example.websitetechworld.exception.BusinessException;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PinAdminService {
    private final PinRepository pinRepository;
    private final ModelMapper modelMapper;

    private PinAdminResponse convert(Pin pin) {
        return modelMapper.map(pin, PinAdminResponse.class);
    }

    private List<PinAdminResponse> convertList(List<Pin> pins) {
        return pins.stream()
                .map(this::convert)
                .toList();
    }

    public List<PinAdminResponse> getAllPinList() {
        List<Pin> pins = pinRepository.findAll();
        return convertList(pins);
    }

    public Page<PinAdminResponse> getAllPin(Pageable pageable) {
        Page<Pin> pins = pinRepository.findAll(pageable);
        return pins.map(this::convert);
    }

    public Page<PinAdminResponse> searchPins(String search, Pageable pageable) {
        Page<Pin> pinPage;
        if (StringUtils.hasText(search)) {
            pinPage = pinRepository.findByPhienBanContainingIgnoreCaseOrCongSuatSacContainingIgnoreCase(search, search, pageable);
        } else {
            pinPage = pinRepository.findAll(pageable);
        }
        return pinPage.map(this::convert);
    }

    @Transactional
    public PinAdminResponse createPin(PinAdminRequest pinAdminRequest) {
        String rawPhienBanPin = pinAdminRequest.getPhienBan().trim();
        String normalizedPhienBanPin = normalizePhienBanPin(rawPhienBanPin);

        // B2: Validate bằng regex (cho phép prefix + dung lượng)
        if (!normalizedPhienBanPin.trim().matches("^[A-Za-z-]+\\d+mAh$")) {
            throw new BusinessException(
                    "Dung lượng pin không hợp lệ! Ví dụ: Li-Ion3800mAh hoặc Li-Ion 3800mAh. " +
                            "Không được có khoảng trắng giữa số và 'mAh', không chứa ký tự đặc biệt ngoài '-' trong tên công nghệ."
            );
        }

        // B3: Check trùng (so với normalized)
        if (pinRepository.existsByPhienBan(normalizedPhienBanPin)) {
            throw new BusinessException("Phiên bản đã tồn tại");
        }

        Pin pin = modelMapper.map(pinAdminRequest, Pin.class);
        pin.setPhienBan(normalizedPhienBanPin);

        Pin saved = pinRepository.save(pin);
        return convert(saved);
    }

    @Transactional
    public PinAdminResponse createPinQuick(PinQuicKCreateAdminRequest pinAdminRequest) {
        String rawPhienBanPin = pinAdminRequest.getPhienBan().trim();
        String normalizedPhienBanPin = normalizePhienBanPin(rawPhienBanPin);

        // B2: Validate bằng regex (cho phép prefix + dung lượng)
        if (!normalizedPhienBanPin.trim().matches("^[A-Za-z-]+\\d+mAh$")) {
            throw new BusinessException(
                    "Dung lượng pin không hợp lệ! Ví dụ: Li-Ion3800mAh hoặc Li-Ion 3800mAh. " +
                            "Không được có khoảng trắng giữa số và 'mAh', không chứa ký tự đặc biệt ngoài '-' trong tên công nghệ."
            );
        }

        // B3: Check trùng (so với normalized)
        if (pinRepository.existsByPhienBan(normalizedPhienBanPin)) {
            throw new BusinessException("Phiên bản đã tồn tại");
        }

        // B4: Lưu với normalized
        Pin pin = modelMapper.map(pinAdminRequest, Pin.class);
        pin.setPhienBan(normalizedPhienBanPin); // dùng bản chuẩn hóa

        Pin saved = pinRepository.save(pin);
        return convert(saved);
    }

    @Transactional
    public PinAdminResponse updatePin(Integer id, PinAdminRequest pinAdminRequest) {
        Pin pin = pinRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Pin ID: " + id));

        String rawPhienBanPin = pinAdminRequest.getPhienBan().trim();
        String normalizedPhienBanPin = normalizePhienBanPin(rawPhienBanPin);

        // B2: Validate bằng regex (cho phép prefix + dung lượng)
        if (!normalizedPhienBanPin.trim().matches("^[A-Za-z-]+\\d+mAh$")) {
            throw new BusinessException(
                    "Dung lượng pin không hợp lệ! Ví dụ: Li-Ion3800mAh hoặc Li-Ion 3800mAh. " +
                            "Không được có khoảng trắng giữa số và 'mAh', không chứa ký tự đặc biệt ngoài '-' trong tên công nghệ."
            );
        }

        if (pinRepository.existsByPhienBanAndIdNot(normalizedPhienBanPin, pin.getId())) {
            throw new BusinessException("Phiên bản đã tồn tại");
        }
        modelMapper.map(pinAdminRequest, pin);
        pin.setPhienBan(normalizedPhienBanPin);
        
        Pin saved = pinRepository.save(pin);
        return convert(saved);
    }

    @Transactional
    public PinAdminResponse deletePin(Integer id) {
        Pin pin = pinRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Pin ID: " + id));
        pinRepository.deleteById(id);
        return convert(pin);
    }

    public PinAdminResponse detailPin(Integer id) {
        Pin pin = pinRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Pin ID: " + id));
        return convert(pin);
    }

    private String normalizePhienBanPin(String input) {
        if (input == null) return null;
        // Bỏ khoảng trắng giữa chữ và số (Li-Ion 3800mAh -> Li-Ion3800mAh)
        return input.trim().replaceAll("([A-Za-z-])\\s+(\\d)", "$1$2");
    }
}