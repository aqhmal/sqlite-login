package com.aqhmal.loginapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText inputName, inputPassword;
    Button loginBtn, registerBtn;
    String NameHolder, PasswordHolder;
    Boolean EditTextEmptyHolder;
    SQLiteDatabase sqLiteDatabaseObj;
    SQLiteHelper sqLiteHelper;
    Cursor cursor;
    String TempPassword = "NOT FOUND";
    public static final String UserName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inputName = findViewById(R.id.namaInput);
        inputPassword = findViewById(R.id.passwordInput);
        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);
        sqLiteHelper = new SQLiteHelper(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEditTextStatus();
                LoginFunction();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void LoginFunction() {
        if(EditTextEmptyHolder) {
            sqLiteDatabaseObj = sqLiteHelper.getWritableDatabase();
            cursor = sqLiteDatabaseObj.query(SQLiteHelper.TABLE_NAME, null, " " +
                    SQLiteHelper.NAME + "=?", new String[] {NameHolder}, null, null,
                    null);
            while(cursor.moveToNext()) {
                if(cursor.isFirst()) {
                    cursor.moveToFirst();
                    TempPassword = cursor.getString(cursor.getColumnIndex(SQLiteHelper.PASSWORD));
                    cursor.close();
                }
            }
            CheckFinalResult();
        } else {
            Toast.makeText(LoginActivity.this, "Please enter name and password",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void CheckEditTextStatus() {
        NameHolder = inputName.getText().toString();
        PasswordHolder = inputPassword.getText().toString();
        EditTextEmptyHolder = !TextUtils.isEmpty(NameHolder) && !TextUtils.isEmpty(PasswordHolder);
    }

    public void CheckFinalResult() {
        if(TempPassword.equalsIgnoreCase(PasswordHolder)) {
            Toast.makeText(LoginActivity.this, "Successfully logged in.",
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            intent.putExtra(UserName, NameHolder);
            startActivity(intent);
        } else {
            Toast.makeText(LoginActivity.this, "Username or password is incorrect.",
                    Toast.LENGTH_LONG).show();
        }
        TempPassword = "NOT_FOUND";
    }
}

