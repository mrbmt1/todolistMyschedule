package MySche22.com.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.switchmaterial.SwitchMaterial;

import MySche22.com.R;


public class SettingFragment extends Fragment {

    public static final String PREFERENCES = "preference";
    public static final String CUSTOM_THEME = "customtheme";
    public static final String LIGHT_THEME = "lighttheme";
    public static final String DARK_THEME = "dark_theme";

    private String customTheme;
    private View parentView;
    private SwitchMaterial themeSwitch;
//    private  UserSetting setting;
    




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
        
//        itWidget();

    }


    private void initWidget() {
        themeSwitch = getActivity().findViewById(R.id.settingTheme);
        parentView = getActivity().findViewById(R.id.parentView);
    }

    public String getCustomTheme() {
        return customTheme;
    }

    public void setCustomTheme(String customTheme) {
        this.customTheme = customTheme;
    }
}