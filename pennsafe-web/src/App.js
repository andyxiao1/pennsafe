import React from 'react';
import Card from './shared/Card';
import WeatherDashboard from './weather/WeatherDashboard';
import Feed from './feed/Feed';
import './App.css';

const App = () => {
  return (
    <div className="App">
      <Card>
        <WeatherDashboard />
      </Card>
      <Card>
        <Feed />
      </Card>
    </div>
  );
};

export default App;
