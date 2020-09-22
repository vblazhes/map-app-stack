import React, {Component} from 'react';
import './App.css';
import {
    BrowserRouter as Switch,
    Link,
    Route,
    Redirect
} from "react-router-dom";
import Header from "./Components/Header/Header";
import Mapbox from "./Components/Mapbox/Mapbox";
import Explore from "./Components/Explore/Explore";
import Profile from "./Components/Profile/Profile";
import SignIn from "./Components/Authentication/Login/SignIn";
import SignUp from "./Components/Authentication/Register/SignUp";
import AuthService from './Repository/axiosAuthRepository';
import MapEdit from "./Components/MapEdit/MapEdit";
import MapCreate from "./Components/MapCreate/MapCreate";
import Admin from "./Components/Admin/Admin";
import Home from "./Components/Home/Home";

class App extends Component {

    constructor(props) {
        super(props);
        this.state = {
            user: {},
            token: "",
            isSignedIn: false,
            role:{},
        }
    }

    componentDidMount() {
        let state = this.getInitialState();
        console.log(state);
        this.setState({...state});
    }

    getInitialState = () => {
        // debugger;

        let stateStr = localStorage.getItem('state') || "{}";

        let state = JSON.parse(stateStr);
        console.log(state);

        return {
            user: state.user || {},
            token: state.token || "",
            isSignedIn: state.isSignedIn || false,
            role: state.role || {}
        };
    };

    setUserState = (state) => {
        // debugger;
        // console.log("setUserState:");
        // console.log(state);
        localStorage.setItem('state', JSON.stringify(state));

        this.setState({
            user: state.user,
            token: state.token,
            isSignedIn: state.isSignedIn,
            role: state.role
        });
    };

    signUpUser = ((userDetails) => {
        const signUserDetails = {
            "usernameOrEmail": userDetails.username,
            "password": userDetails.password
        };
        AuthService.signUpUser(userDetails).then((response) => {
            debugger;
            console.log(response.data);
            this.signInUser(signUserDetails)
        });
    });

    signInUser = ((userDetails) => {
        // debugger;
     return  AuthService.signInUser(userDetails).then((response) => {
            // console.log(response.data);
            AuthService.getUserRole(response.data.user.id).then((response1)=>{

                //console.log(response1);

                this.setUserState({
                    "user": response.data.user,
                    "token": response.data.accessToken,
                    "isSignedIn": true,
                    "role":response1.data
                });
            })
        })
    });

    onUserLogout = ()=>{
        const state =({
            user: {},
            token: "",
            isSignedIn: false,
            role: {}
        });

        this.setUserState(state);

    };

    render() {
        return (
            <div className="App">
                <Switch>
                    <Header isSignedIn={this.state.isSignedIn} user={this.state.user} role={this.state.role} onUserLogout={this.onUserLogout}/>

                    <Route path={"/"} exact render={()=>
                        <Home/>
                    }>
                    </Route>

                    <Route path={"/explore/maps"} exact render={() =>
                        <Explore/>
                    }>
                    </Route>

                    <Route path={"/explore/maps/:map_id"} exact render={() =>
                        <Mapbox user={this.state.user}/>
                    }>
                    </Route>

                    <Route path={"/sign-in"} exact render={() =>
                        <SignIn onSubmit={this.signInUser} isSignedIn={this.state.isSignedIn}/>
                    }>
                    </Route>

                    <Route path={"/sign-up"} exact render={() =>
                        <SignUp onSubmit={this.signUpUser}/>
                    }>
                    </Route>

                    <Route path={"/explore/maps/:map_id/edit"} exact render={() =>
                        <MapEdit user={this.state.user} token={this.state.token}/>
                    }>
                    </Route>

                    <Route path={"/maps/create"} exact render={() =>
                        <MapCreate user={this.state.user} token={this.state.token}/>
                    }>
                    </Route>

                    {this.state.isSignedIn && this.state.role.name === 'ROLE_USER' &&
                    <Route path={"/profile"} exact render={() =>
                        <Profile user={this.state.user} token={this.state.token}/>
                    }>
                    </Route>
                    }

                    {this.state.isSignedIn && this.state.role.name === 'ROLE_ADMIN' &&
                    <Route path={"/admin"} exact render={() =>
                       <Admin token={this.state.token}/>
                    }>
                    </Route>
                    }

                </Switch>
            </div>
        );
    }
}

export default App;