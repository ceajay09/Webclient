import React, { useState, useEffect } from "react";


export const Stream = ({ onLogout, onFormSwitch, userInfo }) => {
    // const [email, setEmail] = useState('');
    // const [password, setPassword] = useState('');
    // const [firstName, setFirstName] = useState('');
    // const [lastName, setLastName] = useState('');
    // const [company, setCompany] = useState('');
    // const [phoneNumber, setPhoneNumber] = useState('');



    const token = localStorage.getItem('token'); // Token aus dem Local Storage abrufen

    const getUserData = () => {  //
        handleReset()
        const token = localStorage.getItem('token');

        // Fetch-Anfrage mit dem Token als Header
        fetch('http://localhost:8081/api/video/Krypto_T1_V2.mp4', {
            headers: {
                Authorization: `Bearer ${token}` // Token als Bearer-Token im Header senden
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                setVideoUrl(data.videoUrl);

            })
            .catch(error => {
                // Hier können Sie Fehlerbehandlung durchführen
                console.error('Error:', error);
            });
    }



    useEffect(() => {
        handleReset()
        getUserData()
    }, []);

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

    return (
        <>
            {videoUrl &&
                <>
                    <label htmlFor="video">Video:</label>
                    <video src={videoUrl} width="720" height="480" controls preload="none"></video>
                </>
            }
        </>
    );
}