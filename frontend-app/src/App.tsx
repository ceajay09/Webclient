import React, { useState } from 'react';
import logo from './logo.svg';
import './App.css';
// import SignInJs from './components/SignIn.js';
// import SignIn from './components/SignIn';
// import log4js from 'log4js';
import { Login } from "./Login";
import { Register } from "./Register";

// const logger = log4js.getLogger('React-Logger');

function App() {
  const [currentForm, setCurrentForm] = useState('login');

  const toggleForm = (formName: string) => {
    setCurrentForm(formName);
  }

  return (
    <div className="App">
      {/* <header className="App-header">

        <img src={logo} className="App-logo" alt="logo" /> 
        <div>
          Hallo welt
        </div>
        <p>
          Edit <code>src/App.tsx</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
        

      </header> */}
      {
        currentForm === "login" ? <Login onFormSwitch={toggleForm} /> : <Register onFormSwitch={toggleForm} /> //wenn condition=login dann zu login bzw. wenn register dann zu register
      }
    </div>
  );
}

export default App;
