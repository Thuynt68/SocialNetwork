import React, { useContext, useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { fetchMyNotifications, markAsRead } from '../../services/notificationService'
import { NotificationContext } from '../../contexts/NotificationContext'
import { handleScroll } from '../../services/infiniteScroll'
import { IconButton, Tooltip, Dialog, DialogTitle, DialogContent, Typography, Badge, Box } from "@mui/material";
import NotificationsOutlinedIcon from "@mui/icons-material/NotificationsOutlined";
import '../../assets/styles/Notification.css'

const Notification = () => {
    const [notifications, setNotifications] = useState([])
    const [hasMore, setHasMore] = useState(true)
    const [lastId, setLastId] = useState(0);
    const { notificationSource, unreadTotal, setUnreadTotal } = useContext(NotificationContext)
    const [dialogOpen, setDialogOpen] = useState(false)
    const navigate = useNavigate()

    const loadMoreNotifications = async () => {
        if (!hasMore) return
        const data = await fetchMyNotifications(lastId);
        setHasMore(data.length === 10)
        setNotifications([...notifications, ...data]);
        if (data.length === 10)
            setLastId(data.at(-1).id)
    };

    useEffect(() => {
        if (notificationSource) {
            loadMoreNotifications()
            notificationSource.onmessage = (message) => {
                const newNotification = JSON.parse(message.data)
                setUnreadTotal(prev => prev + 1)
                setNotifications((prevNotifications) => [newNotification, ...prevNotifications])
            }
            return () => {
                notificationSource.onmessage = null
            };
        }
    }, [notificationSource])

    const toggleDialog = () => {
        setDialogOpen(!dialogOpen)
    }

    const handleClickNotification = async (notification) => {
        if (!notification.read) {
            markAsRead(notification.id)
            setUnreadTotal(unreadTotal - 1)
        }
        setDialogOpen(false)
        window.location.href = notification.navigateUrl
        setNotifications((prevNotifications) =>
            prevNotifications.map((n) =>
                n.id === notification.id ? { ...n, read: true } : n
            )
        )
    }

    return (
        <Box>
            <Tooltip title="Thông báo" arrow>
                <IconButton onClick={toggleDialog} component={Link} to="#" color="inherit">
                    <Badge badgeContent={unreadTotal} color="error" overlap="circular">
                        <NotificationsOutlinedIcon />
                    </Badge>
                </IconButton>
            </Tooltip>

            <Dialog
                open={dialogOpen}
                onClose={() => setDialogOpen(false)}
                PaperProps={{
                    sx: {
                        width: 400, maxHeight: 500, overflowY: 'auto', position: 'fixed',
                        top: 50, right: 10, transform: 'translateX(0)'
                    }
                }}>
                <DialogTitle sx={{ borderBottom: "1px solid #ddd" }}>
                    Thông báo
                </DialogTitle>
                <DialogContent onScroll={(event) => handleScroll(event, loadMoreNotifications)}>
                    {notifications.length === 0 && <Typography variant="h6">Không có thông báo mới.</Typography>}
                    {notifications.length > 0 && notifications.map((notification) => (
                        <Box key={notification.id}
                            onClick={() => handleClickNotification(notification)}
                            className="notification-item"
                            style={{ cursor: 'pointer', padding: '10px', marginBottom: '5px', borderRadius: '5px' }}>
                            <Typography className={notification.read ? "read" : "unread"} variant="body">
                                {notification.content}</Typography><br />
                            <Typography variant="caption" color="textSecondary">
                                {new Date(notification.time).toLocaleString()}</Typography>
                        </Box>
                    ))}
                </DialogContent>
            </Dialog>
        </Box>
    )
}

export default Notification
