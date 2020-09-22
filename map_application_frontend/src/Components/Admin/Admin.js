import React, {useState, useEffect} from "react";
import MapsService from "../../Repository/axiosMapsRepository";
import AuthService from "../../Repository/axiosAuthRepository";
import {Link} from 'react-router-dom';

const Admin = (props) => {

    const [maps, setMaps] = useState([]);
    const [users, setUsers] = useState([]);

    useEffect(() => {
        MapsService.fetchPendingMaps(props.token).then((response) => {
            // console.log(response.data);
            setMaps(response.data);

        })
    }, []);

    const onApprove = (map_id, decision) => {
        MapsService.approveMapById(map_id, decision, props.token).then((response)=>{

            const reduced_maps = maps.filter((map) => {
                if (map.id !== map_id) {
                    return map;
                }
            });

            setMaps(reduced_maps);
        })
    };


    const newlyCreatedMaps = maps.map((map) => {
        return (
            <tr key={map.id}>
                <td style={{width: "200px"}}>
                    <Link to={"/explore/maps/"+map.id}><img style={{width: "100%"}} src={map.background} alt=""/></Link>
                </td>

                <td>
                    {map.name}
                    <br/>
                    <small>By {map.owner.name}</small>
                </td>
                <td>
                    <Link to={"/admin"} className="btn" style={{background:'#17b585', color:'white'}} onClick={() => onApprove(map.id, 1)}>
                        Approve
                    </Link>
                </td>
                <td>
                    <Link to={"/admin"} className="btn btn-danger" onClick={() => onApprove(map.id, 2)}>
                        Disapprove
                    </Link>
                </td>
            </tr>
        )
    });

    return (
        <div className="container" style={{textAlign: "left", paddingTop: "30px"}}>
            <div className="container">
                <br/>
                <div className="row justify-content-center">
                    <div className="col-md-8">
                        <div className="card">
                            <div className="card-header">Dashboard</div>

                            <div className="card-body text-center">
                                <h3>Admin Panel</h3>
                            </div>
                            <table className="table table-striped">
                                <thead>
                                <tr>
                                    <th></th>
                                    <th style={{textAlign: "center", paddingLeft:'80px'}}><b>New Maps Requests</b></th>
                                    <th></th>
                                    <th></th>
                                </tr>
                                </thead>

                                <tbody>
                                {newlyCreatedMaps.length > 0 ?
                                    newlyCreatedMaps :
                                    <tr style={{height:'80px'}}>
                                        <td></td>
                                        <td style={{textAlign: "center", verticalAlign:"middle", color:"darkslategray", paddingLeft:'80px'}}>No new requests!</td>
                                        <td></td>
                                        <td></td>
                                    </tr>
                                }
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    )
};
export default Admin;