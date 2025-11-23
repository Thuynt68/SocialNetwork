import React, { useContext, useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { createFriendRequest, actionFriendRequestByUserId, unfriend } from "../../services/friendService"
import { fetchProfileById, updateMyProfile } from '../../services/profileService'
import { Avatar, Box, Button, IconButton, Menu, MenuItem, Paper, Stack, Tab, Tabs, Tooltip, Typography } from '@mui/material'
import ZoomImage from '../common/ZoomImage'
import PostList from '../post/PostList'
import PeopleIcon from '@mui/icons-material/People';
import MoreVertIcon from "@mui/icons-material/MoreVert";
import PendingIcon from '@mui/icons-material/Pending';
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import MessageIcon from '@mui/icons-material/Message';
import LockIcon from '@mui/icons-material/Lock';
import EditIcon from "@mui/icons-material/Edit";
import { ChatSocketContext } from '../../contexts/ChatSocketContext'
import { format } from 'date-fns'
import EditProfileDialog from './EditProfileDialog'
import ChangePasswordDialog from './ChangePasswordDialog'

const Profile = () => {
    const { id } = useParams()
    const [profile, setProfile] = useState({})
    const [editing, setEditing] = useState(false)
    const [openChangePassword, setOpenChangePassword] = useState(false)
    const [zoom, setZoom] = useState(false)
    const [activeTabIndex, setActiveTabIndex] = useState(0)
    const [myPosts, setMyPosts] = useState([])
    const [anchorEl, setAnchorEl] = useState(null)
    const { openChatByFriend } = useContext(ChatSocketContext)

    useEffect(() => {
        const getProfile = async () => {
            const profileData = await fetchProfileById(id)
            setProfile(profileData)
        }
        getProfile()
    }, [id])

    const handleTabIndexChange = (_, newIndex) => {
        setActiveTabIndex(newIndex)
    }

    const sendFriendRequest = () => {
        createFriendRequest(profile.id)
        setProfile({ ...profile, relation: "sentRequest" })
        setAnchorEl(null)
    }

    const updateFriendRequest = (accept) => {
        setProfile({
            ...profile,
            relation: accept ? "friend" : "toSendRequest"
        })
        actionFriendRequestByUserId(profile.id, accept)
    }

    const handleUnfriend = () => {
        unfriend(profile.id)
        setProfile({ ...profile, relation: "toSendRequest" })
    }


    const handleOpenMenu = (event) => {
        console.log("mở menu")
        if (["friend", "sentRequest"].includes(profile.relation))
            setAnchorEl(event.currentTarget)
    }

    const handleCloseMenu = () => {
        setAnchorEl(null)
    }


    const tabStyle = {
        fontWeight: "bold", textTransform: 'none',
    }

    const buttonStyle = {
        borderRadius: "10px",
        textTransform: "none",
        height: "30px",
        width: "fit-content",
        fontWeight: "bold",
        p: 2
    }

    const relationLabel = {
        friend: "Bạn bè",
        sentRequest: "Đã gửi lời mời kết bạn",
    }


    return (
        <Box sx={{ ml: "27%", width: "632px" }}>
            <Paper elevation={3} sx={{ p: "32px 0 16px 32px", borderRadius: 3, }}>
                <Stack direction="column" spacing={2}>
                    <Avatar src={profile.avatarUrl} alt="Avatar" sx={{ width: 120, height: 120, mb: 2, cursor: "pointer" }}
                        onClick={() => setZoom(true)} />
                    <ZoomImage open={zoom} onClose={() => setZoom(false)} imageSrc={profile.avatarUrl} />
                    {profile.id && <Typography variant="h5" sx={{ fontWeight: 'bold', width: "fit-content" }}>
                        {`${profile.firstName} ${profile.lastName}`}
                    </Typography>}
                    {profile.id && profile.relation !== "myProfile" &&
                        <Stack direction="row" alignItems="center" spacing={1}>
                            {["friend", "sentRequest"].includes(profile.relation) &&
                                <Stack direction="row" spacing={1} alignItems="center"
                                    sx={{
                                        p: 1, boxShadow: 2, borderRadius: "8px",
                                        width: "fit-content", color: "#0288d1", border: "1px solid #ddd"
                                    }} >
                                    {profile.relation === "friend" ? <PeopleIcon /> : <PendingIcon />}
                                    <Typography fontWeight="bold" fontSize={14}>{relationLabel[profile.relation]}</Typography>
                                </Stack>}
                            {profile.relation === "friend" &&
                                <Button variant='outlined' sx={{ ...buttonStyle, padding: "20px" }}
                                    onClick={() => openChatByFriend(profile)} startIcon={<MessageIcon />}>
                                    Nhắn tin
                                </Button>}

                            {["friend", "sentRequest"].includes(profile.relation) &&
                                <>
                                    <Tooltip title="Tùy chọn">
                                        <IconButton onClick={handleOpenMenu}>
                                            <MoreVertIcon />
                                        </IconButton>
                                    </Tooltip>
                                    <Menu anchorEl={anchorEl} open={Boolean(anchorEl)} onClose={handleCloseMenu}
                                        anchorOrigin={{ vertical: 'top', horizontal: 'right' }}>
                                        <MenuItem sx={{ padding: "0 10px 0px 10px" }}>
                                            {profile.relation === "friend" &&
                                                <Stack direction="column" spacing={1}>

                                                    <Button color="inherit" sx={buttonStyle} onClick={handleUnfriend}>
                                                        Hủy kết bạn
                                                    </Button>
                                                </Stack>}

                                            {profile.relation === "sentRequest" &&
                                                <Button color='inherit' sx={buttonStyle}
                                                    onClick={() => updateFriendRequest(false)}>
                                                    Hủy yêu cầu kết bạn
                                                </Button>}
                                        </MenuItem>
                                    </Menu>
                                </>}
                            {["toSendRequest", "hasRequest"].includes(profile.relation) &&
                                <>
                                    {profile.relation === "toSendRequest" &&
                                        <Button variant="contained" sx={buttonStyle} onClick={sendFriendRequest}
                                            startIcon={<PersonAddIcon />}>
                                            Gửi kết bạn
                                        </Button>}

                                    {profile.relation === "hasRequest" &&
                                        <>
                                            <Button variant="contained" sx={buttonStyle}
                                                onClick={() => updateFriendRequest(true)}>
                                                Chấp nhận
                                            </Button>
                                            <Button variant="outlined" sx={{ ...buttonStyle, ml: 1 }}
                                                onClick={() => updateFriendRequest(false)}>
                                                Từ chối
                                            </Button></>}
                                </>}

                        </Stack>}
                </Stack>
            </Paper>

            <Paper elevation={3} sx={{ p: "10px 30px", borderRadius: 3, mt: 2 }}>
                <Tabs value={activeTabIndex} onChange={handleTabIndexChange} sx={{ mb: 2 }}>
                    <Tab label="Bài viết" sx={tabStyle} />
                    <Tab label="Thông tin cá nhân" sx={tabStyle} />
                    {profile.relation === "myProfile" &&
                        <Tab label="Tùy chọn" sx={tabStyle} />
                    }
                </Tabs>
                {activeTabIndex === 0 &&
                    <PostList posts={myPosts} setPosts={setMyPosts} profile={profile} />}
                {activeTabIndex === 1 &&
                    <Box sx={{ width: "632px", textAlign: "left" }}>
                        <Typography fontWeight="bold">
                            {`Tên: ${profile.fullName}`}
                        </Typography>
                        <Typography fontWeight="bold">
                            {`Sinh nhật: ${format(profile.dateOfBirth, "dd/MM/yyyy")}`}
                        </Typography>
                    </Box>}
                {activeTabIndex === 2 &&
                    <>
                        <Stack direction="column" spacing={1} alignItems="left" sx={{ width: "632px" }}>
                            <Button variant="outlined" sx={buttonStyle} startIcon={<EditIcon />}
                                onClick={() => setEditing(true)}>
                                Sửa thông tin cá nhân
                            </Button>
                            <Button variant="outlined" color="warning" sx={buttonStyle} startIcon={<LockIcon />}
                                onClick={() => setOpenChangePassword(true)}>
                                Đổi mật khẩu
                            </Button>
                        </Stack>
                        <EditProfileDialog open={editing} onClose={() => setEditing(false)}
                            profile={profile} setProfile={setProfile} />
                        <ChangePasswordDialog open={openChangePassword} onClose={() => setOpenChangePassword(false)} />
                    </>}
            </Paper>
        </Box >

    )
}

export default Profile
