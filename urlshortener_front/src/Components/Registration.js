import React, { useState } from "react";
import axios from "axios";
import Header from "./Header";
import Modal from "./Modal";
import LoginModal from "./LoginModal";

const Registration = () => {
  const [username, setUsername] = useState("");
  const [desiredUsername, setDesiredUsername] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const [success, setSuccess] = useState("");
  const [loggedIn, setLoggedIn] = useState(false);
  const [url, setUrl] = useState("");
  const [statistics, setStatistics] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [showLoginModal, setShowLoginModal] = useState(false);
  const [showStats, setShowStats] = useState(false);

  const handleInputChange = (e) => {
    const { placeholder, value } = e.target;
    if (placeholder === "Enter username") {
      setDesiredUsername(value);
    } else if (placeholder === "USERNAME") {
      setUsername(value);
    } else if (placeholder === "PASSWORD") {
      setPassword(value);
    } else if (placeholder === "Enter URL") {
      setUrl(value);
    }
  };

  const handleRegisterSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post("/administration/register", {
        accountId: desiredUsername,
      });
      if (response.data.success) {
        const successMessage = `Your password is: ${response.data.password}`;
        setMessage(successMessage);
        setSuccess("Successful registration");
        setUsername(desiredUsername);
        setPassword(response.data.password);
        setLoggedIn(true);
        setShowModal(true);
        axios.defaults.headers.common["Authorization"] = `Basic ${btoa(
          desiredUsername + ":" + response.data.password
        )}`;
      }
    } catch (error) {
      const errorMessage =
        error.response?.data?.description ||
        "An error occurred during registration.";
      setSuccess("Unsuccessful registration");
      setMessage(errorMessage);
      setShowModal(true);
    }
  };

  const handleLoginSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post("/administration/login", {
        accountId: username,
        password: password,
      });
      if (response.data) {
        setLoggedIn(true);
        const successMessage = "Successful login";
        setSuccess(successMessage);
        setMessage(`Welcome ${username}!`);
        setShowModal(true);
        setShowLoginModal(false);
        axios.defaults.headers.common["Authorization"] = `Basic ${btoa(
          username + ":" + password
        )}`;
      }
    } catch (error) {
      const errorMessage = error.response?.data?.description;
      setSuccess("Unsuccessful login");
      setMessage(errorMessage);
      setShowModal(true);
    }
  };

  const handleShortenUrl = () => {
    axios
      .post("/administration/short", {
        url: url,
        redirectType: "302",
      })
      .then((response) => {
        setMessage(`Your new URL is: ${response.data.shortUrl}`);
        setSuccess("Shortening successful");
        setShowModal(true);
      })
      .catch((err) => {
        console.error("Error shortening URL:", err);
        let errorMessage = "An error occurred while shortening the URL.";
        if (err.response) {
          if (err.response.data && err.response.data.description) {
            errorMessage = err.response.data.description;
          } else if (err.response.statusText) {
            errorMessage = err.response.statusText;
          }
        } else if (err.message) {
          errorMessage = err.message;
        }
        setSuccess("Shortening unsuccessful");
        setMessage(errorMessage);
        setShowModal(true);
      });
  };

  const handleGetStatistics = async () => {
    try {
      const response = await axios.get("/administration/statistics");
      const statsData = response.data;

      if (Object.keys(statsData).length === 0) {
        const noDataMessage = "You didn't shorten any URLs";
        setSuccess("Unsuccessful statistics");
        setMessage(noDataMessage);
        setShowModal(true);
        return;
      }

      setStatistics(statsData);
      setShowStats(true);
      setSuccess("Successful statistics");
    } catch (error) {
      console.error("Caught error:", error);
      const errorMessage =
        error.response?.data?.description ||
        "An error occurred while retrieving statistics.";
      setShowModal(true);
      setSuccess("Unsuccessful statistics");
      setMessage(errorMessage);
    }
  };

  const handleLogout = () => {
    setUsername("");
    setPassword("");
    setLoggedIn(false);
    setUrl("");
    setStatistics(null);
    delete axios.defaults.headers.common["Authorization"];
    setShowStats(false);
  };

  const closeModal = () => {
    setShowModal(false);
    setShowStats(false);
  };

  const openLoginModal = () => {
    setShowLoginModal(true);
  };

  const closeLoginModal = () => {
    setShowLoginModal(false);
  };

  return (
    <div>
      <Header username={loggedIn ? username : ""} onLogout={handleLogout} />
      <div style={styles.container}>
        {loggedIn ? (
          showStats ? (
            <div style={styles.statsContainer}>
              <div style={styles.infoTextStats}>
                <p>
                  Generated a report on setrieved statistics for the URLs you've
                  shortened and the number of times each URL has been shortened.
                </p>
              </div>
              <ul style={styles.statsList}>
                {Object.entries(statistics).map(([key, value]) => (
                  <li key={key} style={styles.statsListItem}>
                    {key}: {value}
                  </li>
                ))}
              </ul>
            </div>
          ) : (
            <>
              <div style={styles.infoTextShort}>
                <p>
                  Get statistics: Click 'Statistics' to retrieve the statistics
                  for the URLs you've shortened and the number of times each URL
                  has been shortened.
                </p>
              </div>
              <div style={styles.shorteningBox}>
                <h2>Shorten your URL</h2>
                <input
                  type="text"
                  placeholder="Enter URL"
                  value={url}
                  onChange={handleInputChange}
                  style={styles.input}
                />
                <button style={styles.button} onClick={handleShortenUrl}>
                  SHORTEN MY URL
                </button>
                <button style={styles.button} onClick={handleGetStatistics}>
                  STATISTICS
                </button>
              </div>
              <div style={styles.infoTextShort}>
                <p>
                  Shorten your URL: Click 'Shorten my URL' to shorten a desired
                  URL by entering it into the input form and clicking "Shorten
                  my URL" to generate a shortened link. You can then use the
                  shortened URL to redirect to the original URL.
                </p>
              </div>
            </>
          )
        ) : (
          <div style={styles.registrationContainer}>
            <div style={styles.infoText}>
              <h2>Welcome to our Registration</h2>
              <p>
                Register to access our URL shortening service. Once you have an
                account, you can shorten URLs and view statistics.
              </p>
            </div>
            <div style={styles.registrationBox}>
              <h2>Registration</h2>
              <form onSubmit={handleRegisterSubmit}>
                <input
                  type="text"
                  placeholder="Enter username"
                  value={desiredUsername}
                  onChange={handleInputChange}
                  style={styles.input}
                />
                <button type="submit" style={styles.button}>
                  CREATE ACCOUNT
                </button>
              </form>
              <h3 style={styles.alreadyHaveAccount}>
                Already have an account?
              </h3>
              <button style={styles.button} onClick={openLoginModal}>
                LOGIN
              </button>
            </div>
          </div>
        )}
      </div>
      {showModal && (
        <Modal message={message} success={success} onClose={closeModal} />
      )}
      <LoginModal
        show={showLoginModal}
        onClose={closeLoginModal}
        onSubmit={handleLoginSubmit}
        username={username}
        password={password}
        handleInputChange={handleInputChange}
      />
    </div>
  );
};

