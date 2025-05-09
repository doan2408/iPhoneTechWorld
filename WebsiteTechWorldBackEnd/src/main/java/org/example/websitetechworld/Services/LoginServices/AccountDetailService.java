package org.example.websitetechworld.Services.LoginServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Repository.JointAccount;
import org.example.websitetechworld.Repository.KhachHangRepository;
import org.example.websitetechworld.Repository.NhanVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AccountDetailService implements UserDetailsService {
    private final NhanVienRepository nhanVienRepository;
    private final KhachHangRepository khachHangRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        JointAccount user = nhanVienRepository.findByTaiKhoan(username)
                .map(nv -> (JointAccount) nv)
                .orElseGet(() -> khachHangRepository.findByTaiKhoan(username).map(
                                kh -> (JointAccount) kh)
                        .orElseThrow(() -> new UsernameNotFoundException("Can't found account " + username)));
        return new CustomUserDetails(user);
//        return new User(
//                user.tai_khoan(),
//                user.mat_khau(),
//                Collections.singleton(new SimpleGrantedAuthority(user.getRole()))
//        );
    }
}
