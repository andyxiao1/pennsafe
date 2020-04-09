import React, { Component } from 'react'
import {Container, InputGroup, Spinner, FormControl, Table, Button} from 'react-bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css';
import "./AdminPage.css";
import MetricsComponent from './components/MetricsComponent';
import Util from "./Util";


class AdminPage extends Component {

    constructor(props) {
        super(props);
        this.sortMethods = {
            USERNAME: "username",
            TIME: "time",
            LOCATION: "location"
        };
        this.state = {
            usersLoaded: false,
            error: false,
            filter: null,
            sortMethod: this.sortMethods.USERNAME,
            users: [],
            errorMessage: null
        };
        this.debug = false;
    }

    componentDidMount() {
        this.loadUsers();
    }

    loadTestData() {
        this.setState({
            usersLoaded: true,
            users: [{
                "username": "aaaaa",
                "banned": false,
                "latitude": 34,
                "longitude": -76,
                "lastLoggedIn": 1586379883003
            },
            {
                "username": "ddddd",
                "banned": true,
                "latitude": 39,
                "longitude": -75,
                "lastLoggedIn": 1586279835109
            },{
                "username": "bbbbb",
                "banned": false,
                "latitude": 39,
                "longitude": -74,
                "lastLoggedIn": 1586379281003
            },{
                "username": "badd",
                "banned": false,
                "latitude": 39.9,
                "longitude": -75.2,
                "lastLoggedIn": 1586979891003
            }]
        });
    }

