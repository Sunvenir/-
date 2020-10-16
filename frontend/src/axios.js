import axios from 'axios'
const instance = axios.create({
    // baseURL:'http://139.9.216.155:8888'
    baseURL:'http://localhost:8888'
    // baseURL:'http://10.136.208.76:8888' //郭荣
    // baseURL:'http://192.168.31.89:8888'
    // baseURL:'http://10.136.158.100:8888'
})
export default instance