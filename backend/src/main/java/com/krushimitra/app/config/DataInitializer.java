package com.krushimitra.app.config;

import com.krushimitra.app.entity.GovernmentScheme;
import com.krushimitra.app.entity.Role;
import com.krushimitra.app.entity.User;
import com.krushimitra.app.repository.GovernmentSchemeRepository;
import com.krushimitra.app.repository.RoleRepository;
import com.krushimitra.app.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Data Initializer - populates database with initial seed data
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired private RoleRepository roleRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private GovernmentSchemeRepository schemeRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        initRoles();
        initAdminUser();
        initSchemes();
    }

    private void initRoles() {
        if (roleRepository.count() == 0) {
            Role r1 = new Role(); r1.setName(Role.ERole.ROLE_FARMER); roleRepository.save(r1);
            Role r2 = new Role(); r2.setName(Role.ERole.ROLE_ADMIN);  roleRepository.save(r2);
            Role r3 = new Role(); r3.setName(Role.ERole.ROLE_SUPER_ADMIN); roleRepository.save(r3);
            logger.info("Roles initialized.");
        }
    }

    private void initAdminUser() {
        if (!userRepository.existsByUsername("admin")) {
            Role adminRole = roleRepository.findByName(Role.ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);

            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@krushimitra.com");
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setFullName("System Administrator");
            admin.setActive(true);
            admin.setEmailVerified(true);
            admin.setRoles(roles);
            userRepository.save(admin);
            logger.info("Default admin user created: admin / Admin@123");
        }
    }

    private void initSchemes() {
        if (schemeRepository.count() == 0) {
            schemeRepository.save(buildScheme("PM-KISAN",
                "PM Kisan Samman Nidhi (PM-KISAN)",
                "Under this scheme, financial benefit of Rs.6000/- per year is provided to all farmer families across the country in three equal installments of Rs.2000/-",
                "Direct income support of ₹6,000 per year in three installments of ₹2,000 each",
                "All land holding farmer families. Excludes: Government employees, Income Tax payers",
                "Aadhaar Card, Bank Account, Land Records, Mobile Number",
                "Visit pmkisan.gov.in or CSC center",
                "Ministry of Agriculture & Farmers Welfare",
                LocalDate.of(2019, 2, 24), "https://pmkisan.gov.in", "155261 / 1800-115-526",
                "Small and Marginal Farmers", GovernmentScheme.SchemeCategory.INCOME_SUPPORT,
                "Central Sector Scheme", "₹6,000 per year", true));

            schemeRepository.save(buildScheme("PMFBY",
                "Pradhan Mantri Fasal Bima Yojana (PMFBY)",
                "A crop insurance scheme providing financial support to farmers suffering crop loss/damage due to unforeseen events.",
                "Comprehensive risk coverage for pre-sowing to post-harvest losses at low premium rates",
                "All farmers growing notified crops. Compulsory for loanee farmers.",
                "Aadhaar Card, Bank Account, Land Records, Sowing Certificate",
                "Through banks/cooperative societies/CSC centers",
                "Ministry of Agriculture & Farmers Welfare",
                LocalDate.of(2016, 1, 18), "https://pmfby.gov.in", "1800-200-7710",
                "All Farmers", GovernmentScheme.SchemeCategory.CROP_INSURANCE,
                "Centrally Sponsored Scheme", "Premium: Kharif 2%, Rabi 1.5%", true));

            schemeRepository.save(buildScheme("KCC",
                "Kisan Credit Card (KCC)",
                "A credit scheme providing farmers with timely and adequate credit for agricultural needs.",
                "Flexible credit, no collateral for loans up to ₹1.6 lakh, 2% interest subvention",
                "Farmers individually or jointly owning cultivated land. Tenant farmers, oral lessees.",
                "Aadhaar Card, PAN Card, Land Records, Passport Size Photo, Bank Account",
                "Apply at nearest bank branch (nationalized banks, cooperative banks, RRBs)",
                "Ministry of Agriculture & Farmers Welfare",
                LocalDate.of(1998, 8, 1), "https://www.nabard.org", "1800-200-7710",
                "All Farmers including Tenant Farmers", GovernmentScheme.SchemeCategory.CREDIT,
                "Credit Linked Scheme", "2% interest subvention on loans", true));

            schemeRepository.save(buildScheme("SHC",
                "Soil Health Card Scheme",
                "Provides soil health cards carrying crop-wise recommendations of nutrients and fertilizers for individual farms.",
                "Free soil testing, personalized fertilizer recommendations, reduced input costs",
                "All farmers across India are eligible",
                "Aadhaar Card, Land Records (for registration)",
                "Contact local agriculture department or KVK for soil sample collection",
                "Ministry of Agriculture & Farmers Welfare",
                LocalDate.of(2015, 2, 19), "https://soilhealth.dac.gov.in", "1800-180-1551",
                "All Farmers", GovernmentScheme.SchemeCategory.SOIL_HEALTH,
                "Centrally Sponsored Scheme", "Free soil testing and Soil Health Card", true));

            schemeRepository.save(buildScheme("PMKSY",
                "Pradhan Mantri Krishi Sinchayee Yojana (PMKSY)",
                "A mission to achieve water use efficiency through 'More Crop Per Drop'.",
                "Micro-irrigation equipment subsidy, improved water use efficiency",
                "All categories of farmers including SC/ST, small, marginal, and large farmers",
                "Aadhaar Card, Land Records, Bank Account, Photo",
                "Apply through district agriculture office or state-level implementing agency",
                "Ministry of Agriculture & Farmers Welfare",
                LocalDate.of(2015, 7, 1), "https://pmksy.gov.in", "1800-180-1551",
                "All Farmers", GovernmentScheme.SchemeCategory.IRRIGATION,
                "Centrally Sponsored Scheme", "55% subsidy for small/marginal, 45% for others", true));

            schemeRepository.save(buildScheme("ENAM",
                "National Agriculture Market (eNAM)",
                "A pan-India electronic trading portal networking existing APMC mandis.",
                "Better price discovery, transparent auction process, direct market access",
                "Farmers registered with eNAM platform",
                "Aadhaar Card, Bank Account, Mobile Number",
                "Register at enam.gov.in or visit nearest eNAM-integrated mandi",
                "Ministry of Agriculture & Farmers Welfare",
                LocalDate.of(2016, 4, 14), "https://enam.gov.in", "1800-270-0224",
                "Farmers and Traders", GovernmentScheme.SchemeCategory.MARKET_ACCESS,
                "Central Sector Scheme", "Online trading platform access", true));

            schemeRepository.save(buildScheme("FPO",
                "Formation and Promotion of 10,000 FPOs",
                "A scheme to form and promote Farmer Producer Organizations for collective action.",
                "Equity grant up to ₹18 lakh per FPO, credit guarantee up to ₹2 crore",
                "Minimum 300 farmers for plain areas, 100 for hilly/tribal/NE regions",
                "Group formation documents, registration papers, land records of member farmers",
                "Contact NABARD, SFAC, or state agriculture department",
                "Ministry of Agriculture & Farmers Welfare",
                LocalDate.of(2020, 2, 1), "https://sfacindia.com", "1800-270-0224",
                "Farmer Groups (FPOs)", GovernmentScheme.SchemeCategory.MARKET_ACCESS,
                "Central Sector Scheme", "₹18 lakh equity grant + ₹2 crore credit guarantee per FPO", true));

            schemeRepository.save(buildScheme("PMKUSUM",
                "Pradhan Mantri Kisan Urja Suraksha evam Utthaan Mahabhiyan (PM-KUSUM)",
                "A scheme to help farmers install solar pumps and grid-connected solar power plants, reducing dependence on diesel and grid electricity while creating an additional income source.",
                "Up to 60% subsidy on solar pump installation (30% central + 30% state), 30% available as bank loan so farmers pay as little as 10% upfront; surplus power can be sold to DISCOMs",
                "Individual farmers, cooperatives, panchayats, FPOs and water user associations owning cultivable land, especially in off-grid or low-grid areas",
                "Aadhaar Card, Land Records, Bank Account, Electricity Connection details (if grid-connected)",
                "Apply through the state nodal agency or the PM-KUSUM portal (pmkusum.mnre.gov.in)",
                "Ministry of New and Renewable Energy",
                LocalDate.of(2019, 3, 1), "https://pmkusum.mnre.gov.in", "1800-180-3333",
                "All Farmers (individual and collective)", GovernmentScheme.SchemeCategory.TECHNOLOGY,
                "Central Sector Scheme", "Up to 60% subsidy on solar pump/plant cost", true));

            schemeRepository.save(buildScheme("PKVY",
                "Paramparagat Krishi Vikas Yojana (PKVY)",
                "Promotes cluster-based organic farming across India, helping farmer groups shift to chemical-free practices with support for inputs, certification, and marketing.",
                "Financial assistance of ₹15,000/ha (of ₹31,500/ha total) paid directly to farmers via DBT over 3 years for organic inputs, plus support for certification, training and marketing",
                "Farmers willing to join a 20-hectare organic cluster group; open to all states except North-East (covered under MOVCDNER instead)",
                "Aadhaar Card, Land Records, Bank Account, Cluster/Group registration",
                "Enroll through the local Krishi Vigyan Kendra (KVK) or State Agriculture Department to join an organic cluster",
                "Ministry of Agriculture & Farmers Welfare",
                LocalDate.of(2015, 4, 1), "https://pgsindia-ncof.gov.in", "1800-180-1551",
                "Farmer Groups / Organic Clusters", GovernmentScheme.SchemeCategory.ORGANIC_FARMING,
                "Centrally Sponsored Scheme (under PM-RKVY)", "₹31,500 per hectare over 3 years", true));

            schemeRepository.save(buildScheme("AIF",
                "Agriculture Infrastructure Fund (AIF)",
                "A medium-to-long-term financing facility supporting the creation of post-harvest management infrastructure such as warehouses, cold storage and processing units.",
                "3% per annum interest subvention on loans up to ₹2 crore for a maximum of 7 years, plus credit guarantee coverage under CGTMSE for eligible borrowers",
                "Farmers, FPOs, PACS, agri-entrepreneurs, startups, SHGs, APMCs and cooperatives investing in post-harvest infrastructure or community farming assets",
                "Aadhaar Card, PAN Card, Land/Lease Documents, Detailed Project Report, Bank Account",
                "Register and apply on the AIF portal (agriinfra.dac.gov.in), then approach a partner bank for the loan",
                "Ministry of Agriculture & Farmers Welfare",
                LocalDate.of(2020, 7, 8), "https://agriinfra.dac.gov.in", "1800-180-1551",
                "Farmers, FPOs, Agri-Entrepreneurs, Cooperatives", GovernmentScheme.SchemeCategory.INFRASTRUCTURE,
                "Central Sector Financing Facility", "3% interest subvention on loans up to ₹2 crore", true));

            logger.info("Sample government schemes initialized.");
        }
    }

    private GovernmentScheme buildScheme(
            String code, String name, String description, String benefits,
            String eligibility, String documents, String howToApply, String ministry,
            LocalDate launchDate, String website, String helpline,
            String beneficiaryType, GovernmentScheme.SchemeCategory category,
            String schemeType, String financialAssistance, boolean active) {

        GovernmentScheme s = new GovernmentScheme();
        s.setSchemeCode(code);
        s.setName(name);
        s.setDescription(description);
        s.setBenefits(benefits);
        s.setEligibilityCriteria(eligibility);
        s.setRequiredDocuments(documents);
        s.setHowToApply(howToApply);
        s.setMinistry(ministry);
        s.setLaunchDate(launchDate);
        s.setOfficialWebsite(website);
        s.setHelplineNumber(helpline);
        s.setBeneficiaryType(beneficiaryType);
        s.setCategory(category);
        s.setSchemeType(schemeType);
        s.setFinancialAssistance(financialAssistance);
        s.setActive(active);
        s.setCentralScheme(true);
        s.setApplicableStates("ALL");
        return s;
    }
}