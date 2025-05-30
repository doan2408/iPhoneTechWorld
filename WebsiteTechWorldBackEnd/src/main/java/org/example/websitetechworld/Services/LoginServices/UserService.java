package org.example.websitetechworld.Services.LoginServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Repository.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final NhanVienRepository nhanVienRepository;
    private final KhachHangRepository khachHangRepository;

    public Optional<JointAccount> findByEmail(String email) {
        Optional<JointAccount> nv = nhanVienRepository.findByEmail(email)
                .map(n -> (JointAccount) n);
        if (nv.isPresent()) return nv;

        return khachHangRepository.findByEmail(email)
                .map(k -> (JointAccount) k);
    }



}
