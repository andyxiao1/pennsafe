var express = require('express');
var app = express();
const server = require('http').createServer(app);
const path = require('path');
const DBHandler = require("./dbHandler");
const NotificationHandler = require("./notificationHandler");

let accountsHandler = new DBHandler("user_accounts", "user_account_records");
let crimeHandler = new DBHandler("crimes", "crime_records");
let notificationHandler = new NotificationHandler(server);

accountsHandler.init();
crimeHandler.init();

app.use(express.json());
app.use(express.urlencoded());

// socketio.on('connection', () => {
//     console.log("someone connected");
// });

// socketio.on("joined", data => {
//     console.log("joined");
//     console.log(data);
// });

app.get("/users", (req, res) => {
    accountsHandler.findMultipleRecords({}, (err, result) => {
        if (err) {
            return res.json({successful: false, users: []});
        } else {
            return res.json({successful: true, users: result});
        }
    });
});

app.get("/user/:username", (req, res) => {
    let username = req.params.username;
    accountsHandler.findRecord({username}, (err, result) => {
        res.json({successful: !err, user: result});
    });
});

app.post("/user/:username/ban", (req, res) => {
    let username = req.params.username;
    let banned = req.body.banned;
    if (banned !== "undefined") {
        accountsHandler.updateRecord({username}, {banned}, (err, result) => {
            res.json({successful: !err, message: ""});
        });
    } else {
        res.json({successful: false, message: "Invalid request params."});
    }
    
});

app.get('/validateLogin', (req, res) => {
    const { username, password, isAdmin } = req.query;
    if (!username || !password) {
        return res.json({ successful: false, message: "Invalid request params." });
    }

    accountsHandler.findRecord({ username }, (err, result) => {
        if (err) {
            return res.json({ successful: false, message: "Server error." });
        } else if (!result) {
            return res.json({ successful: false, message: "User does not exist." });
        } else if (password != result.password) {
            return res.json({ successful: false, message: "Incorrect password." });
        } else if (isAdmin === 'true' && result.accountType !== 'admin') {
            return res.json({ successful: false, message: "Not an admin." });
        } else {
            return res.json({ successful: true, message: "" });
        }
    });
});

app.post('/signup', (req, res) => {
    let username = req.query.username;
    let password = req.query.password;
    if (!username || !password) {
        return res.json({successful: false, message: "Invalid request params."});
    }
    accountsHandler.findRecord({username}, (err, result) => {
        if (result && result.length) {
            return res.json({
                successful: false,
                message: "User already exists."
            });
        } else if (err) {
            return res.json({
                successful: false,
                message: "Backend error."
            });
        } else {
            accountsHandler.addRecord(
                {
                    username,
                    password,
                    banned: false,
                    accountType: 'user',
                    gps: true,
                    lastLoggedIn: Date.now()
                }, (err, _) => {
                let successful = true;
                let message = "";
                if (err) {
                    successful = false;
                    message = "Backend error."
                }
                return res.json({successful, message});
            });
        }
    });
});

app.post("/deleteAccount", (req, res) => {
    let username = req.query.username;
    if (username) {
        accountsHandler.deleteMatchingRecord({username}, (err, result) => {
            return res.json({successful: !err, message: ""});
        });
    } else {
        return res.json({successful: false, message: "Invalid request params."});
    }
});

app.post("/login", (req, res) => {
    let username = req.query.username;
    let latitude = req.query.latitude;
    let longitude = req.query.longitude;
    if (username) {
        let update = (latitude && longitude) ? 
            {latitude, longitude, lastLoggedIn: Date.now()} :
            {lastLoggedIn: Date.now()};
        accountsHandler.updateRecord({username}, update, (err, result) => {
            return res.json({successful: !err, message: ""});
        });
    } else {
        return res.json({successful: false, message: "Invalid request params."});
    }
});

app.post("/uploadPhoto", (req, res) => {
    let username = req.query.username;
    let image = req.body.image;
    if (username) {
        accountsHandler.updateRecord({username}, {image}, (err, result) => {
            return res.json({successful: !err, message: ""});
        });
    } else {
        return res.json({successful: false, message: "Invalid request params."});
    }
    
});

app.post("/updateUser", (req, res) => {
    let username = req.query.username;
    let update = {};
    let email = req.query.email;
    let telephone = req.query.phone;
    let address = req.query.address;
    let nickname = req.query.nickname;
    let other = req.query.other;
    
    if (email) {
        update["email"] = email;
    }

    if (telephone) {
        update["telephone"] = telephone;
    }

    if (address) {
        update["address"] = address;
    }

    if (nickname) {
        update["nickname"] = nickname;
    }

    if (other) {
        update["other"] = other;
    }

    if (username) {
        accountsHandler.updateRecord({username}, update, (err, result) => {
            return res.json({successful: !err, message: ""});
        });
    } else {
        return res.json({successful: false, message: "Invalid request params."});
    }
});

app.post("/user/:username/gps", (req, res) => {
    let username = req.params.username;
    let gps = req.query.gps;
    if (username) {
        accountsHandler.updateRecord({username}, {gps}, (err, result) => {
            return res.json({successful: !err, message: ""});
        });
    } else {
        return res.json({successful: false, message: "Invalid request params."});
    }
    
});

app.post('/setCrime', (req, res) => {
    let crime = req.body.username;
    console.log(crime);
    // if (!username || !password) {
    //     return res.json({successful: false, message: "Invalid request params."});
    // }
    // accountsHandler.findRecord({username}, (err, result) => {
    //     if (result && result.length) {
    //         return res.json({
    //             successful: false,
    //             message: "User already exists."
    //         });
    //     } else if (err) {
    //         return res.json({
    //             successful: false,
    //             message: "Backend error."
    //         });
    //     } else {
    //         accountsHandler.addRecord(
    //             {
    //                 username,
    //                 password,
    //                 banned: false,
    //                 accountType: 'user',
    //                 gps: true,
    //                 lastLoggedIn: Date.now()
    //             }, (err, _) => {
    //             let successful = true;
    //             let message = "";
    //             if (err) {
    //                 successful = false;
    //                 message = "Backend error."
    //             }
    //             return res.json({successful, message});
    //         });
    //     }
    // });
});

app.use(express.static(path.join(__dirname, '/../webapp/build/')));

server.listen(3000, () => {
    console.log('Listening on port 3000');
});