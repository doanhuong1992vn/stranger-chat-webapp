export const getServerErrorMessages = (data) => {
    const errorData = data.error;
    if (typeof errorData === "string") {
        return [errorData];
    } else {
        let messages = [];
        errorData?.forEach(error => {
            if (error.message) {
                messages.push(error.message);
            }
        });
        return messages;
    }
}