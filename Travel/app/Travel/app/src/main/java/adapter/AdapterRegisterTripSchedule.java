package adapter;


import android.content.Context;
;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.chr.travel.R;


import java.util.ArrayList;

/* 여행 일정 등록 Activity의 Adapter */

public class AdapterRegisterTripSchedule extends BaseAdapter {
    private Context mContext;
    private int mResource;
    private ArrayList<RegisterTripSchedule> mItems = new ArrayList<RegisterTripSchedule>();

    public AdapterRegisterTripSchedule(Context mContext, int mResource, ArrayList<RegisterTripSchedule> mItems) {
        this.mContext = mContext;
        this.mResource = mResource;
        this.mItems = mItems;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) { // 해당 항목 뷰가 이전에 생성된 적이 없는 경우
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 항목 뷰를 정의한 xml 리소스(여기서는 mResource 값)으로부터 항목 뷰 객체를 메모리로 로드
            convertView = inflater.inflate(mResource, parent,false);
        }

        // txt_date
        TextView txt_date = (TextView) convertView.findViewById(R.id.txt_day);
        txt_date.setText(mItems.get(position).getDay());

        // 일정 등록 버튼 클릭 시
       Button btn_registerPlace = convertView.findViewById(R.id.btn_registerSchedule);
       btn_registerPlace.setText(mItems.get(position).getBtn());
        btn_registerPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mContext instanceof OnButtonSelectedListener){
                    ((OnButtonSelectedListener)mContext).onButtonSelected(position);
                }

            }
        });

        // txt_schedule
        TextView txt_tripSchedule = (TextView) convertView.findViewById(R.id.txt_tripSchedule);
        txt_tripSchedule.setText(mItems.get(position).getSchedule());

        return convertView;
    }

    // 관광지 등록 버튼 인터페이스 정의(관광지 검색 액티비티로 이동하기 위해)
    public interface OnButtonSelectedListener {
        public void onButtonSelected(int i);
    }
}
