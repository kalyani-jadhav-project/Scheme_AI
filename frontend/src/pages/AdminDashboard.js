import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import { adminAPI, applicationAPI } from '../services/api';
import { schemeAPI } from '../services/api';
import './AdminDashboard.css';

const AdminDashboard = () => {
  const [stats, setStats] = useState(null);
  const [applications, setApplications] = useState([]);
  const [farmers, setFarmers] = useState([]);
  const [activeTab, setActiveTab] = useState('dashboard');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([
      adminAPI.getDashboard(),
      adminAPI.getAllApplications(0, 10),
      adminAPI.getAllFarmers(0, 10),
    ]).then(([statsRes, appsRes, farmersRes]) => {
      setStats(statsRes.data.data);
      setApplications(appsRes.data.data?.content || []);
      setFarmers(farmersRes.data.data?.content || []);
    }).catch(console.error)
    .finally(() => setLoading(false));
  }, []);

  const updateApplicationStatus = async (id, status) => {
    try {
      await applicationAPI.updateStatus(id, status, '');
      setApplications(apps => apps.map(a => a.id === id ? {...a, status} : a));
    } catch (err) {
      console.error(err);
    }
  };

  if (loading) return <div><Navbar /><div className="loading-container"><div className="spinner"></div></div></div>;

  return (
    <div>
      <Navbar />
      <div className="page-wrapper">
        <div className="admin-layout">
          {/* Admin Sidebar */}
          <aside className="sidebar">
            <div className="sidebar-logo">
              <div className="logo-text">⚙️ Admin Panel</div>
              <div className="logo-sub">KrushiMitra AI</div>
            </div>
            <nav className="sidebar-nav">
              {[
                { id: 'dashboard', label: '📊 Dashboard', icon: '' },
                { id: 'applications', label: '📋 Applications', icon: '' },
                { id: 'farmers', label: '👨‍🌾 Farmers', icon: '' },
                { id: 'schemes', label: '📝 Schemes', icon: '' },
              ].map(item => (
                <button key={item.id}
                  className={activeTab === item.id ? 'active' : ''}
                  onClick={() => setActiveTab(item.id)}
                  style={{background:'none',border:'none',width:'100%',textAlign:'left',cursor:'pointer',fontFamily:'inherit'}}>
                  {item.label}
                </button>
              ))}
            </nav>
          </aside>

          {/* Admin Main Content */}
          <main className="main-content">
            {activeTab === 'dashboard' && stats && (
              <>
                <h1 style={{marginBottom:'1.5rem',fontSize:'1.4rem',fontWeight:700}}>Admin Dashboard</h1>
                <div className="grid grid-4 mb-3">
                  {[
                    { icon:'👨‍🌾', label:'Total Farmers', val: stats.totalFarmers, color:'green' },
                    { icon:'📋', label:'Total Applications', val: stats.totalApplications, color:'blue' },
                    { icon:'📦', label:'Active Schemes', val: stats.activeSchemes, color:'orange' },
                    { icon:'✅', label:'Approved', val: stats.approvedApplications, color:'green' },
                  ].map((s, i) => (
                    <div key={i} className="stat-card">
                      <div className={`stat-icon ${s.color}`}>{s.icon}</div>
                      <div className="stat-content"><h3>{s.val}</h3><p>{s.label}</p></div>
                    </div>
                  ))}
                </div>

                {/* Apps by Status */}
                <div className="grid grid-2">
                  <div className="card card-body">
                    <h2 style={{marginBottom:'1rem',fontSize:'1rem',fontWeight:600}}>Applications by Status</h2>
                    {Object.entries(stats.applicationsByStatus || {}).map(([status, count]) => (
                      <div key={status} style={{display:'flex',justifyContent:'space-between',padding:'0.5rem 0',borderBottom:'1px solid var(--gray-100)'}}>
                        <span className={`badge status-${status}`}>{status.replace(/_/g,' ')}</span>
                        <strong>{count}</strong>
                      </div>
                    ))}
                  </div>
                  <div className="card card-body">
                    <h2 style={{marginBottom:'1rem',fontSize:'1rem',fontWeight:600}}>Applications by Scheme</h2>
                    {Object.entries(stats.applicationsByScheme || {}).map(([scheme, count]) => (
                      <div key={scheme} style={{display:'flex',justifyContent:'space-between',padding:'0.5rem 0',borderBottom:'1px solid var(--gray-100)',fontSize:'0.875rem'}}>
                        <span style={{maxWidth:'70%',color:'var(--gray-600)'}}>{scheme}</span>
                        <strong>{count}</strong>
                      </div>
                    ))}
                  </div>
                </div>
              </>
            )}

            {activeTab === 'applications' && (
              <>
                <h1 style={{marginBottom:'1.5rem',fontSize:'1.4rem',fontWeight:700}}>All Applications</h1>
                <div className="table-container">
                  <table>
                    <thead>
                      <tr>
                        <th>Ref No.</th>
                        <th>Farmer</th>
                        <th>Scheme</th>
                        <th>Status</th>
                        <th>Date</th>
                        <th>Actions</th>
                      </tr>
                    </thead>
                    <tbody>
                      {applications.map(app => (
                        <tr key={app.id}>
                          <td><code style={{fontSize:'0.75rem'}}>{app.applicationNumber}</code></td>
                          <td style={{fontWeight:'500'}}>{app.farmerName}</td>
                          <td>{app.schemeName}</td>
                          <td><span className={`badge status-${app.status}`}>{app.status?.replace(/_/g,' ')}</span></td>
                          <td style={{fontSize:'0.8rem'}}>{app.createdAt ? new Date(app.createdAt).toLocaleDateString('en-IN') : '-'}</td>
                          <td>
                            <div style={{display:'flex',gap:'0.4rem',flexWrap:'wrap'}}>
                              {app.status !== 'APPROVED' && (
                                <button className="btn btn-success btn-sm" onClick={() => updateApplicationStatus(app.id, 'APPROVED')}>✓ Approve</button>
                              )}
                              {app.status !== 'REJECTED' && (
                                <button className="btn btn-danger btn-sm" onClick={() => updateApplicationStatus(app.id, 'REJECTED')}>✗ Reject</button>
                              )}
                            </div>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </>
            )}

            {activeTab === 'farmers' && (
              <>
                <h1 style={{marginBottom:'1.5rem',fontSize:'1.4rem',fontWeight:700}}>Registered Farmers</h1>
                <div className="table-container">
                  <table>
                    <thead>
                      <tr>
                        <th>Name</th>
                        <th>Email</th>
                        <th>State</th>
                        <th>District</th>
                        <th>Category</th>
                        <th>Applications</th>
                        <th>Joined</th>
                      </tr>
                    </thead>
                    <tbody>
                      {farmers.map(f => (
                        <tr key={f.id}>
                          <td style={{fontWeight:'500'}}>{f.fullName}</td>
                          <td style={{fontSize:'0.8rem',color:'var(--gray-500)'}}>{f.email}</td>
                          <td>{f.state || '-'}</td>
                          <td>{f.district || '-'}</td>
                          <td>{f.farmerCategory ? <span className="badge badge-primary">{f.farmerCategory}</span> : '-'}</td>
                          <td>{f.totalApplications}</td>
                          <td style={{fontSize:'0.8rem'}}>{f.createdAt ? new Date(f.createdAt).toLocaleDateString('en-IN') : '-'}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </>
            )}

            {activeTab === 'schemes' && (
              <div>
                <div style={{display:'flex',justifyContent:'space-between',alignItems:'center',marginBottom:'1.5rem'}}>
                  <h1 style={{fontSize:'1.4rem',fontWeight:700}}>Manage Schemes</h1>
                  <Link to="/schemes" className="btn btn-outline btn-sm">View Public Schemes</Link>
                </div>
                <div className="alert alert-info">
                  Use the Schemes page to view all schemes. Admin can add/edit/delete schemes via API.
                </div>
              </div>
            )}
          </main>
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default AdminDashboard;
