import React, {useEffect, useState} from "react";
import ReactMapGL, {Marker, Popup, GeolocateControl, NavigationControl} from "react-map-gl";
import PinsService from "../../Repository/axiosPinsRepository"
import MapsService from "../../Repository/axiosMapsRepository";
import {useParams} from "react-router";
import Panel from "../Panel/Panel";
import "../Mapbox/Mapbox.css"
import {Link} from "react-router-dom";
// import { View, Text, Switch, StyleSheet} from 'react-native'


const MapEdit = (props) => {

    // const [viewport, setViewport] = useState({});
    const [map, setMap] = useState({});
    const [pins, setPins] = useState([]);
    const [newPins, setNewPins] = useState([]);
    const [selectedPin, setSelectedPin] = useState(null);
    const {map_id} = useParams();
    const [lngLat, setLngLat] = useState({});
    // let counter = 2;
    // let lastClick = 0;
    // let currClick = 0;


    useEffect(() => {

        navigator.geolocation.getCurrentPosition((position) => {
            const initialPosition = JSON.stringify(position);
            console.log("Initial pos:" + initialPosition);
        });

        MapsService.fetchMap(map_id).then((response) => {
            //console.log(response.data[0]);
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
        PinsService.addPin(newPin, props.token);
        let pins_upt = newPins.concat(newPin);
        setNewPins(pins_upt);
    });

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

        setLngLat(e.lngLat);

    };

    const handleOnDrag = e => {
        debugger;
        setLngLat({});
    };

    //PIN DELETE FUNCTION
    const onDelete = (pin_id) => {
        // debugger;
        setSelectedPin(null);
        PinsService.deletePinById(map_id, pin_id, props.token).then((response) => {

            const reduced_pins_1 = pins.filter((pin) => {
                if (pin.id !== pin_id) {
                    return pin;
                }
            });

            const reduced_pins_2 = newPins.filter((pin) => {
                if (pin.id !== pin_id) {
                    return pin;
                }
            });

            setPins(reduced_pins_1);
            setNewPins(reduced_pins_2)
        })
    };

    const saveMap = ((newMap) => {
        debugger;
        let constructedMap = {
            id: map.id,
            default_zoom: map.default_zoom,
            center_latitude: map.center_latitude,
            center_longitude: map.center_longitude,
            style: map.style,
            name: newMap.name,
            background: newMap.background,
            description: newMap.description,
            visibility: newMap.visibility.toUpperCase(),
            approved: map.approved,
            owner: props.user
        };

        console.log(constructedMap);

        MapsService.updateMap(constructedMap, props.token).then((response) => {

            for (let i = 0; i < newPins.length; i++) {
                newPins[i].map_id = response.data.id;
                PinsService.updatePin(newPins[i], map_id ,props.token);
            }
        });

        setMap(constructedMap);

    });

    const onStyleChange = (style) => {
        setMap(() => {
            return {
                center_latitude: map.center_latitude,
                center_longitude: map.center_longitude,
                default_zoom: map.default_zoom,
                style: style,
                info: map.info
            }
        });
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
                                    // src="http://pluspng.com/img-png/google-logo-png-open-2000.png"
                                    alt="Google Logo PNG"/>
                            </button>
                        </Marker>
                    ))}

                    {newPins.map(pin => (
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
                                    // src="http://pluspng.com/img-png/google-logo-png-open-2000.png"
                                    alt="Google Logo PNG"/>
                            </button>
                        </Marker>
                    ))}

                    {/*POPUP CODE*/}

                    {selectedPin && (
                        <div>
                            <Popup latitude={selectedPin.latitude} longitude={selectedPin.longitude} onClose={() => {
                                setSelectedPin(null)
                            }} closeOnClick={false}>
                                <div className="popup-container">
                                    <img className="popup-image" src={selectedPin.image}
                                         alt={"pin visual description"}/>
                                    <p>{selectedPin.name}</p>

                                    <hr/>
                                    <Link to={"#"} className="btn btn-danger" onClick={() => onDelete(selectedPin.id)}>
                                        Delete
                                    </Link>
                                    <br/>
                                    <br/>
                                </div>
                            </Popup>
                        </div>
                    )}


                    {/*MAP TOOLBOX*/}
                    <div style={{
                        width: "30px",
                        float: "left",
                        marginLeft: "10px",
                        position: "relative",
                        top: "605px",
                        display: "block"
                    }} className="">
                        <GeolocateControl positionOptions={{enableHighAccuracy: true}} trackUserLocation={true}/>

                    </div>


                    <div style={{width: "30px", float: "right", marginTop: "10px", marginRight: "25px"}}>
                        <NavigationControl/>
                    </div>

                </ReactMapGL>
            </div>
            <div className="col-md-2.5">
                <Panel pins={pins} lngLat={lngLat} mode={1} onPinSubmit={addNewPin} onMapSubmit={saveMap}
                       onStyleChange={onStyleChange} map={map}/>
            </div>
        </div>
    )
};

export default MapEdit;