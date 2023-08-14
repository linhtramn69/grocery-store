import axios from "axios";
import authHeader from "./authHeader.service";

const API_URL = "http://localhost:8080/api/order-details/";

const getOrderDetails = (id) => {
    return axios.get(API_URL + `getOrderDetails/${id}`, { headers: authHeader() });
};

const OrderProductService = {
    getOrderDetails,
};

export default OrderProductService;