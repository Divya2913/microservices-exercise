import axios from "axios";

const BASE_URL = "http://localhost:8081/products";

export const getProducts = () => axios.get(BASE_URL);
export const createProduct = (product) => axios.post(BASE_URL, product);