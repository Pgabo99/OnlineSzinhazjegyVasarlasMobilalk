package com.example.onlineszinhazjegyvasarlas;

import static android.content.ContentValues.TAG;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.onlineszinhazjegyvasarlas.Model.Booking;
import com.example.onlineszinhazjegyvasarlas.Service.BookingService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BookingActivity extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    Bundle extras;
    String id;
    FirebaseAuth auth;
    FirebaseUser user;
    TextView textView;
    TextInputEditText editTextName, editTextSeats, editPhoneNumber;
    Spinner spinnerPlayName;
    Spinner spinnerDate;
    Button btn;
    BookingService bookingService;

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
        } else if (id == R.id.profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        spinnerPlayName = findViewById(R.id.playname);
        editTextName = findViewById(R.id.name);
        editPhoneNumber = findViewById(R.id.phonenumber);
        editTextSeats = findViewById(R.id.seats);
        btn = findViewById(R.id.btn_book);
        textView=findViewById(R.id.fogalt);

        extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString("id");
            db.collection("Bookings").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        editTextName.setText(task.getResult().getData().get("name").toString());
                        editPhoneNumber.setText(task.getResult().getData().get("phoneNumber").toString());
                        editTextSeats.setText(task.getResult().getData().get("seats").toString());
                        int selected=0;
                        switch (task.getResult().getData().get("playName").toString()) {
                            case "A Pál utcai fiúk":
                               selected=0;
                                break;
                            case "Diótörő":
                                selected=1;
                                break;
                            case "RÓMEÓ ÉS JÚLIA":
                                selected=2;
                                break;
                            case "Macskafogó":
                                selected=3;
                                break;
                            case "Nyakamon a nászmenet":
                                selected=4;
                                break;
                            case "A Játékkészítő":
                                selected=5;
                                break;
                            case "Kabaré":
                                selected=7;
                                break;
                            case "W. Shakespeare: Vízkereszt, vagy amit akartok":
                                selected=8;
                                break;
                            case "Pretty Woman":
                                selected=6;
                                break;
                            default:
                                selected=9;
                                break;
                        }
                        spinnerPlayName.setSelection(selected);
                        btn.setText("Módosítás(Dátumot válassz!)");
                        textView.setText("Módosítás (Dátumot válassz!)");
                    }
                }
            });
        }


        bookingService = new BookingService();
        spinnerPlayName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerDate = findViewById(R.id.date);
                String item = parent.getItemAtPosition(position).toString();
                String[] lista2 = getResources().getStringArray(R.array.play_names);
                switch (item) {
                    case "A Pál utcai fiúk":
                        lista2 = getResources().getStringArray(R.array.palutcai);
                        break;
                    case "Diótörő":
                        lista2 = getResources().getStringArray(R.array.diotoro);
                        break;
                    case "RÓMEÓ ÉS JÚLIA":
                        lista2 = getResources().getStringArray(R.array.romeoesjulai);
                        break;
                    case "Macskafogó":
                        lista2 = getResources().getStringArray(R.array.macskafogo);
                        break;
                    case "Nyakamon a nászmenet":
                        lista2 = getResources().getStringArray(R.array.nyakamon);
                        break;
                    case "A Játékkészítő":
                        lista2 = getResources().getStringArray(R.array.jatekkeszito);
                        break;
                    case "Kabaré":
                        lista2 = getResources().getStringArray(R.array.kabare);
                        break;
                    case "W. Shakespeare: Vízkereszt, vagy amit akartok":
                        lista2 = getResources().getStringArray(R.array.vizkereszt);
                        break;
                    case "Pretty Woman":
                        lista2 = getResources().getStringArray(R.array.prettywoman);
                        break;
                    default:
                        lista2 = getResources().getStringArray(R.array.madeinhungaria);
                        break;
                }

                ArrayList<String> arrayList2 = new ArrayList<>(Arrays.asList(lista2));
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList2);
                adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                spinnerDate.setAdapter(adapter);

                if (user == null) {
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String[] lista = getResources().getStringArray(R.array.play_names);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(lista));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinnerPlayName.setAdapter(adapter);

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(BookingActivity.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(BookingActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TextView textView = (TextView) spinnerPlayName.getSelectedView();
                String result = textView.getText().toString();

                TextView textView2 = (TextView) spinnerDate.getSelectedView();
                String result2 = textView2.getText().toString();
                Booking booking = new Booking(UUID.randomUUID().toString(), String.valueOf(editTextName.getText()), user.getUid().toString(), String.valueOf(editPhoneNumber.getText()), Integer.valueOf(String.valueOf(editTextSeats.getText())), result, result2);

                if (id != null) {
                    booking._setId(id);
                    makeNotificationUpdate();
                    bookingService.updateById(booking);
                    Intent intent = new Intent(getApplicationContext(), PlayList.class);
                    startActivity(intent);
                    finish();

                } else {
                    bookingService.addBooking(booking);
                    makeNotification();
                    Intent intent = new Intent(getApplicationContext(), PlayList.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    public void makeNotification() {
        String chanelId = "CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), chanelId);
        builder.setSmallIcon(R.drawable.notification);
        builder.setContentTitle("Sikeres foglalás!");
        builder.setContentText("Foglalás megtekintéséhez kattints a Profil menüpontra");
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
        notificationManager.notify(0, builder.build());
    }
    public void makeNotificationUpdate() {
        String chanelId = "CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), chanelId);
        builder.setSmallIcon(R.drawable.notification);
        builder.setContentTitle("Sikeres módosítás!");
        builder.setContentText("Foglalásaid megtekintéséhez kattints a Profil menüpontra");
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
        notificationManager.notify(0, builder.build());
    }
}