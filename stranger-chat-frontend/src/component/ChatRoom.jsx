import {Button} from "primereact/button";
import {useEffect, useRef, useState} from "react";
import {Dialog} from "primereact/dialog";
import {getId, getJsonObjectUser, getUser, getUsername} from "../service/userService.js";
import {Client} from "@stomp/stompjs";
import {ProgressSpinner} from 'primereact/progressspinner';
import {WS_BROKER_URL, WS_USER_NOTIFICATION_TOPIC, WS_START, WS_USER_CHAT_TOPIC, WS_SEND_MESSAGE} from "../api/api.js";
import "../assets/chatBox.css";
import {IoSend} from "react-icons/io5";
import {Toast} from "primereact/toast";
import blankAvatar from '../assets/blank-avatar.jpg';
import {Avatar} from "primereact/avatar";

let stompClient;

function ChatRoom() {
    const [isDialogVisible, setDialogVisible] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [messages, setMessages] = useState([]);
    const [content, setContent] = useState("");
    const [partner, setPartner] = useState({});
    const toast = useRef(null);


    useEffect(() => {
        if (!stompClient) {
            stompClient = new Client({
                brokerURL: WS_BROKER_URL
            });
            stompClient.onConnect = () => {
                showConnectionToast(true, "Connecting...", "Searching for a match... 🤝");
                stompClient.publish({
                    destination: WS_START, body: getJsonObjectUser()
                });
                stompClient.subscribe(WS_USER_NOTIFICATION_TOPIC + getId(), (response) => {
                    setIsLoading(false);
                    const {success, partner, message} = JSON.parse(response.body);
                    if (success) {
                        setDialogVisible(true);
                        setPartner({name: partner})
                    }
                    const summary = success ? 'Connection successful!' : 'Connection failed!';
                    showConnectionToast(success, summary, message);
                })
                stompClient.subscribe(WS_USER_CHAT_TOPIC + getId(), (message) => {
                    if (!isDialogVisible) {
                        setDialogVisible(true);
                    }
                    setMessages(prevMessages => [...prevMessages, {isSend: false, message: message.body}]);
                });
            }
            stompClient.onWebSocketError = (error) => {
                console.error('Error with websocket', error);
                disconnect();
            }
            stompClient.onStompError = (frame) => {
                console.error('Broker reported error: ' + frame.headers['message']);
                console.error('Additional details: ' + frame.body);
                disconnect();
            }
            return () => {
                disconnect();
            }
        }
    }, []);

    useEffect(() => {
        const dialogContent = document.querySelector(".my-dialog-content");
        if (dialogContent) {
            dialogContent.scrollTop = dialogContent.scrollHeight;
        }
    }, [messages]);


    const showConnectionToast = (success, summary, message) => {
        setTimeout(() => {
            if (toast.current) {
                toast.current.show({
                    severity: success ? 'success' : 'warn',
                    summary: summary,
                    detail: message,
                    life: 5000
                });
            }
        }, 50)
    }


    const disconnect = () => {
        if (stompClient.active) {
            stompClient.deactivate().then(() => {
                showConnectionToast(false, "Disconnected", "");
            });
        }
        setMessages([]);
        setDialogVisible(false);
    };


    const handleClickStartChatting = async () => {
        if (!stompClient.active) {
            activate();
        } else {
            await disconnect();
            activate();
        }
    }

    const activate = () => {
        stompClient.activate();
        setIsLoading(true);
    }

    const handleDialogHide = () => {
        disconnect();
        setDialogVisible(false);
    };

    const sendMessage = () => {
        if (stompClient.active) {
            if (content) {
                const user = getUser();
                stompClient.publish({
                    destination: WS_SEND_MESSAGE,
                    body: JSON.stringify({'content': content, 'senderId': user.id, 'senderName': user.username})
                });
                setMessages([...messages, {isSend: true, message: content}]);
                setContent('');
            } else {
                showConnectionToast(false, "No message to send", "Please enter your message!")
            }
        } else {
            showConnectionToast(false, "Connection disconnected", "Connection timed out due to inactivity!")
        }
    };

    const handleEnterSendMessage = (e) => {
        if (e.key === 'Enter') {
            sendMessage()
        }
    }

    const footerTemplate = () => (
        <div className="col-12" onKeyDown={handleEnterSendMessage} tabIndex={0}>
            <div className="chat-box-tray flex justify-content-between">
                <input
                    type="text"
                    placeholder={`Hey ${getUsername()}, type your message here...`}
                    value={content}
                    onChange={e => setContent(e.target.value)}
                    className={"w-100 input-message ps-3"}
                />
                <IoSend size={40} onClick={sendMessage} cursor={"pointer"} color={"#278be5"}/>
            </div>
        </div>
    );

    const headerTemplate = () => (
        <div className="settings-tray">
            <div className="friend-drawer no-gutters friend-drawer--grey">
                <Avatar image={blankAvatar} size="normal" shape="circle"/>
                <div className="text">
                    <h6>{partner.name}</h6>
                </div>
            </div>
        </div>
    )


    return (
        <div className={"container col-8 d-flex justify-content-center"}>
            <Toast ref={toast} style={{zIndex: 9999}}/>
            {
                isLoading
                    ?
                    <div className={"d-flex flex-column justify-content-center"}>
                        <ProgressSpinner
                            style={{width: '200px', height: '200px'}}
                            strokeWidth="10"
                            fill="var(--surface-ground)"
                            animationDuration="1s"
                        />
                        <div className={"flex justify-content-center mt-4 h3"}>Searching for a match... 🤝</div>
                    </div>
                    :
                    <Button size={"large"} className={"h1 text-uppercase rounded-8"} onClick={handleClickStartChatting}>
                        Start Chatting
                    </Button>
            }
            <Dialog
                header={headerTemplate}
                visible={isDialogVisible}
                modal={true}
                style={{width: '50vw', minHeight: '95vh'}}
                onHide={handleDialogHide}
                footer={footerTemplate}
                contentClassName={"my-dialog-content"}
            >
                <div>
                    {messages.map(({isSend, message}, index) => {
                        return (
                            <div className="row no-gutters" key={index}>
                                {
                                    isSend
                                        ? <div className="w-auto offset-md-9">
                                            <div className="chat-bubble chat-bubble--right">
                                                {message}
                                            </div>
                                        </div>
                                        : <div className="w-auto">
                                            <div className="chat-bubble chat-bubble--left">
                                                {message}
                                            </div>
                                        </div>
                                }
                            </div>
                        )
                    })}
                </div>
            </Dialog>
        </div>);
}

export default ChatRoom;