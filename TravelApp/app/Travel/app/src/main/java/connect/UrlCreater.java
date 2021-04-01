package connect;

public class UrlCreater {
    private static String ip = "15.164.218.65";
    private static int port = 3001;
    public static String getUrl(){
        String url = String.format("http://%s:%d", ip, port);

        return url;
    }
}
