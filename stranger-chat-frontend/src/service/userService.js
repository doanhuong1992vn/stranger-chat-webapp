export const saveUser = (user) => {
    localStorage.setItem("user", JSON.stringify(user));
}

export const getJsonObjectUser = () => {
    return localStorage.getItem("user");
}

export const getUser = () => {
    let user = getJsonObjectUser();
    if (user) {
        return JSON.parse(user);
    }
    return null;
}

export const getId = () => {
    let user = getJsonObjectUser();
    if (user) {
        return JSON.parse(user).id;
    }
    return null;
}

export const getUsername = () => {
    let user = getJsonObjectUser();
    if (user) {
        return JSON.parse(user).username;
    }
    return null;
}