class WebSocketManager {
    constructor() {
        if (!WebSocketManager.instance) {
            this.socket = null;
            this.listeners = [];
            WebSocketManager.instance = this;
        }
        return WebSocketManager.instance;
    }

    connect(url) {
        if (!this.socket || this.socket.readyState !== WebSocket.OPEN) {
            this.socket = new WebSocket(url);
            this.socket.onmessage = (event) => {
                const data = JSON.parse(event.data);
                this.listeners.forEach((listener) => listener(data));
            };
            this.socket.onopen = () => console.log("WebSocket connected");
            this.socket.onerror = (error) => console.error("WebSocket error:", error);
            this.socket.onclose = () => console.log("WebSocket connection closed");
        }
    }

    addListener(listener) {
        this.listeners.push(listener);
    }

    removeListener(listener) {
        this.listeners = this.listeners.filter((l) => l !== listener);
    }

    sendMessage(message) {
        if (this.socket && this.socket.readyState === WebSocket.OPEN) {
            this.socket.send(JSON.stringify(message));
        }
    }

    close() {
        if (this.socket) {
            this.socket.close();
        }
    }
}

const instance = new WebSocketManager();
Object.freeze(instance);

export default instance;
