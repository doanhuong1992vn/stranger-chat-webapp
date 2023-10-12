import {BrowserRouter, Route, Routes} from "react-router-dom";
import {CHAT_ROOM, LOGIN_PAGE} from "./constant/page.js";
import LoginPage from "./page/LoginPage.jsx";
import ChatPage from "./page/ChatPage.jsx";


function App() {
    return (
        <BrowserRouter>
            <div className={"d-flex align-items-center min-h-screen"}>
                    <Routes>
                        <Route path={LOGIN_PAGE} element={<LoginPage/>}/>
                        <Route path={CHAT_ROOM} element={<ChatPage />}/>
                    </Routes>
            </div>
        </BrowserRouter>
    )
}

export default App;
