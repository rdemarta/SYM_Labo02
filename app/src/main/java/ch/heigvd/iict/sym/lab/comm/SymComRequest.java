package ch.heigvd.iict.sym.lab.comm;

public class SymComRequest {

    private String url;
    private String message;
    private HTTPMethod httpMethod;
    private String contentType;
    private boolean compress;
    private NetworkSpeed networkSpeed;

    public SymComRequest(String url, String message, HTTPMethod httpMethod, String contentType, boolean compress, NetworkSpeed networkSpeed) {
        this.url = url;
        this.message = message;
        this.httpMethod = httpMethod;
        this.contentType = contentType;
        this.compress = compress;

        if(networkSpeed != null) {
            this.networkSpeed = networkSpeed;
        }
    }

    public String getUrl() {
        return url;
    }

    public String getMessage() {
        return message;
    }

    public HTTPMethod getHttpMethod() {
        return httpMethod;
    }

    public String getContentType() {
        return contentType;
    }

    public boolean getCompress() {
        return compress;
    }

    public NetworkSpeed getNetworkSpeed() {
        return networkSpeed;
    }

}
