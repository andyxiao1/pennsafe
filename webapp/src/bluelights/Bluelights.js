import React, { Component } from 'react';
import { Container, Form, Button } from 'react-bootstrap';
import axios from 'axios';

const initialState = { latitude: '', longitude: '', status: '' };

class Bluelights extends Component {
  state = initialState;

  handleSubmit = (e) => {
    e.preventDefault();
    const { latitude, longitude } = this.state;
    if (!latitude || !longitude) {
      this.setState({ status: 'Missing values!' });
    } else if (isNaN(parseInt(latitude)) || isNaN(parseInt(longitude))) {
      this.setState({ status: 'Invalid inputs!' });
    } else {
      axios
        .post('/insertBlueLight', {
          latitude: parseInt(latitude),
          longitude: parseInt(longitude),
        })
        .then(({ data: { successful, message } }) => {
          if (!successful) {
            throw new Error(message);
          } else {
            this.setState(initialState);
          }
        })
        .catch((err) => {
          this.setState({ status: err.message });
        });
    }
  };

  handleChange = ({ target: { name, value } }) => {
    this.setState({ [name]: value });
  };

  render() {
    const { latitude, longitude, status } = this.state;
    return (
      <Container>
        <Form onSubmit={this.handleSubmit}>
          <Form.Group controlId="latitude">
            <Form.Label>Latitude</Form.Label>
            <Form.Control
              name="latitude"
              onChange={this.handleChange}
              value={latitude}
            />
          </Form.Group>
          <Form.Group controlId="longitude">
            <Form.Label>Longitude</Form.Label>
            <Form.Control
              name="longitude"
              onChange={this.handleChange}
              value={longitude}
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

export default Bluelights;
