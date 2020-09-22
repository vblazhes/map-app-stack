import React, {useState} from "react";
import {Link, withRouter} from "react-router-dom";
import "./AuthStyle1.css"
import {FaExclamationCircle} from 'react-icons/fa';

const SignIn = (props) => {

    const [status, setStatus] = useState("def");
    let stat = "def";
    const onFormSubmit = (e) => {
        e.preventDefault();

        debugger;

        let username = e.target.usernameOrEmail.value;

        const status_tmp = async () => {
            await props.onSubmit({
                "usernameOrEmail": username,
                "password": e.target.password.value
            });
        };


        let result = async () => {
            await status_tmp().then(() => {
                stat = "resolved";
                setStatus("resolved")
            }).catch(() => {
                stat = "rejected";
                setStatus("rejected")
            });
        };


        result().then(() => {
            if (stat === "resolved") {
                debugger;
                if(username === "admin"){
                    props.history.push("/admin")
                }else{
                    props.history.push("/profile")
                }
            }else if(stat === "rejected"){
                props.history.push("/sign-in")
            }
        })
    };

    return (

        <div className="signin">
            <h4>Sign in to MapApp</h4>
            <div className="messages">

            </div>

            {status === "rejected" && <div style={{visibility: status === "rejected" ? "visible" : "hidden"}}>
                <span style={{color: "rgb(224, 36, 94)"}}><FaExclamationCircle/></span>
                <span style={{
                    color: "rgb(224, 36, 94)",
                    textAlign: "center"
                }}>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Invalid username or password!</span>
            </div>}

            <form onSubmit={onFormSubmit}>

                <div>
                    <label htmlFor="login">Username or email</label><br/>
                    <input autoCapitalize="off" autoCorrect="off" autoFocus="autofocus" id="login"
                           name="usernameOrEmail" tabIndex="1" type="text" className="signin-input form-control"/>
                </div>
                <div>
                    <label htmlFor="password">Password</label>
                    <a className="forgot-password" href="https://maphub.net/auth/reset_password"
                       style={{float: "right"}}>Forgot password?</a><br/>
                    <input id="password" name="password" tabIndex="2" type="password"
                           className="signin-input form-control"/>
                </div>

                <input type="submit" tabIndex="3" value="Log in" className="btn "
                       style={{marginBottom: "15px", background: "#17b585", color: "white", width: "100px"}}/>

            </form>

            <div className="register-notice">
                You don't have an account yet?
                <Link to={"/sign-up"}> Sign up here</Link>.
            </div>
        </div>
    );
};

export default withRouter(SignIn);