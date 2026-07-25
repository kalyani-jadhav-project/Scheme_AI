package com.krushimitra.app.controller;

import com.krushimitra.app.dto.response.ApiResponse;
import com.krushimitra.app.dto.response.FarmerProfileResponse;
import com.krushimitra.app.entity.User;
import com.krushimitra.app.exception.ResourceNotFoundException;
import com.krushimitra.app.repository.UserRepository;
import com.krushimitra.app.security.UserPrincipal;
import com.krushimitra.app.service.FarmerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * REST Controller for user profile management
 */
@RestController
@RequestMapping("/profile")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProfileController {

    @Autowired private UserRepository userRepository;
    @Autowired private FarmerService farmerService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<FarmerProfileResponse>> getProfile() {
        return ResponseEntity.ok(ApiResponse.success("Profile fetched", farmerService.getCurrentFarmerProfile()));
    }

    @PutMapping("/update-name")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<String>> updateName(@RequestParam String fullName) {
        UserPrincipal current = (UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        User user = userRepository.findById(current.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setFullName(fullName);
        userRepository.save(user);
        return ResponseEntity.ok(ApiResponse.success("Name updated", null));
    }

    @PutMapping("/update-phone")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<String>> updatePhone(@RequestParam String phone) {
        UserPrincipal current = (UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        User user = userRepository.findById(current.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setPhoneNumber(phone);
        userRepository.save(user);
        return ResponseEntity.ok(ApiResponse.success("Phone updated", null));
    }

    @PostMapping("/upload-picture")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<String>> uploadProfilePicture(
            @RequestParam("file") MultipartFile file) throws IOException {
        UserPrincipal current = (UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Path uploadPath = Paths.get("./uploads/profiles");
        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        User user = userRepository.findById(current.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setProfilePicture("/uploads/profiles/" + fileName);
        userRepository.save(user);

        return ResponseEntity.ok(ApiResponse.success("Profile picture uploaded", user.getProfilePicture()));
    }
}
