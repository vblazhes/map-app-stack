import React, {useEffect, useState} from "react";
import ReactMapGL, {Marker, Popup, Layer, GeolocateControl, NavigationControl} from "react-map-gl";
import PinsService from "../../Repository/axiosPinsRepository"
import MapsService from "../../Repository/axiosMapsRepository";
import Panel from "../Panel/Panel";
import "../Mapbox/Mapbox.css"
import { FaMapMarker } from 'react-icons/fa';
import {Link} from "react-router-dom";


const MapCreate = (props) => {

    // const [viewport, setViewport] = useState({});
    const [map, setMap] = useState({});
    const [pins, setPins] = useState([]);
    const [selectedPin, setSelectedPin] = useState(null);
    const [lngLat, setLngLat] = useState({});
    let counter = 2;
    let lastClick = 0;
    let currClick = 0;


    let getCurrentPosition = (position) => {
    };

    useEffect(() => {

        setMap({
            id: "",
            default_zoom: 1,
            center_latitude: 1,
            center_longitude: 1,
            style: "mapbox://styles/mapbox/streets-v11",
            name: "",
            background: ""
        });

        const listener = e => {
            if (e.key === "Escape") {
                setSelectedPin(null);
            }
        };
        window.addEventListener("keydown", listener);

    }, []);

    const addNewPin = ((newPin) => {
        let pins_updated = pins.concat(newPin);
        setPins(pins_updated);
    });

    const saveMap = ((newMap)=>{
        debugger;

        console.log(newMap.imageFile);

        let constructedMap = {
            id:"",
            default_zoom: map.default_zoom,
            center_latitude: map.center_latitude,
            center_longitude: map.center_longitude,
            style: map.style,
            name: newMap.name,
            background: newMap.background,
            description: newMap.description,
            visibility: newMap.visibility.toUpperCase(),
            imageFile: newMap.imageFile,
            approved: 0,
            owner: props.user
        };

        console.log(constructedMap);

        MapsService.saveMap(constructedMap, props.token).then((response)=>{
            for(let i = 0; i<pins.length;i++){
                pins[i].map_id = response.data.id;
                PinsService.addPin(pins[i], props.token);
            }
        });

        setMap(constructedMap);

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

    const updateViewport = (viewport) =>{
        setMap(() => {
            return {
                center_latitude: viewport.latitude,
                center_longitude: viewport.longitude,
                default_zoom: viewport.zoom,
                style: map.style,
                info: map.info
            }});
    };


    const onStyleChange=(style) =>{
        setMap(() => {
            return {
                center_latitude: map.center_latitude,
                center_longitude: map.center_longitude,
                default_zoom: map.default_zoom,
                style: style,
                info: map.info
            }});
    };

    const saveMapNotification = ()=>{

    };

    //PIN DELETE FUNCTION
    const onDelete = (pin_name) => {
        // debugger;
        setSelectedPin(null);

            const reduced_pins_1 = pins.filter((pin) => {
                if (pin.name !== pin_name) {
                    return pin;
                }
            });


            setPins(reduced_pins_1);
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

                    {/*CODE FOR PIN POPUP*/}
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
                                    <Link to={"#"} className="btn btn-danger" onClick={() => onDelete(selectedPin.name)}>
                                        Delete
                                    </Link>
                                    <br/>
                                    <br/>
                                </div>
                            </Popup>
                        </div>
                    )}

                    {/*CODE FOR MAP TOOLS*/}
                    <div style={{
                        width: "30px",
                        float: "left",
                        marginLeft: "10px",
                        position: "relative",
                        top: "570px",
                        display: "block"
                    }} className="">
                        <button style={{marginBottom:"5px"}}><i><FaMapMarker/></i></button>
                        <GeolocateControl positionOptions={{enableHighAccuracy: true}} trackUserLocation={true}
                                          showUserLocation={true} onViewportChange={updateViewport}/>
                    </div>


                    <div style={{width: "30px", float: "right", marginTop: "10px", marginRight: "25px"}}>
                        <NavigationControl/>
                    </div>

                </ReactMapGL>
            </div>

            {/*MAP PANEL*/}
            <div className="col-md-2.5">
                <Panel pins={pins} lngLat={lngLat} mode={2} onPinSubmit={addNewPin} onMapSubmit={saveMap} onStyleChange={onStyleChange}/>
            </div>

        </div>
    )
};

export default MapCreate;