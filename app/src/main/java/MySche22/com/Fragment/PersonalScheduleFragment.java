package MySche22.com.Fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import MySche22.com.AddModifyTask;
import MySche22.com.Database.Database;
import MySche22.com.ListTaskAdapter;
import MySche22.com.MainActivity;
import MySche22.com.NoScrollListView;
import MySche22.com.R;
import MySche22.com.RegisterActivity;


public class PersonalScheduleFragment extends Fragment {

    Database mydb;
    LinearLayout empty;
    NestedScrollView scrollView;
    Intent intent;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout todayContainer, tomorrowContainer, otherContainer;
    NoScrollListView taskListToday, taskListTomorrow, taskListUpcoming;
    ArrayList<HashMap<String, String>> todayList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> tomorrowList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> upcomingList = new ArrayList<HashMap<String, String>>();
    String account;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_personal_schedule, container, false);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

//        getActivity().setContentView(R.layout.fragment_personal_schedule);
        mydb = new Database(getActivity());
        empty = v.findViewById(R.id.empty);
        scrollView = v.findViewById(R.id.scrollView);
        todayContainer = v.findViewById(R.id.todayContainer);
        tomorrowContainer = v.findViewById(R.id.tomorrowContainer);
        otherContainer = v.findViewById(R.id.otherContainer);
        taskListToday = v.findViewById(R.id.taskListToday);
        taskListTomorrow = v.findViewById(R.id.taskListTomorrow);
        taskListUpcoming = v.findViewById(R.id.taskListUpcoming);
        swipeRefreshLayout = v.findViewById(R.id.refresh);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onResume();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        intent = new Intent(getActivity(), AddModifyTask.class);
        final FloatingActionButton button1 = (FloatingActionButton) v.findViewById(R.id.btnfab);


        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        final Button button2 = (Button) v.findViewById(R.id.btnopenAddModifyTask);

        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        populateData();

    }
    public void populateData() {
        mydb = new Database(getActivity());

        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                fetchDataFromDB();
            }
        });
    }
    public void fetchDataFromDB() {
        todayList.clear();
        tomorrowList.clear();
        upcomingList.clear();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("account", getActivity().MODE_PRIVATE);
        String account = sharedPreferences.getString("account","");

        Cursor today = mydb.getTodayTask(account);
        Cursor tomorrow = mydb.getTomorrowTask(account);
        Cursor upcoming = mydb.getUpcomingTask(account);

        loadDataList(today, todayList);
        loadDataList(tomorrow, tomorrowList);
        loadDataList(upcoming, upcomingList);


        if (todayList.isEmpty() && tomorrowList.isEmpty() && upcomingList.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        } else {
            empty.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);

            if (todayList.isEmpty()) {
                todayContainer.setVisibility(View.GONE);
            } else {
                todayContainer.setVisibility(View.VISIBLE);
                loadListView(taskListToday, todayList);
            }

            if (tomorrowList.isEmpty()) {
                tomorrowContainer.setVisibility(View.GONE);
            } else {
                tomorrowContainer.setVisibility(View.VISIBLE);
                loadListView(taskListTomorrow, tomorrowList);
            }

            if (upcomingList.isEmpty()) {
                otherContainer.setVisibility(View.GONE);
            } else {
                otherContainer.setVisibility(View.VISIBLE);
                loadListView(taskListUpcoming, upcomingList);
            }
        }
    }


    public void loadDataList(Cursor cursor, ArrayList<HashMap<String, String>> dataList) {
        if (cursor != null) {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {

                HashMap<String, String> mapToday = new HashMap<String, String>();
                mapToday.put("id", cursor.getString(0).toString());
                mapToday.put("task", cursor.getString(1).toString());
                mapToday.put("date", cursor.getString(2).toString());
                mapToday.put("time", cursor.getString(3).toString());
                mapToday.put("status", cursor.getString(4).toString());
                mapToday.put("user", cursor.getString(5).toString());
                dataList.add(mapToday);
                cursor.moveToNext();
            }
        }
    }

    public void loadListView(NoScrollListView listView, final ArrayList<HashMap<String, String>> dataList) {
        ListTaskAdapter adapter = new ListTaskAdapter(getActivity(), dataList, mydb);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i = new Intent(getActivity(), AddModifyTask.class);
                i.putExtra("isModify", true);
                i.putExtra("id", dataList.get(+position).get("id"));
                startActivity(i);
            }
        });
    }

}