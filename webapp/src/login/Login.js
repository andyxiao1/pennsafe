import React, { Component } from 'react';
import { Form, Button, Container } from 'react-bootstrap';

const initialState = { email: '', password: '', isAdmin: false };

class Login extends Component {
  state = initialState;

  handleChange = ({ target: { name, value, checked } }) => {
    this.setState({ [name]: name === 'isAdmin' ? checked : value });
  };

  handleSubmit = (e) => {
    e.preventDefault();
    // TODO handle submit
    this.setState(initialState);
  };

  render() {
    const { email, password, isAdmin } = this.state;
    return (
      <Container>
        <Form onSubmit={this.handleSubmit}>
          <Form.Group controlId="formEmail">
            <Form.Label>Email address</Form.Label>
            <Form.Control
              name="email"
              onChange={this.handleChange}
              value={email}
              type="email"
              placeholder="Enter email"
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
        </Form>
      </Container>
    );
  }
}

export default Login;
