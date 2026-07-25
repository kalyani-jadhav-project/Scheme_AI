import React, { useState, useEffect } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import { schemeAPI } from '../services/api';
import { useAuth } from '../context/AuthContext';
import './SchemeDetails.css';

const SchemeDetails = () => {
  const { id } = useParams();
  const [scheme, setScheme] = useState(null);
  const [loading, setLoading] = useState(true);
  const { user } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    schemeAPI.getSchemeById(id)
      .then(res => setScheme(res.data.data))
      .catch(console.error)
      .finally(() => setLoading(false));
  }, [id]);

  if (loading) return <div><Navbar /><div className="loading-container"><div className="spinner"></div></div></div>;
  if (!scheme) return <div><Navbar /><div className="container" style={{padding:'4rem 1.5rem', textAlign:'center'}}><h2>Scheme not found</h2><Link to="/schemes" className="btn btn-primary mt-2">Back to Schemes</Link></div></div>;

  return (
    <div>
      <Navbar />
      <div className="page-wrapper">
        <div className="page-header">
          <div className="container">
            <div style={{marginBottom:'0.5rem'}}>
              <span className="badge badge-success">{scheme.category?.replace(/_/g,' ')}</span>
              {scheme.centralScheme && <span className="badge badge-info" style={{marginLeft:'0.5rem'}}>Central Scheme</span>}
            </div>
            <h1>{scheme.name}</h1>
            {scheme.schemeCode && <p>Code: {scheme.schemeCode} | Ministry: {scheme.ministry}</p>}
          </div>
        </div>

        <div className="container" style={{padding:'2rem 1.5rem'}}>
          <div className="scheme-detail-grid">
            <div className="scheme-detail-main">
              {/* Overview */}
              <div className="card card-body mb-3">
                <h2>📋 Overview</h2>
                <p style={{lineHeight:'1.8', color:'var(--gray-600)', marginTop:'0.75rem'}}>{scheme.description}</p>
              </div>

              {/* Benefits */}
              <div className="card card-body mb-3">
                <h2>💰 Benefits</h2>
                <div className="detail-content" style={{marginTop:'0.75rem'}}>
                  {scheme.benefits?.split('\n').map((line, i) => line && <p key={i}>✓ {line}</p>)}
                </div>
                {scheme.financialAssistance && (
                  <div className="benefit-highlight">
                    <strong>Financial Assistance:</strong> {scheme.financialAssistance}
                  </div>
                )}
              </div>

              {/* Eligibility */}
              <div className="card card-body mb-3">
                <h2>✅ Eligibility Criteria</h2>
                <p style={{lineHeight:'1.8', color:'var(--gray-600)', marginTop:'0.75rem'}}>{scheme.eligibilityCriteria}</p>
              </div>

              {/* Required Documents */}
              <div className="card card-body mb-3">
                <h2>📄 Required Documents</h2>
                <div style={{marginTop:'0.75rem'}}>
                  {scheme.requiredDocuments?.split(',').map((doc, i) => (
                    <div key={i} className="document-item">
                      <span>📌</span> {doc.trim()}
                    </div>
                  ))}
                </div>
              </div>

              {/* How to Apply */}
              <div className="card card-body">
                <h2>🚀 How to Apply</h2>
                <p style={{lineHeight:'1.8', color:'var(--gray-600)', marginTop:'0.75rem'}}>{scheme.howToApply}</p>
              </div>
            </div>

            {/* Sidebar */}
            <div className="scheme-detail-sidebar">
              <div className="card card-body mb-2">
                <h3>Apply for this Scheme</h3>
                <p style={{fontSize:'0.875rem',color:'var(--gray-500)',margin:'0.5rem 0 1rem'}}>
                  Check your eligibility and apply online
                </p>
                {user ? (
                  <Link to={`/apply/${scheme.id}`} className="btn btn-primary btn-full">
                    🚀 Apply Now
                  </Link>
                ) : (
                  <>
                    <Link to="/login" className="btn btn-primary btn-full mb-2">Login to Apply</Link>
                    <Link to="/eligibility" className="btn btn-outline btn-full">Check Eligibility First</Link>
                  </>
                )}
              </div>

              <div className="card card-body mb-2">
                <h3>Scheme Info</h3>
                <div className="info-list">
                  <InfoItem label="Ministry" value={scheme.ministry} />
                  <InfoItem label="Scheme Type" value={scheme.schemeType} />
                  <InfoItem label="Beneficiary" value={scheme.beneficiaryType} />
                  {scheme.launchDate && <InfoItem label="Launch Date" value={new Date(scheme.launchDate).toLocaleDateString('en-IN', {year:'numeric',month:'long'})} />}
                  {scheme.applicationEndDate && <InfoItem label="Apply By" value={new Date(scheme.applicationEndDate).toLocaleDateString('en-IN')} />}
                </div>
              </div>

              {(scheme.officialWebsite || scheme.helplineNumber) && (
                <div className="card card-body">
                  <h3>Contact & Resources</h3>
                  {scheme.officialWebsite && (
                    <a href={scheme.officialWebsite} target="_blank" rel="noopener noreferrer" className="btn btn-outline btn-full mb-2">
                      🌐 Official Website
                    </a>
                  )}
                  {scheme.helplineNumber && (
                    <div className="helpline-info">
                      📞 Helpline: <strong>{scheme.helplineNumber}</strong>
                    </div>
                  )}
                </div>
              )}
            </div>
          </div>

          <div style={{marginTop:'1.5rem'}}>
            <Link to="/schemes" className="btn btn-outline">← Back to All Schemes</Link>
          </div>
        </div>
      </div>
      <Footer />
    </div>
  );
};

const InfoItem = ({ label, value }) => value ? (
  <div style={{display:'flex',justifyContent:'space-between',padding:'0.5rem 0',borderBottom:'1px solid var(--gray-100)'}}>
    <span style={{fontSize:'0.8rem',color:'var(--gray-500)'}}>{label}</span>
    <span style={{fontSize:'0.85rem',fontWeight:'500',color:'var(--gray-800)',textAlign:'right',maxWidth:'55%'}}>{value}</span>
  </div>
) : null;

export default SchemeDetails;
