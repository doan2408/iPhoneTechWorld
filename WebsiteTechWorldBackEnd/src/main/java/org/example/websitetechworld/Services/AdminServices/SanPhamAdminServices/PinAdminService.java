package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.PinAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.PinAdminResponse;
import org.example.websitetechworld.Entity.Pin;
import org.example.websitetechworld.Entity.Rom;
import org.example.websitetechworld.Repository.PinRepository;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PinAdminService {
    private final PinRepository pinRepository;
    private final ModelMapper modelMapper;

    private PinAdminResponse convert(Pin pin) {
        return modelMapper.map(pin, PinAdminResponse.class);
    }

    public Page<PinAdminResponse> getAllPin(Pageable pageable) {
        Page<Pin> pins = pinRepository.findAll(pageable);
        return pins.map(this::convert);
    }

    @Transactional
    public PinAdminResponse createPin(PinAdminRequest pinAdminRequest) {
        Pin pin = pinRepository.save(modelMapper.map(pinAdminRequest, Pin.class));
        return convert(pin);
    }

    @Transactional
    public PinAdminResponse updatePin(Integer id, PinAdminRequest pinAdminRequest) {
        Pin pin = pinRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Pin ID: " + id));
        modelMapper.map(pinAdminRequest, pin);
        pinRepository.save(pin);

        return convert(pin);
    }

    @Transactional
    public PinAdminResponse deletePin(Integer id) {
        Pin pin = pinRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy pin ID: " + id));
        pinRepository.deleteById(id);

        return convert(pin);
    }

    public PinAdminResponse detailPin(Integer id) {
        Pin pin = pinRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy pin ID:" + id));
        return convert(pin);
    }
}
