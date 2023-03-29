import React, { useState } from "react";


export const Dashboard = ({ props, onLogout, userInfo }) => {
    const [email, setEmail] = useState(userInfo.email);
    const [password, setPassword] = useState(userInfo.password);
    const [firstName, setFirstName] = useState(userInfo.firstName);
    const [lastName, setLastName] = useState(userInfo.lastName);
    const [company, setCompany] = useState(userInfo.company);
    const [phoneNumber, setPhoneNumber] = useState(userInfo.phoneNumber);


    const handleLogout = () => {
        // Hier können Sie die Logik für die Abmeldung ausführen, z. B. das Löschen des Tokens aus dem Local Storage
        // localStorage.removeItem('token');
        props.onLogout();
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log(email);

        // Hier wird die Fetch-Anfrage ausgeführt, um die Registrierungsdaten an das Backend zu senden
        fetch('http://localhost:8080/api/dashboard', {
            method: 'POST',
            body: JSON.stringify({ email, password, firstName, lastName, company, phoneNumber }),
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                console.log('Registration successful:', data);
                // Hier können Sie die weitere Logik ausführen, z. B. den Benutzer zur Login-Seite weiterleiten
                props.onFormSwitch('dashboard')
                handleReset()
            })
            .catch(error => {
                console.error('Registration failed. User already exists:', error);
                // Hier können Sie Fehler behandeln, z. B. eine Fehlermeldung anzeigen
                alert('Registration failed. User ' + '"' + email + '"' + ' already exists');
            });

    }

    const handleReset = () => {
        setEmail('');
        setPassword('');
        setFirstName('');
        setLastName('');
        setCompany('');
        setPhoneNumber('');
    };


    return (
        <div className="auth-form-container">
            <h2>Welcome to your Dashboard</h2>
            <form className="dashboard-form">
                <label htmlFor="email">Email:</label>
                <input type="email" id="email" name="email" value={email} readOnly />

                <label htmlFor="firstName">First Name:</label>
                <input type="text" id="firstName" name="firstName" value={firstName} readOnly />

                <label htmlFor="lastName">Last Name:</label>
                <input type="text" id="lastName" name="lastName" value={lastName} readOnly />

                <label htmlFor="company">Company:</label>
                <input type="text" id="company" name="company" value={company} readOnly />

                <label htmlFor="phoneNumber">Phone Number:</label>
                <input type="text" id="phoneNumber" name="phoneNumber" value={phoneNumber} readOnly />
            </form>
            <button onClick={handleLogout}>Logout</button>
        </div>
    )
}
