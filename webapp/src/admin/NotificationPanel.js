import React, { Component } from 'react'
import { Button, InputGroup, FormControl } from 'react-bootstrap'
import "./ComponentStyles.css";

class NotificationPanel extends Component {

    constructor(props) {
        super(props);
        this.state = {
            open: false,
            title: null,
            content: null,
            message: null,
            sending: false
        };
    }

    onNewNotification() {
        this.setState({open: true});
    }

    onTitleUpdated(event) {
        this.setState({ title: event.target.value });
    }

    onContentUpdated(event) {
        this.setState({ content: event.target.value });
    }

    onCancel() {
        this.setState({
            open: false,
            title: null,
            content: null
        });
    }

    async onSend() {
        let params = {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                "title": this.state.title,
                "message": this.state.content
            })
        };

        this.setState({
            open: false,
            title: null,
            content: null,
            sending: true,
            message: "Your message is sending..."
        });
        
        let message = "Your message has been sent";
        try {
            const response = await fetch('/notification', params);
            const json = await response.json();
            if (!json || !json.successful) {
                message = "Your message failed to send";
            }
        } catch (e) {
            console.log(e);
            message = "Your message failed to send";
        }

        this.setState({
            open: false,
            message: message,
            sending: false
        });


        setTimeout(() => {
            this.setState({message: null})
        }, 3000);
    }

    render() {
        return (
            <div className="notif_root">
                <Button disabled={this.state.sending} className={this.state.open ? "notif_hidden" : ""} size="sm" variant="success" onClick={() => this.onNewNotification()}>New Notification</Button>
                <span className="notif_message">{this.state.message}</span>
                {
                    this.state.open && (
                        <div className="notif_inputs">
                            <InputGroup className="notif_input_group">
                                <InputGroup.Prepend>
                                    <InputGroup.Text>Title</InputGroup.Text>
                                </InputGroup.Prepend>
                                <FormControl placeholder="Notification Title" onKeyUp={event => this.onTitleUpdated(event)} />
                            </InputGroup>
                            <InputGroup className="notif_input_group">
                                <InputGroup.Prepend>
                                    <InputGroup.Text>Content</InputGroup.Text>
                                </InputGroup.Prepend>
                                <FormControl placeholder="Notification Content" onKeyUp={event => this.onContentUpdated(event)} />
                            </InputGroup>
                            <div>
                                <Button className="notif_button" variant="secondary" size="sm" onClick={() => this.onCancel()}>Cancel</Button>
                                <Button disabled={!this.state.title || !this.state.content} className="notif_button" variant="success" size="sm" onClick={() => this.onSend()}>Send</Button>
                            </div>
                        </div>
                    )
                }
            </div>
        )
    }
}

export default NotificationPanel;