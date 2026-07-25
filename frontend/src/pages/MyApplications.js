import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import { applicationAPI } from '../services/api';

const MyApplications = () => {
  const [applications, setApplications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  useEffect(() => {
    applicationAPI.getMyApplications(page, 10)
      .then(res => {
        const d = res.data.data;
        setApplications(d?.content || []);
        setTotalPages(d?.totalPages || 0);
      })
      .catch(console.error)
      .finally(() => setLoading(false));
  }, [page]);

  const statusColors = {
    SUBMITTED: 'badge-info',
    UNDER_REVIEW: 'badge-warning',
    APPROVED: 'badge-success',
    REJECTED: 'badge-danger',
    DISBURSED: 'badge-primary',
    DRAFT: 'badge-gray',
    CANCELLED: 'badge-gray',
  };

  return (
    <div>
      <Navbar />
      <div className="page-wrapper">
        <div className="page-header">
          <div className="container">
            <h1>📋 My Applications</h1>
            <p>Track all your government scheme applications</p>
          </div>
        </div>
        <div className="container" style={{padding:'2rem 1.5rem'}}>
          {loading ? (
            <div className="loading-container"><div className="spinner"></div></div>
          ) : applications.length === 0 ? (
            <div className="card card-body text-center" style={{padding:'4rem'}}>
              <div style={{fontSize:'4rem', marginBottom:'1rem'}}>📭</div>
              <h3>No Applications Yet</h3>
              <p className="text-muted" style={{marginBottom:'1.5rem'}}>You haven't applied for any schemes yet.</p>
              <Link to="/schemes" className="btn btn-primary">Explore Schemes</Link>
            </div>
          ) : (
            <>
              <div style={{marginBottom:'1.5rem'}}>
                <p className="text-muted">Showing {applications.length} applications</p>
              </div>
              <div className="table-container">
                <table>
                  <thead>
                    <tr>
                      <th>Ref No.</th>
                      <th>Scheme Name</th>
                      <th>Status</th>
                      <th>Submitted</th>
                      <th>Remarks</th>
                      <th>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    {applications.map(app => (
                      <tr key={app.id}>
                        <td><code style={{fontSize:'0.8rem'}}>{app.applicationNumber}</code></td>
                        <td>
                          <div style={{fontWeight:'500'}}>{app.schemeName}</div>
                          <div style={{fontSize:'0.8rem',color:'var(--gray-400)'}}>{app.schemeCode}</div>
                        </td>
                        <td>
                          <span className={`badge ${statusColors[app.status] || 'badge-gray'}`}>
                            {app.status?.replace(/_/g,' ')}
                          </span>
                        </td>
                        <td style={{fontSize:'0.875rem'}}>
                          {app.submittedAt ? new Date(app.submittedAt).toLocaleDateString('en-IN') : '-'}
                        </td>
                        <td style={{fontSize:'0.8rem', maxWidth:'200px', color:'var(--gray-500)'}}>
                          {app.adminRemarks || app.remarks || '-'}
                        </td>
                        <td>
                          <Link to={`/my-applications/${app.id}`} className="btn btn-outline btn-sm">View</Link>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
              {totalPages > 1 && (
                <div className="pagination" style={{display:'flex',gap:'1rem',justifyContent:'center',marginTop:'2rem'}}>
                  <button className="btn btn-outline btn-sm" disabled={page === 0} onClick={() => setPage(p => p-1)}>← Prev</button>
                  <span style={{padding:'0.5rem',color:'var(--gray-500)',fontSize:'0.875rem'}}>Page {page+1}</span>
                  <button className="btn btn-outline btn-sm" disabled={page >= totalPages-1} onClick={() => setPage(p => p+1)}>Next →</button>
                </div>
              )}
            </>
          )}
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default MyApplications;
