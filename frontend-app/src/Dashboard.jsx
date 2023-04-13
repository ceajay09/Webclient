import React, { useState } from "react";
import { UserSettings } from "./UserSettings";
import { sendLogout } from './Logout';

export const Dashboard = ({ props, onLogout, onFormSwitch, userInfo }) => {
    const [currentForm, setCurrentForm] = useState('Dashboard');
    const [email, setEmail] = useState(userInfo.email);
    const [password, setPassword] = useState(userInfo.password);
    const [firstName, setFirstName] = useState(userInfo.firstName);
    const [lastName, setLastName] = useState(userInfo.lastName);
    const [company, setCompany] = useState(userInfo.company);
    const [phoneNumber, setPhoneNumber] = useState(userInfo.phoneNumber);


    const handleLogout = () => {
        // Hier können Sie die Logik für die Abmeldung ausführen, z. B. das Löschen des Tokens aus dem Local Storage
        // console.log("Token : " + localStorage.getItem('token'))
        // localStorage.removeItem('token');
        // console.log("Token entfernt (null): " + localStorage.getItem('token'))
        sendLogout();
        onLogout();
    };

    const handleFormSwitch = (UserSettings) => {
        onFormSwitch(UserSettings);
    };

    const toggleForm = (formName) => {
        setCurrentForm(formName);
    }

    const handleUserSettings = () => {
        console.log("handleUserSettings called");
        setCurrentForm("UserSettings");
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


    // return (
    //     <><div className="auth-form-container">
    //         <h2>Dashboard</h2>
    //         <form className="dashboard-form">
    //             <label htmlFor="email">Email:</label>
    //             <input type="email" id="email" name="email" value={email} readOnly />
    //             <label htmlFor="firstName">First Name:</label>
    //             <input type="text" id="firstName" name="firstName" value={firstName} readOnly />
    //             <label htmlFor="lastName">Last Name:</label>
    //             <input type="text" id="lastName" name="lastName" value={lastName} readOnly />
    //             <label htmlFor="company">Company:</label>
    //             <input type="text" id="company" name="company" value={company} readOnly />
    //             <label htmlFor="phoneNumber">Phone Number:</label>
    //             <input type="text" id="phoneNumber" name="phoneNumber" value={phoneNumber} readOnly />
    //         </form>
    //         <button onClick={() => handleUserSettings('UserSettings')}>Update User Info</button>
    //         <button onClick={() => handleFormSwitch('changePassword')}>Change Password</button>
    //     </div>
    //         <div className="logout-button">
    //             <button onClick={handleLogout}>Logout</button>
    //         </div></>
    // )

    return (
        <>
            {currentForm === "UserSettings" ? (
                <UserSettings onFormSwitch={toggleForm} handleUserSettings={handleUserSettings} />
            ) : (
                <div className="auth-form-container">
                    <h2>Dashboard</h2>
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
                    <button onClick={() => toggleForm("UserSettings")}>Update User Info</button>
                    <button onClick={() => toggleForm("changePassword")}>Change Password</button>
                </div>
            )}

            <div className="logout-button">
                <button onClick={handleLogout}>Logout</button>
            </div>
        </>
    );

}
