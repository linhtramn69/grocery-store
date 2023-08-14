import axios from "axios";
import authHeader from "./authHeader.service";

const API_URL = "http://localhost:8080/api/orders/";

const checkout = (data) => {
    return axios.post(API_URL + "checkout", data, { headers: authHeader() });
};

const getAllOrders = () => {
    return axios.get(API_URL + `getAll`, { headers: authHeader() });
};

const getOrderById = (id) => {
    return axios.get(API_URL + `getOrder/${id}`, { headers: authHeader() });
};

const updateStatusOrderById = (id, statusData) => {
    return axios.patch(API_URL + `update-status/${id}`, {status: statusData}, { headers: authHeader() });
};

const getTotalByYear = (year) => {
    return axios.get(API_URL + `getTotalByYear/${year}`, { headers: authHeader() });
};

const OrderService = {
    checkout,
    getAllOrders,
    getOrderById,
    updateStatusOrderById,
    getTotalByYear
};

export default OrderService;