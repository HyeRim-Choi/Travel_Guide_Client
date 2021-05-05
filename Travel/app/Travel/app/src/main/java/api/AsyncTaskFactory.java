package api;

import android.app.Activity;

import api.background.BackLocationRequest;
import api.callback.AsyncTaskCallBack;
import api.post.*;
import api.get.*;

public class AsyncTaskFactory{
    // Get AsyncTasck
    public static GetRequest getApiGetTask(Activity activity,int chk, String info, AsyncTaskCallBack callBack){
        switch(chk){
            case API_CHOICE.IDCHECK:
                return new GetIdCheck(activity, info, callBack);

            case API_CHOICE.GROUP_SEARCH:
                return new GetGroupSearch(activity, info, callBack);

             case API_CHOICE.GROUP_ID_CHECK:
                 return new GetGroupIdCheck(activity, info, callBack);

            case API_CHOICE.GROUP_MEMBER:
                return new GetGroupMember(activity, info, callBack);

            case API_CHOICE.LOGOUT:
                return new GetLogout(activity, info, callBack);

            case API_CHOICE.TRAVELER_GROUP_SEARCH:
                return new GetTravelerGroupSearch(activity, info, callBack);

            case API_CHOICE.MEMBER_LOCATION_RELOAD_SEND:
                return new GetMemberReloadLocation(activity, info, callBack);

            case API_CHOICE.MEMBER_LOCATION_SEND_DONE:
                return new GetMemberLocationSendDone(activity, info);

            case API_CHOICE.MANAGER_SHOW_PLACE:
                return new GetManagerShowPlace(activity, "", callBack);

            case API_CHOICE.MANAGER_ROUTE_PLACE_SEARCH:
                return new GetManagerPlaceRouteSearch(activity, info, callBack);

            case API_CHOICE.GROUP_TRIP_DATE:
                return new GetGroupTripDate(activity, info, callBack);

            default:
                return null;
        }
    }

    // Post AsyncTask
    public static PostRequest getApiPostTask(Activity activity,int chk,AsyncTaskCallBack callBack){
        switch (chk){
            case API_CHOICE.SIGNUP:
                return new PostSignUp(activity, callBack);

            case API_CHOICE.LOGIN:
                return new PostLogin(activity, callBack);

            case API_CHOICE.FIND_ID:
                return new PostFindId(activity, callBack);

             case API_CHOICE.FIND_PWD:
                 return new PostFindPwd(activity, callBack);

             case API_CHOICE.GROUP_ADD:
                 return new PostGroupAdd(activity, callBack);

            case API_CHOICE.MANAGER_ADD_PLACE:
                return new PostManagerPlaceAdd(activity, null);

             default:
                return null;

        }
    }



}
