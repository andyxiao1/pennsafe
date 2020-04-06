import React from 'react';
import './CurrentWeather.css';

const CurrentWeather = ({
  data: {
    temp,
    humidity,
    clouds,
    wind_speed,
    uvi,
    weather: [{ main, icon }]
  }
}) => {
  return (
    <>
      <h5>Weather</h5>
      <h4>University of Pennsylvania</h4>
      <h5>Philadelphia, PA</h5>
      <br />
      <div>{temp}° F</div>
      <img
        height={100}
        width={100}
        src={`http://openweathermap.org/img/w/${icon}.png`}
        alt="Weather Icon"
      />
      <div>{main}</div>
      <div className="CurrentWeather-details">
        <div className="row">
          <div>Wind: {wind_speed}mph</div>
          <div>UV Index: {uvi}</div>
        </div>
        <div className="row">
          <div>Humidity: {humidity}%</div>
          <div>Cloudiness: {clouds}%</div>
        </div>
      </div>
    </>
  );
};

export default CurrentWeather;
