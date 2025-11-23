import { initializeApp } from "firebase/app";
import { deleteObject, getDownloadURL, getStorage, ref, uploadBytes } from "firebase/storage";

const firebaseConfig = {
    apiKey: "AIzaSyAX-0G8BJ6n6ekzj1wM8ETeb_Tw6Ku-JcI",
    authDomain: "social-network-d428b.firebaseapp.com",
    projectId: "social-network-d428b",
    storageBucket: "social-network-d428b.appspot.com",
    messagingSenderId: "220583753891",
    appId: "1:220583753891:web:081fe94adf518809f3e716",
    measurementId: "G-Y9GKXNDRPW"
};

const firebaseApp = initializeApp(firebaseConfig);
const storage = getStorage(firebaseApp)
const uploadFileToFirebase = async(file, filePath) => {
    if (!file) return
    const storageRef = ref(storage, filePath);
    await uploadBytes(storageRef, file);
    const downloadURL = await getDownloadURL(storageRef);
    return downloadURL;
}

const getFirebaseFileURL = async(filePath) => {
    const storageRef = ref(storage, filePath);
    let url
    try {
        url = await getDownloadURL(storageRef)
    } catch (error) {
        url = ''
    }
    return url
}

const deleteFileFirebase = async(filePath) => {
    const storageRef = ref(storage, filePath)
    try {
        await deleteObject(storageRef)
    } catch (error) {
        console.log("Lỗi khi xóa file", error)
    }
}
export { uploadFileToFirebase, deleteFileFirebase, getFirebaseFileURL };