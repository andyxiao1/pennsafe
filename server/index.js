var express = require('express');
var app = express();
let DBHandler = require("./dbHandler");

let accountsHandler = new DBHandler("user_accounts", "user_account_records");
accountsHandler.init();

app.get('/validateLogin', (req, res) => {
    let username = req.query.username;
    let password = req.query.password;
    if (!username || !password) {
        return res.json({successful: false, message: "Invalid request params."});
    }

    accountsHandler.findRecord({username}, (err, result) => {
        if (err) {
            return res.json({successful: false, message: "Server error."});
        } else if (!result.length) {
            return res.json({successful: false, message: "User does not exist."});
        } else if (password != result[0].password) {
            return res.json({successful: false, message: "Incorrect password."});
        } else {
            return res.json({successful: true, message: ""});
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
        if (result.length) {
            return res.json({
                "successful": false,
                "message": "User already exists."
            });
        } else if (err) {
            return res.json({
                "successful": false,
                "message": "Backend error."
            });
        } else {
            accountsHandler.addRecord({username, password}, (err, _) => {
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

app.listen(3000, () => {
    console.log('Listening on port 3000');
});