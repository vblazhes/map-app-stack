import React, {useEffect, useState} from "react";
import './Panel.css';
import {useParams} from "react-router";
import {withRouter} from "react-router-dom";
import {Tabs, Tab} from 'react-bootstrap'
import AddPin from "../AddPin/AddPin";
import AddMap from "../AddMap/AddMap";
import {FaSearch} from 'react-icons/fa';
import MapsService from "../../Repository/axiosMapsRepository";


const Panel = (props) => {

    const [searchedPins, setSearchedPins] = useState([]);

    useEffect(() => {
        setSearchedPins(props.pins);
    }, [props.pins]);

    function onSelectPinItem(pin) {
        props.onSelectItem(pin);
    }

    let pin_items = props.pins.map((pin) => {
        return (
            <a href="#" className="list-group-item list-group-item-action bg-light" key={pin.id}
               onClick={() => onSelectPinItem(pin)}>{pin.name}</a>
        )
    });

    let pin_items_searched = searchedPins.map((pin) => {
        return (
            <a href="#" className="list-group-item list-group-item-action bg-light" key={pin.id}
               onClick={() => onSelectPinItem(pin)}>{pin.name}</a>
        )
    });

    const {map_id} = useParams();

    const onSearchPins = (e)  =>{
        debugger;
        console.log(e.target.value);
        // console.log(searchedPins);
        const tmpSearchedPins = Object.values(props.pins).filter(function (pin) {
            console.log(pin.name);
            return pin.name.toLowerCase().includes(e.target.value.toLowerCase());
        });
        console.log(tmpSearchedPins);
        setSearchedPins(tmpSearchedPins);
    };

    return (
        <div id="sidebar-container">
            <div className="bg-light border-left" id="sidebar-wrapper">
                <div id="problematic-div" className="list-group list-group-flush border-bottom">

                    <div style={{marginLeft: "5px", marginTop: "10px"}}>
                        <Tabs defaultActiveKey={(props.mode === 1 || props.mode === 2) ? "add" : "items"}
                              id="uncontrolled-tab-example">


                            <Tab eventKey="map" title="Map">

                                <AddMap onMapSubmit={props.onMapSubmit} onStyleChange={props.onStyleChange}
                                        lngLat={props.lngLat} mode={props.mode} map={props.map}/>
                            </Tab>

                            <Tab eventKey="items" title="Items">
                                <div className="md-form mt-1" style={{width: "93%"}}>
                                    <input className="form-control" type="text" placeholder="Search"
                                           aria-label="Search"
                                           style={{width: "90%", marginRight: "10px", display: "inline"}} onChange={onSearchPins}/>
                                    <i style={{color: "#17b585"}}><FaSearch/></i>
                                </div>
                                <br/>
                                <div className="tab_items" id="items" style={{marginLeft: "-5px"}}>
                                    {pin_items_searched}
                                </div>
                            </Tab>

                            {(props.mode === 1 || props.mode === 2) &&
                            <Tab eventKey="add" title="Add">
                                <AddPin onPinSubmit={props.onPinSubmit} lngLat={props.lngLat} mapId={map_id}
                                        mode={props.mode}/>
                            </Tab>
                            }

                        </Tabs>
                    </div>

                </div>
            </div>
        </div>

    )
};
export default withRouter(Panel);


