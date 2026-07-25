import React from 'react';
import { Link } from 'react-router-dom';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';

const NotFound = () => (
  <div>
    <Navbar />
    <div style={{
      minHeight: 'calc(100vh - 70px)',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      flexDirection: 'column',
      textAlign: 'center',
      padding: '2rem',
      marginTop: '70px',
    }}>
      <div style={{fontSize:'8rem', marginBottom:'1rem'}}>🌾</div>
      <h1 style={{fontSize:'5rem', fontWeight:900, color:'var(--primary)', lineHeight:1}}>404</h1>
      <h2 style={{fontSize:'1.5rem', color:'var(--gray-700)', marginBottom:'0.75rem'}}>Page Not Found</h2>
      <p style={{color:'var(--gray-500)', marginBottom:'2rem', maxWidth:'400px'}}>
        The page you're looking for seems to have gone into the fields. Let's get you back on track!
      </p>
      <div style={{display:'flex', gap:'1rem'}}>
        <Link to="/" className="btn btn-primary btn-lg">🏠 Go Home</Link>
        <Link to="/schemes" className="btn btn-outline btn-lg">📋 Browse Schemes</Link>
      </div>
    </div>
    <Footer />
  </div>
);

export default NotFound;
