import React from "react";
import {withRouter} from "react-router-dom";


const AddPin = (props) =>{

    const onFormSubmit = (e) => {
        debugger;
        e.preventDefault();
        props.mode ===1 ? props.history.push('/explore/maps/' + props.mapId+"/edit") : props.history.push('/maps/create');
        props.onPinSubmit({
            "map_id": props.mode ===1 ? props.mapId : "",
            "latitude": e.target.latitude.valueAsNumber,
            "longitude": e.target.longitude.valueAsNumber,
            "name": e.target.name.value,
            "description": e.target.description.value,
            "image": e.target.image.value,
        })
    };



    return(
      <div
          className="tab row text-center border-right border-left border-bottom border-top tab_items"
          style={{width: "300px", marginLeft: "-5px", paddingLeft: "5px"}} id="addPin">

          <form style={{textAlign: "left", marginBottom: "30px"}}
                onSubmit={onFormSubmit}>

              <div className="form-group">
                  <label htmlFor="latitude">Latitude:</label>
                  <input type="number" className="form-control" id="latitude"
                         name={"latitude"} readOnly={true}
                         style={{width: "270px"}}
                         value={props.lngLat[1] !== undefined ? props.lngLat[1] : ""}/>
              </div>

              <div className="form-group">
                  <label htmlFor="longitude">Longitude:</label>
                  <input type="number" className="form-control" id="longitude"
                         name={"longitude"} readOnly={true}
                         style={{width: "270px"}}
                         value={props.lngLat[0] !== undefined ? props.lngLat[0] : ""}/>
              </div>


              <div className="form-group">
                  <label htmlFor="name">Name:</label>
                  <input type="text" className="form-control" id="name" name={"name"}
                         style={{width: "270px"}}/>
              </div>

              <div className="form-group">
                  <label htmlFor="description">Description:</label>
                  <textarea id={"description"} name={"description"} rows={3}
                            style={{width: "270px"}} className="form-control"/>
              </div>

              <div className="form-group">
                  <label htmlFor="image">Image:</label>
                  <input type="text" className="form-control" id="image" name={"image"}
                         style={{width: "270px"}}/>
              </div>
              <input type={"submit"} className="btn btn-primary" value={"Add Pin"}/>
          </form>
      </div>
  )
};
export default withRouter(AddPin);