import React, { Component } from 'react'
import { Row, Col } from 'react-bootstrap';
import "./ComponentStyles.css";
import Util from "../utils/Util";

class MetricsComponent extends Component {

    getTimingMetrics() {
        let now = new Date().getTime();
        let lastHour = new Date(now - (1000 * 60 * 60));
        let lastDay = new Date(now - (1000 * 60 * 60 * 24));
        let lastWeek = new Date(now - (1000 * 60 * 60 * 24 * 7));
        let lastMonth = new Date(now - (1000 * 60 * 60 * 24 * 30));
        let data = [0, 0, 0, 0];
        this.props.users.forEach(user => {
            if (user.lastLoggedIn >= lastHour) data[0]++;
            if (user.lastLoggedIn >= lastDay) data[1]++;
            if (user.lastLoggedIn >= lastWeek) data[2]++;
            if (user.lastLoggedIn >= lastMonth) data[3]++;
        });
        return { names: ["hour", "day", "week", "month"], data };
    }

    getLocationMetrics() {
        let pennLat = 39.951980;
        let pennLng = -75.193804;

        let data = [0, 0, 0, 0];
        this.props.users.forEach(user => {
            let d = Util.haversineDistance(pennLat, pennLng, user.latitude, user.longitude);
            if (d <= 1) data[0]++;
            if (d <= 5) data[1]++;
            if (d <= 10) data[2]++;
            if (d <= 100) data[3]++;
        });
        return { names: ["1", "5", "10", "100"], data };
    }

    render() {
        let timingMetrics = this.getTimingMetrics();
        let locationMetrics = this.getLocationMetrics();
        return (
            <div className="metrics_root">
                <Row>
                    <Col>
                        <div className="metrics_section">
                            {
                                timingMetrics.data.map((x, i) => {
                                    return (
                                        <div key={"timing" + i}>
                                            <b>{x}</b> users logged in within the last <b>{timingMetrics.names[i]}</b>
                                        </div>
                                    )
                                })
                            }
                        </div>
                    </Col>
                    <Col>
                        <div className="metrics_section">
                            {
                                locationMetrics.data.map((x, i) => {
                                    return (
                                        <div key={"location" + i}>
                                            <b>{x}</b> users within <b>{locationMetrics.names[i]}</b> mile{i ? "s" : ""} of Penn
                                        </div>
                                    )
                                })
                            }
                        </div>
                    </Col>
                </Row>
            </div>
        )
    }
}

export default MetricsComponent;
