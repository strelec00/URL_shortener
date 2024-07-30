import React from "react";
import "./App.css";
import Registration from "./Components/Registration";
import Footer from "./Components/Footer";

const App = () => {
  return (
    <div className="App">
      <div>
        <Registration />
      </div>
      <Footer />
    </div>
  );
};

export default App;
