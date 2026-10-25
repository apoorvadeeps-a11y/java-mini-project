import { useState, useEffect, useCallback } from 'react';
import './App.css';

const API_BASE = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

// One card in the report lists
function ReportCard({ report, onAlertClick }) {
  if (!report) return null;

  return (
    <div className="report-card">
      <div className="report-header">
        <span className={`severity severity-${report.severity || 3}`}>
          Severity: {report.severity || 0}/5
        </span>
        <span className={`status status-${(report.status || 'PENDING').toLowerCase()}`}>
          {report.status || 'PENDING'}
        </span>
      </div>
      <h3>{report.locationName || 'Unknown Location'}</h3>
      <p>{report.description || 'No description provided'}</p>
      <div className="report-meta">
        <span>📍 {(report.latitude || 0).toFixed(4)}, {(report.longitude || 0).toFixed(4)}</span>
        <span>📅 {report.createdAt ? new Date(report.createdAt).toLocaleString() : 'Date unknown'}</span>
      </div>
      {report.severity >= 4 && (
        <div className="report-emergency-bar">
          <span>⚠️ Critical Hazard</span>
          <div className="emergency-actions">
            <a href="tel:1916" className="btn-call-mini">📞 Call 1916</a>
            {onAlertClick && (
              <button type="button" onClick={() => onAlertClick(report)} className="btn-alert-mini">
                🚨 Alert Municipal Desk
              </button>
            )}
          </div>
        </div>
      )}
    </div>
  );
}

