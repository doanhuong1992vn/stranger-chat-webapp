import {httpRequest} from "./apiConfig.js";
import {LOGIN_API} from "./api.js";

export const login = (body) => {
    return httpRequest.post(LOGIN_API, body);
}
