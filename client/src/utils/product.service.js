import axios from "axios";
import authHeader from "./authHeader.service";

const API_URL = "http://localhost:8080/api/product/";

const getAllProducts = () => {
    return axios.get(API_URL + "getAll");
};

const getProductById = (id) => {
    return axios.get(API_URL + `${id}`);
};

const getProductsByType = (type) => {
    return axios.get(API_URL + `getByType/${type}`);
};

const createProduct = (data) => {
    return axios.post(API_URL + `create`, data, { headers: authHeader() });
};

const updateProductById = (id, data) => {
    return axios.patch(API_URL + `update/${id}`, data, { headers: authHeader() });
};

const getPopularProductsByYear = (year) => {
    return axios.get(API_URL + `getPopularProductsByYear/${year}`, { headers: authHeader() });
};

const search = (value) => {
    return axios.get(API_URL + `search/${value}`);
};

const ProductService = {
    getAllProducts,
    getProductsByType,
    getProductById,
    createProduct,
    updateProductById,
    getPopularProductsByYear,
    search
};

export default ProductService;