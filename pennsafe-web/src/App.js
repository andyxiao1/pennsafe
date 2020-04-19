import React from 'react';
import { BrowserRouter as Router, Switch, Route, Link } from 'react-router-dom';
import { Container, Row, Col, Nav, Navbar } from 'react-bootstrap';
import Home from './home/Home';
import Login from './login/Login';
import Statistics from './statistics/Statistics';

const App = () => (
  <Router>
    <Container fluid>
      <Row>
        <Col>
          <Navbar>
            <Navbar.Brand href="/">PennSafe</Navbar.Brand>
            <Nav variant="pills">
              <Nav.Item>
                <Nav.Link eventKey="home" as={Link} to="/">
                  Home
                </Nav.Link>
              </Nav.Item>
              <Nav.Item>
                <Nav.Link eventKey="login" as={Link} to="/login">
                  Login
                </Nav.Link>
              </Nav.Item>
              <Nav.Item>
                <Nav.Link eventKey="statistics" as={Link} to="/statistics">
                  Statistics
                </Nav.Link>
              </Nav.Item>
            </Nav>
          </Navbar>
        </Col>
      </Row>
      <Row>
        <Col>
          {/* Sets up URL routes */}
          <Switch>
            <Route path="/statistics" component={Statistics}></Route>
            <Route path="/login" component={Login}></Route>
            <Route path="/" component={Home}></Route>
          </Switch>
        </Col>
      </Row>
    </Container>
  </Router>
);

export default App;
