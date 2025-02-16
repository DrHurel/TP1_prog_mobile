package com.example.tp1.contact_form;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.tp1.R;

import androidx.appcompat.app.AppCompatActivity;

public class DisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmation_page);

        String name = getIntent().getStringExtra("name");
        String surname = getIntent().getStringExtra("surname");
        String birthdate = getIntent().getStringExtra("birthdate");
        String skill = getIntent().getStringExtra("skill");
        String phone_number = getIntent().getStringExtra("phone_number");

        TextView tvNom = findViewById(R.id.recap_name);
        TextView tvPrenom = findViewById(R.id.recap_surname);
        TextView tvAge = findViewById(R.id.recap_birthdate);
        TextView tvDomaine = findViewById(R.id.recap_skill);
        TextView tvTelephone = findViewById(R.id.tvTelephone);

        tvNom.setText(getString(R.string.name) + " " + name);
        tvPrenom.setText(getString(R.string.surname) + " " + surname);
        tvAge.setText(getString(R.string.birthdate) + " " + birthdate);
        tvDomaine.setText(getString(R.string.skill) + " " + skill);
        tvTelephone.setText(getString(R.string.phone_number) + " " + phone_number);


        Button btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayActivity.this, CallActivity.class);
                intent.putExtra("phone_number", phone_number);
                startActivity(intent);
            }
        });

        Button btnRetour = findViewById(R.id.btnRetour);
        btnRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
