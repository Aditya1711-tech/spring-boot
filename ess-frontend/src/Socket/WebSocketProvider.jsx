import React, { createContext, useContext, useEffect, useState } from 'react';

const WebSocketContext = createContext(null);

export const WebSocketProvider = ({ children }) => {
    const [socket, setSocket] = useState(null);
    const [notifications, setNotifications] = useState([]);

    useEffect(() => {
        const token = localStorage.getItem('jwtToken'); // Replace with your token retrieval logic
        const newSocket = new WebSocket(`ws://localhost:8080/ws?token=${encodeURIComponent(token)}`);

        // Set up event listeners
        newSocket.onopen = () => {
            console.log('Connected to the WebSocket server');
        };

        newSocket.onmessage = (event) => {
            const data = JSON.parse(event.data);
            console.log('Notification received:', data);
            setNotifications((prev) => [...prev, data]);
        };

        newSocket.onerror = (error) => {
            console.error('WebSocket error:', error);
        };

        newSocket.onclose = () => {
            console.log('WebSocket connection closed');
        };

        // Store the socket
        setSocket(newSocket);

        // Cleanup on component unmount
        // return () => {
        //     newSocket.close();
        // };
    }, []);

    return (
        <WebSocketContext.Provider value={{ socket, notifications }}>
            {children}
        </WebSocketContext.Provider>
    );
};

export const useWebSocket = () => {
    return useContext(WebSocketContext);
};
