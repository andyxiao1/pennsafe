const socketio = require('socket.io');

class NotificationHandler {

    constructor(server) {
        this.io = socketio(server);
        this.io.on('connection', () => {
            console.log("someone connected");
        });
        
        this.io.on("joined", data => {
            console.log("joined");
            console.log(data);
        });
        this.test();
    }

    test() {
        if (this.io) this.io.emit("safety-app-notification", {
            title: "Weather Warning",
            message: "Please be advised, until 8 PM April 22, there will be high winds."
        });
        setTimeout(() => this.test(), 5000);
    }

}

module.exports = NotificationHandler;