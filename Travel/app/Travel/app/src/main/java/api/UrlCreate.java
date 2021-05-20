package api;


public class UrlCreate {
    private static final String ip = "";
    private static final int port = 3001;

    public static String getUrl(int chk, String info) {
        String url = String.format("http://%s", ip);
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

            case API_CHOICE.TRAVELER_GROUP_SEARCH:
                url += "/group/myGroup/" + info;
                return url;

            case API_CHOICE.MEMBER_LOCATION_RELOAD_SEND:
                url += "/location/reload/" + info;
                return url;

            case API_CHOICE.MEMBER_LOCATION_SEND_DONE:
            case API_CHOICE.MANAGER_ROUTE_PLACE_SEARCH:
                url += "/location/" + info;
                return url;

            case API_CHOICE.MANAGER_SHOW_PLACE:
                url += "/map/place";
                return url;

            case API_CHOICE.GROUP_TRIP_DATE:
                url += "/group/route/" + info;
                return url;

            case API_CHOICE.MANAGER_REGISTERED_ROUTE_TITLE:
                url += "/route/title";
                return url;

            case API_CHOICE.REGISTERED_ROUTE_DETAILS:
                url += "/route/" + info;
                return url;

            case API_CHOICE.GROUP_REGISTERED_ROUTE_DETAILS:
                url += "/group/schedule/" + info;
                return url;

            default:
                return "";
        }
    }

    public static String postUrl(int chk){
        String url = String.format("http://%s", ip);
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

             case API_CHOICE.LOCATION_REQ:
                 url += "/push/alarm";
                 return url;

              case API_CHOICE.LOCATION_SEND:
                  url += "/location";
                  return url;

            case API_CHOICE.MANAGER_ADD_PLACE:
                url += "/map/addPlace";
                return url;

            case API_CHOICE.MANAGER_REGISTER_ROUTE:
                url += "/route";
                return url;

            case API_CHOICE.VISUALIZATION:
                url += "/map";
                return url;

            default:
                return "";
        }
    }
}

