package com.example.onlineszinhazjegyvasarlas;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineszinhazjegyvasarlas.Model.PlayItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class PlayList extends AppCompatActivity {
    private static final String LOG_TAG=PlayList.class.getName();
    FirebaseAuth auth;
    FirebaseUser user;

    private RecyclerView recyclerView;
    private ArrayList<PlayItem> playList;
    private PlayItemAdapter adapter;
    private int gridNumber=1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.theater_list_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.profile){
            Intent intent=new Intent(this, ProfileActivity.class);
            startActivity(intent);
            return true;}
        else if(id==R.id.main){
            Intent intent=new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }else if(id==R.id.logout){
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
            return true;
        }else if(id==R.id.book){
            Intent intent=new Intent(this, BookingActivity.class);
            startActivity(intent);
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_play_list);

        androidx.appcompat.widget.Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        if (user==null){
            Log.d(LOG_TAG, "Nem vagy jelentkezve!");
            Intent intent=new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }else{
            Log.d(LOG_TAG, "Be vagy jelentkezve!");
        }
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,gridNumber));
        playList=new ArrayList<>();
        adapter=new PlayItemAdapter(this,playList);
        recyclerView.setAdapter(adapter);
       initializeData();

    }
    private void initializeData(){
        String[] itemsList=getResources().getStringArray(R.array.play_names);
        String[] itemsInfo=getResources().getStringArray(R.array.play_desc);
        String[] itemsPrice=getResources().getStringArray(R.array.play_price);
        TypedArray itemsImageResource=getResources().obtainTypedArray(R.array.play_images);
        TypedArray itemsRate=getResources().obtainTypedArray(R.array.play_rates);

        playList.clear();
        for(int i=0;i<itemsList.length;i++){
            playList.add(new PlayItem(itemsList[i],itemsInfo[i],itemsPrice[i],itemsRate.getFloat(i,0),itemsImageResource.getResourceId(i,0)));
        }

        itemsImageResource.recycle();
        adapter.notifyDataSetChanged();
    }
}