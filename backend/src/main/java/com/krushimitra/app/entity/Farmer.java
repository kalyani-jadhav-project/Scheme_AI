package com.krushimitra.app.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "farmers")
@EntityListeners(AuditingEntityListener.class)
public class Farmer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "age")
    private Integer age;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "district", length = 100)
    private String district;

    @Column(name = "village", length = 100)
    private String village;

    @Column(name = "pincode", length = 10)
    private String pincode;

    @Enumerated(EnumType.STRING)
    @Column(name = "farmer_category")
    private FarmerCategory farmerCategory;

    @Column(name = "land_holding", precision = 10, scale = 2)
    private BigDecimal landHolding;

    @Column(name = "crop_type", length = 200)
    private String cropType;

    @Column(name = "annual_income", precision = 12, scale = 2)
    private BigDecimal annualIncome;

    @Column(name = "aadhaar_number", length = 12)
    private String aadhaarNumber;

    @Column(name = "aadhaar_available")
    private boolean aadhaarAvailable = false;

    @Column(name = "bank_account_number", length = 20)
    private String bankAccountNumber;

    @Column(name = "bank_name", length = 100)
    private String bankName;

    @Column(name = "ifsc_code", length = 15)
    private String ifscCode;

    @Column(name = "bank_account_available")
    private boolean bankAccountAvailable = false;

    @Column(name = "soil_health_card_available")
    private boolean soilHealthCardAvailable = false;

    @Column(name = "soil_health_card_number", length = 50)
    private String soilHealthCardNumber;

    @Column(name = "kisan_credit_card_available")
    private boolean kisanCreditCardAvailable = false;

    @Column(name = "kisan_credit_card_number", length = 20)
    private String kisanCreditCardNumber;

    @Column(name = "farm_location_lat")
    private Double farmLocationLat;

    @Column(name = "farm_location_lng")
    private Double farmLocationLng;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "farmer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SchemeApplication> applications = new ArrayList<>();

    @OneToMany(mappedBy = "farmer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Notification> notifications = new ArrayList<>();

    public Farmer() {}

    public enum FarmerCategory {
        SMALL, MARGINAL, MEDIUM, LARGE, LANDLESS, TENANT
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
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
    public FarmerCategory getFarmerCategory() { return farmerCategory; }
    public void setFarmerCategory(FarmerCategory farmerCategory) { this.farmerCategory = farmerCategory; }
    public BigDecimal getLandHolding() { return landHolding; }
    public void setLandHolding(BigDecimal landHolding) { this.landHolding = landHolding; }
    public String getCropType() { return cropType; }
    public void setCropType(String cropType) { this.cropType = cropType; }
    public BigDecimal getAnnualIncome() { return annualIncome; }
    public void setAnnualIncome(BigDecimal annualIncome) { this.annualIncome = annualIncome; }
    public String getAadhaarNumber() { return aadhaarNumber; }
    public void setAadhaarNumber(String aadhaarNumber) { this.aadhaarNumber = aadhaarNumber; }
    public boolean isAadhaarAvailable() { return aadhaarAvailable; }
    public void setAadhaarAvailable(boolean aadhaarAvailable) { this.aadhaarAvailable = aadhaarAvailable; }
    public String getBankAccountNumber() { return bankAccountNumber; }
    public void setBankAccountNumber(String bankAccountNumber) { this.bankAccountNumber = bankAccountNumber; }
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
    public String getIfscCode() { return ifscCode; }
    public void setIfscCode(String ifscCode) { this.ifscCode = ifscCode; }
    public boolean isBankAccountAvailable() { return bankAccountAvailable; }
    public void setBankAccountAvailable(boolean bankAccountAvailable) { this.bankAccountAvailable = bankAccountAvailable; }
    public boolean isSoilHealthCardAvailable() { return soilHealthCardAvailable; }
    public void setSoilHealthCardAvailable(boolean soilHealthCardAvailable) { this.soilHealthCardAvailable = soilHealthCardAvailable; }
    public String getSoilHealthCardNumber() { return soilHealthCardNumber; }
    public void setSoilHealthCardNumber(String soilHealthCardNumber) { this.soilHealthCardNumber = soilHealthCardNumber; }
    public boolean isKisanCreditCardAvailable() { return kisanCreditCardAvailable; }
    public void setKisanCreditCardAvailable(boolean kisanCreditCardAvailable) { this.kisanCreditCardAvailable = kisanCreditCardAvailable; }
    public String getKisanCreditCardNumber() { return kisanCreditCardNumber; }
    public void setKisanCreditCardNumber(String kisanCreditCardNumber) { this.kisanCreditCardNumber = kisanCreditCardNumber; }
    public Double getFarmLocationLat() { return farmLocationLat; }
    public void setFarmLocationLat(Double farmLocationLat) { this.farmLocationLat = farmLocationLat; }
    public Double getFarmLocationLng() { return farmLocationLng; }
    public void setFarmLocationLng(Double farmLocationLng) { this.farmLocationLng = farmLocationLng; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public List<SchemeApplication> getApplications() { return applications; }
    public void setApplications(List<SchemeApplication> applications) { this.applications = applications; }
    public List<Notification> getNotifications() { return notifications; }
    public void setNotifications(List<Notification> notifications) { this.notifications = notifications; }
}
