package com.example.tp1.contact_form;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tp1.MainActivity;
import com.example.tp1.R;

import androidx.appcompat.app.AppCompatActivity;

public class ContactApp extends AppCompatActivity {

    private EditText name_, surname_, birthdate_, skill_, phone_number_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact_page);

        name_ = findViewById(R.id.name);
        surname_ = findViewById(R.id.surname);
        birthdate_ = findViewById(R.id.etAge);
        skill_ = findViewById(R.id.etDomaine);
        phone_number_ = findViewById(R.id.etTelephone);

        Button btn_validate = findViewById(R.id.btn_validate);
        btn_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayConfirmationMessage();
            }
        });
    }

    private void displayConfirmationMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_title));
        builder.setMessage(getString(R.string.dialog_message));

        builder.setPositiveButton(getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getFormValue();
            }
        });

        builder.setNegativeButton(getString(R.string.btn_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private void getFormValue() {
        String name_info = name_.getText().toString();
        String surname_info = surname_.getText().toString();
        String birthdate_info = birthdate_.getText().toString();
        String skill_info = skill_.getText().toString();
        String phoneNumber = phone_number_.getText().toString();

        if (name_info.isEmpty() || surname_info.isEmpty() || birthdate_info.isEmpty() || skill_info.isEmpty() || phoneNumber.isEmpty()) {
            Toast.makeText(ContactApp.this, getString(R.string.dialog_positive), Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(ContactApp.this, DisplayActivity.class);
        intent.putExtra("name", name_info);
        intent.putExtra("surname", surname_info);
        intent.putExtra("birthdate", birthdate_info);
        intent.putExtra("skill", skill_info);
        intent.putExtra("phone_number", phoneNumber);
        startActivity(intent);
    }

    public void go_back_home(View view){
        Intent intention= new Intent(ContactApp.this, MainActivity.class);
        startActivity(intention);
    }
}
