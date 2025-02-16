package com.example.tp1.contact_form;
import com.example.tp1.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CallActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_contact_page);

        String phone_number = getIntent().getStringExtra("phone_number");

        TextView tvTelephone = findViewById(R.id.tvTelephone);
        tvTelephone.setText(phone_number);

        ImageView imgPhone = findViewById(R.id.imgPhone);
        imgPhone.setImageResource(R.drawable.ic_phone);

        Button btn_call = findViewById(R.id.btn_call);
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone_number));
                startActivity(intent);
            }
        });
    }



}
