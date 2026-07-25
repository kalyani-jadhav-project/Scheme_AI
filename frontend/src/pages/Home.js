import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import { schemeAPI } from '../services/api';
import heroFarmerImage from '../assets/images/hero-farmer-schemes.png';
import './Home.css';

const Home = () => {
  const [schemes, setSchemes] = useState([]);

  useEffect(() => {
    schemeAPI.getPublicSchemes(0, 6)
      .then(res => setSchemes(res.data.data?.content || []))
      .catch(() => {});
  }, []);

  const stats = [
    { number: '7+', label: 'Government Schemes', icon: '📋' },
    { number: '1Cr+', label: 'Beneficiary Farmers', icon: '👨‍🌾' },
    { number: '₹6000', label: 'Annual PM-KISAN Benefit', icon: '💰' },
    { number: '28', label: 'States Covered', icon: '🗺️' },
  ];

  const features = [
    { icon: '🤖', title: 'AI Assistant', desc: 'Get instant help from IBM Watson AI to find schemes suited for you.' },
    { icon: '✅', title: 'Eligibility Checker', desc: 'Check your eligibility for all government schemes in one place.' },
    { icon: '📄', title: 'Easy Application', desc: 'Apply for schemes online with document upload support.' },
    { icon: '🔔', title: 'Status Tracking', desc: 'Track your application status and receive real-time notifications.' },
    { icon: '📱', title: 'Mobile Friendly', desc: 'Access the portal from any device, anywhere, anytime.' },
    { icon: '🌐', title: 'Multi-Language', desc: 'Platform supports multiple regional languages for easy access.' },
  ];

  const testimonials = [
    { name: 'Ramesh Patil', state: 'Maharashtra', text: 'KrushiMitra AI helped me find PM-KISAN scheme and apply in just 10 minutes!', role: 'Small Farmer' },
    { name: 'Sita Devi', state: 'Bihar', text: 'The eligibility checker showed me I qualify for 4 schemes. Very helpful!', role: 'Marginal Farmer' },
    { name: 'Gurpreet Singh', state: 'Punjab', text: 'Got my Kisan Credit Card approved through this portal. Excellent service.', role: 'Medium Farmer' },
  ];

  const faqs = [
    { q: 'Who can use KrushiMitra AI?', a: 'Any farmer in India can register and use the portal to explore government schemes and check eligibility.' },
    { q: 'Is it free to use?', a: 'Yes, the platform is completely free for all farmers. No hidden charges.' },
    { q: 'How does the eligibility checker work?', a: 'Enter your basic details like land holding, income, and state. Our AI will instantly tell you which schemes you qualify for.' },
    { q: 'Can I apply for schemes directly here?', a: 'Yes, you can apply for schemes, upload documents, and track status all from this portal.' },
    { q: 'Is my data safe?', a: 'Your data is encrypted and secured. We follow government data protection guidelines.' },
  ];

  return (
    <div>
      <Navbar />

      {/* Hero Section */}
      <section className="hero">
        <div className="hero-bg-pattern"></div>
        <div className="container hero-content">
          <div className="hero-text">
            <span className="hero-badge">🇮🇳 Government of India Initiative</span>
            <h1>KrushiMitra AI</h1>
            <h2>Your Intelligent Farming Assistant</h2>
            <p>Discover government schemes you qualify for, apply online, and grow your income. Powered by IBM Watson AI.</p>
            <div className="hero-buttons">
              <Link to="/register" className="btn btn-white btn-lg">Get Started Free</Link>
              <Link to="/eligibility" className="btn btn-outline-white btn-lg">Check Eligibility</Link>
            </div>
            <div className="hero-trust">
              <span>✓ 1 Crore+ Farmers Benefited</span>
              <span>✓ 7+ Active Schemes</span>
              <span>✓ Free to Use</span>
            </div>
          </div>
          <div className="hero-visual">
            <img
              src={heroFarmerImage}
              alt="Farmer checking government schemes on mobile app"
              className="hero-image"
            />
          </div>
        </div>
      </section>

      {/* Stats Section */}
      <section className="stats-section">
        <div className="container">
          <div className="stats-grid">
            {stats.map((s, i) => (
              <div key={i} className="stat-item">
                <span className="stat-emoji">{s.icon}</span>
                <strong>{s.number}</strong>
                <span>{s.label}</span>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="section features-section">
        <div className="container">
          <div className="section-header text-center">
            <h2 className="section-title">Everything Farmers Need</h2>
            <p className="section-subtitle">A complete platform to discover, apply, and benefit from government agricultural schemes.</p>
          </div>
          <div className="grid grid-3">
            {features.map((f, i) => (
              <div key={i} className="feature-card">
                <div className="feature-icon">{f.icon}</div>
                <h3>{f.title}</h3>
                <p>{f.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Schemes Preview */}
      <section className="section schemes-preview">
        <div className="container">
          <div className="section-header" style={{display:'flex', justifyContent:'space-between', alignItems:'flex-end', flexWrap:'wrap', gap:'1rem'}}>
            <div>
              <h2 className="section-title">Popular Government Schemes</h2>
              <p className="section-subtitle">Latest agricultural welfare schemes available for farmers.</p>
            </div>
            <Link to="/schemes" className="btn btn-outline">View All Schemes →</Link>
          </div>
          <div className="grid grid-3">
            {schemes.slice(0, 6).map(scheme => (
              <div key={scheme.id} className="scheme-card">
                <div className="scheme-card-header">
                  <span className="scheme-card-badge">{scheme.category?.replace(/_/g,' ')}</span>
                  <div className="scheme-card-title">{scheme.name}</div>
                </div>
                <div className="scheme-card-body">
                  <p className="scheme-card-desc">{scheme.description}</p>
                  <div style={{display:'flex', gap:'0.5rem', flexWrap:'wrap'}}>
                    <span className="badge badge-primary">💰 {scheme.financialAssistance}</span>
                  </div>
                </div>
                <div className="scheme-card-footer">
                  <span className="badge badge-success">Active</span>
                  <Link to={`/schemes/${scheme.id}`} className="btn btn-primary btn-sm">Learn More</Link>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* How It Works */}
      <section className="section how-it-works">
        <div className="container">
          <div className="section-header text-center">
            <h2 className="section-title">How It Works</h2>
            <p className="section-subtitle">Three simple steps to access government benefits.</p>
          </div>
          <div className="steps-grid">
            <div className="step"><div className="step-num">1</div><h3>Register & Profile</h3><p>Create your free account and complete your farmer profile with land and crop details.</p></div>
            <div className="step-arrow">→</div>
            <div className="step"><div className="step-num">2</div><h3>Check Eligibility</h3><p>Our AI instantly checks which government schemes you're eligible for based on your profile.</p></div>
            <div className="step-arrow">→</div>
            <div className="step"><div className="step-num">3</div><h3>Apply & Get Benefits</h3><p>Apply online, upload documents, and receive government benefits directly to your bank account.</p></div>
          </div>
        </div>
      </section>

      {/* Testimonials */}
      <section className="section testimonials-section">
        <div className="container">
          <div className="section-header text-center">
            <h2 className="section-title">What Farmers Say</h2>
          </div>
          <div className="grid grid-3">
            {testimonials.map((t, i) => (
              <div key={i} className="testimonial-card">
                <div className="testimonial-stars">★★★★★</div>
                <p>"{t.text}"</p>
                <div className="testimonial-author">
                  <div className="author-avatar">{t.name[0]}</div>
                  <div>
                    <strong>{t.name}</strong>
                    <span>{t.role} — {t.state}</span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* FAQ Section */}
      <section className="section faq-section">
        <div className="container" style={{maxWidth: '750px'}}>
          <div className="section-header text-center">
            <h2 className="section-title">Frequently Asked Questions</h2>
          </div>
          <div className="faq-list">
            {faqs.map((faq, i) => (
              <FAQItem key={i} question={faq.q} answer={faq.a} />
            ))}
          </div>
        </div>
      </section>

      {/* CTA Banner */}
      <section className="cta-section">
        <div className="container text-center">
          <h2>Ready to Access Your Government Benefits?</h2>
          <p>Join over 1 crore farmers already benefiting from government schemes.</p>
          <div style={{display:'flex', gap:'1rem', justifyContent:'center', flexWrap:'wrap'}}>
            <Link to="/register" className="btn btn-white btn-lg">Start for Free</Link>
            <Link to="/eligibility" className="btn btn-outline-white btn-lg">Check Eligibility</Link>
          </div>
        </div>
      </section>

      <Footer />
    </div>
  );
};

const FAQItem = ({ question, answer }) => {
  const [open, setOpen] = useState(false);
  return (
    <div className={`faq-item ${open ? 'open' : ''}`} onClick={() => setOpen(!open)}>
      <div className="faq-question">
        <span>{question}</span>
        <span className="faq-chevron">{open ? '▲' : '▼'}</span>
      </div>
      {open && <div className="faq-answer">{answer}</div>}
    </div>
  );
};

export default Home;