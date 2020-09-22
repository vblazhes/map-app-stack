import React, {useEffect, useState} from "react";
import ReactMapGL, {Marker, Popup, Layer, GeolocateControl, NavigationControl} from "react-map-gl";
import PinsService from "../../Repository/axiosPinsRepository"
import MapsService from "../../Repository/axiosMapsRepository";
import {useParams} from "react-router";
import Panel from "../Panel/Panel";
import "./Mapbox.css"

const Mapbox = (props) => {

    // const [viewport, setViewport] = useState({});
    const [map, setMap] = useState({});
    const [pins, setPins] = useState([]);
    const [selectedPin, setSelectedPin] = useState(null);
    const {map_id} = useParams();
    const [lngLat, setLngLat] = useState({});
    let counter = 2;
    let lastClick = 0;
    let currClick = 0;


    useEffect(() => {

        MapsService.fetchMap(map_id).then((response) => {
            // console.log(response.data);
            setMap(response.data);
        });

        PinsService.fetchPins(map_id).then((response) => {
            // console.log(response.data);
            setPins(response.data);
        });

        const listener = e => {
            if (e.key === "Escape") {
                setSelectedPin(null);
            }
        };
        window.addEventListener("keydown", listener);

    }, []);

    const addNewPin = ((newPin) => {
        PinsService.addPin(newPin);
        let pins_upt = pins.concat(newPin);
        setPins(pins_upt);
    });

    const selectPinItemPanelHandler = (pin) => {
        setSelectedPin(pin);
        setMap(() => {
            return {
                center_latitude: pin.latitude,
                center_longitude: pin.longitude,
                default_zoom: map.default_zoom,
                style: map.style,
                info: map.info
            }
        })
    };

    const handleOnMapClick = e => {
        // debugger;
        //  counter--;
        //  currClick = new Date().getTime();
        //  console.log(currClick);
        //  let time_span = currClick - lastClick;
        //  lastClick = currClick;
        // if(counter === 0 && time_span < 50){
        //     setLngLat(e.lngLat);
        //     counter = 2;
        // }
        console.log(e.lngLat);

        setLngLat(e.lngLat);

    };

    const handleOnDrag = e => {
        debugger;
        setLngLat({});
    };

    const _onViewportChange = (viewport) => {
        viewport.zoom=13; //Whatever zoom level you want
        setMap(() => {
            return {
                center_latitude: viewport.latitude,
                center_longitude: viewport.longitude,
                default_zoom: viewport.zoom,
                style: map.style,
                info: map.info
            }
        })
    };


    return (
        <div className="row">
            <div className="col" style={{overflow: "hidden"}}>

                <ReactMapGL latitude={map.center_latitude} longitude={map.center_longitude} width={"81.45vw"}
                            height={"92.5vh"}
                            zoom={map.default_zoom}
                            mapboxApiAccessToken={"pk.eyJ1IjoidmJsYXpoZXMiLCJhIjoiY2s2MmRkY3RkMGI3bDNubjIyMng0M3N1MSJ9.WW9-0hwgWyiw45dRlRQNpQ"}
                            mapStyle={map.style}
                            onViewportChange={viewport => {
                                setMap(() => {
                                    return {
                                        center_latitude: viewport.latitude,
                                        center_longitude: viewport.longitude,
                                        default_zoom: viewport.zoom,
                                        style: map.style,
                                        info: map.info
                                    }
                                })

                            }}
                            onClick={handleOnMapClick}
                            onDrag={handleOnDrag}
                >
                    {pins.map(pin => (
                        <Marker key={pin.id} latitude={pin.latitude} longitude={pin.longitude}>
                            <button className="marker-btn" onClick={e => {
                                e.preventDefault();
                                setSelectedPin(pin);
                                setMap(() => {
                                    return {
                                        center_latitude: pin.latitude,
                                        center_longitude: pin.longitude,
                                        default_zoom: map.default_zoom,
                                        style: map.style,
                                        info: map.info
                                    }
                                })
                            }}>
                                <img
                                    src="https://cdn.clipart.email/027ac0d1cf692fed21948b7a035ae78a_clipart-map-transparent-clipart-map-transparent-transparent-free-_800-800.png"
                                    alt="Google Logo PNG"/>
                            </button>
                        </Marker>
                    ))}

                    {selectedPin && (
                        <Popup latitude={selectedPin.latitude} longitude={selectedPin.longitude} onClose={() => {
                            setSelectedPin(null)
                        }}>
                            <div className="popup-container">
                                <img className="popup-image" src={selectedPin.image} alt={"pin visual description"}/>
                                <p>{selectedPin.name}</p>
                            </div>
                        </Popup>
                    )}

                    <div style={{
                        width: "30px",
                        float: "left",
                        marginLeft: "10px",
                        position: "relative",
                        top: "605px",
                        display: "block"
                    }} className="">
                        <GeolocateControl fitBoundsOptions={{maxZoom: 15}} onViewportChange={_onViewportChange} positionOptions={{enableHighAccuracy: true}} trackUserLocation={true} showUserLocation={true}/>

                    </div>


                    <div style={{width: "30px", float: "right", marginTop: "10px", marginRight: "25px"}}>
                        <NavigationControl/>
                    </div>

                </ReactMapGL>
            </div>
            <div className="col-md-2.5">
                <Panel pins={pins} lngLat={lngLat} mode={0} onSubmit={addNewPin} map={map}
                       onSelectItem={selectPinItemPanelHandler}/>
            </div>
        </div>
    )
};

export default Mapbox;