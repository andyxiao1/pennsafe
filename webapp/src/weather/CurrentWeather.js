import React from 'react';
import { Row, Col } from 'react-bootstrap';

const CurrentWeather = ({
  data: {
    temp,
    humidity,
    clouds,
    wind_speed,
    uvi,
    weather: [{ main, icon }],
  },
}) => {
  return (
    <>
      <h1>Weather</h1>
      <h4>University of Pennsylvania</h4>
      <h5 className="mb-4">Philadelphia, PA</h5>
      <h6>{temp}° F</h6>
      <img
        className="align-self-center"
        height={100}
        width={100}
        src={`http://openweathermap.org/img/w/${icon}.png`}
        alt="Weather Icon"
      />
      <h6>{main}</h6>
      <Row className="align-self-center w-50">
        <Col>
          <h6>Wind: {wind_speed}mph</h6>
          <h6>Humidity: {humidity}%</h6>
        </Col>
        <Col>
          <h6>UV Index: {uvi}</h6>
          <h6>Cloudiness: {clouds}%</h6>
        </Col>
      </Row>
    </>
  );
};

export default CurrentWeather;
