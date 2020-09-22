import React, {useState} from "react";
import {withRouter} from "react-router-dom";

const AddMap = (props) => {

    const [file, setFile] = useState({});
    const [toggle, setToggle] = useState(0);

    const onFormSubmit = (e) => {
        e.preventDefault();
        if (props.mode === 1) {
            props.history.push('/explore/maps/' + props.mapId + "/edit");
        } else if (props.mode === 2) {
            props.history.push('/profile');
        }
        // props.history.push('/explore/maps/' + map_id);
        debugger;
        console.log(file);

        //const formData = new FormData();

        // formData.append("name", e.target.name.value);
        // formData.append("description", e.target.description.value);
        // formData.append("background", e.target.background.value);
        // formData.append("visibility", e.target.visibility.value);
        // formData.append("style", e.target.styleMap.value);
        // formData.append("imageFile", file);

        // props.onMapSubmit(formData)

        props.onMapSubmit({
            "name": e.target.name.value,
            "description": e.target.description.value,
            "background": e.target.background.value,
            "visibility": e.target.visibility.value,
            "style": e.target.styleMap.value,
            "imageFile": file
        })
    };

    const handleChange = (e) => {
        //e.preventDefault();
        debugger;
        props.onStyleChange(e.target.value);
    };

    const handleFile = (e) => {
        if (e.target.files[0] !== undefined) {
            setToggle(2)
        } else {
            props.map.background = "";
            setToggle(0);
        }
        debugger;
        setFile(e.target.files[0]);
        console.log(e.target.files[0]);

    };

    const handleToggleURL = (e) => {
        if (e.target.value !== "") {
            setToggle(1);
        } else {
            setToggle(0);
        }
    };


    return (
        <div>
            {(props.mode === 1 || props.mode === 2) ?
                <div
                    className="tab row text-center border-right border-left border-bottom border-top tab_items"
                    style={{width: "300px", marginLeft: "-5px", paddingLeft: "5px"}} id="addPin">

                    <form style={{textAlign: "left", marginBottom: "30px"}}
                          onSubmit={onFormSubmit}>


                        <div className="form-group">
                            <label htmlFor="name">Name:</label>
                            <input type="text" className="form-control" id="name" name={"name"}
                                   defaultValue={props.map !== undefined && props.map.name !== undefined ? props.map.name : ""}
                                   style={{width: "270px"}}/>
                        </div>

                        <div className="form-group">
                            <label htmlFor="description">Description:</label>
                            <textarea id={"description"} name={"description"} rows={3}
                                      defaultValue={props.map !== undefined && props.map.description !== undefined ? props.map.description : ""}
                                      style={{width: "270px"}} className="form-control"/>
                        </div>

                        <div className="form-group">
                            <label htmlFor="background">Background:</label>
                            <input type="text" className="form-control" id="background" name={"background"}
                                   disabled={toggle === 2 || (props.map !== undefined && props.map.background !== undefined && props.map.background.startsWith("/images"))}
                                   onChange={handleToggleURL}
                                   defaultValue={props.map !== undefined && props.map.background !== undefined && !props.map.background.startsWith("/images") ? props.map.background : ""}
                                   style={{width: "270px"}}/>
                        </div>


                        {/*<div className="form-group">*/}
                        {/*    <label htmlFor="image">Background:</label>*/}
                        {/*    <input type="file"  id="image" name={"image"}*/}
                        {/*           style={{width: "270px"}}/>*/}
                        {/*</div>*/}

                        <div className="form-group">
                            <label htmlFor="styleMap">Style:</label>
                            <br/>
                            <select className="form-control" style={{width: "270px"}} name={"styleMap"} id={"styleMap"}
                                    onChange={handleChange}
                                    defaultValue={props.map !== undefined && props.map.style !== undefined ? props.map.style : ""}>
                                <option value='mapbox://styles/vblazhes/ck62e0e7k0z1p1in0fy9cb2ke'>Basic</option>
                                <option value='mapbox://styles/vblazhes/ck6b9ro1u18ug1ikvbju56691'>Satellite</option>
                                <option value='mapbox://styles/vblazhes/ck6bcgstg2mam1imxrhebxn4p'>Decimal</option>
                                <option value="mapbox://styles/vblazhes/ck8w0c43g25dk1irrkilq7ob6">Streets</option>
                                <option value="mapbox://styles/vblazhes/ck8w1r5se2vnn1jsyuh5c58xs">Navigation</option>
                                <option value="mapbox://styles/vblazhes/ck8w1ujzm2kp81iubitfay8do">Monochrome</option>
                                <option value="mapbox://styles/vblazhes/ck8w1vdtq2of41ila05e0445k">Outdoors</option>

                            </select>
                        </div>

                        <div className="form-group">
                            <label htmlFor="visibility">Visibility:</label>
                            <br/>
                            <select className="form-control" style={{width: "100px"}} name={"visibility"}
                                    id={"visibility"}
                                    defaultValnue={(props.map !== undefined && props.map.visibility !== undefined && props.map.visibility.toLowerCase() === 'public') ? 'public' : 'private'}>
                                <option value='private'>private</option>
                                <option value='public'>public</option>
                            </select>
                        </div>

                        <div className="form-group">
                            <label htmlFor="imageFile">imageFile:</label>
                            <input disabled={toggle === 1} type="file" id="imageFile" name={"imageFile"} style={{}}
                                   onChange={(e) => handleFile(e)}
                            />
                        </div>

                        <input type={"submit"} className="btn btn-primary" value={"Save Map"}/>
                    </form>
                </div>
                :

                <div
                    className="tab row text-center border-right border-left border-bottom border-top tab_items"
                    style={{width: "300px", marginLeft: "-5px", paddingLeft: "5px", background: "white"}} id="addPin">
                    <p>{props.map.name}</p>
                    <small>by {props.map.owner !== undefined ? props.map.owner.name : ""}</small>
                </div>

            }
        </div>
    )
};
export default withRouter(AddMap);