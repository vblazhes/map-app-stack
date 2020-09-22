import React, {useState, useEffect} from "react";
import MapsService from "../../Repository/axiosMapsRepository";
import {Link} from 'react-router-dom';
import {Button} from "react-bootstrap";

const Profile = (props) => {

    const [maps, setMaps] = useState([]);
    const [currentPage, setPage] = useState(1);
    const mapsPerPage = 3;

    useEffect(() => {
        console.log(props.user.id);
        MapsService.fetchMapsByOwnerId(props.user.id,props.token).then((response) => {
            // console.log(response.data);
            setMaps(response.data);
        })
    }, []);

    const onDelete = (map_id) => {
        MapsService.deleteMapById(map_id, props.token).then((response) => {

            const reduced_maps = maps.filter((map) => {
                if (map.id !== map_id) {
                    return map;
                }
            });

            setMaps(reduced_maps);

        })
    };

    const onPageChangeNext = () => {
        const nextPage = currentPage + 1;
        setPage(nextPage)
    };

    const onPageChangePrev = () => {
        const nextPage = currentPage - 1;
        setPage(nextPage)
    };


    const userMaps = maps.map((map) => {
        return (
            <tr key={map.id}>
                <td style={{width: "200px"}}>

                        <Link to={"/explore/maps/"+map.id}><img style={{width: "100%"}} src={map.background} alt=""/></Link>


                </td>

                <td>
                    {map.name}
                    <br/>
                    {/* eslint-disable-next-line no-mixed-operators */}
                    {(map.visibility === 'PUBLIC' && map.approved === 1 || map.visibility === 'PRIVATE') &&
                        <small style={{color: 'gray'}}><b>{map.visibility.toLowerCase()}</b></small>
                    }
                    {map.visibility === 'PUBLIC' && map.approved === 0  &&
                        <small style={{color: 'gray'}}><b>pending</b></small>
                    }
                    {map.visibility === 'PUBLIC' && map.approved === 2  &&
                    <small style={{color: 'red'}}><b>disapproved</b></small>
                    }

                </td>
                <td>
                    <Link to={"/explore/maps/"+map.id+"/edit"} className="btn btn-secondary">
                        Edit
                    </Link>
                </td>
                <td>
                    <Link to={"/profile"} className="btn btn-danger" onClick={() => onDelete(map.id)}>
                        Delete
                    </Link>
                </td>
                <td style={{ padding: '2px',width:'1px', backgroundColor: (map.visibility === 'PUBLIC' && map.approved === 1) ? '#17b585' :'lightgray'}}>
                </td>
            </tr>
        )
    });

    const last_index = currentPage * mapsPerPage;
    const fist_index = last_index - mapsPerPage;

    return (
        <div className="container" style={{textAlign: "left", paddingTop: "30px"}}>
            <div className="container">
                <br/>
                <div className="row justify-content-center">
                    <div className="col-md-8">
                        <div className="card">
                            <div className="card-header">Dashboard</div>

                            <div className="card-body">
                                <Link to="/maps/create" className="btn btn-info" style={{color: "white", float: "right"}}>Create
                                    Map</Link>
                                <h3>{props.user.name}</h3>
                                <p>{props.user.username}</p>
                            </div>
                            <table className="table table-striped">
                                <thead>
                                <tr>
                                    <th></th>
                                    <th style={{textAlign: "center"}}><b>Your Maps</b></th>
                                    <th></th>
                                    <th></th>
                                </tr>
                                </thead>

                                <tbody>
                                {userMaps.slice(fist_index,last_index)}
                                </tbody>
                            </table>
                            {
                                userMaps.length >= mapsPerPage &&

                                <div id="paggination" style={{clear: "both", textAlign: "center", paddingLeft: "250px"}}>
                                    <br/>
                                    <nav aria-label="Page navigation example">
                                        <ul className="pagination">
                                            <li className="page-item"><Button className="btn" disabled={currentPage === 1}
                                                                              style={{color: "white"}}
                                                                              onClick={onPageChangePrev}>Previous</Button></li>
                                            <li className="page-item"><Link to={"#"} className="page-link" href="#">{currentPage}</Link>
                                            </li>
                                            <li className="page-item"><Button className="btn"
                                                                              disabled={currentPage * mapsPerPage >= userMaps.length}
                                                                              style={{color: "white"}}
                                                                              onClick={onPageChangeNext}>Next</Button></li>
                                        </ul>
                                    </nav>
                                </div>
                            }
                        </div>
                    </div>
                </div>
            </div>

        </div>
    )
};
export default Profile;