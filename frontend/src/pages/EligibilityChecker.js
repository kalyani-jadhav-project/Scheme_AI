import React, { useState } from 'react';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import { eligibilityAPI } from '../services/api';
import './EligibilityChecker.css';

const FARMER_CATEGORIES = ['SMALL', 'MARGINAL', 'MEDIUM', 'LARGE', 'LANDLESS', 'TENANT'];
const INDIAN_STATES = [
  'Andhra Pradesh','Arunachal Pradesh','Assam','Bihar','Chhattisgarh','Goa','Gujarat','Haryana',
  'Himachal Pradesh','Jharkhand','Karnataka','Kerala','Madhya Pradesh','Maharashtra','Manipur',
  'Meghalaya','Mizoram','Nagaland','Odisha','Punjab','Rajasthan','Sikkim','Tamil Nadu','Telangana',
  'Tripura','Uttar Pradesh','Uttarakhand','West Bengal','Delhi','Jammu & Kashmir'
];

const EligibilityChecker = () => {
  const [step, setStep] = useState(1);
  const [form, setForm] = useState({
    name: '', age: '', state: '', district: '', farmerCategory: '',
    landHolding: '', cropType: '', annualIncome: '',
    aadhaarAvailable: false, bankAccountAvailable: false, soilHealthCardAvailable: false,
  });
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleChange = e => {
    const { name, type, checked, value } = e.target;
    setForm({ ...form, [name]: type === 'checkbox' ? checked : value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const payload = {
        ...form,
        age: parseInt(form.age) || null,
        landHolding: parseFloat(form.landHolding) || null,
        annualIncome: parseFloat(form.annualIncome) || null,
      };
      const res = await eligibilityAPI.check(payload);
      setResult(res.data.data);
      setStep(2);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <Navbar />
      <div className="page-wrapper">
        <div className="page-header">
          <div className="container">
            <h1>✅ Eligibility Checker</h1>
            <p>Find out which government schemes you qualify for instantly.</p>
          </div>
        </div>

        <div className="container" style={{padding:'2rem 1.5rem'}}>
          {step === 1 && (
            <div className="eligibility-form-container">
              <div className="eligibility-intro">
                <div className="intro-card">
                  <span>⚡</span>
                  <div><strong>Instant Results</strong><p>Get eligibility results in seconds</p></div>
                </div>
                <div className="intro-card">
                  <span>🎯</span>
                  <div><strong>7+ Schemes</strong><p>Checked against all active schemes</p></div>
                </div>
                <div className="intro-card">
                  <span>🔒</span>
                  <div><strong>Secure</strong><p>Your data is never stored</p></div>
                </div>
              </div>

              <div className="card card-body">
                <h2 style={{marginBottom:'1.5rem'}}>Enter Your Details</h2>
                <form onSubmit={handleSubmit}>
                  <div className="form-section-title">Personal Information</div>
                  <div className="form-row-3">
                    <div className="form-group">
                      <label className="form-label">Full Name *</label>
                      <input type="text" name="name" className="form-control" placeholder="Your name" value={form.name} onChange={handleChange} required />
                    </div>
                    <div className="form-group">
                      <label className="form-label">Age *</label>
                      <input type="number" name="age" className="form-control" placeholder="Age in years" value={form.age} onChange={handleChange} required min="18" max="100" />
                    </div>
                    <div className="form-group">
                      <label className="form-label">State *</label>
                      <select name="state" className="form-control form-select" value={form.state} onChange={handleChange} required>
                        <option value="">Select State</option>
                        {INDIAN_STATES.map(s => <option key={s} value={s}>{s}</option>)}
                      </select>
                    </div>
                  </div>

                  <div className="form-row-3">
                    <div className="form-group">
                      <label className="form-label">District</label>
                      <input type="text" name="district" className="form-control" placeholder="Your district" value={form.district} onChange={handleChange} />
                    </div>
                    <div className="form-group">
                      <label className="form-label">Farmer Category *</label>
                      <select name="farmerCategory" className="form-control form-select" value={form.farmerCategory} onChange={handleChange} required>
                        <option value="">Select Category</option>
                        {FARMER_CATEGORIES.map(c => <option key={c} value={c}>{c} Farmer</option>)}
                      </select>
                    </div>
                    <div className="form-group">
                      <label className="form-label">Land Holding (Acres)</label>
                      <input type="number" name="landHolding" className="form-control" placeholder="e.g., 2.5" value={form.landHolding} onChange={handleChange} min="0" step="0.1" />
                    </div>
                  </div>

                  <div className="form-section-title">Agricultural Details</div>
                  <div className="form-row-2">
                    <div className="form-group">
                      <label className="form-label">Crop Type</label>
                      <input type="text" name="cropType" className="form-control" placeholder="e.g., Wheat, Rice, Cotton" value={form.cropType} onChange={handleChange} />
                    </div>
                    <div className="form-group">
                      <label className="form-label">Annual Income (₹)</label>
                      <input type="number" name="annualIncome" className="form-control" placeholder="Annual household income" value={form.annualIncome} onChange={handleChange} min="0" />
                    </div>
                  </div>

                  <div className="form-section-title">Document Availability</div>
                  <div className="checkbox-grid">
                    <label className="checkbox-card">
                      <input type="checkbox" name="aadhaarAvailable" checked={form.aadhaarAvailable} onChange={handleChange} />
                      <span className="checkbox-icon">🪪</span>
                      <span>Aadhaar Card Available</span>
                    </label>
                    <label className="checkbox-card">
                      <input type="checkbox" name="bankAccountAvailable" checked={form.bankAccountAvailable} onChange={handleChange} />
                      <span className="checkbox-icon">🏦</span>
                      <span>Bank Account Available</span>
                    </label>
                    <label className="checkbox-card">
                      <input type="checkbox" name="soilHealthCardAvailable" checked={form.soilHealthCardAvailable} onChange={handleChange} />
                      <span className="checkbox-icon">🌱</span>
                      <span>Soil Health Card Available</span>
                    </label>
                  </div>

                  <button type="submit" className="btn btn-primary btn-lg mt-3" disabled={loading} style={{minWidth:'200px'}}>
                    {loading ? '⌛ Checking...' : '✅ Check My Eligibility'}
                  </button>
                </form>
              </div>
            </div>
          )}

          {step === 2 && result && (
            <div className="eligibility-results">
              {/* Summary */}
              <div className="results-summary">
                <div className="result-summary-item eligible">
                  <span className="result-num">{result.eligibleCount}</span>
                  <span>Eligible Schemes</span>
                </div>
                <div className="result-summary-divider">out of {result.totalSchemesChecked} checked</div>
                <div className="result-summary-item not-eligible">
                  <span className="result-num">{result.notEligibleCount}</span>
                  <span>Not Eligible</span>
                </div>
              </div>

              <h2 style={{marginBottom:'1.5rem', color:'var(--success)'}}>✅ Eligible Schemes ({result.eligibleCount})</h2>
              <div className="grid grid-2 mb-4">
                {result.eligibleSchemes?.map((s, i) => (
                  <div key={i} className="result-card eligible-card">
                    <div className="result-card-header">
                      <div>
                        <span className="badge badge-success">Eligible ✓</span>
                        <h3>{s.schemeName}</h3>
                      </div>
                    </div>
                    <div className="result-card-body">
                      <p className="result-reason">💡 {s.eligibilityReason}</p>
                      {s.benefits && <p style={{fontSize:'0.85rem',color:'var(--gray-600)',marginTop:'0.5rem'}}><strong>Benefits:</strong> {s.benefits.substring(0,100)}...</p>}
                      {s.nextSteps?.length > 0 && (
                        <div className="next-steps">
                          <strong>Next Steps:</strong>
                          <ol>
                            {s.nextSteps.map((step, j) => <li key={j}>{step}</li>)}
                          </ol>
                        </div>
                      )}
                    </div>
                    <div className="result-card-footer">
                      <a href={s.officialWebsite} target="_blank" rel="noopener noreferrer" className="btn btn-outline btn-sm">Official Site</a>
                      <a href="/login" className="btn btn-primary btn-sm">Apply Now</a>
                    </div>
                  </div>
                ))}
              </div>

              {result.notEligibleCount > 0 && (
                <>
                  <h2 style={{marginBottom:'1.5rem', color:'var(--danger)'}}>❌ Not Eligible ({result.notEligibleCount})</h2>
                  <div className="grid grid-2 mb-4">
                    {result.notEligibleSchemes?.map((s, i) => (
                      <div key={i} className="result-card not-eligible-card">
                        <div className="result-card-header">
                          <div>
                            <span className="badge badge-danger">Not Eligible</span>
                            <h3>{s.schemeName}</h3>
                          </div>
                        </div>
                        <div className="result-card-body">
                          <p className="result-reason">⚠️ {s.eligibilityReason}</p>
                          <p style={{fontSize:'0.8rem',color:'var(--gray-500)'}}>
                            Required: {s.requiredDocuments}
                          </p>
                        </div>
                      </div>
                    ))}
                  </div>
                </>
              )}

              <button className="btn btn-outline" onClick={() => setStep(1)}>← Check Again</button>
            </div>
          )}
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default EligibilityChecker;
