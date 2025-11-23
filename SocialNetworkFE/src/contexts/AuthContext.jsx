import { jwtDecode } from "jwt-decode";
import React, { createContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getFirebaseFileURL } from "../configs/firebaseSDK";


export const AuthContext = createContext()

export const AuthProvider = ({ children }) => {
    const [isLoggedIn, setIsLoggedIn] = useState(false)
    const [user, setUser] = useState(null)
    const navigate = useNavigate()

    // chạy khi truy cập trang web và có sẵn token
    useEffect(() => {
        const token = localStorage.getItem("token")
        if (token) {
            getInfoFromToken(token)
        }
    }, [])

    const loginSuccess = (token) => {
        localStorage.setItem("token", token)
        getInfoFromToken(token)
    }

    const getInfoFromToken = async (token) => {
        const payload = jwtDecode(token)
        const claim = payload.customClaim
        const username = claim.username
        const avatarUrl = await getFirebaseFileURL(`/avatars/${username}`)
        let loggedInUser = {
            id: claim.id,
            fullName: claim.fullName,
            username: claim.username,
            avatarUrl
        }
        setUser(loggedInUser)
        setIsLoggedIn(true)
    }

    const logout = () => {
        setIsLoggedIn(false)
        setUser(null)
        localStorage.removeItem("token")
        navigate("/")
    }


    const PROVIDER_VALUE = { isLoggedIn, user, setUser, loginSuccess, logout }
    return (
        <AuthContext.Provider value={PROVIDER_VALUE}>
            {children}
        </AuthContext.Provider>
    )

}