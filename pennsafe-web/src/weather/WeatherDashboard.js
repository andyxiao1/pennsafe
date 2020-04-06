import React, { Component } from 'react';
import axios from 'axios';
import _ from 'lodash';
import './WeatherDashboard.css';
import dummyData from '../data/DummyWeatherData';

// constants - TODO put somewhere better/safer
const PENN_LATITUDE = '39.9522';
const PENN_LONGITUDE = '-75.1932';
const WEATHER_API_KEY = 'ed797d5e04b1dc08c14f6bce3cbb19f1';

class WeatherDashboard extends Component {
  state = {
    data: null
  };

  componentDidMount() {
    // axios
    //   .get(
    //     `https://api.openweathermap.org/data/2.5/onecall?lat=${PENN_LATITUDE}&lon=${PENN_LONGITUDE}&appid=${WEATHER_API_KEY}&units=imperial`
    //   )
    //   .then(({ data }) => this.setState({ data }))
    //   .catch(err => console.log(err));

    // use dummy data to save api calls
    this.setState({ data: dummyData });
  }

  render() {
    const { data } = this.state;
    if (
      !data ||
      _.isEmpty(data) ||
      _.isEmpty(data.current) ||
      _.isEmpty(data.hourly)
    ) {
      return null;
    }
    const {
      temp,
      feels_like,
      humidity,
      clouds,
      wind_speed,
      uvi,
      weather: [{ main, icon }]
    } = data.current;
    return (
      <div className="WeatherDashboard">
        <h4>Weather</h4>
        <h5>University of Pennsylvania</h5>
        <h6>Philadelphia, PA</h6>
        <img
          src={`http://openweathermap.org/img/w/${icon}.png`}
          alt="Weather Icon"
        />
        <div>{temp}°</div>
        <div>Feels like {feels_like}°</div>
        <div>Humidity: {humidity}%</div>
        <div>Cloudiness: {clouds}%</div>
        <div>Wind: {wind_speed}mph</div>
        <div>UV Index: {uvi}</div>
        <div>Weather: {main}</div>
      </div>
    );
  }
}

export default WeatherDashboard;
