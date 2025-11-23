import React, { createContext, useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"

export const MenuContext = createContext()

export const MenuProvider = ({ children }) => {
    const [activeMenu, setActiveMenu] = useState(sessionStorage.getItem('activeMenu')
                                                ? parseInt(sessionStorage.getItem('activeMenu')) : 1)
    const [friendMenu, setFriendMenu] = useState(1)
    const [groupMenu, setGroupMenu] = useState(1)
    const navigate = useNavigate()

    const handleClickFriendMenu = (menuId) => {
        setFriendMenu(menuId)
        navigate("/friends")
    }

    const handleClickGroupMenu = (menuId) => {
        setFriendMenu(menuId)
        navigate("/group")
    }

    const PROVIDER_VALUE = {
        activeMenu, setActiveMenu,
        friendMenu, setFriendMenu: handleClickFriendMenu,
        groupMenu, setGroupMenu: handleClickGroupMenu
    }

    return (
        <MenuContext.Provider value={PROVIDER_VALUE}>
            {children}
        </MenuContext.Provider>
    )
}