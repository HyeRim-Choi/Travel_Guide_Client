package adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chr.travel.R;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;

public class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {

    private final View mCalloutBalloon;

    TextView txt_route, txt_subPlace;

    public CustomCalloutBalloonAdapter(LayoutInflater layoutInflater) {
        mCalloutBalloon = layoutInflater.inflate(R.layout.adapter_custom_callout_balloon, null);

        txt_route = mCalloutBalloon.findViewById(R.id.txt_route);
        txt_subPlace = mCalloutBalloon.findViewById(R.id.txt_subPlace);
    }


    // 말풍선 띄우기
    @Override
    public View getCalloutBalloon(MapPOIItem mapPOIItem) {
        // 서브 관광지 마커이면
        if(mapPOIItem.getTag() == 1){
            String rank = mapPOIItem.getItemName().substring(0,1);

            if(rank.equals("\n")){
                txt_route.setText("");
                txt_subPlace.setText(mapPOIItem.getItemName());
            }

            else{
                txt_route.setText(rank);
                txt_subPlace.setText(mapPOIItem.getItemName().substring(2));
            }


        }

        // 큰 관광지 마커이면
        else{
            txt_route.setText("");
            txt_subPlace.setText(mapPOIItem.getItemName());
        }

        return mCalloutBalloon;
    }

    // 말풍선 클릭 시
    @Override
    public View getPressedCalloutBalloon(MapPOIItem mapPOIItem) {
        return mCalloutBalloon;
    }
}
