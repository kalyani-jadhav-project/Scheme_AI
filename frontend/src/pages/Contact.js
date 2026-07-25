import React, { useState } from 'react';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';

const Contact = () => {
  const [form, setForm] = useState({ name:'', email:'', subject:'', message:'' });
  const [submitted, setSubmitted] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
    setSubmitted(true);
  };

  return (
    <div>
      <Navbar />
      <div className="page-wrapper">
        <div className="page-header">
          <div className="container">
            <h1>Contact Us</h1>
            <p>We're here to help farmers access their rightful benefits</p>
          </div>
        </div>
        <div className="container" style={{padding:'3rem 1.5rem'}}>
          <div className="contact-grid" style={{display:'grid',gridTemplateColumns:'1fr 1fr',gap:'2rem',maxWidth:'900px',margin:'0 auto'}}>
            {/* Contact Info */}
            <div>
              <h2 style={{marginBottom:'1.5rem'}}>Get in Touch</h2>
              {[
                { icon:'📞', label:'Helpline', val:'1800-180-1551 (Toll Free)', sub:'Mon-Sat, 9 AM - 6 PM' },
                { icon:'✉️', label:'Email', val:'support@krushimitra.gov.in', sub:'We respond within 24 hours' },
                { icon:'🏢', label:'Office', val:'Ministry of Agriculture', sub:'New Delhi - 110001' },
              ].map((info, i) => (
                <div key={i} style={{display:'flex',gap:'1rem',marginBottom:'1.5rem'}}>
                  <div style={{width:48,height:48,background:'var(--primary-bg)',borderRadius:'var(--radius)',display:'flex',alignItems:'center',justifyContent:'center',fontSize:'1.4rem',flexShrink:0}}>{info.icon}</div>
                  <div>
                    <p style={{fontSize:'0.75rem',color:'var(--gray-400)',marginBottom:'0.1rem'}}>{info.label}</p>
                    <strong style={{color:'var(--gray-800)'}}>{info.val}</strong>
                    <p style={{fontSize:'0.8rem',color:'var(--gray-500)'}}>{info.sub}</p>
                  </div>
                </div>
              ))}
            </div>

            {/* Contact Form */}
            <div className="card card-body">
              {submitted ? (
                <div className="text-center" style={{padding:'2rem'}}>
                  <div style={{fontSize:'4rem',marginBottom:'1rem'}}>✅</div>
                  <h3>Message Sent!</h3>
                  <p className="text-muted" style={{marginTop:'0.5rem'}}>We'll get back to you within 24 hours.</p>
                </div>
              ) : (
                <form onSubmit={handleSubmit}>
                  <div className="form-group">
                    <label className="form-label">Your Name *</label>
                    <input type="text" className="form-control" value={form.name} onChange={e=>setForm({...form,name:e.target.value})} placeholder="Full Name" required />
                  </div>
                  <div className="form-group">
                    <label className="form-label">Email Address *</label>
                    <input type="email" className="form-control" value={form.email} onChange={e=>setForm({...form,email:e.target.value})} placeholder="your@email.com" required />
                  </div>
                  <div className="form-group">
                    <label className="form-label">Subject</label>
                    <input type="text" className="form-control" value={form.subject} onChange={e=>setForm({...form,subject:e.target.value})} placeholder="How can we help?" />
                  </div>
                  <div className="form-group">
                    <label className="form-label">Message *</label>
                    <textarea className="form-control" rows={4} value={form.message} onChange={e=>setForm({...form,message:e.target.value})} placeholder="Your message..." required style={{resize:'vertical'}} />
                  </div>
                  <button type="submit" className="btn btn-primary btn-full">📨 Send Message</button>
                </form>
              )}
            </div>
          </div>
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default Contact;
