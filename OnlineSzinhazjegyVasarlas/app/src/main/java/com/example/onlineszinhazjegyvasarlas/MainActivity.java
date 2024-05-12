package com.example.onlineszinhazjegyvasarlas;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    TextView textView;
    FirebaseUser user;
    TableLayout tableLayout;

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
        else if(id==R.id.onscreen){
            Intent intent=new Intent(this, PlayList.class);
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
        /*EdgeToEdge.enable(this);*/

        setContentView(R.layout.activity_main);
        androidx.appcompat.widget.Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        auth=FirebaseAuth.getInstance();
        textView=findViewById(R.id.user_details);
        user=auth.getCurrentUser();

        if (user==null){
            Intent intent=new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        Animation animation;
        animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fadein);
        tableLayout=findViewById(R.id.TableLayout01);
        tableLayout.startAnimation(animation);

    }
}