import React from 'react';
import ReactDOM from 'react-dom';
import MultiExApp from './MultiExApp';
import {render, fireEvent} from 'react-testing-library'
import 'jest-dom/extend-expect'

// based on https://kentcdodds.com/blog/introducing-the-react-testing-library
test('shows at least one geo-location', async () => {
  	// Arrange
//  	axiosMock.get.mockImplementationOnce(({name}) =>
//    	Promise.resolve({
//      		data: {greeting: `Hello ${name}`},
//    	}),
//  	)
	const {getByLabelText, getByText, getByTestId, container} = render(
    	<MultiExApp locations={[{ latitude: 63.1, longitude: 11.2 }, { latitude: 63.2,longitude: 11.0 }]}/>,
  	)
  	// Act
	var locationElement = getByTestId("listItem0")
  	fireEvent.click(locationElement)
})
