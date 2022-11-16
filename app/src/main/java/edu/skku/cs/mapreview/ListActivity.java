package edu.skku.cs.mapreview;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.naver.maps.map.overlay.Marker;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements MySimpleContract.ContractForView {
    private ListView listview;
    private ListViewAdapter listviewadapter;
    private ArrayList<Review> items;
    private ImageView btn_write;
    private MySimplePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restlistview);

        presenter = new MySimplePresenter(this, new MySimpleModel(presenter, ListActivity.this));

        listview = findViewById(R.id.restaurantlist);
        items = new ArrayList<Review>();
        items.add(new Review("POSITIVE 92%", "2022.8.21", "대학생에게 가성비 최고인 곳임... 진짜 여기 가고싶어서 금요일마다 오픈 시간에 기다림.. 사장님 이렇게 싸게 팔고 뭐가 남으세요??? 많이 팔고 행복하세요..", R.drawable.reviewphoto1, "+5"));
        items.add(new Review("POSITIVE 90%", "2022.8.22", "정말 맛있어요! 부모님이랑 왔는데 부모님께서도 또 오시고 싶다고 하실 정도로 분위기나 청결이 좋았어요. 제가 방문한 시간에는 사람이 좀 많긴 했어요.", R.drawable.reviewphoto2, "+2"));
        items.add(new Review("NEGATIVE 20%", "2022.8.23", "정말 맛없어요! 부모님이랑 왔는데 부모님께서도 또 오고싶지않다고 하실 정도로 분위기나 청결이 별루... 제가 방문한 시간에는 사람이 좀 적긴 했어요.", R.drawable.reviewphoto2, "+3"));
        listviewadapter = new ListViewAdapter(items, getApplicationContext());

        listview.setAdapter(listviewadapter);

        btn_write = findViewById(R.id.imagewrite);
        btn_write.setOnClickListener(view -> {
            presenter.onWriteClicked();
        });
    }

    @Override
    public void setMarker(Marker marker, double lat, double lon, int resourceID) {

    }

    @Override
    public void onLoginResult(Boolean result) {

    }

    @Override
    public void displayValue(int value) {

    }
}
