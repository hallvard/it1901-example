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
    	<MultiExApp />,
  	)
  	// Act
	var latLongElement = getByTestId("latLong0")
  	fireEvent.click(latLongElement)
})