function App() {
  const [activeTab, setActiveTab] = useState('home');
  const [user, setUser] = useState(null);
  const [reports, setReports] = useState([]);
  const [allReports, setAllReports] = useState([]);
  const [loading, setLoading] = useState(false);
  const [refreshKey, setRefreshKey] = useState(0);

  // Form states
  const [loginForm, setLoginForm] = useState({ username: '', password: '' });
  const [registerForm, setRegisterForm] = useState({
    username: '',
    email: '',
    password: '',
    fullName: '',
    role: 'CITIZEN'
  });
  const [reportForm, setReportForm] = useState({
    description: '',
    latitude: '',
    longitude: '',
    locationName: '',
    severity: 3
  });

  // Emergency / Government Alert state
  const [dispatchForm, setDispatchForm] = useState({
    location: '',
    issueType: 'Garbage / Waste Burning',
    description: ''
  });
  const [dispatchSuccess, setDispatchSuccess] = useState(false);
  const [dispatchRef, setDispatchRef] = useState('');

  const handleDispatchAlert = (e) => {
    e.preventDefault();
    const ref = 'MC-' + Math.floor(100000 + Math.random() * 900000);
    setDispatchRef(ref);
    setDispatchSuccess(true);
  };

  // Load user from localStorage once
  useEffect(() => {
    const savedUser = localStorage.getItem('user');
    if (savedUser) {
      try {
        setUser(JSON.parse(savedUser));
      } catch (e) {
        console.error('Failed to parse saved user', e);
        localStorage.removeItem('user');
      }
    }
  }, []);

  // Fetch personal reports when user, tab, or refreshKey changes
  useEffect(() => {
    const fetchReports = async () => {
      if (!user?.id || activeTab !== 'reports') return;
      setLoading(true);
      try {
        const response = await fetch(`${API_BASE}/reports/user/${user.id}`);
        const data = await response.json();
        setReports(Array.isArray(data) ? data : []);
      } catch (error) {
        console.error('Fetch reports error:', error);
      }
      setLoading(false);
    };

    fetchReports();
  }, [user?.id, activeTab, refreshKey]);

  // Fetch all reports for Air Quality and Alerts tabs
  useEffect(() => {
    const fetchAllReports = async () => {
      if (activeTab !== 'airquality' && activeTab !== 'alerts') return;
      setLoading(true);
      try {
        const response = await fetch(`${API_BASE}/reports`);
        const data = await response.json();
        setAllReports(Array.isArray(data) ? data : []);
      } catch (error) {
        console.error('Fetch all reports error:', error);
      }
      setLoading(false);
    };

    fetchAllReports();
  }, [activeTab, refreshKey]);

  // Login function
  const handleLogin = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const response = await fetch(`${API_BASE}/users/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(loginForm)
      });
      const data = await response.json();
      if (data.success) {
        setUser(data.user);
        localStorage.setItem('user', JSON.stringify(data.user));
        alert('Login successful!');
        setRefreshKey(prev => prev + 1);
        setActiveTab('home');
      } else {
        alert('Login failed');
      }
    } catch (error) {
      console.error('Login error:', error);
      alert('Login error');
    }
    setLoading(false);
  };

  // Register function
  const handleRegister = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const response = await fetch(`${API_BASE}/users/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(registerForm)
      });
      const data = await response.json();
      if (data.success) {
        alert('Registration successful! Please login.');
        setActiveTab('login');
      } else {
        alert('Registration failed');
      }
    } catch (error) {
      console.error('Register error:', error);
      alert('Registration error');
    }
    setLoading(false);
  };

  // Submit report function
  const handleSubmitReport = async (e) => {
    e.preventDefault();
    if (!user) {
      alert('Please login first');
      return;
    }
    setLoading(true);
    try {
      const response = await fetch(`${API_BASE}/reports`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ ...reportForm, userId: user.id })
      });
      const data = await response.json();
      if (data.success) {
        alert('Report submitted successfully!');
        setReportForm({
          description: '',
          latitude: '',
          longitude: '',
          locationName: '',
          severity: 3
        });
        setRefreshKey(prev => prev + 1);
      } else {
        alert('Failed to submit report');
      }
    } catch (error) {
      console.error('Submit report error:', error);
      alert('Error submitting report');
    }
    setLoading(false);
  };

  // Logout
  const handleLogout = () => {
    setUser(null);
    localStorage.removeItem('user');
    setActiveTab('home');
  };

  // Get current location
  const handleGetLocation = () => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          setReportForm({
            ...reportForm,
            latitude: position.coords.latitude,
            longitude: position.coords.longitude
          });
          alert('Location detected successfully!');
        },
        (error) => {
          console.error('Geolocation error:', error);
          alert('Could not get location');
        }
      );
    } else {
      alert('Geolocation is not supported by this browser');
    }
  };

  // Used by the "Report Pollution" card on the home page
  const goToReport = () => {
    if (user) {
      setActiveTab('submit');
    } else {
      alert('Please login first to report pollution');
      setActiveTab('login');
    }
  };

  const alerts = allReports.filter((report) => report.severity >= 4);
  const averageSeverity = allReports.length
    ? (allReports.reduce((sum, report) => sum + report.severity, 0) / allReports.length).toFixed(1)
    : '0.0';

  return (
    <div className="app">
      {/* Header */}
      <header className="header">
        <h1>🌿 AirWatch</h1>
        <p>Air Quality Monitoring System</p>
        {user && (
          <div className="user-info">
            <span>Welcome, {user.username}!</span>
            <button onClick={handleLogout} className="btn-logout">Logout</button>
          </div>
        )}
      </header>

      {/* Navigation */}
      <nav className="nav">
        <button onClick={() => setActiveTab('home')} className={activeTab === 'home' ? 'active' : ''}>
          Home
        </button>
        <button onClick={() => setActiveTab('airquality')} className={activeTab === 'airquality' ? 'active' : ''}>
          Air Quality
        </button>
        <button onClick={() => setActiveTab('alerts')} className={activeTab === 'alerts' ? 'active' : ''}>
          Alerts
        </button>
        <button onClick={() => setActiveTab('helpline')} className={activeTab === 'helpline' ? 'active' : ''}>
          🚨 Govt Alert
        </button>
        {!user ? (
          <>
            <button onClick={() => setActiveTab('login')} className={activeTab === 'login' ? 'active' : ''}>
              Login
            </button>
            <button onClick={() => setActiveTab('register')} className={activeTab === 'register' ? 'active' : ''}>
              Register
            </button>
          </>
        ) : (
          <>
            <button onClick={() => setActiveTab('submit')} className={activeTab === 'submit' ? 'active' : ''}>
              Submit Report
            </button>
            <button onClick={() => setActiveTab('reports')} className={activeTab === 'reports' ? 'active' : ''}>
              My Reports
            </button>
          </>
        )}
      </nav>

      {/* Main Content */}
      <main className="main">
        {/* Home Tab */}
        {activeTab === 'home' && (
          <div className="tab-content">
            <h2>Welcome to AirWatch</h2>
            <p>A citizen-powered air quality monitoring platform.</p>
            <div className="features">
              <button className="feature-card" onClick={goToReport}>
                <h3>📍 Report Pollution</h3>
                <p>Submit a pollution report in your area</p>
              </button>
              <button className="feature-card" onClick={() => setActiveTab('airquality')}>
                <h3>📊 Track Air Quality</h3>
                <p>See all reports and the average severity</p>
              </button>
              <button className="feature-card" onClick={() => setActiveTab('alerts')}>
                <h3>🚨 Get Alerts</h3>
                <p>Reports marked severity 4 or 5</p>
              </button>
              <button className="feature-card alert-feature-card" onClick={() => setActiveTab('helpline')}>
                <h3>🏛️ Alert Govt Services</h3>
                <p>Municipal toll-free helplines & instant civic dispatch</p>
              </button>
            </div>
          </div>
        )}

        {/* Login Tab */}
        {activeTab === 'login' && (
          <div className="tab-content">
            <h2>Login</h2>
            <form onSubmit={handleLogin} className="form">
              <input
                type="text"
                placeholder="Username"
                value={loginForm.username}
                onChange={(e) => setLoginForm({ ...loginForm, username: e.target.value })}
                required
              />
              <input
                type="password"
                placeholder="Password"
                value={loginForm.password}
                onChange={(e) => setLoginForm({ ...loginForm, password: e.target.value })}
                required
              />
              <button type="submit" disabled={loading} className="btn-primary">
                {loading ? 'Loading...' : 'Login'}
              </button>
            </form>
          </div>
        )}

        {/* Register Tab */}
        {activeTab === 'register' && (
          <div className="tab-content">
            <h2>Register</h2>
            <form onSubmit={handleRegister} className="form">
              <input
                type="text"
                placeholder="Username"
                value={registerForm.username}
                onChange={(e) => setRegisterForm({ ...registerForm, username: e.target.value })}
                required
              />
              <input
                type="email"
                placeholder="Email"
                value={registerForm.email}
                onChange={(e) => setRegisterForm({ ...registerForm, email: e.target.value })}
                required
              />
              <input
                type="password"
                placeholder="Password (min 6 characters)"
                value={registerForm.password}
                onChange={(e) => setRegisterForm({ ...registerForm, password: e.target.value })}
                required
                minLength={6}
              />
              <input
                type="text"
                placeholder="Full Name"
                value={registerForm.fullName}
                onChange={(e) => setRegisterForm({ ...registerForm, fullName: e.target.value })}
              />
              <select
                value={registerForm.role}
                onChange={(e) => setRegisterForm({ ...registerForm, role: e.target.value })}
              >
                <option value="CITIZEN">Citizen</option>
                <option value="MUNICIPAL_OFFICER">Municipal Officer</option>
              </select>
              <button type="submit" disabled={loading} className="btn-primary">
                {loading ? 'Loading...' : 'Register'}
              </button>
            </form>
          </div>
        )}

        {/* Submit Report Tab */}
        {activeTab === 'submit' && user && (
          <div className="tab-content">
            <h2>Submit Pollution Report</h2>
            <form onSubmit={handleSubmitReport} className="form">
              <textarea
                placeholder="Describe the pollution issue (min 10 characters)"
                value={reportForm.description}
                onChange={(e) => setReportForm({ ...reportForm, description: e.target.value })}
                required
                minLength={10}
                rows={4}
              />
              <input
                type="text"
                placeholder="Location Name"
                value={reportForm.locationName}
                onChange={(e) => setReportForm({ ...reportForm, locationName: e.target.value })}
                required
              />
              <div className="location-group">
                <input
                  type="number"
                  step="any"
                  placeholder="Latitude"
                  value={reportForm.latitude}
                  onChange={(e) => setReportForm({ ...reportForm, latitude: e.target.value })}
                  required
                />
                <input
                  type="number"
                  step="any"
                  placeholder="Longitude"
                  value={reportForm.longitude}
                  onChange={(e) => setReportForm({ ...reportForm, longitude: e.target.value })}
                  required
                />
                <button type="button" onClick={handleGetLocation} className="btn-secondary">
                  Get Location
                </button>
              </div>
              <div className="severity-group">
                <label>Severity: {reportForm.severity}/5</label>
                <input
                  type="range"
                  min="1"
                  max="5"
                  value={reportForm.severity}
                  onChange={(e) => setReportForm({ ...reportForm, severity: parseInt(e.target.value) })}
                />
              </div>
              {reportForm.severity >= 4 && (
                <div className="severity-alert-hint">
                  ⚠️ <strong>Critical pollution level selected!</strong> For urgent civic hazards, you can also alert municipal services directly via the <strong>🚨 Govt Alert</strong> tab or dial <strong>1916</strong>.
                </div>
              )}
              <button type="submit" disabled={loading} className="btn-primary">
                {loading ? 'Submitting...' : 'Submit Report'}
              </button>
            </form>
          </div>
        )}

        {/* My Reports Tab */}
        {activeTab === 'reports' && user && (
          <div className="tab-content">
            <h2>My Reports</h2>
            {loading ? (
              <p>Loading...</p>
            ) : reports.length === 0 ? (
              <p>No reports yet. Submit your first report!</p>
            ) : (
              <div className="reports-list">
                {reports.map((report) => (
                  <ReportCard
                    key={report.id}
                    report={report}
                    onAlertClick={(rep) => {
                      setDispatchForm({
                        location: rep.locationName || '',
                        issueType: 'Hazardous Pollution (Severity ' + rep.severity + '/5)',
                        description: rep.description || ''
                      });
                      setActiveTab('helpline');
                    }}
                  />
                ))}
              </div>
            )}
          </div>
        )}

        {/* Air Quality Tab */}
        {activeTab === 'airquality' && (
          <div className="tab-content">
            <h2>Air Quality</h2>
            <p>Every pollution report submitted by users.</p>
            {loading ? (
              <p>Loading...</p>
            ) : allReports.length === 0 ? (
              <p>No reports yet. Be the first to report pollution!</p>
            ) : (
              <>
                <div className="summary">
                  <span>Total reports: {allReports.length}</span>
                  <span>Average severity: {averageSeverity} / 5</span>
                </div>
                <div className="reports-list">
                  {allReports.map((report) => (
                    <ReportCard
                      key={report.id}
                      report={report}
                      onAlertClick={(rep) => {
                        setDispatchForm({
                          location: rep.locationName || '',
                          issueType: 'Hazardous Pollution (Severity ' + rep.severity + '/5)',
                          description: rep.description || ''
                        });
                        setActiveTab('helpline');
                      }}
                    />
                  ))}
                </div>
              </>
            )}
          </div>
        )}

        {/* Alerts Tab */}
        {activeTab === 'alerts' && (
          <div className="tab-content">
            <div className="alerts-banner">
              <div>
                <h2>Alerts</h2>
                <p>Reports with severity 4 or 5 - these need urgent civic attention.</p>
              </div>
              <button onClick={() => setActiveTab('helpline')} className="btn-alert-quick">
                🚨 Alert Municipal Services
              </button>
            </div>
            {loading ? (
              <p>Loading...</p>
            ) : alerts.length === 0 ? (
              <p>No alerts right now. Air looks fine!</p>
            ) : (
              <div className="reports-list">
                {alerts.map((report) => (
                  <ReportCard
                    key={report.id}
                    report={report}
                    onAlertClick={(rep) => {
                      setDispatchForm({
                        location: rep.locationName || '',
                        issueType: 'Hazardous Pollution (Severity ' + rep.severity + '/5)',
                        description: rep.description || ''
                      });
                      setActiveTab('helpline');
                    }}
                  />
                ))}
              </div>
            )}
          </div>
        )}

        {/* Government & Municipal Alert Tab */}
        {activeTab === 'helpline' && (
          <div className="tab-content">
            <h2>🚨 Alert Government & Municipal Services</h2>
            <p className="subtitle">
              Quickly contact municipal authorities, report hazardous air conditions, or dial emergency helplines.
            </p>

            <div className="helpline-grid">
              <div className="helpline-card highlight">
                <div className="helpline-icon">🏛️</div>
                <div className="helpline-info">
                  <h3>Municipal Corporation Helpline</h3>
                  <p>Toll-free helpline for municipal civic issues, waste burning & smog complaints.</p>
                  <div className="helpline-number">Toll-Free: <strong>1916</strong></div>
                </div>
                <a href="tel:1916" className="btn-call">📞 Call 1916</a>
              </div>

              <div className="helpline-card highlight">
                <div className="helpline-icon">🏭</div>
                <div className="helpline-info">
                  <h3>Pollution Control Board (CPCB)</h3>
                  <p>National Air Quality Control Room & SAMEER grievance helpline.</p>
                  <div className="helpline-number">Toll-Free: <strong>1800-180-1817</strong></div>
                </div>
                <a href="tel:18001801817" className="btn-call">📞 Call CPCB</a>
              </div>

              <div className="helpline-card">
                <div className="helpline-icon">🚒</div>
                <div className="helpline-info">
                  <h3>Fire & Hazard Emergency</h3>
                  <p>For chemical spills, large-scale smoke, factory fires, or toxic emissions.</p>
                  <div className="helpline-number">Emergency: <strong>101</strong></div>
                </div>
                <a href="tel:101" className="btn-call btn-call-emergency">📞 Call 101</a>
              </div>

              <div className="helpline-card">
                <div className="helpline-icon">🚨</div>
                <div className="helpline-info">
                  <h3>National Emergency (All-in-One)</h3>
                  <p>Unified emergency response support system (ERSS) across India.</p>
                  <div className="helpline-number">Emergency: <strong>112</strong></div>
                </div>
                <a href="tel:112" className="btn-call btn-call-emergency">📞 Call 112</a>
              </div>
            </div>

            {/* Quick Dispatch Form */}
            <div className="alert-dispatch-box">
              <h3>⚡ Quick Alert Dispatch to Municipal Desk</h3>
              <p>Send an immediate incident notification directly to the civic response team.</p>
              
              {dispatchSuccess ? (
                <div className="dispatch-success-msg">
                  <div className="success-icon">✅</div>
                  <div>
                    <h4>Alert Dispatched Successfully!</h4>
                    <p>Reference Ticket: <strong>#{dispatchRef}</strong></p>
                    <p>The municipal control center has received this alert for immediate inspection. You can also dial <strong>1916</strong> with this ticket number for instant status.</p>
                    <button onClick={() => setDispatchSuccess(false)} className="btn-secondary" style={{ marginTop: '0.75rem' }}>
                      Send Another Alert
                    </button>
                  </div>
                </div>
              ) : (
                <form onSubmit={handleDispatchAlert} className="form" style={{ marginTop: '1rem', maxWidth: '600px' }}>
                  <input
                    type="text"
                    placeholder="Affected Area / Location (e.g., Sector 15, Near City Park)"
                    value={dispatchForm.location}
                    onChange={(e) => setDispatchForm({ ...dispatchForm, location: e.target.value })}
                    required
                  />
                  <select
                    value={dispatchForm.issueType}
                    onChange={(e) => setDispatchForm({ ...dispatchForm, issueType: e.target.value })}
                  >
                    <option value="Garbage / Waste Burning">🔥 Open Garbage / Waste Burning</option>
                    <option value="Industrial Toxic Smoke">🏭 Industrial / Factory Toxic Smoke</option>
                    <option value="Severe Construction Dust">🏗️ Heavy Construction Dust</option>
                    <option value="Stubble / Biomass Burning">🌾 Stubble / Biomass Burning</option>
                    <option value="Other Hazardous Air Issue">⚠️ Other Severe Air Hazard</option>
                  </select>
                  <textarea
                    placeholder="Brief description of the urgent issue (e.g. thick black smoke spreading into residential area)..."
                    rows={3}
                    value={dispatchForm.description}
                    onChange={(e) => setDispatchForm({ ...dispatchForm, description: e.target.value })}
                    required
                  />
                  <div style={{ display: 'flex', gap: '0.75rem', flexWrap: 'wrap' }}>
                    <button type="submit" className="btn-primary" style={{ background: '#b91c1c' }}>
                      🚨 Dispatch Municipal Alert
                    </button>
                    <a
                      href={`mailto:complaints-cpcb@gov.in?subject=${encodeURIComponent(`Urgent Air Pollution Alert: ${dispatchForm.location || 'Civic Issue'}`)}&body=${encodeURIComponent(`Incident Location: ${dispatchForm.location}\nIssue: ${dispatchForm.issueType}\nDescription: ${dispatchForm.description}`)}`}
                      className="btn-secondary"
                      style={{ textDecoration: 'none', display: 'inline-flex', alignItems: 'center', justifyContent: 'center' }}
                    >
                      ✉️ Email Authorities
                    </a>
                  </div>
                </form>
              )}
            </div>
          </div>
        )}
      </main>

      {/* Footer */}
      <footer className="footer">
        <p>© 2024 AirWatch - Full Stack Java Mini Project</p>
      </footer>
    </div>
  );
}

export default App;
