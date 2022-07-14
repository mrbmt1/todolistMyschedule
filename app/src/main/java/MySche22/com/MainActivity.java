package MySche22.com;

import androidx.appcompat.app.AppCompatActivity;


import android.accounts.Account;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import MySche22.com.Database.Database;


public class MainActivity extends AppCompatActivity {
    Button btnRegister, btnLogin;
    EditText edtAccount, edtPassword;
    CheckBox checkBoxRemember;
    Database db;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private static final  int TIME_INTERVAL = 2000;
    private long backPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRegister = (Button) findViewById(R.id.buttonSignup);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        edtAccount = (EditText) findViewById(R.id.editTextAccount);
        edtPassword = (EditText) findViewById(R.id.editTextPassword);
        btnLogin = (Button) findViewById(R.id.buttonLogin);
        checkBoxRemember = (CheckBox) findViewById(R.id.checkBoxRemember);

        sharedPreferences = getSharedPreferences("AccountPrefs", MODE_PRIVATE);
        String account = sharedPreferences.getString("account","");
        if (!account.isEmpty()){
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.putExtra("account", account);
            startActivity(intent);
            finish();

        }

        db=new Database(this);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String account = edtAccount.getText().toString();
                String password = edtPassword.getText().toString();

                if (account.equals("") || password.equals("")){
                    Toast.makeText(MainActivity.this,
                            "Không được để trống tài khoản và mật khẩu",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Boolean result = db.checkUserNamePassword(account, password);
                    if(result) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        if(checkBoxRemember.isChecked()){
                            editor.putString("account", account);
                            editor.putString("password", password);
                            SharedPreferences sharedPref1 = getSharedPreferences("account", MODE_PRIVATE);
                            SharedPreferences.Editor editor1 = sharedPref1.edit();
                            editor1.putString("account", account);
                            editor1.apply();
                        } else {
                            editor.clear();
                        }
                        editor.apply();
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            intent.putExtra("account", account);
                        SharedPreferences sharedPref1 = getSharedPreferences("account", MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = sharedPref1.edit();
                        editor1.putString("account", account);
                        editor1.apply();
                            startActivity(intent);
                            finish();
                        } else {
                        Boolean userCheckResult = db.checkUserName(account);
                        if(userCheckResult == true){
                            Toast.makeText(MainActivity.this,
                                    "Sai mật khẩu!", Toast.LENGTH_SHORT).show();
                            edtPassword.setError("Sai mật khẩu!");
                        } else{
                        Toast.makeText(MainActivity.this,
                            "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                            edtAccount.setError("Tài khoản không tồn tại!");
                        }
                    }
                }
            }
        });

    }
    @Override
    public void onBackPressed(){
        if(backPressed + TIME_INTERVAL > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        } else{
            Toast.makeText(getBaseContext(),"Nhấn lần nữa để thoát ứng dụng", Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();
    }


}