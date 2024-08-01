import React from "react";

const LoginModal = ({
  show,
  onClose,
  onSubmit,
  username,
  password,
  handleInputChange,
}) => {
  if (!show) {
    return null;
  }

  return (
    <div style={modalStyles.overlay}>
      <div style={modalStyles.modal}>
        <h2 style={modalStyles.title}>
          <strong>LOG IN</strong>
        </h2>
        <form onSubmit={onSubmit} style={modalStyles.form}>
          <div style={modalStyles.inputContainer}>
            <input
              type="text"
              placeholder="USERNAME"
              value={username}
              onChange={handleInputChange}
              style={modalStyles.input}
            />
          </div>
          <div style={modalStyles.inputContainer}>
            <input
              type="password"
              placeholder="PASSWORD"
              value={password}
              onChange={handleInputChange}
              style={modalStyles.input}
            />
          </div>
          <div style={modalStyles.buttonContainer}>
            <button type="button" style={modalStyles.button} onClick={onClose}>
              BACK
            </button>
            <button type="submit" style={modalStyles.button}>
              NEXT
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

const modalStyles = {
  overlay: {
    position: "fixed",
    top: 0,
    bottom: 0,
    left: 0,
    right: 0,
    backgroundColor: "rgba(0,0,0,0.5)",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
  },
  modal: {
    backgroundColor: "#fff",
    paddingTop: "20px",
    borderRadius: "20px",
    border: "2px solid #333",
    boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
    minWidth: "400px",
  },
  title: {
    textAlign: "center",
  },
  form: {
    display: "flex",
    flexDirection: "column",
  },
  inputContainer: {
    marginBottom: "10px",
    display: "flex",
    justifyContent: "center",
    width: "100%",
  },
  input: {
    padding: "10px",
    borderRadius: "5px",
    border: "1px solid #ddd",
    display: "block",
    width: "90%",
    boxSizing: "border-box",
    textAlign: "center",
  },
  buttonContainer: {
    display: "flex",
    justifyContent: "space-between",
  },
  button: {
    marginTop: "20px",
    paddingBottom: "5px",
    paddingTop: "2px",
    borderTop: "1px solid #333",
    borderLeft: "0.3px solid #333",
    borderRight: "0.3px solid #333",
    color: "#333",
    cursor: "pointer",
    width: "100%",
    margin: "20px 0 0 0",
    display: "block",
    backgroundColor: "transparent",
    borderBottom: "1px solid transparent",
  },
};

export default LoginModal;
