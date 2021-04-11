package api;


public class UrlCreate {
    private static final String ip = "";
    private static final int port = 3001;

    public static String getUrl(int chk, String info) {
        String url = String.format("http://%s:%d", ip, port);
        switch (chk) {
            case API_CHOICE.IDCHECK:
                url += "/auth/join/" + info;
                return url;

            case API_CHOICE.GROUP_SEARCH:
                url += "/group/"+info;
                return url;

             case API_CHOICE.GROUP_ID_CHECK:
                 url += "/group/idCheck/"+info;
                 return url;

            case API_CHOICE.GROUP_MEMBER:
                url+= "/group/member/" + info;
                return url;

            case API_CHOICE.LOGOUT:
                url += "/auth/logout/" + info;
                return url;
            default:
                return "";
        }
    }

    public static String postUrl(int chk){
        String url = String.format("http://%s:%d", ip, port);
        switch (chk){
            case API_CHOICE.SIGNUP:
                url += "/auth/join";
                return url;

             case API_CHOICE.LOGIN:
                 url += "/auth/login";
                 return url;

             case API_CHOICE.FIND_ID:
                 url += "/auth/findId";
                 return url;

             case API_CHOICE.FIND_PWD:
                 url += "/auth/findPw";
                 return url;

             case API_CHOICE.GROUP_ADD:
                 url += "/group";
                 return url;

             // url change?
             case API_CHOICE.LOCATION_REQ:
                 url += "/push/alarm";
                 return url;

            // url change?
              case API_CHOICE.LOCATION_SEND:
                  url += "/location";
                  return url;

            default:
                return "";
        }
    }
}

