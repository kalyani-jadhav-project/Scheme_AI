package com.krushimitra.app.dto.request;

import com.krushimitra.app.entity.Farmer;
import java.math.BigDecimal;

public class EligibilityCheckRequest {
    private String name;
    private Integer age;
    private String state;
    private String district;
    private Farmer.FarmerCategory farmerCategory;
    private BigDecimal landHolding;
    private String cropType;
    private BigDecimal annualIncome;
    private boolean aadhaarAvailable;
    private boolean bankAccountAvailable;
    private boolean soilHealthCardAvailable;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }
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
}
