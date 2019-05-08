import React from 'react';

import './MultiExApp.css';

import LatLongsComponent from './LatLongsComponent.js';

const defaultLatLocations = [{latitude: 63.1, longitude: 11.2}, {latitude: 63.2,longitude: 11.0}];

function MultiExApp() {
  return (
    <div className="MultiExApp">
		<LatLongsComponent locations={defaultLatLocations}/>
    </div>
  );
}

export default MultiExApp;
