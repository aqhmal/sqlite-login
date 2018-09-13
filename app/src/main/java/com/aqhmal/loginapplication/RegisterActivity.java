package com.aqhmal.loginapplication;

import android.content.Context;
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


public class RegisterActivity extends AppCompatActivity {

    EditText input_id, input_email, input_name, input_password;
    Button btnRegister, btnLogin;
    String IdHolder, NameHolder, EmailHolder, PasswordHolder;
    Boolean EditTextEmptyHolder;
    SQLiteDatabase sqLiteDatabaseObj;
    String SQLiteDataBaseQueryHolder;
    SQLiteHelper sqLiteHelper;
    Cursor cursor;
    String F_Result = "Not Found";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        input_id = findViewById(R.id.idInput);
        input_email = findViewById(R.id.emailInput);
        input_name = findViewById(R.id.namaInput);
        input_password = findViewById(R.id.passwordInput);
        btnRegister = findViewById(R.id.registerBtn);
        btnLogin = findViewById(R.id.loginBtn);
        sqLiteHelper = new SQLiteHelper(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDataBaseBuild();
                SQLiteTableBuild();
                CheckEditTextStatus();
                CheckingIdAlreadyExistsOrNot();
                EmptyEditTextAfterDataInsert();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,
                        DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void SQLiteDataBaseBuild() {
        sqLiteDatabaseObj = openOrCreateDatabase(SQLiteHelper.DATABASE_NAME, Context.MODE_PRIVATE,
                null);
    }

    public void SQLiteTableBuild() {
        sqLiteDatabaseObj.execSQL("CREATE TABLE IF NOT EXISTS " + SQLiteHelper.TABLE_NAME +
                "(" +
                SQLiteHelper.ID + " INTEGER PRIMARY KEY NOT NULL," +
                SQLiteHelper.NAME + " VARCHAR," +
                SQLiteHelper.EMAIL + " VARCHAR," +
                SQLiteHelper.PASSWORD + " VARCHAR);");
    }

    public void InsertDataIntoSQLiteDatabase() {
        if(EditTextEmptyHolder) {
            SQLiteDataBaseQueryHolder = "INSERT INTO " + SQLiteHelper.TABLE_NAME + " (id, name, " +
                    "email, password) " +
                    "VALUES(" + Integer.parseInt(IdHolder) +", '" + NameHolder + "', '" +
                    EmailHolder + "', '" + PasswordHolder +"');";
            sqLiteDatabaseObj.execSQL(SQLiteDataBaseQueryHolder);
            sqLiteDatabaseObj.close();
            Toast.makeText(RegisterActivity.this, "User Registered Successfully",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(RegisterActivity.this, "Please Fill All The Required Fields",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void EmptyEditTextAfterDataInsert() {
        input_id.getText().clear();
        input_name.getText().clear();
        input_email.getText().clear();
        input_password.getText().clear();
    }

    public void CheckEditTextStatus() {
        IdHolder = input_id.getText().toString();
        NameHolder = input_name.getText().toString();
        EmailHolder = input_email.getText().toString();
        PasswordHolder = input_password.getText().toString();
        EditTextEmptyHolder = !TextUtils.isEmpty(IdHolder) && !TextUtils.isEmpty(NameHolder) &&
                !TextUtils.isEmpty(EmailHolder) && !TextUtils.isEmpty(PasswordHolder);
    }

    public void CheckingIdAlreadyExistsOrNot() {
        sqLiteDatabaseObj = sqLiteHelper.getWritableDatabase();
        cursor = sqLiteDatabaseObj.query(SQLiteHelper.TABLE_NAME, null, " " +
                SQLiteHelper.ID + "=?", new String[] {IdHolder}, null, null,
                null);
        while(cursor.moveToNext()) {
            if(cursor.isFirst()) {
                cursor.moveToFirst();
                F_Result = "ID Found";
                cursor.close();
            }
        }
        CheckFinalResult();
    }

    // Checking Result
    public void CheckFinalResult() {
        if(F_Result.equalsIgnoreCase("ID Found")) {
            Toast.makeText(RegisterActivity.this, "ID Already Exists",
                    Toast.LENGTH_LONG).show();
        } else {
            InsertDataIntoSQLiteDatabase();
        }
        F_Result = "Not Found";
    }
}

