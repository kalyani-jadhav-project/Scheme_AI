import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './Navbar.css';

const Navbar = () => {
  const { user, logout, isAdmin } = useAuth();
  const navigate = useNavigate();
  const [menuOpen, setMenuOpen] = useState(false);
  const [dropdownOpen, setDropdownOpen] = useState(false);

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <nav className="navbar">
      <div className="container navbar-container">
        <Link to="/" className="navbar-brand">
          <span className="brand-icon">🌾</span>
          <div>
            <span className="brand-name">KrushiMitra AI</span>
            <span className="brand-tag">Farmer Welfare Portal</span>
          </div>
        </Link>

        <button className="hamburger" onClick={() => setMenuOpen(!menuOpen)} aria-label="Menu">
          <span /><span /><span />
        </button>

        <div className={`navbar-links ${menuOpen ? 'open' : ''}`}>
          <Link to="/" onClick={() => setMenuOpen(false)}>Home</Link>
          <Link to="/schemes" onClick={() => setMenuOpen(false)}>Schemes</Link>
          <Link to="/eligibility" onClick={() => setMenuOpen(false)}>Eligibility</Link>
          <Link to="/about" onClick={() => setMenuOpen(false)}>About</Link>
          <Link to="/contact" onClick={() => setMenuOpen(false)}>Contact</Link>
        </div>

        <div className="navbar-actions">
          {user ? (
            <div className="user-menu" onMouseLeave={() => setDropdownOpen(false)}>
              <button
                className="user-btn"
                onClick={() => setDropdownOpen(!dropdownOpen)}
              >
                <span className="user-avatar">{user.fullName?.[0]?.toUpperCase() || 'U'}</span>
                <span className="user-name">{user.fullName?.split(' ')[0]}</span>
                <span className="chevron">▾</span>
              </button>
              {dropdownOpen && (
                <div className="user-dropdown">
                  {isAdmin() ? (
                    <Link to="/admin" onClick={() => setDropdownOpen(false)}>⚙️ Admin Panel</Link>
                  ) : (
                    <Link to="/dashboard" onClick={() => setDropdownOpen(false)}>📊 Dashboard</Link>
                  )}
                  <Link to="/profile" onClick={() => setDropdownOpen(false)}>👤 Profile</Link>
                  {!isAdmin() && (
                    <Link to="/my-applications" onClick={() => setDropdownOpen(false)}>📋 My Applications</Link>
                  )}
                  <hr />
                  <button onClick={handleLogout}>🚪 Logout</button>
                </div>
              )}
            </div>
          ) : (
            <div className="auth-buttons">
              <Link to="/login" className="btn btn-outline btn-sm">Login</Link>
              <Link to="/register" className="btn btn-primary btn-sm">Register</Link>
            </div>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
