import axios from '../custom-axios/axios'
import qs from 'qs'

const PinsService = {

    fetchPins: (map_id)=>{
        return axios.get('http://localhost:8080/maps/'+map_id+'/pins');
    },

    deletePinById: (map_id,pin_id, token)=>{
        return axios.delete('http://localhost:8080/maps/'+map_id+'/pins/'+pin_id,{
            headers:{
                'Authorization' : 'Bearer '+ token
            }
        })
    },

    addPin: (newPin, token) =>{
        const data = {
            ...newPin,
        };
        const formParams = qs.stringify(data);
        return axios.post('http://localhost:8080/maps/'+newPin.map_id+'/pins',formParams, {
            headers:{
                'Content-Type': 'application/x-www-form-urlencoded',
                'Authorization' : 'Bearer '+ token
            }
        });
    },

    updatePin:(updatedPin, map_id,token)=>{
        const data = {...updatedPin};

        return axios.put('http://localhost:8080/maps/'+map_id+'/pins/update', data,{
            headers: {
                'Content-Type': 'application/json',
                'Authorization' : 'Bearer '+ token
            }
        })
    },

};
export default PinsService;