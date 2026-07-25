package com.krushimitra.app.service;

import com.krushimitra.app.dto.request.*;
import com.krushimitra.app.dto.response.JwtResponse;
import com.krushimitra.app.entity.Farmer;
import com.krushimitra.app.entity.Role;
import com.krushimitra.app.entity.User;
import com.krushimitra.app.exception.BadRequestException;
import com.krushimitra.app.exception.DuplicateResourceException;
import com.krushimitra.app.exception.ResourceNotFoundException;
import com.krushimitra.app.repository.FarmerRepository;
import com.krushimitra.app.repository.RoleRepository;
import com.krushimitra.app.repository.UserRepository;
import com.krushimitra.app.security.JwtTokenProvider;
import com.krushimitra.app.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for authentication operations
 */
@Service
@Transactional
public class AuthService {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private FarmerRepository farmerRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtTokenProvider tokenProvider;

    /**
     * Authenticate user and return JWT token
     */
    public JwtResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateJwtToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(
                ((UserPrincipal) authentication.getPrincipal()).getUsername());

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setToken(jwt);
        jwtResponse.setRefreshToken(refreshToken);
        jwtResponse.setType("Bearer");
        jwtResponse.setId(userPrincipal.getId());
        jwtResponse.setUsername(userPrincipal.getUsername());
        jwtResponse.setEmail(userPrincipal.getEmail());
        jwtResponse.setFullName(userPrincipal.getFullName());
        jwtResponse.setRoles(roles);
        return jwtResponse;
    }

    /**
     * Register a new farmer user
     */
    public User register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new DuplicateResourceException("Username is already taken: " + registerRequest.getUsername());
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new DuplicateResourceException("Email is already registered: " + registerRequest.getEmail());
        }

        Role farmerRole = roleRepository.findByName(Role.ERole.ROLE_FARMER)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer role not found in database"));

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFullName(registerRequest.getFullName());
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        user.setActive(true);
        user.setEmailVerified(false);
        user.setRoles(Set.of(farmerRole));

        User savedUser = userRepository.save(user);

        // Create Farmer profile
        Farmer farmer = new Farmer();
        farmer.setUser(savedUser);
        farmer.setState(registerRequest.getState());
        farmer.setDistrict(registerRequest.getDistrict());
        farmerRepository.save(farmer);

        return savedUser;
    }

    /**
     * Handle forgot password
     */
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("No account found with email: " + request.getEmail()));

        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        // In production, send email with token
        // emailService.sendPasswordResetEmail(user.getEmail(), token);
    }

    /**
     * Reset password using token
     */
    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByResetPasswordToken(request.getToken())
                .orElseThrow(() -> new BadRequestException("Invalid or expired reset token"));

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Reset token has expired. Please request a new one.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetPasswordToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
    }

    /**
     * Refresh JWT token
     */
    public JwtResponse refreshToken(String refreshToken) {
        if (!tokenProvider.validateJwtToken(refreshToken)) {
            throw new BadRequestException("Invalid refresh token");
        }
        String username = tokenProvider.getUsernameFromJwtToken(refreshToken);
        String newToken = tokenProvider.generateTokenFromUsername(username);
        String newRefreshToken = tokenProvider.generateRefreshToken(username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());

        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setToken(newToken);
        jwtResponse.setRefreshToken(newRefreshToken);
        jwtResponse.setType("Bearer");
        jwtResponse.setId(user.getId());
        jwtResponse.setUsername(user.getUsername());
        jwtResponse.setEmail(user.getEmail());
        jwtResponse.setFullName(user.getFullName());
        jwtResponse.setRoles(roles);
        return jwtResponse;
    }
}
