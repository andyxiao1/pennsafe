import React, { Component } from 'react';
import { Container, Row, Col } from 'react-bootstrap';
import crimeData from '../data/DummyCrimeData.json';
import _ from 'lodash';
import {
  XYPlot,
  XAxis,
  YAxis,
  HorizontalGridLines,
  VerticalGridLines,
  VerticalBarSeries,
  RadialChart,
} from 'react-vis';
import 'react-vis/dist/style.css';

class Statistics extends Component {
  state = {
    classificationData: [],
    classificationPieData: [],
    timeData: [],
    dateData: [],
  };

  componentDidMount() {
    this.loadClassificationData();
    this.loadTimeData();
    this.loadDateData();
  }

  loadClassificationData = () => {
    const classificationDataObj = {};
    crimeData.forEach(({ classification }) => {
      if (classificationDataObj.hasOwnProperty(classification)) {
        classificationDataObj[classification]++;
      } else {
        classificationDataObj[classification] = 1;
      }
    });
    const classificationData = Object.keys(classificationDataObj).map(
      (key) => ({
        x: key,
        y: classificationDataObj[key],
      })
    );
    const classificationPieData = classificationData.map((val) => ({
      angle: val.y,
      label: val.x,
    }));
    this.setState({ classificationData, classificationPieData });
  };

  loadTimeData = () => {
    const timeDataObj = {};
    crimeData.forEach(({ time }) => {
      if (timeDataObj.hasOwnProperty(time)) {
        timeDataObj[time]++;
      } else {
        timeDataObj[time] = 1;
      }
    });
    const timeData = Object.keys(timeDataObj).map((key) => ({
      x: key,
      y: timeDataObj[key],
    }));
    this.setState({ timeData });
  };

  loadDateData = () => {
    const dateDataObj = {};
    crimeData.forEach(({ date }) => {
      if (dateDataObj.hasOwnProperty(date)) {
        dateDataObj[date]++;
      } else {
        dateDataObj[date] = 1;
      }
    });
    const dateData = Object.keys(dateDataObj).map((key) => ({
      x: key.slice(5, 10),
      y: dateDataObj[key],
    }));
    this.setState({ dateData });
  };

  render() {
    const {
      classificationData,
      classificationPieData,
      timeData,
      dateData,
    } = this.state;
    if (
      _.isEmpty(classificationData) ||
      _.isEmpty(classificationPieData) ||
      _.isEmpty(timeData) ||
      _.isEmpty(dateData)
    ) {
      return null;
    }
    return (
      <Container className="text-center">
        <h1>Statistics</h1>
        <Row>
          <Col>
            <XYPlot width={500} height={500} xType="ordinal">
              <HorizontalGridLines />
              <VerticalGridLines />
              <VerticalBarSeries data={classificationData} />
              <XAxis />
              <YAxis />
            </XYPlot>
          </Col>
          <Col>
            <RadialChart
              width={500}
              height={500}
              data={classificationPieData}
              labelsStyle={{ fontSize: 20 }}
              showLabels
            />
          </Col>
        </Row>
        <Row>
          <Col>
            <XYPlot width={500} height={500} color="green" xType="ordinal">
              <HorizontalGridLines />
              <VerticalGridLines />
              <VerticalBarSeries data={dateData} />
              <XAxis />
              <YAxis />
            </XYPlot>
          </Col>
          <Col>
            {' '}
            <XYPlot width={500} height={500} color="red" xType="ordinal">
              <HorizontalGridLines />
              <VerticalGridLines />
              <VerticalBarSeries data={timeData} />
              <XAxis />
              <YAxis />
            </XYPlot>
          </Col>
        </Row>
      </Container>
    );
  }
}

export default Statistics;
