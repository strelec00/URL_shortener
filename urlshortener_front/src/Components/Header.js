import React from "react";

const Header = ({ username, onLogout }) => {
  return (
    <header style={styles.header}>
      <h1>Shorty - The best way to shorten your URLs</h1>
      {username && (
        <div style={styles.userSection}>
          <small style={styles.welcomeMessage}>Welcome, {username}!</small>
          <button style={styles.logoutButton} onClick={onLogout}>
            Log Out
          </button>
        </div>
      )}
    </header>
  );
};

const styles = {
  header: {
    backgroundColor: "#777",
    padding: "20px",
    textAlign: "center",
    color: "white",
    position: "relative",
  },
  userSection: {
    position: "absolute",
    right: "20px",
    top: "20px",
    textAlign: "right",
  },
  welcomeMessage: {
    display: "block",
    fontSize: "14px",
    marginBottom: "5px",
  },
  logoutButton: {
    padding: "5px 10px",
    borderRadius: "5px",
    border: "none",
    backgroundColor: "#333",
    color: "white",
    cursor: "pointer",
    fontSize: "12px",
  },
};

export default Header;
