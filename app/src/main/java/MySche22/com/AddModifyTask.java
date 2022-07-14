package MySche22.com;

import static MySche22.com.R.id.btn_back;
import static MySche22.com.R.id.date_picker;
import static MySche22.com.R.id.timeText;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.autofill.AutofillValue;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;

import MySche22.com.Database.Database;
import MySche22.com.Fragment.PersonalScheduleFragment;

public class AddModifyTask extends AppCompatActivity {

    Calendar calendar;
    Database mydb;
    private Calendar mCalendar;
    Boolean isModify = false;
    String task_id, user;
    TextView toolbar_title;
    EditText edit_text;
    TextView dateText, textViewDateTaskList, timeText;
    Button save_btn;
    ImageButton back_button;
    SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_add_modify_task);
        mydb = new Database(getApplicationContext());
        calendar = new GregorianCalendar();
        toolbar_title = findViewById(R.id.toolbar_title);
        timeText = findViewById(R.id.timeText);
        edit_text = findViewById(R.id.edit_text);
        dateText = findViewById(R.id.dateText);
        save_btn = findViewById(R.id.save_btn);
        back_button = findViewById(btn_back);
        textViewDateTaskList = findViewById(R.id.textViewDateTaskList);
        ///set currenttime for textview
        dateText.setText(new SimpleDateFormat("E, dd MMMM yyyy").format(calendar.getTime()));
        timeText.setText(new SimpleDateFormat("HH:mm").format(calendar.getTime()));




        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra("isModify")) {
            isModify = intent.getBooleanExtra("isModify", false);
            task_id = intent.getStringExtra("id");
            init_modify();
        }



    }

    public void init_modify() {
        toolbar_title.setText("");
        save_btn.setText("Cập nhật");
        LinearLayout deleteTask = findViewById(R.id.deleteTask);
        deleteTask.setVisibility(View.VISIBLE);
        Cursor task = mydb.getSingleTask(task_id);
        if (task != null) {
            task.moveToFirst();

            edit_text.setText(task.getString(1));
            SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH-mm");
            try {
                calendar.setTime(iso8601Format.parse(task.getString(2)));
                calendar.setTime(timeFormat.parse(task.getString(3)));
            } catch (ParseException e) {
            }
            ////display time in update
            dateText.setText(new SimpleDateFormat("E, dd MMMM yyyy").format(calendar.getTime()));
            timeText.setText(new SimpleDateFormat("HH:mm").format(calendar.getTime()));

        }

    }


    public void saveTask(View v) {
        /*Checking for Empty Task*/
        if (edit_text.getText().toString().trim().length() > 0) {
            if (isModify) {
                SharedPreferences sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
                String account = sharedPreferences.getString("account","");
                ////display in task after update
                mydb.updateTask(task_id,
                        edit_text.getText().toString(),
                        new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()),
                        new SimpleDateFormat("HH:mm").format(calendar.getTime()),
                        account);
                Toast.makeText(getApplicationContext(), "Tác vụ đã được cập nhật.", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
                String account = sharedPreferences.getString("account","");
                ///display in task after insert
                mydb.insertTask(edit_text.getText().toString(),
                        new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()),
                        account,
                        new SimpleDateFormat("HH:mm").format(calendar.getTime()));
                Toast.makeText(getApplicationContext(), "Đã thêm.", Toast.LENGTH_SHORT).show();
            }
            finish();

        } else {
            Toast.makeText(getApplicationContext(), "Bạn không thể để nội dung trống.", Toast.LENGTH_SHORT).show();
        }

    }

    public void deleteTask(View v) {
        mydb.deleteTask(task_id);
        Toast.makeText(getApplicationContext(), "Đã xóa tác vụ", Toast.LENGTH_SHORT).show();
        finish();
    }


    public void chooseDate(View view) {
        final View dialogView = View.inflate(this, R.layout.date_picker, null);
        final DatePicker datePicker = dialogView.findViewById(R.id.date_picker);
//        datePicker.updateDate(calendar.get(Calendar.YEAR),
//                calendar.get(Calendar.MONTH),
//                calendar.get(Calendar.DAY_OF_MONTH));


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("Chọn thời gian");
        builder.setNegativeButton("Hủy", null);
        builder.setPositiveButton("Chọn", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ///display after choose time
                calendar = new GregorianCalendar(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth());
                dateText.setText(new SimpleDateFormat("E, dd MMMM yyyy").format(calendar.getTime()));
            }
        });
        builder.show();
    }

    public void chooseTime(View view) {
        final View dialogView = View.inflate(this, R.layout.time_picker, null);
        final TimePicker timePicker = dialogView.findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
//        timePicker.updateDate(calendar.get(Calendar.HOUR_OF_DAY),
//                calendar.get(Calendar.MINUTE));
//        timePicker.getHour();




        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("Chọn thời gian");
        builder.setNegativeButton("Hủy", null);
        builder.setPositiveButton("Chọn", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
//                calendar = new TimePicker(timePicker.getHour(), timePicker.getMinute());
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                calendar.set(Calendar.MINUTE, timePicker.getMinute());
                timeText.setText(new SimpleDateFormat("HH:mm").format(calendar.getTime()));
                ///Time new task
            }
        });
        builder.show();
    }


    }
