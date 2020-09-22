import React, {useState, useEffect} from "react";
import MapsService from "../../Repository/axiosMapsRepository";
import MapCard from "../MapCard/MapCard";
import {Link} from "react-router-dom";
import {Button} from "react-bootstrap";


const Explore = (props) => {

    const [maps, setMaps] = useState([]);
    const [currentPage, setPage] = useState(1);
    const mapsPerPage = 4;
    let flag = true;

    useEffect(() => {

        MapsService.fetchAllMaps().then((response) => {
            // console.log(response.data);
            setMaps(response.data);
        })

    }, []);

    const onPageChangeNext = () => {
        const nextPage = currentPage + 1;
        setPage(nextPage)
    };

    const onPageChangePrev = () => {
        const nextPage = currentPage - 1;
        setPage(nextPage)
    };

    let map_cards_list = maps.map((map) => {
        if (map.visibility === 'PUBLIC' && map.approved === 1) {
            if (flag) {
                flag = false;
                // flag += 1;
                return (
                    <div style={{display: "inline"}} key={map.id}>
                        <Link to={"/explore/maps/" + map.id} className="navbar-brand" style={{float: "left"}}><MapCard
                            map={map} width={"1065px"} height={"400px"}/></Link>
                    </div>
                );
            } else {
                // flag += 1;
                // if(flag === 4){
                //     flag = 0;
                // }
                return (
                    <div style={{display: "inline"}} key={map.id}>
                        <Link to={"/explore/maps/" + map.id} className="navbar-brand" style={{float: "left"}}><MapCard
                            map={map} width={"345px"} height={"140px"}/></Link>
                    </div>
                );
            }

        }
    });

    const last_index = currentPage * mapsPerPage;
    const fist_index = last_index - mapsPerPage;

    return (
        <div className="container">
            <div style={{paddingTop:"20px"}}>
            {map_cards_list.slice(fist_index, last_index)}
            </div>

            {
                map_cards_list.length >= mapsPerPage &&

                <div id="paggination" style={{clear: "both", textAlign: "center", paddingLeft: "450px"}}>
                    <br/>
                    <nav aria-label="Page navigation example">
                        <ul className="pagination">
                            <li className="page-item"><Button className="btn" disabled={currentPage === 1}
                                                              style={{color: "white"}}
                                                              onClick={onPageChangePrev}>Previous</Button></li>
                            <li className="page-item"><Link to={"/#"} className="page-link" href="#">{currentPage}</Link>
                            </li>
                            <li className="page-item"><Button className="btn"
                                                              disabled={currentPage * mapsPerPage >= map_cards_list.length}
                                                              style={{color: "white"}}
                                                              onClick={onPageChangeNext}>Next</Button></li>
                        </ul>
                    </nav>
                </div>
            }
        </div>
    )
};
export default Explore;