export const handleLogin = (setIsLoggedIn, setCurrentForm) => {
  setIsLoggedIn(true);
  setCurrentForm("dashboard");
};

export const handleLogout = (setIsLoggedIn, setCurrentForm) => {
  setIsLoggedIn(false);
  setCurrentForm("login");
};

  // const handleLogin = () => {
  //   setIsLoggedIn(true);
  //   setCurrentForm("dashboard");
  // }

  // const handleLogout = () => {
  //   setIsLoggedIn(false);
  //   setCurrentForm("login");
  // }