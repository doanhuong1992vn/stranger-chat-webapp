import {InputText} from 'primereact/inputtext';
import {Button} from 'primereact/button';
import {BsInfoCircle} from "react-icons/bs";
import {useState} from "react";
import {login} from "../api/authApi.js";
import {saveUser} from "../service/userService.js";
import {useNavigate} from "react-router-dom";
import {CHAT_ROOM} from "../constant/page.js";
import {getServerErrorMessages} from "../utils/errorUtils.js";
import ErrorMessages from "./ErrorMessages.jsx";
import {CONNECTION_ERROR, EMPTY_INPUT_ERROR} from "../constant/message.js";


export default function LoginForm() {
    const navigate = useNavigate();

    const [username, setUsername] = useState("Stranger " + Math.floor(Math.random() * 900 + 100));
    const [usernameError, setUsernameError] = useState('');
    const [serverMessages, setServerMessages] = useState([]);


    const handleChangeUsername = (username) => {
        setUsername(username);
        setUsernameError(username ? '' : EMPTY_INPUT_ERROR);
    }

    const handleClickLogin = async () => {
        if (username) {
            await login({username})
                .then((response) => {
                    saveUser(response.data.data);
                    navigate(CHAT_ROOM);
                })
                .catch((error) => {
                    if (error.response) {
                        setServerMessages(getServerErrorMessages(error.response.data));
                    } else {
                        setServerMessages([CONNECTION_ERROR])
                    }
                });
        }
    }


    return (
        <div className="card ">
            <div className="flex flex-column">
                <div className="h2 text-uppercase text-center text-primary">
                    Stranger Chat
                </div>
                <div className="flex flex-wrap justify-content-center align-items-center gap-3 mt-2">
                    <label htmlFor="username" className="w-6rem">
                        Username
                    </label>
                    <InputText
                        id="username"
                        type="text"
                        value={username}
                        maxLength={20}
                        onChange={(e) => handleChangeUsername(e.target.value)}
                    />
                </div>
                {
                    usernameError?.length > 0 &&
                    <div className={"flex justify-content-start"}>
                        <span className={"text-danger"}>
                            <BsInfoCircle className={"me-1 mb-1"}/>{usernameError}
                        </span>
                    </div>
                }
                {
                    serverMessages.length > 0 &&
                    <ErrorMessages messages={serverMessages} />
                }
                <Button
                    label="Login"
                    icon="pi pi-user"
                    className="w-10rem mx-auto rounded-5 mt-3"
                    onClick={handleClickLogin}
                />
            </div>
        </div>
    )
}
