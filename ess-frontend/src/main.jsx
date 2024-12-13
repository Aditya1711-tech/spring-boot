import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App.jsx";
import "./index.css";
import { AppProvider } from "./context/appContext.jsx";
import { WebSocketProvider } from "./Socket/WebSocketProvider.jsx";

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <WebSocketProvider>
      <AppProvider>
        <App />
      </AppProvider>
    </WebSocketProvider>
  </React.StrictMode>
);
