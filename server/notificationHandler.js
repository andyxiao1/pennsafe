const socketio = require('socket.io');

class NotificationHandler {

    constructor(server) {
        this.io = socketio(server);
    }

    send(title, message) {
        if (this.io) {
            this.io.emit("safety-app-notification", { title, message });
        }
    }

}

module.exports = NotificationHandler;