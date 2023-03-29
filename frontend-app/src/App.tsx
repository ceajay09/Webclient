import React, { useState } from 'react';
import './App.css';
// import SignInJs from './components/SignIn.js';
// import SignIn from './components/SignIn';
// import log4js from 'log4js';
import { Login } from "./Login";
import { Register } from "./Register";
import { Dashboard } from "./Dashboard";
// const logger = log4js.getLogger('React-Logger');

function App() {
  const [currentForm, setCurrentForm] = useState('login');
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  const toggleForm = (formName: string) => {
    setCurrentForm(formName);

  }
  const handleLogin = () => {
    setIsLoggedIn(true);
    setCurrentForm("dashboard");
  };

  const handleLogout = () => {
    setIsLoggedIn(false);
    setCurrentForm("login");
    return;
  };

  return (
    <div className="App">
      {isLoggedIn ? (
        <Dashboard props={undefined} onLogout={handleLogout} userInfo={{
          firstName: '',
          lastName: '',
          email: '',
          company: '',
          phoneNumber: ''
        }} />
      ) : (
        <>
          {currentForm === "login" ? (
            <Login onFormSwitch={toggleForm} handleLogin={handleLogin} />
          ) : (
            <Register onFormSwitch={toggleForm} />
          )}
        </>
      )}
    </div>
  );
}

export default App;
