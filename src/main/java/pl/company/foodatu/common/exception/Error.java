package pl.company.foodatu.common.exception;

public class Error {
    private String message;
    private String code;
    private String details;
    private String path;
    private String userMessage;

    public Error() {
    }

    public Error(String message, String code, String details, String path, String userMessage) {
        this.message = message;
        this.code = code;
        this.details = details;
        this.path = path;
        this.userMessage = userMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }
}
