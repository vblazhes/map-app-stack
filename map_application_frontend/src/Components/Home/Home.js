import React from "react";
import {Link} from 'react-router-dom';
import './Home.css'

const Home = () => {
    return(

        <div style={{width:"100%",position:"relative"}}>
            <div id="intro">Create interactive maps</div>
            <img src="https://live.staticflickr.com/8672/16266655632_f7358b18cf_b.jpg" alt="Home" id="img-home" style={{width: "100%",height:"665px"}}/>
            <Link to={"/explore/maps"} className="btn" id="exp-btn">Explore Maps</Link>
        </div>
    )
};

export default Home;