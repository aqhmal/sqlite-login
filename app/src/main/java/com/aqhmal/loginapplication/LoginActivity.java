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

        // Adding click listener to log in button
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Calling EditText is empty or not
                CheckEditTextStatus();
                // Calling login method
                LoginFunction();
            }
        });

        // Addting click listener to register button
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Opening new user registration activity using intent on button click.
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // Login function starts from here.
    public void LoginFunction() {
        if(EditTextEmptyHolder) {
            // Opening SQLite Database write permission
            sqLiteDatabaseObj = sqLiteHelper.getWritableDatabase();
            // Adding search email query to cursor
            cursor = sqLiteDatabaseObj.query(SQLiteHelper.TABLE_NAME, null, " " + SQLiteHelper.NAME + "=?", new String[] {NameHolder}, null, null, null);
            while(cursor.moveToNext()) {
                if(cursor.isFirst()) {
                    cursor.moveToFirst();
                    // Storing Password associated with entered email
                    TempPassword = cursor.getString(cursor.getColumnIndex(SQLiteHelper.PASSWORD));
                    // Closing cursor
                    cursor.close();
                }
            }
            // Calling method to check final result
            CheckFinalResult();
        } else {
            // If any of login EditText empty then this block will be executed
            Toast.makeText(LoginActivity.this, "Please enter name and password", Toast.LENGTH_LONG).show();
        }
    }

    // Checking EditText is empty or not.
    public void CheckEditTextStatus() {
        // Getting value from all EditText and storing into String variables
        NameHolder = inputName.getText().toString();
        PasswordHolder = inputPassword.getText().toString();
        // Checking EditText is empty or not
        EditTextEmptyHolder = !TextUtils.isEmpty(NameHolder) && !TextUtils.isEmpty(PasswordHolder);
    }

    // Checking entered password from SQLite Database email associated password.
    public void CheckFinalResult() {
        if(TempPassword.equalsIgnoreCase(PasswordHolder)) {
            Toast.makeText(LoginActivity.this, "Successfully logged in.", Toast.LENGTH_LONG).show();
            // Going to Home Activity after login success message
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            // Sending Email to Home Activity Using Intent
            intent.putExtra(UserName, NameHolder);
            startActivity(intent);
        } else {
            Toast.makeText(LoginActivity.this, "Username or password is incorrect.", Toast.LENGTH_LONG).show();
        }
        TempPassword = "NOT_FOUND";
    }
}
