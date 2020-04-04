var express = require('express');
var app = express();

let users = new Set();
users.add("a");

app.get('/validateLogin', (req, res) => {
    let username = req.query.username;
    let password = req.query.password;
    if (username === "a") {
        if (password === "b") {
            res.json({
                "successful": true,
                "message": ""
            });
        } else {
            res.json({
                "successful": false,
                "message": "Incorrect password."
            });
        }
    } else {
        res.json({
            "successful": false,
            "message": "User does not exist."
        });
    }
});

app.get('/signup', (req, res) => {
    let username = req.query.username;
    let password = req.query.password;
    if (users.has(username)) {
        res.json({
            "successful": false,
            "message": "User already exists."
        });
    } else {
        res.json({
            "successful": true,
            "message": ""
        });
    }
});

app.listen(3000, () => {
    console.log('Listening on port 3000');
});