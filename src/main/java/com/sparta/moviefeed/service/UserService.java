package com.sparta.moviefeed.service;

import com.sparta.moviefeed.dto.requestdto.UserSignupRequestDto;
import com.sparta.moviefeed.dto.requestdto.UserWithdrawalRequestDto;
import com.sparta.moviefeed.entity.User;
import com.sparta.moviefeed.enumeration.UserStatus;
import com.sparta.moviefeed.exception.BadRequestException;
import com.sparta.moviefeed.exception.ConflictException;
import com.sparta.moviefeed.exception.DataNotFoundException;
import com.sparta.moviefeed.exception.UnauthorizedException;
import com.sparta.moviefeed.repository.UserRepository;
import com.sparta.moviefeed.security.UserDetailsImpl;
import com.sparta.moviefeed.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public void signup(UserSignupRequestDto requestDto) {

        findByUserId(requestDto.getUserId()).ifPresent( (el) -> {
            throw new ConflictException("이미 사용 중인 아이디입니다.");
        });

        UserStatus userStatus = UserStatus.NORMAL;
        User user = new User(requestDto, userStatus);
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        user.encryptionPassword(encodedPassword);
        userRepository.save(user);

    }

    @Transactional
    public void logout() {

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = findByUserId(userDetails.getUsername()).orElseThrow( () -> new DataNotFoundException("해당 회원은 존재하지 않습니다."));

        user.updateRefreshToken(null);

    }

    @Transactional
    public void withdrawal(UserWithdrawalRequestDto requestDto) {

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = findByUserId(userDetails.getUsername()).orElseThrow( () -> new DataNotFoundException("해당 회원은 존재하지 않습니다."));

        if (!checkPassword(requestDto.getPassword(), user.getPassword())) {
            throw new BadRequestException("비밀번호를 확인해주세요.");
        }

        if (user.getUserStatus() == UserStatus.LEAVE) {
            throw new ConflictException("이미 탈퇴한 회원입니다.");
        }

        UserStatus userStatus = UserStatus.LEAVE;
        LocalDateTime now = LocalDateTime.now();

        user.updateUserStatus(userStatus, now);

    }

    public String refresh(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        String tokenValue = null;

        if (cookies == null) {
            throw new BadRequestException("잘못된 요청입니다.");
        }

        for (Cookie cookie : cookies) {
            if ("refreshToken".equals(cookie.getName())) {
                tokenValue = cookie.getValue();
            }
        }

        if (!StringUtils.hasText(tokenValue)) {
            throw new BadRequestException("잘못된 요청입니다.");
        }

        jwtUtil.checkTokenExpiration(tokenValue);

        if (!jwtUtil.validateToken(tokenValue)) {
            throw new UnauthorizedException("토큰 검증 실패");
        }

        Claims info = jwtUtil.getClaimsFromToken(tokenValue);
        User user = findByUserId(info.getSubject()).orElseThrow( () -> new DataNotFoundException("해당 회원은 존재하지 않습니다."));

        if (!user.getRefreshToken().equals(tokenValue)) {
            throw new UnauthorizedException("토큰 검증 실패");
        }

        return jwtUtil.generateAccessToken(user.getUserId(), user.getUserName());
    }

    public Optional<User> findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    public boolean checkPassword(String requestPassword, String userPassword) {
        return passwordEncoder.matches(requestPassword, userPassword);
    }

}
