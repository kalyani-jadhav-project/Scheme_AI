package com.krushimitra.app.service;

import com.krushimitra.app.dto.request.FarmerProfileRequest;
import com.krushimitra.app.dto.response.FarmerProfileResponse;
import com.krushimitra.app.entity.Farmer;
import com.krushimitra.app.entity.User;
import com.krushimitra.app.exception.ResourceNotFoundException;
import com.krushimitra.app.repository.FarmerRepository;
import com.krushimitra.app.repository.SchemeApplicationRepository;
import com.krushimitra.app.repository.UserRepository;
import com.krushimitra.app.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for farmer profile management
 */
@Service
@Transactional
public class FarmerService {

    @Autowired private FarmerRepository farmerRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private SchemeApplicationRepository applicationRepository;

    /**
     * Get current authenticated farmer profile
     */
    public FarmerProfileResponse getCurrentFarmerProfile() {
        UserPrincipal currentUser = getCurrentUser();
        return getFarmerProfileByUserId(currentUser.getId());
    }

    /**
     * Get farmer profile by user ID
     */
    public FarmerProfileResponse getFarmerProfileByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Farmer farmer = farmerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer profile not found for user: " + userId));

        return mapToFarmerProfileResponse(user, farmer);
    }

    /**
     * Update farmer profile
     */
    public FarmerProfileResponse updateFarmerProfile(FarmerProfileRequest request) {
        UserPrincipal currentUser = getCurrentUser();
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", currentUser.getId()));
        Farmer farmer = farmerRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Farmer profile not found"));

        // Update farmer fields
        if (request.getAge() != null) farmer.setAge(request.getAge());
        if (request.getState() != null) farmer.setState(request.getState());
        if (request.getDistrict() != null) farmer.setDistrict(request.getDistrict());
        if (request.getVillage() != null) farmer.setVillage(request.getVillage());
        if (request.getPincode() != null) farmer.setPincode(request.getPincode());
        if (request.getFarmerCategory() != null) farmer.setFarmerCategory(request.getFarmerCategory());
        if (request.getLandHolding() != null) farmer.setLandHolding(request.getLandHolding());
        if (request.getCropType() != null) farmer.setCropType(request.getCropType());
        if (request.getAnnualIncome() != null) farmer.setAnnualIncome(request.getAnnualIncome());
        if (request.getAadhaarNumber() != null) farmer.setAadhaarNumber(request.getAadhaarNumber());
        farmer.setAadhaarAvailable(request.isAadhaarAvailable());
        if (request.getBankAccountNumber() != null) farmer.setBankAccountNumber(request.getBankAccountNumber());
        if (request.getBankName() != null) farmer.setBankName(request.getBankName());
        if (request.getIfscCode() != null) farmer.setIfscCode(request.getIfscCode());
        farmer.setBankAccountAvailable(request.isBankAccountAvailable());
        farmer.setSoilHealthCardAvailable(request.isSoilHealthCardAvailable());
        if (request.getSoilHealthCardNumber() != null) farmer.setSoilHealthCardNumber(request.getSoilHealthCardNumber());
        farmer.setKisanCreditCardAvailable(request.isKisanCreditCardAvailable());
        if (request.getKisanCreditCardNumber() != null) farmer.setKisanCreditCardNumber(request.getKisanCreditCardNumber());
        if (request.getFarmLocationLat() != null) farmer.setFarmLocationLat(request.getFarmLocationLat());
        if (request.getFarmLocationLng() != null) farmer.setFarmLocationLng(request.getFarmLocationLng());

        Farmer savedFarmer = farmerRepository.save(farmer);
        return mapToFarmerProfileResponse(user, savedFarmer);
    }

    /**
     * Get all farmers (admin)
     */
    public Page<FarmerProfileResponse> getAllFarmers(Pageable pageable) {
        return farmerRepository.findAll(pageable)
                .map(farmer -> mapToFarmerProfileResponse(farmer.getUser(), farmer));
    }

    /**
     * Get farmer by ID (admin)
     */
    public FarmerProfileResponse getFarmerById(Long farmerId) {
        Farmer farmer = farmerRepository.findById(farmerId)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer", "id", farmerId));
        return mapToFarmerProfileResponse(farmer.getUser(), farmer);
    }

    private FarmerProfileResponse mapToFarmerProfileResponse(User user, Farmer farmer) {
        FarmerProfileResponse response = new FarmerProfileResponse();
        response.setId(farmer.getId());
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setProfilePicture(user.getProfilePicture());
        response.setAge(farmer.getAge());
        response.setState(farmer.getState());
        response.setDistrict(farmer.getDistrict());
        response.setVillage(farmer.getVillage());
        response.setPincode(farmer.getPincode());
        response.setFarmerCategory(farmer.getFarmerCategory());
        response.setLandHolding(farmer.getLandHolding());
        response.setCropType(farmer.getCropType());
        response.setAnnualIncome(farmer.getAnnualIncome());
        response.setAadhaarAvailable(farmer.isAadhaarAvailable());
        response.setBankAccountAvailable(farmer.isBankAccountAvailable());
        response.setSoilHealthCardAvailable(farmer.isSoilHealthCardAvailable());
        response.setKisanCreditCardAvailable(farmer.isKisanCreditCardAvailable());
        response.setCreatedAt(farmer.getCreatedAt());

        int total = (int) applicationRepository.countByFarmerId(farmer.getId());
        int approved = (int) applicationRepository.countByFarmerIdAndStatus(
                farmer.getId(), com.krushimitra.app.entity.SchemeApplication.ApplicationStatus.APPROVED);
        response.setTotalApplications(total);
        response.setApprovedApplications(approved);
        return response;
    }

    private UserPrincipal getCurrentUser() {
        return (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
