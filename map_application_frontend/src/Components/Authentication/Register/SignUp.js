import React, {useState} from "react";
import "../Login/AuthStyle1.css"
import {Link, withRouter} from "react-router-dom";
import {FaExclamationCircle} from 'react-icons/fa';


const SignUp = (props) => {

        const [status, setStatus] = useState("def");
        let stat = "def";


        const onFormSubmit = (e) => {

            e.preventDefault();

            debugger;
            let status_tmp;

            props.history.push("/profile");

            props.onSubmit({
                "name": e.target.name.value,
                "username": e.target.username.value,
                "email": e.target.email.value,
                "password": e.target.password.value
            });

        };


        return (
            <div className="signin">
                <h4>Sign up in to MapApp</h4>
                <div className="messages">

                </div>

                <div className="register-notice">
                    Already have an account?
                    <Link to={"/sign-in"}> Log in here</Link>.
                    <br/>
                    <br/>
                </div>

                {status === "rejected" && <div style={{visibility: status === "rejected" ? "visible" : "hidden"}}>
                    <span style={{color: "rgb(224, 36, 94)"}}><FaExclamationCircle/></span>
                    <span style={{
                        color: "rgb(224, 36, 94)",
                        textAlign: "center"
                    }}>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Username or email already exist!</span>
                </div>}

                <form onSubmit={onFormSubmit}>

                    <div>
                        <label htmlFor="name">Name</label><br/>
                        <input autoCapitalize="off" autoCorrect="off" autoFocus="autofocus" id="name"
                               name="name" tabIndex="1" type="text" className="signin-input form-control"/>
                    </div>

                    <div>
                        <label htmlFor="username">Username</label><br/>
                        <input id="username" name="username" tabIndex="1" type="text"
                               className="signin-input form-control"/>
                    </div>

                    <div>
                        <label htmlFor="email">Email</label><br/>
                        <input id="email" name="email" tabIndex="1" type="email" className="signin-input form-control"/>
                    </div>

                    <div>
                        <label htmlFor="password">Password</label>
                        <input id="password" name="password" tabIndex="2" type="password"
                               className="signin-input form-control"/>
                    </div>

                    <p>
                        By signing up, I agree to MapApp's
                        <br/>
                        <a href="#" style={{color:"dimgrey"}}>Terms of Service</a> and <a href="#" style={{color:"dimgrey"}}>Privacy Policy</a>.
                    </p>

                    <input type="submit" tabIndex="3" value="Sign up" className="btn "
                           style={{marginBottom: "15px", background: "#17b585", color: "white", width: "100px"}}/>

                </form>

            </div>
        );
    }
;

export default withRouter(SignUp);