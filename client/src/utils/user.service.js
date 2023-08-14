import axios from "axios";
import authHeader from "./authHeader.service";

const API_URL = "http://localhost:8080/api/user/";

const getAllUsers = () => {
    return axios.get(API_URL + "getAll", { headers: authHeader() });
};

const UserService = {
    getAllUsers
};

export default UserService;