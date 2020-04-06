import React from 'react';
import WeatherDashboard from './WeatherDashboard';
import Feed from './Feed';
import './App.css';

const App = () => {
  return (
    <div className="App">
      <div className="App-header">PennSafe</div>
      <WeatherDashboard />
      {/* <Feed /> */}
    </div>
  );
};

export default App;
