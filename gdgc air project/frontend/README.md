# AirWatch Frontend

A small React app (built with Vite) for the AirWatch project. It talks to the
Spring Boot backend over HTTP using `fetch`.

## Run it

```bash
npm install
npm run dev
```

Opens on `http://localhost:5173`. The backend must be running on
`http://localhost:8080/api` first.

## Screens

- **Home** - short intro page
- **Login** - log in with a username and password
- **Register** - create a new account
- **Submit Report** - describe the pollution, get your location, set a severity
- **My Reports** - list of the reports you have submitted

The logged-in user is kept in `localStorage` so a refresh does not log you out.

## Files

```
frontend/
├── src/
│   ├── App.jsx        # Everything: state, forms, API calls, screens
│   ├── App.css        # Styling
│   ├── main.jsx       # React entry point
│   └── index.css      # Global styles
├── index.html
├── package.json
└── vite.config.js
```

## Talking to the backend

The backend address is one constant at the top of `App.jsx`:

```javascript
const API_BASE = 'http://localhost:8080/api';
```

Calls look like this:

```javascript
const res = await fetch(`${API_BASE}/users/login`, {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ username, password })
});
const data = await res.json();
```
