import React from 'react';
import { Map, Marker, Popup, TileLayer } from 'react-leaflet'

class LatLongMap extends React.Component {

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
		console.log("Rendering marker: " + this.state.marker)
		const center = this.state.marker != null ? this.state.marker : [0, 0]
    	const markerElement = this.state.marker != null ? (
      		<Marker
				position={this.state.marker}
				draggable={true}
          		onDragend={this.props.markerDragged}
				>
        		<Popup>@ {this.state.marker[0]}, {this.state.marker[1]}</Popup>
      		</Marker>
    	) : null

    	return (
      		<Map ref={this.mapRef} center={center} zoom={8}>
	       		<TileLayer
	          		attribution='&amp;copy <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
	          		url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
	        	/>
	        	{markerElement}
      		</Map>
    	)
  	}
}

class LatLongList extends React.Component {

	constructor(props) {
		super(props)
		this.state = {
			latLongs: props.locations,
			selectedIndex: 0
		}
	}

	updateSelectedLatLong(latLng) {
		var copy = this.state.latLongs.slice()
		copy.splice(this.state.selectedIndex, 1, { latitude: latLng.lat, longitude: latLng.lng })
		this.setState({ latLongs: copy })
		this.props.latLongMapRef.current.setMarker([latLng.lat, latLng.lng])
	}

	render() {
		var num = -1
		return <table id="latLongList">
			<tbody>
				{
					this.state.latLongs.map(latLong => {
						num = num + 1
						const lat = latLong.latitude
						const lon = latLong.longitude
						const selectedIndex = num
						const handleClick = () => {
							console.log("You clicked item " + selectedIndex + " @ " + lat + ", " + lon)
							this.setState({ selectedIndex : selectedIndex})
							this.props.latLongMapRef.current.setMarker([lat, lon])
						}
						return <tr key={lat + "," + lon} className={num === this.state.selectedIndex ? "selected-list-item" : "list-item-" + (num % 2)}>
							<td data-testid={"latLong" + num} onClick={handleClick}>{lat + ", " + lon}</td>
						</tr>
					}
				)}
			</tbody>
	    </table>
	}
}

const LatLongsComponent = ({locations}) => {
	// default marker is first marker in latLongs, or 0, 0 if latLongs is empty
	const marker = locations.length > 0 ? [locations[0].latitude, locations[0].longitude] : [0, 0]
	// allows access to actual LatLongMap
	const mapRef = React.createRef()
	const listRef = React.createRef()
	const updateMapLong = (event) => {
		listRef.current.updateSelectedLatLong(event.target._latlng)
	}
	const latLongMap = <LatLongMap ref={mapRef} marker={marker} markerDragged={updateMapLong}/>
    return <table>
		<tbody>
			<tr>
				{ /* reference to actual LatLongMap is passed to LatLongList */ }
				<td><LatLongList ref={listRef} locations={locations} latLongMapRef={mapRef}/></td>
				<td>{latLongMap}</td>
			</tr>
		</tbody>
    </table>
  }

export default LatLongsComponent;
