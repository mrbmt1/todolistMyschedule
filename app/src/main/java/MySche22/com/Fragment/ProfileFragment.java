package MySche22.com.Fragment;

import android.accounts.Account;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import MySche22.com.AddModifyTask;
import MySche22.com.Database.Database;
import MySche22.com.MainActivity;
import MySche22.com.R;


public class ProfileFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        Database database = new Database(getActivity());
        TextView textViewName = v.findViewById(R.id.textViewUserName);
        TextView textViewEmail = v.findViewById(R.id.textViewUserEmail);
        TextView textViewPhone = v.findViewById(R.id.textViewPhone);
        TextView textViewAccount = v.findViewById(R.id.textViewAccount);

        String account = getActivity().getIntent().getStringExtra("account");
        Cursor cursor = database.getInfo(account);

        while (cursor.moveToNext()) {
            textViewName.setText(cursor.getString(4) + " " + (cursor.getString(3)));
            textViewPhone.setText(cursor.getString(5));
            textViewEmail.setText(cursor.getString(6));
            textViewAccount.setText(account);

        }
        return v;
    }





}