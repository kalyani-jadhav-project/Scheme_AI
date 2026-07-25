package com.krushimitra.app.dto.response;

import com.krushimitra.app.entity.Farmer;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FarmerProfileResponse {
    private Long id;
    private Long userId;
    private String username;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String profilePicture;
    private Integer age;
    private String state;
    private String district;
    private String village;
    private String pincode;
    private Farmer.FarmerCategory farmerCategory;
    private BigDecimal landHolding;
    private String cropType;
    private BigDecimal annualIncome;
    private boolean aadhaarAvailable;
    private boolean bankAccountAvailable;
    private boolean soilHealthCardAvailable;
    private boolean kisanCreditCardAvailable;
    private LocalDateTime createdAt;
    private int totalApplications;
    private int approvedApplications;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }
    public String getVillage() { return village; }
    public void setVillage(String village) { this.village = village; }
    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }
    public Farmer.FarmerCategory getFarmerCategory() { return farmerCategory; }
    public void setFarmerCategory(Farmer.FarmerCategory farmerCategory) { this.farmerCategory = farmerCategory; }
    public BigDecimal getLandHolding() { return landHolding; }
    public void setLandHolding(BigDecimal landHolding) { this.landHolding = landHolding; }
    public String getCropType() { return cropType; }
    public void setCropType(String cropType) { this.cropType = cropType; }
    public BigDecimal getAnnualIncome() { return annualIncome; }
    public void setAnnualIncome(BigDecimal annualIncome) { this.annualIncome = annualIncome; }
    public boolean isAadhaarAvailable() { return aadhaarAvailable; }
    public void setAadhaarAvailable(boolean aadhaarAvailable) { this.aadhaarAvailable = aadhaarAvailable; }
    public boolean isBankAccountAvailable() { return bankAccountAvailable; }
    public void setBankAccountAvailable(boolean bankAccountAvailable) { this.bankAccountAvailable = bankAccountAvailable; }
    public boolean isSoilHealthCardAvailable() { return soilHealthCardAvailable; }
    public void setSoilHealthCardAvailable(boolean soilHealthCardAvailable) { this.soilHealthCardAvailable = soilHealthCardAvailable; }
    public boolean isKisanCreditCardAvailable() { return kisanCreditCardAvailable; }
    public void setKisanCreditCardAvailable(boolean kisanCreditCardAvailable) { this.kisanCreditCardAvailable = kisanCreditCardAvailable; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public int getTotalApplications() { return totalApplications; }
    public void setTotalApplications(int totalApplications) { this.totalApplications = totalApplications; }
    public int getApprovedApplications() { return approvedApplications; }
    public void setApprovedApplications(int approvedApplications) { this.approvedApplications = approvedApplications; }
}
