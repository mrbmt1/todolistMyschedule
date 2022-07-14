package MySche22.com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import MySche22.com.Database.Database;

import java.util.regex.Pattern;
import android.util.Patterns;




public class RegisterActivity extends AppCompatActivity {



    Database db;
    Button btnRegister;
    EditText edtUserAccount, edtPasswordRegister, edtUserFirstName, edtUserLastName, edtUserBirth,
            edtUserEmail, edtUserPhoneNumber, edtUserRepassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        btnRegister = (Button) findViewById(R.id.buttonRegister);

        edtUserAccount = (EditText) findViewById(R.id.editTextAccountRegister);
        edtPasswordRegister = (EditText) findViewById(R.id.editTextPasswordRegister);
        edtUserFirstName= (EditText) findViewById(R.id.editTextUserFirstName);
        edtUserLastName = (EditText) findViewById(R.id.editTextUserLastName);
        edtUserRepassword = (EditText) findViewById(R.id.editTextUserRePassword);
        edtUserEmail = (EditText) findViewById(R.id.editTextUserEmail);
        edtUserPhoneNumber = (EditText) findViewById(R.id.editTextUserPhoneNumber);

        db = new Database(this);



        btnRegister.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String userAccount=edtUserAccount.getText().toString();
                String userPassword=edtPasswordRegister.getText().toString();
                String userRepassword=edtUserRepassword.getText().toString();
                String userFirstName=edtUserFirstName.getText().toString();
                String userLastName=edtUserLastName.getText().toString();
                String userEmail=edtUserEmail.getText().toString();
                String userPhoneNumber=edtUserPhoneNumber.getText().toString();



                if(userAccount.equals("") || userPassword.equals("") || userFirstName.equals("") ||
                        userLastName.equals("") || userEmail.equals("") ||
                        userPhoneNumber.equals(""))
                {
                    Toast.makeText(RegisterActivity.this,
                            "Bạn không được bỏ trống bất kỳ trường nào",
                            Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(userPassword.equals(userRepassword))
                    {
                        Boolean userCheckResult = db.checkUserName(userAccount);
                        if(userCheckResult == false
                                && (isValidEmailId(edtUserEmail.getText().toString().trim()))
                                && (isValidPhoneNumber(edtUserPhoneNumber.getText().toString().trim())))
                        {
                            Boolean regResult = db.insertData(userAccount, userPassword,
                                    userFirstName, userLastName, userEmail, userPhoneNumber);
                            if(regResult == true)
                            {
                                Toast.makeText(RegisterActivity.this,
                                        "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),
                                        MainActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(RegisterActivity.this,
                                        "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else if(userCheckResult == true)
                        {   Toast.makeText(RegisterActivity.this,
                                "Tài khoản đã tồn tại",
                                Toast.LENGTH_SHORT).show();
                            edtUserAccount.setError("Tài khoản đã tồn tại!");
                        }
                    }
                    else
                    {

                        edtUserRepassword.setError("Mật khẩu không khớp!");
                    }
                }

                if(!isValidEmailId(edtUserEmail.getText().toString().trim())){
                    edtUserEmail.setError("Email không hợp lệ!");
        }
                if(!isValidPhoneNumber(edtUserPhoneNumber.getText().toString().trim())){
                    edtUserPhoneNumber.setError("Số điện thoại không hợp lệ!");
                }







            }
        });


        }
    private boolean isValidEmailId(String userEmail){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(userEmail).matches();
        }

    private boolean isValidPhoneNumber(String userPhoneNumber){

        return Pattern.compile("^(0\\d{9})$").matcher(userPhoneNumber).matches();
    }


        }

