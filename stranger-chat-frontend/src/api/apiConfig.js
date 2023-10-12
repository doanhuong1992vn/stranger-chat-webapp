
import axios from 'axios';
import {HOST} from "./api.js";

export const httpRequest = axios.create({
    baseURL: HOST,
    headers: {
        'Content-Type': 'application/json',
    },
});