    async loadUsers() {
        if (this.debug) {
            this.loadTestData();
            return;
        }
        let params = {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            }
        };
        const response = await fetch("/users", params);
        try {
            const json = await response.json();
            if (!json.successful) {
                this.setState({usersLoaded: true, error: true, errorMessage: json.message});
            } else {
                this.setState({usersLoaded: true, users: json.users, error: false});
            }
        } catch (e) {
            console.log(e);
            this.setState({usersLoaded: false, users: [], error: true, errorMessage: "Error fetching data, server might be down."});
        }
        
    }

    onFilterUpdated(event) {
        this.setState({filter: event.target.value});
    }

    async onBanClick(user, newValue) {
        let params = {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({ "banned": newValue })
        };
        const response = await fetch(`/user/${user}/ban`, params);
        try {
            const json = await response.json();
            if (!json.successful) {
                this.setState({error: true, errorMessage: json.message});
                setTimeout(() => {
                    this.setState({error: false, errorMessage: null});
                }, 3000);
            } else {
                this.setState({usersLoaded: false, users: []});
                this.loadUsers();
            }
        } catch (e) {
            console.log(e);
            this.setState({error: true, errorMessage: "Failed to ban user, server error."});
            setTimeout(() => {
                this.setState({error: false, errorMessage: null});
            }, 3000);
        }
        
    }

    trimNumber(n, x) {
        if (x.toString().length > n) {
            if (x.toString().indexOf(".") > n - 1) {
                return x.toString().substring(0, x.toString().indexOf("."));
            } else {
                return x.substring(0, n);
            }
        } else {
            return x.toString();
        }
    }
    
    getTimeString(d) {
        let monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
        let period = d.getHours() >= 12 ? "PM" : "AM";
        let minutes = d.getMinutes();
        if (minutes < 10) minutes = "0"+minutes;
        return `${d.getHours() % 12}:${minutes} ${period}, ${monthNames[d.getMonth()]} ${d.getDate()}`;
    }

    getSortedUsers() {
        let pennLat = 39.951980;
        let pennLng = -75.193804;
        let f =
            this.state.sortMethod === this.sortMethods.USERNAME ? (a, b) => a.username === b.username ? 0 : a.username > b.username ? 1 : -1 :
            this.state.sortMethod === this.sortMethods.TIME ? (a, b) => {
                let x = a.lastLoggedIn;
                let y = b.lastLoggedIn;
                return x === y ? 0 : x < y ? 1 : -1;
            }  :
            this.state.sortMethod === this.sortMethods.LOCATION ? (a, b) => {
                let d1 = Util.haversineDistance(pennLat, pennLng, a.latitude, a.longitude);
                let d2 = Util.haversineDistance(pennLat, pennLng, b.latitude, b.longitude);
                return d1 === d2 ? 0 : d1 > d2 ? 1 : -1;
            } :
            null;
        let users = this.state.users.slice();
        users.sort(f);
        return users;
    }

    renderTable() {
        let sortedUsers = this.getSortedUsers();
        let sortUsernameClass = "sort_button"+(this.state.sortMethod === this.sortMethods.USERNAME ? "_selected" : "");
        let sortTimeClass = "sort_button"+(this.state.sortMethod === this.sortMethods.TIME ? "_selected" : "");
        let sortLocationClass = "sort_button"+(this.state.sortMethod === this.sortMethods.LOCATION ? "_selected" : "");
        return (
            <div>
                <InputGroup>
                    <InputGroup.Prepend>
                        <InputGroup.Text>Search Users</InputGroup.Text>
                    </InputGroup.Prepend>
                    <FormControl placeholder="Username" onKeyUp={event => this.onFilterUpdated(event)}/>
                </InputGroup>
                <div className="sort_buttons">
                    <Button className={sortUsernameClass} size="sm" onClick={() => this.setState({sortMethod: this.sortMethods.USERNAME})}>Username</Button>
                    <Button className={sortTimeClass} size="sm" onClick={() => this.setState({sortMethod: this.sortMethods.TIME})}>Login Time</Button>
                    <Button className={sortLocationClass} size="sm" onClick={() => this.setState({sortMethod: this.sortMethods.LOCATION})}>Distance from Penn</Button>
                </div>
                <Table bordered hover>
                    <thead>
                        <tr>
                            <th>Username</th>
                            <th>Status</th>
                            <th>Last Logged In</th>
                            <th>Last Location</th>
                            <th>Ban User</th>
                        </tr>
                    </thead>
                    <tbody>
                        {
                            sortedUsers
                                .filter(user => !this.state.filter || user.username.startsWith(this.state.filter))
                                .map(user => {
                                let bannedClass = (user.banned ? "banned" : "active") + "_user";
                                let banned = <span className={bannedClass}>{user.banned ? "Banned" : "Allowed"}</span>
                                let date = new Date(user.lastLoggedIn);
                                return (
                                    <tr key={user.username}>
                                        <td>{user.username}</td>
                                        <td>{banned}</td>
                                        <td>{isNaN(date.getTime()) ? "Unknown" : this.getTimeString(date)}</td>
                                        <td>
                                            {
                                                user.latitude && user.longitude ? (
                                                    <span>{this.trimNumber(7, user.latitude)}, {this.trimNumber(7, user.longitude)}</span>
                                                ) : (
                                                    <span>Unknown</span>
                                                )
                                            }
                                        </td>
                                        <td>
                                            <Button variant="sm" className="ban_button" onClick={() => this.onBanClick(user.username, !user.banned)}>
                                                {user.banned ? "Un-ban" : "Ban"}
                                            </Button>
                                        </td>
                                    </tr>
                                )
                                
                            })
                        }
                    </tbody>
                </Table>
            </div>
        );
    }

    render() {
        return (
            <Container className="admin_page">
                <span className="title">Admin Console</span>
                <span className="error_text">
                    { this.state.error && this.state.errorMessage }
                </span>
                <hr/>
                <div style={{textAlign: "center"}}>
                    { !this.state.error && !this.state.usersLoaded && <Spinner animation="border" variant="secondary"/> }
                </div>
                { this.state.usersLoaded && <MetricsComponent users={this.state.users}/> }
                { this.state.usersLoaded && this.renderTable() }
            </Container>
        )
    }
}

export default AdminPage;