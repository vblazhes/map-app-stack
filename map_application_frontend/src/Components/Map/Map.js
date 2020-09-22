import React from "react";
import {GoogleMap, withScriptjs, withGoogleMap} from "react-google-maps";


const Map = withScriptjs(withGoogleMap((props) =>
    <GoogleMap defaultZoom={10} defaultCenter={{lat: 41.123096, lng:20.801647}}/>
));

export default Map;