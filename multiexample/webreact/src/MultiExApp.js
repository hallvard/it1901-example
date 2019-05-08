import React from 'react';
import { Map, Marker, Popup, TileLayer } from 'react-leaflet'

import './MultiExApp.css';

class LocationMap extends React.Component {

	constructor(props) {
		super(props)
	  	this.state = {
	    	marker: props.marker
	    }
	}

	setMarker(marker) {
		this.setState({ marker: marker })
	}

	render() {
		const markerDraggedCallback = (event) => {
			var latLng = event.target._latlng
			this.props.mapMarkerChangedCallback({ latitude: latLng.lat, longitude: latLng.lng })
		}
		const marker = this.state.marker != null ? [this.state.marker.latitude, this.state.marker.longitude] : [0, 0]
    	const markerElement = this.state.marker != null ? (
      		<Marker
				position={marker}
				draggable={true}
          		onDragend={markerDraggedCallback}
				>
        		<Popup>@ {marker[0]}, {marker[1]}</Popup>
      		</Marker>
    	) : null

    	return (
      		<Map center={marker} zoom={8}>
	       		<TileLayer
	          		attribution='&amp;copy <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
	          		url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
	        	/>
	        	{markerElement}
      		</Map>
    	)
  	}
}

class ItemsList extends React.Component {

	constructor(props) {
		super(props)
		this.state = {
			items: this.props.items,
			selectedIndex: 0
		}
	}

	setItems(items) {
		this.setState({ items: items})
	}

	render() {
		var num = -1
		return <table id="itemsList">
			<tbody>
				{
					this.state.items.map(item => {
						num = num + 1
						const index = num
						const handleClick = () => {
							this.setState({ selectedIndex : index})
							this.props.selectedLocationChangedCallback(index)
						}
						var label = this.props.labelProvider(item)
						return <tr key={label} className={num === this.state.selectedIndex ? "selected-list-item" : "list-item-" + (num % 2)}>
							<td data-testid={"listItem" + num} onClick={handleClick}>{label}</td>
						</tr>
					}
				)}
			</tbody>
	    </table>
	}
}

const MultiExApp = ({locations}) => {
	// default marker is first marker in latLongs, or 0, 0 if latLongs is empty
	const marker = locations.length > 0 ? locations[0] : { latitude: 0, longitude: 0}
	// allows access to actual LatLongMap
	const mapRef = React.createRef()
	const listRef = React.createRef()
	const selectedLocationChangedCallback = (num) => {
		mapRef.current.setMarker(locations[num])
	}
	const mapMarkerChangedCallback = (marker) => {
		locations = locations.slice()
		locations.splice(listRef.current.state.selectedIndex, 1, marker)
		listRef.current.setItems(locations)
		mapRef.current.setMarker(marker)
	}
	const location2Label = (location) => location.latitude + ", " + location.longitude
    return <div className="MultiExApp">
 		<table>
			<tbody>
				<tr>
					<td><ItemsList ref={listRef} items={locations} labelProvider={location2Label} selectedLocationChangedCallback={selectedLocationChangedCallback}/></td>
					<td><LocationMap ref={mapRef} marker={marker} mapMarkerChangedCallback={mapMarkerChangedCallback}/></td>
				</tr>
			</tbody>
    	</table>
    </div>
}

export default MultiExApp;
