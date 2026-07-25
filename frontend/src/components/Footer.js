import React from 'react';
import { Link } from 'react-router-dom';
import './Footer.css';

const Footer = () => {
  const year = new Date().getFullYear();
  return (
    <footer className="footer">
      <div className="container">
        <div className="footer-grid">
          <div className="footer-col">
            <h3>🌾 KrushiMitra AI</h3>
            <p>Empowering farmers with easy access to government schemes and AI-powered assistance.</p>
          </div>
          <div className="footer-col">
            <h4>Quick Links</h4>
            <ul>
              <li><Link to="/schemes">Government Schemes</Link></li>
              <li><Link to="/eligibility">Eligibility Checker</Link></li>
              <li><Link to="/about">About Us</Link></li>
              <li><Link to="/contact">Contact</Link></li>
            </ul>
          </div>
          <div className="footer-col">
            <h4>Support</h4>
            <ul>
              <li><Link to="/contact">Help Center</Link></li>
              <li><a href="tel:1800-180-1551">📞 1800-180-1551</a></li>
              <li><a href="mailto:support@krushimitra.gov.in">✉️ support@krushimitra.gov.in</a></li>
            </ul>
          </div>
          <div className="footer-col">
            <h4>Legal</h4>
            <ul>
              <li><a href="#privacy">Privacy Policy</a></li>
              <li><a href="#terms">Terms of Service</a></li>
            </ul>
          </div>
        </div>
        <div className="footer-bottom">
          <p>&copy; {year} KrushiMitra AI. All Rights Reserved. | Developed for Farmers of India 🇮🇳</p>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
