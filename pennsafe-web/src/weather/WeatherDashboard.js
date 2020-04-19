import React, { Component } from 'react';
import axios from 'axios';
import { Card } from 'react-bootstrap';
import _ from 'lodash';
import dummyData from '../data/DummyWeatherData';
import CurrentWeather from './CurrentWeather';

// constants - TODO put somewhere better/safer
const PENN_LATITUDE = '39.9522';
const PENN_LONGITUDE = '-75.1932';
const WEATHER_API_KEY = 'ed797d5e04b1dc08c14f6bce3cbb19f1';

class WeatherDashboard extends Component {
  state = {
    data: null,
  };

  componentDidMount() {
    // axios
    //   .get(
    //     `https://api.openweathermap.org/data/2.5/onecall?lat=${PENN_LATITUDE}&lon=${PENN_LONGITUDE}&appid=${WEATHER_API_KEY}&units=imperial`
    //   )
    //   .then(({ data }) => this.setState({ data }))
    //   .catch((err) => console.log(err));

    // use dummy data to save api calls
    this.setState({ data: dummyData });
  }

  render() {
    const { data } = this.state;
    if (!data || _.isEmpty(data) || _.isEmpty(data.current)) {
      return null;
    }
    return (
      <Card className="p-3 mt-3 text-center">
        <CurrentWeather data={data.current} />
      </Card>
    );
  }
}

export default WeatherDashboard;
