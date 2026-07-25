import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import { applicationAPI, farmerAPI } from '../services/api';

const ApplyScheme = () => {
  const { schemeId } = useParams();
  const navigate = useNavigate();
  const [form, setForm] = useState({ schemeId: parseInt(schemeId), remarks: '' });
  const [profile, setProfile] = useState(null);
  const [profileLoading, setProfileLoading] = useState(true);
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState('');
  const [error, setError] = useState('');

  // Auto-fill: pull the farmer's saved profile so they can review it
  // before it gets attached to this application.
  useEffect(() => {
    farmerAPI.getProfile()
      .then(res => setProfile(res.data.data))
      .catch(console.error)
      .finally(() => setProfileLoading(false));
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      const res = await applicationAPI.apply(form);
      setSuccess(`Application submitted! Reference: ${res.data.data.applicationNumber}`);
      setTimeout(() => navigate('/my-applications'), 2000);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to submit application.');
    } finally {
      setLoading(false);
    }
  };

  // Fields we check for completeness before letting the farmer submit confidently.
  const missingFields = [];
  if (profile) {
    if (!profile.state) missingFields.push('State');
    if (!profile.district) missingFields.push('District');
    if (!profile.landHolding) missingFields.push('Land Holding');
    if (!profile.aadhaarAvailable) missingFields.push('Aadhaar Card');
    if (!profile.bankAccountAvailable) missingFields.push('Bank Account');
  }

  return (
    <div>
      <Navbar />
      <div className="page-wrapper">
        <div className="page-header">
          <div className="container">
            <h1>Apply for Scheme</h1>
            <p>Submit your application for the selected government scheme</p>
          </div>
        </div>
        <div className="container" style={{padding:'2rem 1.5rem', maxWidth:'700px'}}>
          {success && <div className="alert alert-success">🎉 {success}</div>}
          {error && <div className="alert alert-error">⚠️ {error}</div>}

          {/* Auto-filled profile summary */}
          <div className="card card-body" style={{marginBottom:'1.5rem'}}>
            <h2 style={{marginBottom:'0.25rem'}}>🪪 Your Details (auto-filled from Profile)</h2>
            <p style={{color:'var(--gray-500)', fontSize:'0.9rem', marginBottom:'1rem'}}>
              These details are pulled from your saved profile and will be attached to this application automatically.
            </p>

            {profileLoading ? (
              <div className="loading-container"><div className="spinner"></div></div>
            ) : profile ? (
              <>
                <div style={{display:'grid', gridTemplateColumns:'1fr 1fr', gap:'0.75rem 1.5rem', fontSize:'0.95rem'}}>
                  <div><strong>Name:</strong> {profile.fullName || '—'}</div>
                  <div><strong>Phone:</strong> {profile.phoneNumber || '—'}</div>
                  <div><strong>State:</strong> {profile.state || '—'}</div>
                  <div><strong>District:</strong> {profile.district || '—'}</div>
                  <div><strong>Village:</strong> {profile.village || '—'}</div>
                  <div><strong>PIN Code:</strong> {profile.pincode || '—'}</div>
                  <div><strong>Farmer Category:</strong> {profile.farmerCategory || '—'}</div>
                  <div><strong>Land Holding:</strong> {profile.landHolding ? `${profile.landHolding} acres` : '—'}</div>
                  <div><strong>Crop Type:</strong> {profile.cropType || '—'}</div>
                  <div><strong>Annual Income:</strong> {profile.annualIncome ? `₹${profile.annualIncome}` : '—'}</div>
                </div>

                <div style={{display:'flex', gap:'0.5rem', flexWrap:'wrap', marginTop:'1rem'}}>
                  <span className={`badge ${profile.aadhaarAvailable ? 'badge-success' : 'badge-error'}`}>
                    {profile.aadhaarAvailable ? '✓' : '✗'} Aadhaar Card
                  </span>
                  <span className={`badge ${profile.bankAccountAvailable ? 'badge-success' : 'badge-error'}`}>
                    {profile.bankAccountAvailable ? '✓' : '✗'} Bank Account
                  </span>
                  <span className={`badge ${profile.soilHealthCardAvailable ? 'badge-success' : 'badge-error'}`}>
                    {profile.soilHealthCardAvailable ? '✓' : '✗'} Soil Health Card
                  </span>
                  <span className={`badge ${profile.kisanCreditCardAvailable ? 'badge-success' : 'badge-error'}`}>
                    {profile.kisanCreditCardAvailable ? '✓' : '✗'} Kisan Credit Card
                  </span>
                </div>

                {missingFields.length > 0 && (
                  <div className="alert alert-error" style={{marginTop:'1rem'}}>
                    ⚠️ Missing: {missingFields.join(', ')}. This may affect eligibility review.{' '}
                    <Link to="/profile" style={{fontWeight:600, textDecoration:'underline'}}>Update your profile</Link> before applying, or continue anyway.
                  </div>
                )}
              </>
            ) : (
              <div className="alert alert-error">
                ⚠️ Couldn't load your profile.{' '}
                <Link to="/profile" style={{fontWeight:600, textDecoration:'underline'}}>Complete your profile</Link> first.
              </div>
            )}
          </div>

          <div className="card card-body">
            <h2 style={{marginBottom:'1.5rem'}}>📋 Application Form</h2>
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label className="form-label">Additional Remarks (Optional)</label>
                <textarea
                  className="form-control"
                  rows={4}
                  placeholder="Any additional information or special circumstances..."
                  value={form.remarks}
                  onChange={e => setForm({...form, remarks: e.target.value})}
                  style={{resize:'vertical'}}
                />
              </div>
              <div className="alert alert-info" style={{marginBottom:'1rem'}}>
                ℹ️ By submitting this application, you confirm all provided information (including the auto-filled details above) is accurate.
              </div>
              <div style={{display:'flex', gap:'1rem'}}>
                <button type="submit" className="btn btn-primary btn-lg" disabled={loading}>
                  {loading ? '⌛ Submitting...' : '🚀 Submit Application'}
                </button>
                <button type="button" className="btn btn-outline" onClick={() => navigate(-1)}>
                  Cancel
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default ApplyScheme;