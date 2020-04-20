import React, { Component } from 'react';
import { Form, Button, Container } from 'react-bootstrap';
import axios from 'axios';

const initialState = { username: '', password: '', isAdmin: false, status: '' };

class Login extends Component {
  state = initialState;

  handleChange = ({ target: { name, value, checked } }) => {
    this.setState({ [name]: name === 'isAdmin' ? checked : value });
  };

  handleSubmit = (e) => {
    e.preventDefault();
    const { username, password, isAdmin } = this.state;
    const { onAccountChange } = this.props;
    axios
      .get(
        `/validateLogin?username=${username}&password=${password}&isAdmin=${isAdmin}`
      )
      .then(({ data }) => {
        if (data.successful) {
          onAccountChange(isAdmin ? 'Admin' : 'User');
          this.setState(initialState);
        } else {
          throw new Error(data.message);
        }
      })
      .catch((err) => {
        console.log(err);
        this.setState({ status: err.message });
      });
  };

  render() {
    const { username, password, isAdmin, status } = this.state;
    return (
      <Container>
        <Form onSubmit={this.handleSubmit}>
          <Form.Group controlId="formUsername">
            <Form.Label>Username</Form.Label>
            <Form.Control
              name="username"
              onChange={this.handleChange}
              value={username}
              placeholder="Enter username"
            />
          </Form.Group>
          <Form.Group controlId="formPassword">
            <Form.Label>Password</Form.Label>
            <Form.Control
              name="password"
              onChange={this.handleChange}
              value={password}
              type="password"
              placeholder="Password"
            />
          </Form.Group>
          <Form.Group controlId="formAdminCheckbox">
            <Form.Check
              name="isAdmin"
              onChange={this.handleChange}
              checked={isAdmin}
              type="checkbox"
              label="Admin?"
            />
          </Form.Group>
          <Button variant="primary" type="submit">
            Submit
          </Button>
          <p className="text-danger">{status}</p>
        </Form>
      </Container>
    );
  }
}

export default Login;
