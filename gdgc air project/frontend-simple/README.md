# AirWatch Frontend (React)

## Topics Covered

### React Fundamentals (Topic 8)
- ✅ **Components**: Functional components with hooks
- ✅ **JSX**: HTML-like syntax in JavaScript
- ✅ **Props & State**: useState hook for state management
- ✅ **Event Handling**: Form submissions, button clicks
- ✅ **Conditional Rendering**: Showing/hiding content based on state

### HTML, CSS, JavaScript (Topic 5)
- ✅ **HTML Structure**: Semantic markup
- ✅ **CSS Styling**: Responsive design, flexbox, grid
- ✅ **JavaScript Functions**: Event handlers, API calls
- ✅ **Form Handling**: Input validation, form submission

### JavaScript Validation (Topic 13)
- ✅ **Form Validation**: Required fields, minLength
- ✅ **Input Types**: email, password, number
- ✅ **Error Handling**: Try-catch blocks

### DOM Manipulation (Topic 14)
- ✅ **Dynamic Content**: Conditional rendering
- ✅ **Event Listeners**: onClick, onChange, onSubmit
- ✅ **LocalStorage**: Persisting user session

### React & Backend Integration (Topic 21)
- ✅ **fetch() API**: HTTP requests to backend
- ✅ **JSON**: Request/response data format
- ✅ **CRUD Operations**: Create reports, read reports
- ✅ **State Management**: Updating UI based on API responses

### Browser Features (Topic 26)
- ✅ **Geolocation API**: Getting user location
- ✅ **LocalStorage**: Session persistence
- ✅ **DOM**: Document Object Model manipulation

## Setup

1. Install dependencies:
```bash
npm install
```

2. Start development server:
```bash
npm run dev
```

The app will run on `http://localhost:5173`

## Features

1. **User Authentication**
   - Register new account
   - Login/Logout
   - Session persistence

2. **Report Submission**
   - Create pollution reports
   - Geolocation integration
   - Severity rating (1-5)

3. **View Reports**
   - List user's reports
   - Status indicators
   - Severity badges

## Project Structure

```
frontend-simple/
├── src/
│   ├── App.jsx           # Main application component
│   ├── App.css           # Application styles
│   ├── main.jsx          # React entry point
│   └── index.css         # Global styles
├── index.html            # HTML template
├── package.json          # Dependencies
└── vite.config.js        # Vite configuration
```

## Key React Concepts Demonstrated

### State Management (useState)
```javascript
const [user, setUser] = useState(null);
const [reports, setReports] = useState([]);
```

### Effect Hook (useEffect)
```javascript
useEffect(() => {
  fetchReports();
}, [user]);
```

### Event Handling
```javascript
const handleLogin = async (e) => {
  e.preventDefault();
  // API call
};
```

### Conditional Rendering
```javascript
{user ? <Dashboard /> : <Login />}
```

### Form Handling
```javascript
<form onSubmit={handleSubmit}>
  <input 
    value={form.username}
    onChange={(e) => setForm({...form, username: e.target.value})}
  />
</form>
```

## API Integration

All API calls use the native `fetch()` API:

```javascript
const response = await fetch(`${API_BASE}/users/login`, {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(loginForm)
});
const data = await response.json();
```

## Styling Approach

Pure CSS without any external UI libraries:
- Flexbox for layouts
- Grid for responsive cards
- CSS variables for theming
- Media queries for responsive design

## Browser APIs Used

1. **Geolocation API**
```javascript
navigator.geolocation.getCurrentPosition(callback);
```

2. **LocalStorage API**
```javascript
localStorage.setItem('user', JSON.stringify(user));
localStorage.getItem('user');
```

3. **Fetch API**
```javascript
fetch(url, options)
```

## No AI Features

This is a clean, basic implementation without any AI/ML features:
- No Gemini API integration
- No image analysis
- No AI chat
- Simple form-based reporting
- Basic CRUD operations only
