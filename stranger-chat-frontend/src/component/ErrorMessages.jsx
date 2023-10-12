import {BsInfoCircle} from "react-icons/bs";

function ErrorMessages({messages}) {
    return (
        <div className={"flex justify-content-center flex-column"}>
            {messages?.map((message, index) =>
                <div key={index} className={"text-danger flex align-items-center"}>
                    <BsInfoCircle className={"me-1 mb-1"}/>
                    <span>{message}</span>
                </div>
            )}
        </div>
    );
}

export default ErrorMessages;