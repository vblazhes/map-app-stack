import axios from '../custom-axios/axios'
import qs from 'qs'

const MapsService = {

    fetchMap: (map_id)=>{
        return axios.get('http://localhost:8080/maps/'+map_id);
    },

    fetchAllMaps:()=>{
        return axios.get('http://localhost:8080/maps');
    },

    fetchPendingMaps:(token)=>{
        return axios.get('http://localhost:8080/maps/pending',{
            headers:{
                'Authorization' : 'Bearer '+ token
            }
        })
    },

    fetchMapsByOwnerId:(owner_id, token)=>{
        return axios.get('http://localhost:8080/maps/owner/'+owner_id,{
            headers:{
                'Authorization' : 'Bearer '+ token
            }
        })
    },

    deleteMapById:(map_id, token)=>{
        return axios.delete('http://localhost:8080/maps/'+map_id,{
            headers:{
                'Authorization' : 'Bearer '+ token
            }
        });
    },

    saveMap:(newMap, token)=>{

        debugger;
        console.log(newMap.imageFile);

        const formData = new FormData();
        formData.append('default_zoom', newMap.default_zoom);
        formData.append('center_latitude', newMap.center_latitude);
        formData.append('center_longitude', newMap.center_longitude);
        formData.append('style', newMap.style);
        formData.append('name', newMap.name);
        formData.append('background', newMap.background);
        formData.append('description', newMap.description);
        formData.append('visibility', newMap.visibility);
        formData.append('approved', newMap.approved);
        formData.append('imageFile', newMap.imageFile);
        formData.append('owner', JSON.stringify(newMap.owner));

        debugger;
        console.log(formData.get('imageFile'));
        console.log(formData.get('owner'));

        // const data = {
        //     'default_zoom': newMap.default_zoom,
        //     'center_latitude': newMap.center_latitude,
        //     'center_longitude': newMap.center_longitude,
        //     'style': newMap.style,
        //     'name': newMap.name,
        //     'background': newMap.background,
        //     'description': newMap.description,
        //     'visibility': newMap.visibility,
        //     'approved': newMap.approved,
        //     'imageFile': newMap.imageFile,
        //     'owner': newMap.owner,
        // };

        return axios.post('http://localhost:8080/maps', formData, {
            headers: {
                'Authorization' : 'Bearer '+ token
            }
        });
    },

    updateMap:(updatedMap, token)=>{
        const data = {...updatedMap};

        return axios.put('http://localhost:8080/maps/update', data,{
            headers: {
                'Content-Type': 'application/json',
                'Authorization' : 'Bearer '+ token
            }
        })
    },

    approveMapById: (map_id, decision, token) =>{
        const data = {
          'decision' : decision
        };
        const formParam = qs.stringify(data);
      return axios.patch('http://localhost:8080/maps/'+map_id+'/admin-approval', formParam,{
          headers: {
              'Content-Type': 'application/x-www-form-urlencoded',
              'Authorization' : 'Bearer '+ token
          }
      })
    }

};
export default MapsService;