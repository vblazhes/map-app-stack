import React from "react";
import {Link} from 'react-router-dom';
import { FaUser, FaSignInAlt, FaUserPlus } from 'react-icons/fa';
import {withRouter} from "react-router-dom";

const Header = (props) => {
    return (
        <header>
            <nav className="navbar navbar-expand-md navbar-dark navbar-fixed" style={props.location.pathname === "/" ? {backgroundColor: "#343a40"} : {backgroundColor: "#17b585"}}>
                    <span className="container">
                        <Link to={"/"} className="navbar-brand">MapApp</Link>
                    <button className="navbar-toggler" type="button" data-toggle="collapse"
                            data-target="#navbarCollapse"
                            aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
                        <span className="navbar-toggler-icon"/>
                    </button>
                    <div className="collapse navbar-collapse" id="navbarCollapse">
                        <ul className="nav navbar-nav mr-auto">
                            <li className="nav-item">
                                <Link to={"/explore/maps"} className="nav-link">Explore</Link>
                            </li>

                        </ul>


                        {/* Sign-In Sign-Up */}
                        {
                            !props.isSignedIn &&
                            <form className="form-inline mt-2 mt-md-0 ml-3">
                            <Link to={"/sign-in"} className={props.location.pathname === "/" ? "btn btn-outline-info my-2 my-sm-0" : "btn btn-outline-light my-2 my-sm-0"}><i><FaSignInAlt/></i> Sign in</Link>
                            </form>
                        }

                        {/* Sign-In Sign-Up */}
                        {
                            !props.isSignedIn &&
                            <form className="form-inline mt-2 mt-md-0 ml-3">
                                <Link to={"/sign-up"} className={props.location.pathname === "/" ? "btn btn-outline-info my-2 my-sm-0" : "btn btn-outline-light my-2 my-sm-0"}><i><FaUserPlus/></i> Sign up</Link>
                            </form>
                        }

                        {/* Profile */}
                        {
                            props.isSignedIn &&
                            <ul className="nav navbar-nav navbar-right">
                                <li className="nav-item dropdown">
                                    <a className="nav-link dropdown-toggle" id="navbarDropdownMenuLink-333" data-toggle="dropdown"
                                       aria-haspopup="true" aria-expanded="false">
                                        <i><FaUser/></i> {props.user.name.split(' ')[0]}
                                    </a>
                                    <div className="dropdown-menu dropdown-menu-right dropdown-default"
                                         aria-labelledby="navbarDropdownMenuLink-333">
                                        {props.role.name === 'ROLE_USER'?
                                            <Link className="dropdown-item" to={"/profile"}>Profile</Link> :
                                            <Link className="dropdown-item" to={"/admin"}> Admin Panel</Link>
                                        }
                                        <Link className="dropdown-item" to={"/"} onClick={props.onUserLogout}>Logout</Link>
                                    </div>
                                </li>
                            </ul>
                        }



                    </div>
                    </span>

            </nav>
        </header>
    );
};

export default withRouter(Header);
