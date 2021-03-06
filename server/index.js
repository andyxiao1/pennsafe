var express = require('express');
var app = express();
const server = require('http').createServer(app);
const path = require('path');
const DBHandler = require("./dbHandler");
const NotificationHandler = require("./notificationHandler");
const csv = require('csv-parser');
const fs = require('fs');
const count = 0

let accountsHandler = new DBHandler("user_accounts", "user_account_records");
let crimeHandler = new DBHandler("crimes", "crime_records");
const bluelightsHandler = new DBHandler("crimes", "blue_lights");
let notificationHandler = new NotificationHandler(server);

accountsHandler.init();
crimeHandler.init();
bluelightsHandler.init();

setInterval(intervalFunc, 1000);

function intervalFunc() {
    try {
        crimeHandler.deleteAll( () => {

            fs.createReadStream('crimedata.csv')
        .pipe(csv())
        .on('data', (row) => {
            let crime = {
                    date: row['date'],
                    time: row['time'],
                    description: row['description'],
                    latitude: row['latitude'],
                    longitude: row['longitude']
                };

                crimeHandler.findRecord(crime, (err, result) => {
                    if (!result) {
                        crimeHandler.addRecord( crime, (err, _) => {
                            let successful = true;
                            let message = "";
                            if (err) {
                                successful = false;
                                message = "Backend error."
                            }
                        });
                    } else if (err) {
                        return res.json({
                            successful: false,
                            message: "Backend error."
                        });
                    }
                });
        })
            
        } );
    }
        catch (e) { console.log(e); 
    }
}

app.use(express.json());
app.use(express.urlencoded());

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

app.get("/crimes", (req, res) => {
    crimeHandler.findMultipleRecords({}, (err, result) => {
        if (err) {
            return res.json({successful: false, crimes: []});
        } else {
            return res.json({successful: true, crimes: result});
        }
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

app.post("/notification", (req, res) => {
    if (!req.body.title || !req.body.message) {
        res.json({successful: false});
    } else {
        notificationHandler.send(req.body.title, req.body.message);
        res.json({successful: true});
    }
});

app.post('/insertBlueLight', (req, res) => {
    const { latitude, longitude } = req.body;
    if (!latitude || !longitude) {
        res.json({ successful: false, message: 'invalid lat/long' });
    }

    bluelightsHandler.addRecord({ latitude, longitude }, (err, result) => {
        if (err) {
            res.json({ successful: false, message: err.message});
        } else {
            res.json({ successful: true, message: '' });
        }
    })
    // const bluelights = [{latitude: 39.950755, longitude: -75.198928},
    //     { latitude: 39.961587, longitude: -75.208887},
    //     { latitude: 39.958889, longitude: -75.204404},
    //     { latitude: 39.953653, longitude: -75.185253},
    //     { latitude: 39.962250, longitude: -75.198484}]
})

app.get('/getBlueLights', (req, res) => {
    bluelightsHandler.findMultipleRecords({}, (err, result) => {
        if (err) {
            res.json({ successful: false, message: err.message });
        } else {
            res.json({ successful: true, message: '', bluelights: result})
        }
    })
})


app.use(express.static(path.join(__dirname, '/../webapp/build/')));

server.listen(3000, () => {
    console.log('Listening on port 3000');
});