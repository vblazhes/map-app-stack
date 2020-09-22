import axios from '../custom-axios/axios'
import qs from 'qs'

const AuthService = {

    signUpUser: (userDetails) => {

        const data = {
            'name': userDetails.name,
            'username': userDetails.username,
            'email': userDetails.email,
            'password': userDetails.password
        };

        return axios.post('http://localhost:8080/api/auth/signup', data, {
            headers: {
                'Content-Type': 'application/json'
            }
        });
    },


    signInUser: (userDetails) => {
        const data = {
            'usernameOrEmail': userDetails.usernameOrEmail,
            'password': userDetails.password
        };
        return axios.post('http://localhost:8080/api/auth/signin', data, {
            headers: {
                'Content-Type': 'application/json'
            }
        });
    },

    getUserRole: (user_id) =>{
        return axios.get('http://localhost:8080/users/'+user_id+'/role');
    },

    getUser:(user_id)=>{
        return axios.get('http://localhost:8080/users/'+user_id);
    }


};
export default AuthService;