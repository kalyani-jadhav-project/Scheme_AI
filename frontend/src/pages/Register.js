import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import './Auth.css';

const INDIAN_STATES = [
  'Andhra Pradesh','Arunachal Pradesh','Assam','Bihar','Chhattisgarh','Goa','Gujarat','Haryana',
  'Himachal Pradesh','Jharkhand','Karnataka','Kerala','Madhya Pradesh','Maharashtra','Manipur',
  'Meghalaya','Mizoram','Nagaland','Odisha','Punjab','Rajasthan','Sikkim','Tamil Nadu','Telangana',
  'Tripura','Uttar Pradesh','Uttarakhand','West Bengal','Delhi','Jammu & Kashmir','Ladakh'
];

const Register = () => {
  const [form, setForm] = useState({
    username: '', email: '', password: '', confirmPassword: '',
    fullName: '', phoneNumber: '', state: '', district: '',
  });
  const [errors, setErrors] = useState({});
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);
  const { register } = useAuth();
  const navigate = useNavigate();

  const handleChange = e => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setErrors({ ...errors, [e.target.name]: '' });
  };

  const validate = () => {
    const errs = {};
    if (!form.username || form.username.length < 3) errs.username = 'Username must be at least 3 characters';
    if (!form.email || !/\S+@\S+\.\S+/.test(form.email)) errs.email = 'Valid email required';
    if (!form.password || form.password.length < 6) errs.password = 'Password must be at least 6 characters';
    if (form.password !== form.confirmPassword) errs.confirmPassword = 'Passwords do not match';
    if (!form.fullName) errs.fullName = 'Full name is required';
    return errs;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const errs = validate();
    if (Object.keys(errs).length) { setErrors(errs); return; }
    setLoading(true);
    try {
      const { confirmPassword, ...submitData } = form;
      await register(submitData);
      setSuccess('🎉 Registration successful! Please login to continue.');
      setTimeout(() => navigate('/login'), 2000);
    } catch (err) {
      setErrors({ general: err.response?.data?.message || 'Registration failed. Please try again.' });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <Navbar />
      <div className="auth-page">
        <div className="auth-left">
          <div className="auth-branding">
            <div className="auth-logo">🌾</div>
            <h1>Join KrushiMitra AI</h1>
            <p>Register to unlock access to all government agricultural schemes and AI assistance.</p>
            <div className="auth-features">
              <div className="auth-feature">✅ Free registration</div>
              <div className="auth-feature">✅ Instant eligibility check</div>
              <div className="auth-feature">✅ Online scheme application</div>
              <div className="auth-feature">✅ Document management</div>
            </div>
          </div>
        </div>
        <div className="auth-right">
          <div className="auth-form-container">
            <div className="auth-form-header">
              <h2>Create Account</h2>
              <p>Fill in your details to get started</p>
            </div>

            {success && <div className="alert alert-success">{success}</div>}
            {errors.general && <div className="alert alert-error">⚠️ {errors.general}</div>}

            <form onSubmit={handleSubmit}>
              <div className="form-row">
                <div className="form-group">
                  <label className="form-label">Full Name *</label>
                  <input type="text" name="fullName" className="form-control"
                    placeholder="Ramesh Kumar" value={form.fullName} onChange={handleChange} />
                  {errors.fullName && <p className="form-error">{errors.fullName}</p>}
                </div>
                <div className="form-group">
                  <label className="form-label">Username *</label>
                  <input type="text" name="username" className="form-control"
                    placeholder="ramesh_k" value={form.username} onChange={handleChange} />
                  {errors.username && <p className="form-error">{errors.username}</p>}
                </div>
              </div>
              <div className="form-group">
                <label className="form-label">Email Address *</label>
                <input type="email" name="email" className="form-control"
                  placeholder="ramesh@example.com" value={form.email} onChange={handleChange} />
                {errors.email && <p className="form-error">{errors.email}</p>}
              </div>
              <div className="form-group">
                <label className="form-label">Phone Number</label>
                <input type="tel" name="phoneNumber" className="form-control"
                  placeholder="9876543210" value={form.phoneNumber} onChange={handleChange} />
              </div>
              <div className="form-row">
                <div className="form-group">
                  <label className="form-label">State</label>
                  <select name="state" className="form-control form-select" value={form.state} onChange={handleChange}>
                    <option value="">Select State</option>
                    {INDIAN_STATES.map(s => <option key={s} value={s}>{s}</option>)}
                  </select>
                </div>
                <div className="form-group">
                  <label className="form-label">District</label>
                  <input type="text" name="district" className="form-control"
                    placeholder="Your district" value={form.district} onChange={handleChange} />
                </div>
              </div>
              <div className="form-row">
                <div className="form-group">
                  <label className="form-label">Password *</label>
                  <input type="password" name="password" className="form-control"
                    placeholder="Min. 6 characters" value={form.password} onChange={handleChange} />
                  {errors.password && <p className="form-error">{errors.password}</p>}
                </div>
                <div className="form-group">
                  <label className="form-label">Confirm Password *</label>
                  <input type="password" name="confirmPassword" className="form-control"
                    placeholder="Repeat password" value={form.confirmPassword} onChange={handleChange} />
                  {errors.confirmPassword && <p className="form-error">{errors.confirmPassword}</p>}
                </div>
              </div>
              <button type="submit" className="btn btn-primary btn-full btn-lg" disabled={loading}>
                {loading ? '⌛ Creating Account...' : '🚀 Create Free Account'}
              </button>
            </form>

            <p className="auth-footer-text">
              Already have an account? <Link to="/login">Sign in</Link>
            </p>
          </div>
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default Register;
