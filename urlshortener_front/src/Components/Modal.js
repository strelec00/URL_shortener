import React from "react";

const Modal = ({ message, success, onClose }) => {
  return (
    <div style={styles.overlay}>
      <div style={styles.modal}>
        {success && (
          <h3 style={styles.success}>
            <strong>{success}</strong>
          </h3>
        )}
        <p>{message}</p>
        <button onClick={onClose} style={styles.button}>
          Back
        </button>
      </div>
    </div>
  );
};

const styles = {
  overlay: {
    position: "fixed",
    top: 0,
    left: 0,
    width: "100%",
    height: "100%",
    backgroundColor: "rgba(0, 0, 0, 0.5)",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    zIndex: 1000,
  },
  modal: {
    backgroundColor: "#fff",
    paddingTop: "23px",
    paddingBottom: "0px",
    borderRadius: "20px",
    textAlign: "center",
    minWidth: "400px",
    border: "2px solid #333",
    boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
  },
  success: {
    marginBottom: "20px",
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

export default Modal;
