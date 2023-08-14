import axios from "axios";

const API_URL = "http://localhost:8080/api/type-product/";

const getAllTypes = (data) => {
    return axios.get(API_URL + "getAll");
};

const TypeProductService = {
    getAllTypes,
};

export default TypeProductService;