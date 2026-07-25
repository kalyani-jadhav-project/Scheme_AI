import React from 'react';
import { Link } from 'react-router-dom';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';

const About = () => (
  <div>
    <Navbar />
    <div className="page-wrapper">
      <div className="page-header">
        <div className="container">
          <h1>About KrushiMitra AI</h1>
          <p>Empowering India's farmers with technology and information</p>
        </div>
      </div>
      <div className="container" style={{padding:'3rem 1.5rem'}}>
        <div style={{maxWidth:'800px', margin:'0 auto'}}>
          <div className="card card-body mb-3">
            <h2>Our Mission 🌾</h2>
            <p style={{marginTop:'1rem', lineHeight:'1.8', color:'var(--gray-600)'}}>
              KrushiMitra AI is a production-ready digital platform designed to bridge the gap between India's farming community and the numerous government welfare schemes available to them. Our mission is to ensure every eligible farmer receives the financial support and agricultural benefits they deserve.
            </p>
          </div>
          <div className="grid grid-3 mb-3">
            {[
              { icon:'🤖', title:'AI-Powered', desc:'IBM Watson AI chatbot provides personalized guidance in multiple languages.' },
              { icon:'🔍', title:'Smart Matching', desc:'Intelligent eligibility engine matches you with the right schemes instantly.' },
              { icon:'📱', title:'Easy Access', desc:'Mobile-responsive platform accessible from any device, anywhere in India.' },
            ].map((f, i) => (
              <div key={i} className="card card-body text-center">
                <div style={{fontSize:'2.5rem', marginBottom:'0.75rem'}}>{f.icon}</div>
                <h3 style={{marginBottom:'0.5rem'}}>{f.title}</h3>
                <p style={{fontSize:'0.875rem', color:'var(--gray-500)'}}>{f.desc}</p>
              </div>
            ))}
          </div>
          <div className="card card-body mb-3">
            <h2>Key Statistics</h2>
            <div className="grid grid-4 mt-2">
              {[
                { n:'7+', l:'Active Schemes' },
                { n:'1Cr+', l:'Farmer Beneficiaries' },
                { n:'28', l:'States Covered' },
                { n:'₹6000', l:'Annual PM-KISAN' },
              ].map((s, i) => (
                <div key={i} style={{textAlign:'center', padding:'1rem'}}>
                  <div style={{fontSize:'1.8rem',fontWeight:800,color:'var(--primary)'}}>{s.n}</div>
                  <div style={{fontSize:'0.8rem',color:'var(--gray-500)'}}>{s.l}</div>
                </div>
              ))}
            </div>
          </div>
          <div className="text-center mt-3">
            <Link to="/register" className="btn btn-primary btn-lg">Join Today — It's Free</Link>
          </div>
        </div>
      </div>
    </div>
    <Footer />
  </div>
);

export default About;