const styles = {
  container: {
    display: "flex",
    justifyContent: "center",
    margin: "40px 0",
  },
  shorteningBox: {
    backgroundColor: "#777",
    padding: "15px",
    borderRadius: "5px",
    textAlign: "center",
    color: "white",
    marginRight: "20px",
    marginLeft: "20px",
    minWidth: "300px",
  },
  registrationContainer: {
    display: "flex",
    alignItems: "center",
  },
  infoText: {
    padding: "15px",
    borderRadius: "5px",
    color: "black",
    flex: "1",
  },
  infoTextShort: {
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    textAlign: "center",
    color: "black",
    borderRadius: "5px",
    flex: "1",
    padding: "15px",
  },

  infoTextStats: {
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    textAlign: "center",
    color: "black",
    borderRadius: "5px",
    flex: "1",
    margin: "20px",
  },

  registrationBox: {
    backgroundColor: "#777",
    padding: "15px",
    borderRadius: "5px",
    textAlign: "center",
    color: "white",
    flex: "1",
    width: "300px",
  },
  input: {
    padding: "8px",
    margin: "15px 0",
    borderRadius: "4px",
    border: "none",
    display: "block",
    width: "100%",
    boxSizing: "border-box",
  },
  button: {
    padding: "8px 20px",
    margin: "15px 0 0 0",
    borderRadius: "4px",
    border: "none",
    backgroundColor: "#333",
    color: "white",
    cursor: "pointer",
    display: "block",
    width: "100%",
    boxSizing: "border-box",
  },
  alreadyHaveAccount: {
    marginTop: "20px",
  },
  statsContainer: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "flex-start",
    flexDirection: "row",
    margin: "20px 0",
  },
  statsList: {
    listStyleType: "none",
    padding: 0,
    margin: "20px",
    marginRight: "60px",
    width: "auto",
    maxWidth: "500px",
  },
  statsListItem: {
    backgroundColor: "#f8f9fa",
    border: "1px solid #dee2e6",
    borderRadius: "4px",
    padding: "10px",
    margin: "5px 0",
    boxShadow: "0 1px 3px rgba(0,0,0,0.1)",
    textAlign: "center",
  },
};

export default Registration;
