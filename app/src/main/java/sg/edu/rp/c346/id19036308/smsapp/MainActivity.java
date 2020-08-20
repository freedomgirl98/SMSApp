package sg.edu.rp.c346.id19036308.smsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnSend, btnSendMsg;
    TextView tvTo, tvContent;
    EditText etTo, etContent;
    private BroadcastReceiver br;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvContent = findViewById(R.id.textViewContent);
        tvTo = findViewById(R.id.textViewTo);
        etContent = findViewById(R.id.editTextContent);
        etTo = findViewById(R.id.editTextTo);
        btnSend = findViewById(R.id.buttonSend);
        btnSendMsg = findViewById(R.id.buttonMsg);

        br = new MessageReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction("com.example.broadcast.MY_BROADCAST");
        this.registerReceiver(br,filter);


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkPermission();


                SmsManager smsManager = SmsManager.getDefault();

                smsManager.sendTextMessage(etTo.getText().toString(),null, etContent.getText().toString(), null, null);

                Toast toast = Toast.makeText(MainActivity.this, "Message sent", Toast.LENGTH_LONG);
                toast.show();
                etContent.setText("");
                etTo.setText("");
            }
        });

        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                etContent.setText("You are welcome.");
                Uri smsUri = Uri.parse("tel:" + etTo.getText().toString());
                Intent intent = new Intent(Intent.ACTION_VIEW, smsUri);
                intent.putExtra("address", etTo.getText().toString());
                intent.putExtra("sms_body", etContent.getText().toString());
                intent.setType("vnd.android-dir/mms-sms");//here setType will set the previous data null.
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

    }

    private void checkPermission() {
        int permissionSendSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int permissionRecvSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS);
        if (permissionSendSMS != PackageManager.PERMISSION_GRANTED &&
                permissionRecvSMS != PackageManager.PERMISSION_GRANTED) {
            String[] permissionNeeded = new String[]{Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS};
            ActivityCompat.requestPermissions(this, permissionNeeded, 1);
        }
    }

}
