package agrixilla.in.webservices;

public class RestClient {
    public static String ROOT_URL = "http://www.agrixilla.in/api/v1/";
    private static API REST_CLIENT;

    private RestClient() {
    }

    public static API get() {
        return REST_CLIENT;
    }

}
