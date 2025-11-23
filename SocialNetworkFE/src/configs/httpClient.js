import axios from "axios";
import { CONFIG } from "./config";

const httpClient = axios.create({
    baseURL: CONFIG.BASE_URL,
    headers: {
        "Content-Type": "application/json"
    }
})

httpClient.interceptors.request.use(config => {
    const token = localStorage.getItem('token');
    let headers = {
        'ngrok-skip-browser-warning': 'true'
    }
    if (token) {
        headers['Authorization'] = `Bearer ${token}`
    }
    config.headers = headers
    return config;
});

httpClient.interceptors.response.use(
    response => response,
    error => {
        if (error.response.status === 401) {
            console.log("Có status 401")
            localStorage.removeItem("token")
            window.location.href = '/'
        }
    }
)

export default httpClient