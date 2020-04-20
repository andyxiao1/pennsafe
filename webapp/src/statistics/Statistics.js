import React, { Component } from 'react';
import { Container, Row, Col } from 'react-bootstrap';
import crimeData from '../data/DummyCrimeData.json';

class Statistics extends Component {
  state = {};

  render() {
    return (
      <Container className="text-center">
        <Row>
          <Col>
            <h1>Statistics</h1>
          </Col>
        </Row>
      </Container>
    );
  }
}

export default Statistics;
