import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import './Auth.css';

const Login = () => {
  const [form, setForm] = useState({ usernameOrEmail: '', password: '' });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { login, isAdmin } = useAuth();
  const navigate = useNavigate();

  const handleChange = e => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      const userData = await login(form);
      const roles = userData.roles || [];
      if (roles.some(r => r === 'ROLE_ADMIN' || r === 'ROLE_SUPER_ADMIN')) {
        navigate('/admin');
      } else {
        navigate('/dashboard');
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Invalid credentials. Please try again.');
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
            <h1>Welcome Back!</h1>
            <p>Login to access your personalized scheme recommendations and manage your applications.</p>
            <div className="auth-features">
              <div className="auth-feature">✅ Check scheme eligibility</div>
              <div className="auth-feature">✅ Apply for government schemes</div>
              <div className="auth-feature">✅ Track application status</div>
              <div className="auth-feature">✅ AI-powered assistance</div>
            </div>
          </div>
        </div>
        <div className="auth-right">
          <div className="auth-form-container">
            <div className="auth-form-header">
              <h2>Sign In</h2>
              <p>Enter your credentials to continue</p>
            </div>

            {error && <div className="alert alert-error">⚠️ {error}</div>}

            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label className="form-label">Username or Email</label>
                <input
                  type="text"
                  name="usernameOrEmail"
                  className="form-control"
                  placeholder="Enter username or email"
                  value={form.usernameOrEmail}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="form-group">
                <label className="form-label">Password</label>
                <input
                  type="password"
                  name="password"
                  className="form-control"
                  placeholder="Enter your password"
                  value={form.password}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="form-forgot">
                <Link to="/forgot-password">Forgot password?</Link>
              </div>
              <button type="submit" className="btn btn-primary btn-full btn-lg" disabled={loading}>
                {loading ? '⌛ Signing in...' : '🔐 Sign In'}
              </button>
            </form>

            <div className="auth-divider"><span>Demo Credentials</span></div>
            <div className="demo-creds">
              <div className="demo-cred-item">
                <strong>Admin:</strong> admin / Admin@123
              </div>
              <div className="demo-cred-item">
                <strong>Farmer:</strong> Register a new account
              </div>
            </div>

            <p className="auth-footer-text">
              Don't have an account? <Link to="/register">Create one free</Link>
            </p>
          </div>
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default Login;
