import React, { Component } from 'react';
import { BrowserRouter as Router, Switch, Route, Link } from 'react-router-dom';
import { Container, Row, Col, Nav, Navbar } from 'react-bootstrap';
import Home from './home/Home';
import Login from './login/Login';
import Statistics from './statistics/Statistics';
import AdminPage from './admin/AdminPage';
import Bluelights from './bluelights/Bluelights';

class App extends Component {
  state = { account: 'Logged Out' };

  updateAccount = (account) => this.setState({ account });

  render() {
    const { account } = this.state;
    const isAdmin = account === 'Admin';
    return (
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
                  {isAdmin && (
                    <>
                      <Nav.Item>
                        <Nav.Link eventKey="admin" as={Link} to="/admin">
                          Admin
                        </Nav.Link>
                      </Nav.Item>
                      <Nav.Item>
                        <Nav.Link
                          eventKey="bluelights"
                          as={Link}
                          to="/bluelights"
                        >
                          Bluelights
                        </Nav.Link>
                      </Nav.Item>
                    </>
                  )}
                </Nav>
                <Navbar.Collapse className="justify-content-end">
                  <Navbar.Text>Account: {account}</Navbar.Text>
                </Navbar.Collapse>
              </Navbar>
            </Col>
          </Row>
          <Row>
            <Col>
              {/* Sets up URL routes */}
              <Switch>
                <Route path="/statistics" component={Statistics}></Route>
                <Route
                  path="/login"
                  render={(props) => (
                    <Login onAccountChange={this.updateAccount} {...props} />
                  )}
                ></Route>
                {isAdmin && (
                  <>
                    <Route path="/admin" component={AdminPage}></Route>
                    <Route path="/bluelights" component={Bluelights}></Route>
                  </>
                )}
                <Route path="/" component={Home}></Route>
              </Switch>
            </Col>
          </Row>
        </Container>
      </Router>
    );
  }
}

export default App;
