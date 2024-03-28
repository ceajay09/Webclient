com.example.myproject
│
├── Application.java
│
├── config
│   ├── SecurityConfig.java
│   └── SwaggerConfig.java
│
├── controller
│   ├── AccountController.java
│   └── SessionController.java
│
├── service
│   ├── AccountService.java           // Verwaltet Geschäftslogik bezüglich Accounts
│   ├── AuthenticationService.java    // Verwaltet Authentifizierung und Autorisierung
│   ├── TokenService.java             // Verwaltet Token-basierte Operationen
│   └── SessionService.java           // Optional, falls spezifische Logik für Sessions benötigt wird
│
├── repository
│   ├── AccountRepository.java
│   └── SessionRepository.java
│
├── model
│   ├── Account.java
│   ├── Session.java
│   └── dto
│       ├── AccountDTO.java           // Datenübertragungsobjekte für Accounts
│       └── SessionDTO.java           // Datenübertragungsobjekte für Sessions
│
├── exception
│   ├── CustomException.java          // Benutzerdefinierte Ausnahmen
│   ├── NotFoundException.java        // Ausnahme für nicht gefundene Ressourcen
│   └── ExceptionHandlerController.java // Globaler Exception-Handler
│
├── security
│   ├── SecurityUtil.java             // Hilfsfunktionen für Sicherheitsoperationen
│   └── UserDetailsServiceImpl.java   // Benutzerdefinierter Service für UserDetails
│
└── util
├── TokenUtil.java                // Hilfsfunktionen für Token-Operationen
└── SomeOtherUtility.java         // Weitere Hilfsfunktionen

TODO: SessionTermination: @Service + @Autowired
public SessionTerminationService(SessionRepository sessionRepository) {
