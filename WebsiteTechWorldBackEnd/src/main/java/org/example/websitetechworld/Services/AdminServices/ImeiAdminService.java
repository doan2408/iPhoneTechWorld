package org.example.websitetechworld.Services.AdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Response.ImeiAdminResponse;
import org.example.websitetechworld.Entity.Imei;
import org.example.websitetechworld.Repository.ImeiReposiory;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ImeiAdminService {
    private final ImeiReposiory imeiReposiory;

    private final ModelMapper modelMapper;

    public ImeiAdminResponse convert(Imei imei) {
        return modelMapper.map(imei, ImeiAdminResponse.class);
    }

    public Page<ImeiAdminResponse> getAllImei(Pageable pageable) {
        Page<Imei> imeiList = imeiReposiory.findAll(pageable);
        return imeiList.map(this::convert);
    }
}
