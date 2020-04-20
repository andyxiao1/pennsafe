var express = require('express');
var app = express();
const path = require('path');

let DBHandler = require("./dbHandler");

let accountsHandler = new DBHandler("user_accounts", "user_account_records");
accountsHandler.init();

app.use(express.json());

app.get("/users", (req, res) => {
    accountsHandler.findMultipleRecords({}, (err, result) => {
        if (err) {
            return res.json({ successful: false, users: [] });
        } else {
            return res.json({ successful: true, users: result });
        }
    });
});

app.get("/user/:username", (req, res) => {
    let username = req.params.username;
    accountsHandler.findRecord({ username }, (err, result) => {
        res.json({ successful: !err, user: result });
    });
});

app.post("/user/:username/ban", (req, res) => {
    let username = req.params.username;
    let banned = req.body.banned;
    if (banned !== "undefined") {
        accountsHandler.updateRecord({ username }, { banned }, (err, result) => {
            res.json({ successful: !err, message: "" });
        });
    } else {
        res.json({ successful: false, message: "Invalid request params." });
    }

});

app.get('/validateLogin', (req, res) => {
    let username = req.query.username;
    let password = req.query.password;
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
        } else {
            return res.json({ successful: true, message: "" });
        }
    });
});

app.post('/signup', (req, res) => {
    let username = req.query.username;
    let password = req.query.password;
    if (!username || !password) {
        return res.json({ successful: false, message: "Invalid request params." });
    }
    accountsHandler.findRecord({ username }, (err, result) => {
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
                    lastLoggedIn: Date.now()
                }, (err, _) => {
                    let successful = true;
                    let message = "";
                    if (err) {
                        successful = false;
                        message = "Backend error."
                    }
                    return res.json({ successful, message });
                });
        }
    });
});

app.post("/deleteAccount", (req, res) => {
    let username = req.query.username;
    if (username) {
        accountsHandler.deleteMatchingRecord({ username }, (err, result) => {
            return res.json({ successful: !err, message: "" });
        });
    } else {
        return res.json({ successful: false, message: "Invalid request params." });
    }
});

app.post("/login", (req, res) => {
    let username = req.query.username;
    let latitude = req.query.latitude;
    let longitude = req.query.longitude;
    console.log(`login: ${username}`);
    if (username) {
        let update = (latitude && longitude) ?
            { latitude, longitude, lastLoggedIn: Date.now() } :
            { lastLoggedIn: Date.now() };
        accountsHandler.updateRecord({ username }, update, (err, result) => {
            return res.json({ successful: !err, message: "" });
        });
    } else {
        return res.json({ successful: false, message: "Invalid request params." });
    }
});

app.use(express.static(path.join(__dirname, '/../webapp/build/')));

app.listen(3000, () => {
    console.log('Listening on port 3000');
});