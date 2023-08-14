import axios from "axios";
import authHeader from "./authHeader.service";

const API_URL = "http://localhost:8080/api/cart/";

const getCartByUser = () => {
    return axios.get(API_URL + `getByUser/${JSON.parse(localStorage.getItem('user')).id}`, { headers: authHeader() });
};

const CartService = {
    getCartByUser,
    
};

export default CartService;