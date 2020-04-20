import React, { Component } from 'react';
import { TwitterTimelineEmbed } from 'react-twitter-embed';
import { Card, Row, Col, Button } from 'react-bootstrap';

class Feed extends Component {
  state = {
    showPennDPS: true,
  };

  changeFeed = (showPennDPS) => {
    return () => this.setState({ showPennDPS });
  };

  render() {
    const { showPennDPS } = this.state;
    const twitterProps = {
      sourceType: 'profile',
      theme: 'light',
      noBorder: true,
      noHeader: true,
      noFooter: true,
      noScrollbar: true,
      options: { height: '80vh' },
    };
    return (
      <Card className="p-3 mt-3 text-center">
        <h1>Safety Feed</h1>
        <Row className="mb-3">
          <Col>
            <Button
              variant="primary"
              className="mr-3"
              onClick={this.changeFeed(true)}
            >
              Penn Public Safety
            </Button>
            <Button variant="primary" onClick={this.changeFeed(false)}>
              Philadelphia Police
            </Button>
          </Col>
        </Row>
        {/* TwitterTimelineEmbed not updating on prop change, so resorted to this showing/hiding method using css */}
        <div style={{ display: !showPennDPS && 'none' }}>
          <TwitterTimelineEmbed {...twitterProps} screenName="PennDPS" />
        </div>
        <div style={{ display: showPennDPS && 'none' }}>
          <TwitterTimelineEmbed {...twitterProps} screenName="PhillyPolice" />
        </div>
      </Card>
    );
  }
}

export default Feed;
