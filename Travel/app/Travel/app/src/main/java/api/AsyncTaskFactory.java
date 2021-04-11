package api;

import android.app.Activity;
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

                 // change?
             case API_CHOICE.LOCATION_REQ:
                 return new PostLocationReq(activity, callBack);

                 // change??
            case API_CHOICE.LOCATION_SEND:
                return new PostLocationSend(activity, callBack);


            default:
                return null;
        }
    }


}
