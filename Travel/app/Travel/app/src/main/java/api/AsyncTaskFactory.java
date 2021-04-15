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

                 // 가이드가 멤버들에게 알림을 띄워서 위치 요청하기
             case API_CHOICE.LOCATION_REQ:
                 return new PostLocationReq(activity, callBack);

            case API_CHOICE.MANAGER_ADD_PLACE:
                return new PostManagerPlaceAdd(activity, null);

             default:
                return null;

        }
    }


    // BackGround AsyncTask
    public static BackLocationRequest getApiBackTask(Activity activity,int chk, String info, int serviceChk, AsyncTaskCallBack callBack){
        switch (chk){
            // 백그라운드로 여행객들이 가이드에게 위치 보내기
            case API_CHOICE.LOCATION_SEND:
                return new BackLocationRequest(activity, info, serviceChk, callBack);

            default:
                return null;
        }
    }


}
