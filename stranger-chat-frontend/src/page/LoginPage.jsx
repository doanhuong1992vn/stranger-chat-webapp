import LoginForm from "../component/LoginForm.jsx";
import {useEffect} from "react";
function LoginPage() {
    useEffect(() => {
        localStorage.removeItem("user");
    }, []);

    return (
        <div className="container col-6">
            <LoginForm />
        </div>
    );
}

export default LoginPage;