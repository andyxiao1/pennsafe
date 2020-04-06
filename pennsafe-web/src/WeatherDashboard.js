import React, { Component } from 'react';
import axios from 'axios';
import './WeatherDashboard.css';

// constants - TODO put somewhere better/safer
const PENN_LATITUDE = '39.9522';
const PENN_LONGITUDE = '-75.1932';
const WEATHER_API_KEY = 'ed797d5e04b1dc08c14f6bce3cbb19f1';

class WeatherDashboard extends Component {
  state = {
    data: []
  };

  componentDidMount() {
    axios
      .get(
        `https://api.openweathermap.org/data/2.5/onecall?lat=${PENN_LATITUDE}&lon=${PENN_LONGITUDE}&appid=${WEATHER_API_KEY}`
      )
      .then(res => {
        console.log(res);
        console.log(Object.keys(res));
        console.log(res.data);
      })
      .catch(err => console.log(err));
  }

  render() {
    return (
      <>
        <div>Test</div>
      </>
    );
  }
}

export default WeatherDashboard;
