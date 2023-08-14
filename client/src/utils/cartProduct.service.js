import axios from "axios";
import authHeader from "./authHeader.service";

const API_URL = "http://localhost:8080/api/cart-product/";

const getProductsFromCart = (id) => {
    return axios.get(API_URL + `get-products-from-cart/${id}`, { headers: authHeader() });
};

const addProductToCart = (data) => {
    return axios.post(API_URL + "add-to-cart", data, { headers: authHeader() });
};

const deleteProductFromCart = (dataNew) => {
    return axios.delete(API_URL + "delete-product-cart", { headers: authHeader(), data: dataNew });
};

const CartProductService = {
    getProductsFromCart,
    addProductToCart,
    deleteProductFromCart,
    
};

export default CartProductService;