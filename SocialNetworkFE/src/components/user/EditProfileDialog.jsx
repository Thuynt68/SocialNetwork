import React, { useEffect, useState } from 'react'
import { uploadFileToFirebase } from '../../configs/firebaseSDK'
import {
    Dialog, DialogTitle, DialogContent, DialogActions, TextField,
    Button, Box, Avatar, Typography,
    Stack,
    CircularProgress
} from '@mui/material'
import { updateMyProfile } from '../../services/profileService'

const EditProfileDialog = ({ open, onClose, profile, setProfile }) => {
    const [selectedImage, setSelectedImage] = useState(null)
    const [imagePreview, setImagePreview] = useState('')
    const [formData, setFormData] = useState({})
    const [loading, setLoading] = useState(false)

    useEffect(() => {
        setFormData(profile)
    }, [profile])

    const handleImageChange = (e) => {
        const file = e.target.files[0]
        if (file) {
            setSelectedImage(file)
            const reader = new FileReader()
            reader.onloadend = () => {
                setImagePreview(reader.result)
            }
            reader.readAsDataURL(file)
        }
    }

    const handleInputChange = (e) => {
        const { name, value } = e.target
        setFormData({ ...formData, [name]: value })
    }

    const handleUpdateProfile = async () => {
        setLoading(true)
        let updatedData = formData
        const avatarUrl = await uploadFileToFirebase(selectedImage, `avatars/${profile.username}`)
        if (avatarUrl) updatedData.avatarUrl = avatarUrl
        setProfile(updatedData)
        await updateMyProfile(updatedData)
        setLoading(false)
        onClose()
    }

    const buttonStyle = { textTransform: 'none', fontWeight: "bold" }

    return (
        <Dialog open={open} onClose={onClose}>
            <DialogTitle>Sửa thông tin cá nhân</DialogTitle>
            {!loading &&
                <DialogContent>
                    <Box sx={{ my: 2 }}>
                        <TextField fullWidth label="Họ" name="firstName" value={formData.firstName || ''}
                            onChange={handleInputChange} variant="outlined" sx={{ mb: 2 }} />
                        <TextField fullWidth label="Tên" name="lastName" value={formData.lastName || ''}
                            onChange={handleInputChange} variant="outlined" sx={{ mb: 2 }} />
                        <TextField fullWidth label="Ngày sinh" name="dateOfBirth"
                            type="date" value={formData.dateOfBirth || ''} onChange={handleInputChange} variant="outlined"
                            InputLabelProps={{
                                shrink: true,
                            }}
                            sx={{ mb: 2 }} />
                    </Box>

                    <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 2 }}>
                        <Typography variant="subtitle1">Avatar</Typography>
                        <Avatar
                            src={imagePreview || formData.avatarUrl}
                            alt="Avatar"
                            sx={{ width: 100, height: 100 }} />
                        <Button variant="contained" sx={buttonStyle} component="label">
                            Tải lên ảnh
                            <input type="file" accept="image/*" hidden onChange={handleImageChange} />
                        </Button>
                    </Box>
                </DialogContent>}
            {loading && 
            <Stack direction="row" justifyContent="center">
                <CircularProgress/>
            </Stack>}

            <DialogActions>
                <Button onClick={handleUpdateProfile} color="primary" variant="contained"
                    disabled={!formData.firstName || !formData.lastName || !formData.dateOfBirth || loading}
                    sx={buttonStyle}>
                    Xác nhận
                </Button>
                <Button variant="outlined" onClick={onClose} sx={buttonStyle} disabled={loading}>
                    Đóng
                </Button>
            </DialogActions>
        </Dialog>
    )
}

export default EditProfileDialog
