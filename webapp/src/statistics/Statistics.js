import React, { Component } from 'react';
import { Container, Row, Col } from 'react-bootstrap';

class Statistics extends Component {
  state = {};

  render() {
    return (
      <Container className="text-center">
        <Row>
          <Col>
            <h1>Statistics</h1>
            <h4>Statistics</h4>
            <h4>Bar Graphs</h4>
            <h4>Charts</h4>
          </Col>
        </Row>
      </Container>
    );
  }
}

export default Statistics;
