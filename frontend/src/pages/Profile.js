import React, { useState, useEffect } from 'react';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import { farmerAPI, profileAPI } from '../services/api';
import { useAuth } from '../context/AuthContext';
import './Profile.css';

const STATES = ['Andhra Pradesh','Assam','Bihar','Gujarat','Haryana','Karnataka','Kerala','Madhya Pradesh','Maharashtra','Punjab','Rajasthan','Tamil Nadu','Telangana','Uttar Pradesh','West Bengal'];
const CATEGORIES = ['SMALL','MARGINAL','MEDIUM','LARGE','LANDLESS','TENANT'];

const Profile = () => {
  const { user } = useAuth();
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [form, setForm] = useState({});
  const [success, setSuccess] = useState('');
  const [error, setError] = useState('');
  const [activeTab, setActiveTab] = useState('personal');

  useEffect(() => {
    farmerAPI.getProfile()
      .then(res => {
        const data = res.data.data;
        setProfile(data);
        setForm({
          age: data.age || '',
          state: data.state || '',
          district: data.district || '',
          village: data.village || '',
          pincode: data.pincode || '',
          farmerCategory: data.farmerCategory || '',
          landHolding: data.landHolding || '',
          cropType: data.cropType || '',
          annualIncome: data.annualIncome || '',
          aadhaarAvailable: data.aadhaarAvailable || false,
          bankAccountAvailable: data.bankAccountAvailable || false,
          soilHealthCardAvailable: data.soilHealthCardAvailable || false,
          kisanCreditCardAvailable: data.kisanCreditCardAvailable || false,
        });
      })
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  const handleChange = e => {
    const { name, type, checked, value } = e.target;
    setForm({...form, [name]: type === 'checkbox' ? checked : value});
  };

  const handleSave = async (e) => {
    e.preventDefault();
    setSaving(true);
    setError('');
    try {
      await farmerAPI.updateProfile({
        ...form,
        age: parseInt(form.age) || null,
        landHolding: parseFloat(form.landHolding) || null,
        annualIncome: parseFloat(form.annualIncome) || null,
      });
      setSuccess('✅ Profile updated successfully!');
      setTimeout(() => setSuccess(''), 3000);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to update profile.');
    } finally {
      setSaving(false);
    }
  };

  if (loading) return <div><Navbar /><div className="loading-container"><div className="spinner"></div></div></div>;

  return (
    <div>
      <Navbar />
      <div className="page-wrapper">
        <div className="page-header">
          <div className="container">
            <h1>👤 My Profile</h1>
            <p>Manage your farmer profile and account details</p>
          </div>
        </div>
        <div className="container" style={{padding:'2rem 1.5rem', maxWidth:'900px'}}>
          {success && <div className="alert alert-success">{success}</div>}
          {error && <div className="alert alert-error">⚠️ {error}</div>}

          {/* Profile Header */}
          <div className="card card-body mb-3" style={{display:'flex',alignItems:'center',gap:'1.5rem'}}>
            <div style={{width:80,height:80,background:'var(--primary)',color:'white',borderRadius:'50%',display:'flex',alignItems:'center',justifyContent:'center',fontSize:'2rem',fontWeight:700,flexShrink:0}}>
              {profile?.fullName?.[0] || 'F'}
            </div>
            <div>
              <h2 style={{fontSize:'1.3rem',fontWeight:700}}>{profile?.fullName}</h2>
              <p style={{color:'var(--gray-500)',fontSize:'0.9rem'}}>@{profile?.username} · {profile?.email}</p>
              <div style={{display:'flex',gap:'0.5rem',marginTop:'0.5rem'}}>
                <span className="badge badge-primary">{profile?.farmerCategory || 'Category not set'}</span>
                <span className="badge badge-success">Active Farmer</span>
              </div>
            </div>
          </div>

          {/* Tabs */}
          <div className="profile-tabs">
            <button className={`tab-btn ${activeTab==='personal'?'active':''}`} onClick={() => setActiveTab('personal')}>Personal Info</button>
            <button className={`tab-btn ${activeTab==='farm'?'active':''}`} onClick={() => setActiveTab('farm')}>Farm Details</button>
            <button className={`tab-btn ${activeTab==='documents'?'active':''}`} onClick={() => setActiveTab('documents')}>Documents</button>
          </div>

          <form onSubmit={handleSave} className="card card-body">
            {activeTab === 'personal' && (
              <div>
                <div className="form-row" style={{display:'grid',gridTemplateColumns:'1fr 1fr',gap:'1rem'}}>
                  <div className="form-group">
                    <label className="form-label">Age</label>
                    <input type="number" name="age" className="form-control" value={form.age} onChange={handleChange} placeholder="Your age" min="18" />
                  </div>
                  <div className="form-group">
                    <label className="form-label">State</label>
                    <select name="state" className="form-control form-select" value={form.state} onChange={handleChange}>
                      <option value="">Select State</option>
                      {STATES.map(s => <option key={s} value={s}>{s}</option>)}
                    </select>
                  </div>
                  <div className="form-group">
                    <label className="form-label">District</label>
                    <input type="text" name="district" className="form-control" value={form.district} onChange={handleChange} placeholder="Your district" />
                  </div>
                  <div className="form-group">
                    <label className="form-label">Village</label>
                    <input type="text" name="village" className="form-control" value={form.village} onChange={handleChange} placeholder="Village name" />
                  </div>
                  <div className="form-group">
                    <label className="form-label">PIN Code</label>
                    <input type="text" name="pincode" className="form-control" value={form.pincode} onChange={handleChange} placeholder="6-digit pincode" maxLength={6} />
                  </div>
                </div>
              </div>
            )}
            {activeTab === 'farm' && (
              <div>
                <div className="form-row" style={{display:'grid',gridTemplateColumns:'1fr 1fr',gap:'1rem'}}>
                  <div className="form-group">
                    <label className="form-label">Farmer Category</label>
                    <select name="farmerCategory" className="form-control form-select" value={form.farmerCategory} onChange={handleChange}>
                      <option value="">Select Category</option>
                      {CATEGORIES.map(c => <option key={c} value={c}>{c}</option>)}
                    </select>
                  </div>
                  <div className="form-group">
                    <label className="form-label">Land Holding (Acres)</label>
                    <input type="number" name="landHolding" className="form-control" value={form.landHolding} onChange={handleChange} placeholder="e.g., 2.5" step="0.1" min="0" />
                  </div>
                  <div className="form-group">
                    <label className="form-label">Crop Type</label>
                    <input type="text" name="cropType" className="form-control" value={form.cropType} onChange={handleChange} placeholder="e.g., Wheat, Rice, Cotton" />
                  </div>
                  <div className="form-group">
                    <label className="form-label">Annual Income (₹)</label>
                    <input type="number" name="annualIncome" className="form-control" value={form.annualIncome} onChange={handleChange} placeholder="Annual income" min="0" />
                  </div>
                </div>
              </div>
            )}
            {activeTab === 'documents' && (
              <div>
                <p style={{marginBottom:'1.25rem',color:'var(--gray-500)',fontSize:'0.9rem'}}>Mark which documents you have available to improve scheme recommendations.</p>
                <div style={{display:'flex',flexDirection:'column',gap:'0.75rem'}}>
                  {[
                    { name:'aadhaarAvailable', label:'Aadhaar Card', icon:'🪪' },
                    { name:'bankAccountAvailable', label:'Bank Account / Passbook', icon:'🏦' },
                    { name:'soilHealthCardAvailable', label:'Soil Health Card', icon:'🌱' },
                    { name:'kisanCreditCardAvailable', label:'Kisan Credit Card', icon:'💳' },
                  ].map(d => (
                    <label key={d.name} style={{display:'flex',alignItems:'center',gap:'0.75rem',padding:'0.85rem 1rem',border:'1.5px solid',borderColor:form[d.name]?'var(--primary)':'var(--gray-200)',borderRadius:'var(--radius)',cursor:'pointer',background:form[d.name]?'var(--primary-bg)':'white',transition:'all 0.2s'}}>
                      <input type="checkbox" name={d.name} checked={form[d.name]} onChange={handleChange} style={{width:18,height:18,accentColor:'var(--primary)'}} />
                      <span style={{fontSize:'1.5rem'}}>{d.icon}</span>
                      <span style={{fontWeight:'500'}}>{d.label}</span>
                      {form[d.name] && <span className="badge badge-success" style={{marginLeft:'auto'}}>Available ✓</span>}
                    </label>
                  ))}
                </div>
              </div>
            )}

            <div style={{marginTop:'1.5rem',paddingTop:'1rem',borderTop:'1px solid var(--gray-100)'}}>
              <button type="submit" className="btn btn-primary" disabled={saving}>
                {saving ? '⌛ Saving...' : '💾 Save Changes'}
              </button>
            </div>
          </form>
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default Profile;
