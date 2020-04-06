import React, { Component } from 'react';
import { TwitterTimelineEmbed } from 'react-twitter-embed';

import './Feed.css';

class Feed extends Component {
  state = {
    showPennDPS: true
  };

  changeFeed = showPennDPS => {
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
      options: { height: '80vh' }
    };
    return (
      <div className="Feed">
        <h5>Safety Feed</h5>
        <button onClick={this.changeFeed(true)}>Penn Public Safety</button>
        <button onClick={this.changeFeed(false)}>Philadelphia Police</button>
        {/* TwitterTimelineEmbed not updating on prop change, so resorted to this showing/hiding method using css */}
        <div style={{ display: !showPennDPS && 'none' }}>
          <TwitterTimelineEmbed {...twitterProps} screenName="PennDPS" />
        </div>
        <div style={{ display: showPennDPS && 'none' }}>
          <TwitterTimelineEmbed {...twitterProps} screenName="PhillyPolice" />
        </div>
      </div>
    );
  }
}

export default Feed;
