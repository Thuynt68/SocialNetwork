import { API } from "../configs/config"
import httpClient from "../configs/httpClient"

export const login = async(form) => {
    return await httpClient.post(API.LOGIN, form)
}

export const register = async(form) => {
    return await httpClient.post(API.REGISTER, form)
}

export const verifyEmail = async(token) => {
    try {
        const response = await httpClient.post(`${API.REGISTER}/verify`, null, { params: { token } })
        return response.data.result
    } catch (error) {
        console.log("Lỗi khi xác thực email", error.response)
    }
}

export const changePassword = async(currentPassword, newPassword) => {
    try {
        const body = { currentPassword, newPassword }
        const response = await httpClient.put(API.CHANGE_PASSWORD, body)
        return response.data.result
    } catch (error) {
        console.log("Lỗi khi đổi mật khẩu", error.response)
    }
}