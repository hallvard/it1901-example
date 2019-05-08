import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import MultiExApp from './MultiExApp';

const defaultLocations = [{latitude: 63.1, longitude: 11.2}, {latitude: 63.2,longitude: 11.0}];

ReactDOM.render(<MultiExApp locations={defaultLocations}/>, document.getElementById('root'));
