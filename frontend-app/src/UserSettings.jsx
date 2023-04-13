import React, { useState } from "react";


export const UserSettings = ({ props, onLogout, onFormSwitch, userInfo }) => {
    const [email, setEmail] = useState(userInfo ? userInfo.email : '');
    const [password, setPassword] = useState(userInfo ? userInfo.password : '')
    const [firstName, setFirstName] = useState(userInfo ? userInfo.firstName : '');
    const [lastName, setLastName] = useState(userInfo ? userInfo.lastName : '');
    const [company, setCompany] = useState(userInfo ? userInfo.company : '');
    const [phoneNumber, setPhoneNumber] = useState(userInfo ? userInfo.phoneNumber : '');


    const handleLogout = () => {
        // Hier können Sie die Logik für die Abmeldung ausführen, z. B. das Löschen des Tokens aus dem Local Storage
        // console.log("Token : " + localStorage.getItem('token'))
        // localStorage.removeItem('token');
        // console.log("Token entfernt (null): " + localStorage.getItem('token'))
        onLogout();
    };

    const handleFormSwitch = (formName) => {
        onFormSwitch(formName);
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log(email);

        // Hier wird die Fetch-Anfrage ausgeführt, um die Registrierungsdaten an das Backend zu senden

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
        <><div className="auth-form-container">
            <h2>User Settings</h2>
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
            <button onClick={() => handleFormSwitch('changePassword')}>Update user info</button>
            <button onClick={() => handleFormSwitch('updateUserInfo')}>Dashboard</button>
        </div>
            <div className="logout-button">
                <button onClick={handleLogout}>Logout</button>
            </div></>
    )
}
