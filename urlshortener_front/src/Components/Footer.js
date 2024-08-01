import React from "react";
import assecoLogo from "../pictures/asseco_pic.png";

const Footer = () => {
  return (
    <footer style={styles.footer}>
      <div style={styles.container}>
        <div style={styles.text}>
          <p>
            ASEE Group is one of the biggest IT companies in the area of
            production and implementation of its own software solutions and
            services in the region of South Eastern Europe, Turkey, Spain,
            Portugal, Andorra, Colombia, Peru, Dominican Republic, Slovakia and
            Czech Republic. The Company provides ICT solutions for various
            industry verticals including the financial sector, payment sector,
            public administration and telecoms. The company provides products
            and services within Payment business under Payten name. Since
            October 2009, the shares of Asseco South Eastern Europe (WSE:
            ASEECOSEE, ASE) have been listed on the Warsaw Stock Exchange. ASEE
            Group employs over 3,800 people in 23 countries. More than 10 banks
            out of the 15 largest ones in southeastern Europe are already
            clients of ASEE.
          </p>
        </div>
        <div style={styles.imageContainer}>
          <img src={assecoLogo} alt="asee by Asseco" style={styles.image} />
        </div>
      </div>
    </footer>
  );
};

const styles = {
  footer: {
    backgroundColor: "#878a88",
    padding: "30px",
    marginTop: "50px",
    borderTop: "1px solid #ccc",
  },
  container: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
  },
  text: {
    textAlign: "center",
    color: "#fff",
    flex: 1,
    marginRight: "100px",
    marginLeft: "50px",
  },
  imageContainer: {
    flex: "0 0 auto",
  },
  image: {
    width: "600px",
    marginRight: "50px",
    height: "auto",
  },
};

export default Footer;
