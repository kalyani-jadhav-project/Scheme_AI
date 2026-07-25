import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import { dashboardAPI } from '../services/api';
import './Dashboard.css';

const Dashboard = () => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [activeTab, setActiveTab] = useState('overview');

  useEffect(() => {
    dashboardAPI.getFarmerDashboard()
      .then(res => setData(res.data.data))
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  if (loading) return (
    <div><Navbar /><div className="loading-container"><div className="spinner"></div><p>Loading your dashboard...</p></div></div>
  );

  const p = data?.farmerProfile || {};

  return (
    <div>
      <Navbar />
      <div className="page-wrapper">
        <div className="dashboard-layout">
          {/* Sidebar */}
          <aside className="sidebar">
            <div className="sidebar-logo">
              <div className="logo-text">🌾 KrushiMitra AI</div>
              <div className="logo-sub">Farmer Portal</div>
            </div>
            <nav className="sidebar-nav">
              <Link to="/dashboard" className={activeTab === 'overview' ? 'active' : ''} onClick={() => setActiveTab('overview')}>📊 Dashboard</Link>
              <Link to="/schemes">📋 Schemes</Link>
              <Link to="/eligibility">✅ Eligibility</Link>
              <Link to="/my-applications">📄 My Applications</Link>
              <Link to="/profile">👤 Profile</Link>
            </nav>
          </aside>

          {/* Main Content */}
          <main className="main-content">
            {/* Welcome Banner */}
            <div className="welcome-banner">
              <div>
                <h1>Welcome back, {p.fullName?.split(' ')[0] || 'Farmer'}! 👋</h1>
                <p>Here's an overview of your farming assistance dashboard</p>
              </div>
              <div className="welcome-meta">
                <span>📍 {p.district || 'District'}, {p.state || 'State'}</span>
                <span>🌾 {p.cropType || 'Crops not set'}</span>
              </div>
            </div>

            {/* Stats Grid */}
            <div className="grid grid-4 mb-3">
              <div className="stat-card">
                <div className="stat-icon green">📋</div>
                <div className="stat-content">
                  <h3>{data?.totalApplications || 0}</h3>
                  <p>Total Applications</p>
                </div>
              </div>
              <div className="stat-card">
                <div className="stat-icon blue">⏳</div>
                <div className="stat-content">
                  <h3>{data?.pendingApplications || 0}</h3>
                  <p>Pending Review</p>
                </div>
              </div>
              <div className="stat-card">
                <div className="stat-icon green">✅</div>
                <div className="stat-content">
                  <h3>{data?.approvedApplications || 0}</h3>
                  <p>Approved</p>
                </div>
              </div>
              <div className="stat-card">
                <div className="stat-icon orange">📦</div>
                <div className="stat-content">
                  <h3>{data?.totalSchemes || 0}</h3>
                  <p>Available Schemes</p>
                </div>
              </div>
            </div>

            {/* Main Grid */}
            <div className="dashboard-main-grid">
              {/* Recommended Schemes */}
              <div className="card card-body">
                <div className="section-header-inline">
                  <h2>🎯 Recommended Schemes</h2>
                  <Link to="/schemes" className="btn btn-outline btn-sm">View All</Link>
                </div>
                <div className="scheme-list">
                  {(data?.recommendedSchemes || []).slice(0, 4).map(scheme => (
                    <div key={scheme.id} className="scheme-list-item">
                      <div className="scheme-list-info">
                        <strong>{scheme.name}</strong>
                        <span>{scheme.financialAssistance}</span>
                      </div>
                      <Link to={`/schemes/${scheme.id}`} className="btn btn-primary btn-sm">Apply</Link>
                    </div>
                  ))}
                  {(!data?.recommendedSchemes?.length) && (
                    <p className="text-muted">Complete your profile to see recommendations.</p>
                  )}
                </div>
              </div>

              {/* Weather & Notifications */}
              <div>
                {/* Weather Widget */}
                <div className="card card-body mb-2">
                  <h2>🌤️ Weather Update</h2>
                  <div className="weather-widget">
                    <div className="weather-temp">28°C</div>
                    <div>
                      <p className="weather-condition">{data?.weatherInfo?.condition || 'Partly Cloudy'}</p>
                      <p className="weather-location">📍 {p.district}, {p.state}</p>
                      <p className="weather-rain" style={{color:'var(--info)',fontSize:'0.8rem'}}>{data?.weatherInfo?.rainfall}</p>
                    </div>
                  </div>
                </div>
                {/* Notifications */}
                <div className="card card-body">
                  <h2>🔔 Notifications</h2>
                  {(data?.recentNotifications || []).slice(0, 3).map(n => (
                    <div key={n.id} className={`notification-item ${!n.read ? 'unread' : ''}`}>
                      {!n.read && <div className="notification-dot"></div>}
                      <div>
                        <strong style={{fontSize:'0.875rem'}}>{n.title}</strong>
                        <p style={{fontSize:'0.8rem',color:'var(--gray-500)',margin:'0.1rem 0 0'}}>{n.message}</p>
                      </div>
                    </div>
                  ))}
                  {(!data?.recentNotifications?.length) && (
                    <p className="text-muted" style={{fontSize:'0.875rem'}}>No new notifications.</p>
                  )}
                </div>
              </div>
            </div>

            {/* Recent Applications */}
            <div className="card card-body mt-3">
              <div className="section-header-inline">
                <h2>📋 Recent Applications</h2>
                <Link to="/my-applications" className="btn btn-outline btn-sm">View All</Link>
              </div>
              {(data?.recentApplications || []).length > 0 ? (
                <div className="table-container">
                  <table>
                    <thead>
                      <tr>
                        <th>Application No.</th>
                        <th>Scheme</th>
                        <th>Status</th>
                        <th>Date</th>
                      </tr>
                    </thead>
                    <tbody>
                      {(data?.recentApplications || []).map(app => (
                        <tr key={app.id}>
                          <td><code>{app.applicationNumber}</code></td>
                          <td>{app.schemeName}</td>
                          <td><span className={`badge status-${app.status}`}>{app.status?.replace(/_/g,' ')}</span></td>
                          <td>{app.createdAt ? new Date(app.createdAt).toLocaleDateString() : '-'}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              ) : (
                <div className="empty-state">
                  <p>📭 No applications yet.</p>
                  <Link to="/schemes" className="btn btn-primary btn-sm">Explore Schemes</Link>
                </div>
              )}
            </div>

            {/* Farmer Profile Card */}
            <div className="card card-body mt-3">
              <div className="section-header-inline">
                <h2>👤 Your Profile</h2>
                <Link to="/profile" className="btn btn-outline btn-sm">Edit Profile</Link>
              </div>
              <div className="profile-info-grid">
                <ProfileItem label="Name" value={p.fullName} />
                <ProfileItem label="State" value={p.state} />
                <ProfileItem label="District" value={p.district} />
                <ProfileItem label="Land Holding" value={p.landHolding ? `${p.landHolding} acres` : null} />
                <ProfileItem label="Crop Type" value={p.cropType} />
                <ProfileItem label="Farmer Category" value={p.farmerCategory} />
                <ProfileItem label="Aadhaar Available" value={p.aadhaarAvailable ? '✅ Yes' : '❌ No'} />
                <ProfileItem label="Bank Account" value={p.bankAccountAvailable ? '✅ Yes' : '❌ No'} />
              </div>
            </div>
          </main>
        </div>
      </div>
      <Footer />
    </div>
  );
};

const ProfileItem = ({ label, value }) => (
  <div className="profile-info-item">
    <span className="profile-info-label">{label}</span>
    <span className="profile-info-value">{value || <em className="text-muted">Not set</em>}</span>
  </div>
);

export default Dashboard;
