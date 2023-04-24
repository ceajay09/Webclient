import React, { useState } from "react";

export const Login = (props) => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');


    const handleSubmit = (e) => {  //
        e.preventDefault(); //wenn Seite neu geladen wird, verliert ansonsten die informationen
        console.log(email);

        // Hier wird die Fetch-Anfrage ausgeführt, um die Registrierungsdaten an das Backend zu senden
        fetch('http://localhost:8080/api/login', {
            method: 'POST',
            body: JSON.stringify({ email, password }),
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
                console.log('Login successful:', data);
                // Hier können Sie die weitere Logik ausführen, z. B. den Benutzer zur Dashboard-Seite weiterleiten
                localStorage.clear();
                localStorage.setItem('token', data.token);
                // localStorage.setItem('email', data.email);
                // localStorage.setItem('firstName', data.firstName);
                // localStorage.setItem('lastName', data.lastName);
                // localStorage.setItem('company', data.company);
                // localStorage.setItem('phoneNumber', data.phoneNumber);

                alert("token: " + localStorage.getItem('token')); // Token im Local Storage speichern
                props.handleLogin()
                // Wenn der Benutzer angemeldet ist, wechsle zur Dashboard-Seite


            })
            .catch(error => {
                console.error('Login failed. User or password not correct:', error);
                // Hier können Sie Fehler behandeln, z. B. eine Fehlermeldung anzeigen
                alert('Login failed. Please check your email and password.');
            });
    }

    return (
        <div className="auth-form-container">
            <h2>Login</h2>
            <form className="login-form" onSubmit={handleSubmit}>
                <label htmlFor="email">email</label>
                <input value={email} onChange={(e) => setEmail(e.target.value)} type="email" placeholder="youremail@gmail.com" id="email" name="email" />
                <label htmlFor="password">password</label>
                <input value={password} onChange={(e) => setPassword(e.target.value)} type="password" placeholder="********" id="password" name="password" />
                <button type="submit">Log In</button>
            </form>
            <button className="link-btn" onClick={() => props.onFormSwitch('register')}>Don't have an account? Register here.</button>
        </div>
    )
}