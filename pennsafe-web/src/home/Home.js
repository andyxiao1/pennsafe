import React from 'react';
import { Row, Col } from 'react-bootstrap';
import WeatherDashboard from '../weather/WeatherDashboard';
import Feed from '../feed/Feed';

const Home = () => (
  <Row>
    <Col>
      <WeatherDashboard />
    </Col>
    <Col>
      <Feed />
    </Col>
  </Row>
);

export default Home;
