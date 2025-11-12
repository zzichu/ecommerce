package com.ecomerce.domain.auth.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ecomerce.domain.auth.repository.AuthRepository;
import com.ecomerce.domain.auth.entity.UserEntity;
import com.ecomerce.domain.auth.dto.UserDto;

@Service
public class AuthService {
    
    private final AuthRepository authRepository;

    public AuthService(AuthRepository authRepository, PasswordEncoder passwordEncoder) {
        this.authRepository = authRepository;
		this.passwordEncoder = passwordEncoder;
    }
    
    private final PasswordEncoder passwordEncoder;
    
    @Transactional
    public void registerUser(UserDto userDto) {
        if (authRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        UserEntity userEntity = UserEntity.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword())) // TODO: JWT
                .addressRoad(userDto.getAddressRoad())
                .addressDetail(userDto.getAddressDetail())
                .userRole(userDto.getUserRole())
                .build();

        authRepository.save(userEntity);
    }

    
    public UserDto getUserById(Long userId) {
        Optional<UserEntity> userEntityOpt = authRepository.findById(userId);

        UserEntity userEntity = userEntityOpt.orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        return UserDto.builder()
                .userId(userEntity.getUserId())
                .email(userEntity.getEmail())
                .addressRoad(userEntity.getAddressRoad())
                .addressDetail(userEntity.getAddressDetail())
                .userRole(userEntity.getUserRole())
                .created_date(userEntity.getCreatedDate())
                .modified_date(userEntity.getModifiedDate())
                .deleted_status(userEntity.getDeletedStatus())
                .build();
    }
    
    // TODO : JWT토큰, spring security 강의 듣고 적용..
    @Transactional(readOnly = true) 
    public UserDto login(String email, String password) { 
        UserEntity userEntity = authRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!passwordEncoder.matches(password, userEntity.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return UserDto.builder()
                .userId(userEntity.getUserId())
                .email(userEntity.getEmail())
                .userRole(userEntity.getUserRole())
                .addressRoad(userEntity.getAddressRoad())
                .addressDetail(userEntity.getAddressDetail())
                .created_date(userEntity.getCreatedDate())
                .modified_date(userEntity.getModifiedDate())
                .deleted_status(userEntity.getDeletedStatus())
                .build();
    }

    @Transactional
    public UserDto withDraw(Long userId) {
        UserEntity userEntity = authRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        userEntity.setDeletedStatus(true);
        userEntity.setModifiedDate(LocalDateTime.now());
        authRepository.save(userEntity);

        return UserDto.builder()
                .userId(userEntity.getUserId())
                .email(userEntity.getEmail())
                .addressRoad(userEntity.getAddressRoad())
                .addressDetail(userEntity.getAddressDetail())
                .userRole(userEntity.getUserRole())
                .deleted_status(userEntity.getDeletedStatus())
                .created_date(userEntity.getCreatedDate())
                .modified_date(userEntity.getModifiedDate())
                .build();
    }
}
