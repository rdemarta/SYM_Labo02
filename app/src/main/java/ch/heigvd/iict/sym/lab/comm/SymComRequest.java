package ch.heigvd.iict.sym.lab.comm;

public class SymComRequest {

    private String url;
    private String message;
    private HTTPMethod httpMethod;
    private String contentType;
    private NetworkSpeed networkSpeed;

    public SymComRequest(String url, String message, HTTPMethod httpMethod, String contentType, NetworkSpeed networkSpeed) {
        this.url = url;
        this.message = message;
        this.httpMethod = httpMethod;
        this.contentType = contentType;

        if(networkSpeed != null) {
            this.networkSpeed = networkSpeed;
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HTTPMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HTTPMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public NetworkSpeed getNetworkSpeed() {
        return networkSpeed;
    }

    public void setNetworkSpeed(NetworkSpeed networkSpeed) {
        this.networkSpeed = networkSpeed;
    }
}
