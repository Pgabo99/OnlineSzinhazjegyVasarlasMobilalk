package com.example.onlineszinhazjegyvasarlas;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineszinhazjegyvasarlas.Model.Booking;
import com.example.onlineszinhazjegyvasarlas.Service.BookingService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Arrays;

public class ProfileActivity extends AppCompatActivity {


    FirebaseAuth auth;
    FirebaseUser user;
    BookingService bookingService;
    ArrayList<Booking> bookings;
    Spinner spinnerBooks;

    int selected;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView bookedName, bookedDate, bookedPhoneNumber,bookedPlayName,bookedSeats;
    Button btn_delte,btn_update;
    ArrayList<String> nevek;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.theater_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.main) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.onscreen) {
            Intent intent = new Intent(this, PlayList.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
            return true;
        } else if (id == R.id.book) {
            Intent intent = new Intent(this, BookingActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(ProfileActivity.this, android.Manifest.permission.POST_NOTIFICATIONS)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(ProfileActivity.this,new String[]{Manifest.permission.POST_NOTIFICATIONS},101);
            }
        }

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        nevek=new ArrayList<>();
        bookings=new ArrayList<>();
        bookingService=new BookingService();

        bookedName=findViewById(R.id.bookedName);
        bookedPhoneNumber=findViewById(R.id.bookedPhoneNumber);
        bookedPlayName=findViewById(R.id.bookedplayName);
        bookedDate=findViewById(R.id.bookedplayDate);
        bookedSeats=findViewById(R.id.bookedSeats);
        btn_delte=findViewById(R.id.torol);
        btn_update=findViewById(R.id.update);

        spinnerBooks=findViewById(R.id.bookedplays);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nevek);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinnerBooks.setAdapter(adapter);

        spinnerBooks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bookedName.setText("Név: "+bookings.get(position).getName());
                bookedPhoneNumber.setText("Telefonszám: "+bookings.get(position).getPhoneNumber());
                bookedPlayName.setText("Előadás neve: "+bookings.get(position).getPlayName());
                bookedDate.setText("Dátum: "+bookings.get(position).getDate());
                bookedSeats.setText("Jegy: "+bookings.get(position)._getSeatsInString()+" db");
                selected=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        db.collection("Bookings").whereEqualTo("loggedInID", user.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int szamlalo=1;
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Booking seged = new Booking();
                        seged.setDate(doc.get("date").toString());
                        seged.setloggedInID(doc.get("loggedInID").toString());
                        seged.setName(doc.get("name").toString());
                        seged.setSeats(Integer.valueOf(doc.get("seats").toString()));
                        seged.setPhoneNumber(doc.get("phoneNumber").toString());
                        seged.setPlayName(doc.get("playName").toString());
                        seged._setId(doc.getId());
                        bookings.add(seged);
                        nevek.add(szamlalo+". "+doc.get("playName").toString()+": "+doc.get("date").toString());
                        szamlalo++;
                    }
                    if (bookings.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Még nincsenek foglalásaid, először foglalj!",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), BookingActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });


        btn_delte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bookings.get(selected)!=null){
                    bookingService.deleteById(bookings.get(selected));
                    makeNotificationDelete();
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookings.get(selected)!=null) {
                    Intent intent=new Intent(getApplicationContext(),BookingActivity.class);
                    intent.putExtra("id",bookings.get(selected).getId());
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public void makeNotificationDelete() {
        String chanelId = "CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), chanelId);
        builder.setSmallIcon(R.drawable.notification);
        builder.setContentTitle("Sikeres törlés!");
        builder.setContentText("További foglalásaid megtekintéséhez kattints a Profil menüpontra");
        builder.setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(chanelId);
            if (notificationChannel == null) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(chanelId, "Leírás", importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        notificationManager.notify(0,builder.build());
    }


}