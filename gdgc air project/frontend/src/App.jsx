import { useState, useEffect } from 'react';
import './App.css';

const API_BASE = 'http://localhost:8080/api';

function App() {
  const [activeTab, setActiveTab] = useState('home');
  const [user, setUser] = useState(null);
  const [reports, setReports] = useState([]);
  const [loading, setLoading] = useState(false);

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

  useEffect(() => {
    const savedUser = localStorage.getItem('user');
    if (savedUser) {
      setUser(JSON.parse(savedUser));
    }
  }, []);

  useEffect(() => {
    if (user && activeTab === 'reports') {
      fetchReports();
    }
  }, [user, activeTab]);

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
        fetchReports();
      } else {
        alert('Failed to submit report');
      }
    } catch (error) {
      console.error('Submit report error:', error);
      alert('Error submitting report');
    }
    setLoading(false);
  };

  // Fetch reports
  const fetchReports = async () => {
    setLoading(true);
    try {
      const response = await fetch(`${API_BASE}/reports/user/${user.id}`);
      const data = await response.json();
      setReports(data);
    } catch (error) {
      console.error('Fetch reports error:', error);
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
              <div className="feature-card">
                <h3>📍 Report Pollution</h3>
                <p>Submit pollution reports in your area</p>
              </div>
              <div className="feature-card">
                <h3>📊 Track Air Quality</h3>
                <p>Monitor AQI data in real-time</p>
              </div>
              <div className="feature-card">
                <h3>🚨 Get Alerts</h3>
                <p>Receive notifications about nearby pollution</p>
              </div>
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
                  <div key={report.id} className="report-card">
                    <div className="report-header">
                      <span className={`severity severity-${report.severity}`}>
                        Severity: {report.severity}/5
                      </span>
                      <span className={`status status-${report.status.toLowerCase()}`}>
                        {report.status}
                      </span>
                    </div>
                    <h3>{report.locationName}</h3>
                    <p>{report.description}</p>
                    <div className="report-meta">
                      <span>📍 {report.latitude.toFixed(4)}, {report.longitude.toFixed(4)}</span>
                      <span>📅 {new Date(report.createdAt).toLocaleString()}</span>
                    </div>
                  </div>
                ))}
              </div>
            )}
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
