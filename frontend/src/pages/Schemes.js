import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import { schemeAPI } from '../services/api';
import './Schemes.css';

const CATEGORIES = [
  { value: '', label: 'All Categories' },
  { value: 'INCOME_SUPPORT', label: 'Income Support' },
  { value: 'CROP_INSURANCE', label: 'Crop Insurance' },
  { value: 'CREDIT', label: 'Credit' },
  { value: 'IRRIGATION', label: 'Irrigation' },
  { value: 'MARKET_ACCESS', label: 'Market Access' },
  { value: 'SOIL_HEALTH', label: 'Soil Health' },
  { value: 'TECHNOLOGY', label: 'Technology' },
  { value: 'ORGANIC_FARMING', label: 'Organic Farming' },
];

const Schemes = () => {
  const [schemes, setSchemes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [category, setCategory] = useState('');
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  const fetchSchemes = () => {
    setLoading(true);
    const promise = search
      ? schemeAPI.searchSchemes(search, page)
      : schemeAPI.getAllSchemes(page, 9);

    promise
      .then(res => {
        const d = res.data.data;
        setSchemes(d?.content || []);
        setTotalPages(d?.totalPages || 0);
      })
      .catch(console.error)
      .finally(() => setLoading(false));
  };

  useEffect(() => { fetchSchemes(); }, [page, search]);

  const handleSearch = (e) => {
    e.preventDefault();
    setPage(0);
    fetchSchemes();
  };

  const filtered = category
    ? schemes.filter(s => s.category === category)
    : schemes;

  return (
    <div>
      <Navbar />
      <div className="page-wrapper">
        <div className="page-header">
          <div className="container">
            <h1>Government Agricultural Schemes</h1>
            <p>Discover all available government schemes for farmers across India</p>
          </div>
        </div>

        <div className="container" style={{padding: '2rem 1.5rem'}}>
          {/* Search and Filter Bar */}
          <div className="schemes-toolbar">
            <form className="search-form" onSubmit={handleSearch}>
              <input
                type="text"
                className="form-control search-input"
                placeholder="🔍 Search schemes by name, ministry..."
                value={search}
                onChange={e => setSearch(e.target.value)}
              />
              <button type="submit" className="btn btn-primary">Search</button>
              {search && (
                <button type="button" className="btn btn-outline" onClick={() => { setSearch(''); setPage(0); }}>
                  Clear
                </button>
              )}
            </form>
            <select
              className="form-control form-select category-filter"
              value={category}
              onChange={e => setCategory(e.target.value)}
            >
              {CATEGORIES.map(c => <option key={c.value} value={c.value}>{c.label}</option>)}
            </select>
          </div>

          {/* Results Count */}
          {!loading && (
            <p className="results-count">
              Showing {filtered.length} scheme{filtered.length !== 1 ? 's' : ''}
              {search ? ` for "${search}"` : ''}
            </p>
          )}

          {/* Schemes Grid */}
          {loading ? (
            <div className="loading-container"><div className="spinner"></div></div>
          ) : (
            <>
              <div className="grid grid-3">
                {filtered.map(scheme => (
                  <div key={scheme.id} className="scheme-card">
                    <div className="scheme-card-header">
                      <span className="scheme-card-badge">{scheme.category?.replace(/_/g,' ') || 'General'}</span>
                      <div className="scheme-card-title">{scheme.name}</div>
                      {scheme.schemeCode && (
                        <code style={{fontSize:'0.7rem', opacity:0.7}}>{scheme.schemeCode}</code>
                      )}
                    </div>
                    <div className="scheme-card-body">
                      <p className="scheme-card-desc">{scheme.description}</p>
                      <div className="scheme-tags">
                        {scheme.financialAssistance && (
                          <span className="badge badge-primary">💰 {scheme.financialAssistance.substring(0,30)}</span>
                        )}
                        {scheme.centralScheme && (
                          <span className="badge badge-info">Central Scheme</span>
                        )}
                      </div>
                    </div>
                    <div className="scheme-card-footer">
                      <span className="badge badge-success">Active</span>
                      <div style={{display:'flex', gap:'0.5rem'}}>
                        <Link to={`/schemes/${scheme.id}`} className="btn btn-outline btn-sm">Details</Link>
                        <Link to={`/apply/${scheme.id}`} className="btn btn-primary btn-sm">Apply</Link>
                      </div>
                    </div>
                  </div>
                ))}
              </div>

              {filtered.length === 0 && (
                <div className="empty-state" style={{textAlign:'center', padding:'4rem 0'}}>
                  <div style={{fontSize:'4rem', marginBottom:'1rem'}}>🔍</div>
                  <h3>No schemes found</h3>
                  <p className="text-muted">Try different search terms or clear the filter.</p>
                </div>
              )}

              {/* Pagination */}
              {totalPages > 1 && (
                <div className="pagination">
                  <button className="btn btn-outline btn-sm" disabled={page === 0} onClick={() => setPage(p => p - 1)}>← Prev</button>
                  <span className="page-info">Page {page + 1} of {totalPages}</span>
                  <button className="btn btn-outline btn-sm" disabled={page >= totalPages - 1} onClick={() => setPage(p => p + 1)}>Next →</button>
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

export default Schemes;
