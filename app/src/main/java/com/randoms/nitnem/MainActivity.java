package com.randoms.nitnem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    int count = 3;
    ViewPager2 pager;
    FragmentStateAdapter pagerAdapter;
    TabLayout tabLayout;
    int mode, imageID, phase;
    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;
    Homescreen homescreen;
    ContentsList contentsList;
    QuickList quickList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences = this.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mode = mPreferences.getInt(getString(R.string.mode), R.style.Theme0);
        setTheme(mode);
        setContentView(R.layout.activity_main);

        imageID = mPreferences.getInt(getString(R.string.imageID), R.drawable.mondaymorning);
        phase = mPreferences.getInt(getString(R.string.phase), 0);

        pager = findViewById(R.id.pager);
        pagerAdapter = new PagerStateAdapter(this);
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(3);

        homescreen = new Homescreen();
        contentsList = new ContentsList();
        quickList = new QuickList();

        tabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, pager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0){
                    tab.setText("Home");
                } else if (position == 1){
                    tab.setText("Contents");
                } else {
                    tab.setText("Quick");
                }
            }
        }).attach();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences mPreferences = this.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mPreferences.edit();

        Date currentTime = Calendar.getInstance().getTime();
        String[] splitTime = currentTime.toString().split("\\s+");
        int newMode = mode;
        int newPhase = phase;

        switch (splitTime[0]){
            case "Mon":
                if (Integer.valueOf(splitTime[3].split(":")[0])>=0 && Integer.valueOf(splitTime[3].split(":")[0])<3){
                    mEditor.putInt(getString(R.string.mode), R.style.Theme2).commit();
                    mEditor.putInt(getString(R.string.imageID), R.drawable.wednesdaymorning).commit();
                    mEditor.putInt(getString(R.string.phase), 3).commit();
                    newMode = R.style.Theme2;
                    newPhase = 3;
                } else if (Integer.valueOf(splitTime[3].split(":")[0])>=3 && Integer.valueOf(splitTime[3].split(":")[0])<9){
                    mEditor.putInt(getString(R.string.mode), R.style.Theme0).commit();
                    mEditor.putInt(getString(R.string.imageID), R.drawable.mondaymorning).commit();
                    mEditor.putInt(getString(R.string.phase), 0).commit();
                    newMode = R.style.Theme0;
                    newPhase = 0;
                } else if (Integer.valueOf(splitTime[3].split(":")[0])>=9 && Integer.valueOf(splitTime[3].split(":")[0])<15){
                    mEditor.putInt(getString(R.string.mode), R.style.Theme0).commit();
                    mEditor.putInt(getString(R.string.imageID), R.drawable.mondayday).commit();
                    mEditor.putInt(getString(R.string.phase), 1).commit();
                    newMode = R.style.Theme0;
                    newPhase = 1;
                } else if (Integer.valueOf(splitTime[3].split(":")[0])>=15 && Integer.valueOf(splitTime[3].split(":")[0])<=20){
                    mEditor.putInt(getString(R.string.mode), R.style.Theme3).commit();
                    mEditor.putInt(getString(R.string.imageID), R.drawable.thursdayday).commit();
                    mEditor.putInt(getString(R.string.phase), 2).commit();
                    newMode = R.style.Theme3;
                    newPhase = 2;
                }
                else {
                    mEditor.putInt(getString(R.string.mode), R.style.Theme3).commit();
                    mEditor.putInt(getString(R.string.imageID), R.drawable.thursdaymorning).commit();
                    mEditor.putInt(getString(R.string.phase), 3).commit();
                    newMode = R.style.Theme3;
                    newPhase = 3;
                }
                break;
            case "Tue":
                if (Integer.valueOf(splitTime[3].split(":")[0])>=0 && Integer.valueOf(splitTime[3].split(":")[0])<3){
                    mEditor.putInt(getString(R.string.mode), R.style.Theme3).commit();
                    mEditor.putInt(getString(R.string.imageID), R.drawable.thursdaymorning).commit();
                    mEditor.putInt(getString(R.string.phase), 3).commit();
                    newMode = R.style.Theme3;
                    newPhase = 3;
                } else if (Integer.valueOf(splitTime[3].split(":")[0])>=3 && Integer.valueOf(splitTime[3].split(":")[0])<9){
                    mEditor.putInt(getString(R.string.mode), R.style.Theme1).commit();
                    mEditor.putInt(getString(R.string.imageID), R.drawable.tuesdaymorning).commit();
                    mEditor.putInt(getString(R.string.phase), 0).commit();
                    newMode = R.style.Theme1;
                    newPhase = 0;
                } else if (Integer.valueOf(splitTime[3].split(":")[0])>=9 && Integer.valueOf(splitTime[3].split(":")[0])<15){
                    mEditor.putInt(getString(R.string.mode), R.style.Theme1).commit();
                    mEditor.putInt(getString(R.string.imageID), R.drawable.tuesdayday).commit();
                    mEditor.putInt(getString(R.string.phase), 1).commit();
                    newMode = R.style.Theme1;
                    newPhase = 1;
                } else if (Integer.valueOf(splitTime[3].split(":")[0])>=15 && Integer.valueOf(splitTime[3].split(":")[0])<=20){
                    mEditor.putInt(getString(R.string.mode), R.style.Theme4).commit();
                    mEditor.putInt(getString(R.string.imageID), R.drawable.fridayday).commit();
                    mEditor.putInt(getString(R.string.phase), 2).commit();
                    newMode = R.style.Theme4;
                    newPhase = 2;
                }
                else {
                    mEditor.putInt(getString(R.string.mode), R.style.Theme4).commit();
                    mEditor.putInt(getString(R.string.imageID), R.drawable.fridaymorning).commit();
                    mEditor.putInt(getString(R.string.phase), 3).commit();
                    newMode = R.style.Theme4;
                    newPhase = 3;
                }
                break;
            case "Wed":
                if (Integer.valueOf(splitTime[3].split(":")[0])>=0 && Integer.valueOf(splitTime[3].split(":")[0])<3){
                    mEditor.putInt(getString(R.string.mode), R.style.Theme4).commit();
                    mEditor.putInt(getString(R.string.imageID), R.drawable.fridaymorning).commit();
                    mEditor.putInt(getString(R.string.phase), 3).commit();
                    newMode = R.style.Theme4;
                    newPhase = 3;
                } else if (Integer.valueOf(splitTime[3].split(":")[0])>=3 && Integer.valueOf(splitTime[3].split(":")[0])<9){
                    mEditor.putInt(getString(R.string.mode), R.style.Theme2).commit();
                    mEditor.putInt(getString(R.string.imageID), R.drawable.wednesdaymorning).commit();
                    mEditor.putInt(getString(R.string.phase), 0).commit();
                    newMode = R.style.Theme2;
                    newPhase = 0;
                } else if (Integer.valueOf(splitTime[3].split(":")[0])>=9 && Integer.valueOf(splitTime[3].split(":")[0])<15){
                    mEditor.putInt(getString(R.string.mode), R.style.Theme2).commit();
                    mEditor.putInt(getString(R.string.imageID), R.drawable.wednesdayday).commit();
                    mEditor.putInt(getString(R.string.phase), 1).commit();
                    newMode = R.style.Theme2;
                    newPhase = 1;
                } else if (Integer.valueOf(splitTime[3].split(":")[0])>=15 && Integer.valueOf(splitTime[3].split(":")[0])<=20){
                    mEditor.putInt(getString(R.string.mode), R.style.Theme5).commit();
                    mEditor.putInt(getString(R.string.imageID), R.drawable.saturdayday).commit();
                    mEditor.putInt(getString(R.string.phase), 2).commit();
                    newMode = R.style.Theme5;
                    newPhase = 2;
                }
                else {
                    mEditor.putInt(getString(R.string.mode), R.style.Theme5).commit();
                    mEditor.putInt(getString(R.string.imageID), R.drawable.saturdaymorning).commit();
                    mEditor.putInt(getString(R.string.phase), 3).commit();
                    newMode = R.style.Theme5;
                    newPhase = 3;
                }
                break;
            case "Thu":
                if (Integer.valueOf(splitTime[3].split(":")[0])>=0 && Integer.valueOf(splitTime[3].split(":")[0])<3){
                    mEditor.putInt(getString(R.string.mode), R.style.Theme5).commit();
                    mEditor.putInt(getString(R.string.imageID), R.drawable.saturdaymorning).commit();
                    mEditor.putInt(getString(R.string.phase), 3).commit();
                    newMode = R.style.Theme5;
                    newPhase = 3;
                } else if (Integer.valueOf(splitTime[3].split(":")[0])>=3 && Integer.valueOf(splitTime[3].split(":")[0])<9){
                    mEditor.putInt(getString(R.string.mode), R.style.Theme3).commit();
                    mEditor.putInt(getString(R.string.imageID), R.drawable.thursdaymorning).commit();
                    mEditor.putInt(getString(R.string.phase), 0).commit();
                    newMode = R.style.Theme3;
                    newPhase = 0;
                } else if (Integer.valueOf(splitTime[3].split(":")[0])>=9 && Integer.valueOf(splitTime[3].split(":")[0])<15){
                    mEditor.putInt(getString(R.string.mode), R.style.Theme3).commit();
                    mEditor.putInt(getString(R.string.imageID), R.drawable.thursdayday).commit();
                    mEditor.putInt(getString(R.string.phase), 1).commit();
                    newMode = R.style.Theme3;
                    newPhase = 1;
                } else if (Integer.valueOf(splitTime[3].split(":")[0])>=15 && Integer.valueOf(splitTime[3].split(":")[0])<=20){
                    mEditor.putInt(getString(R.string.mode), R.style.Theme6).commit();
                    mEditor.putInt(getString(R.string.imageID), R.drawable.sundayday).commit();
                    mEditor.putInt(getString(R.string.phase), 2).commit();
                    newMode = R.style.Theme6;
                    newPhase = 2;
                }
                else {
                    mEditor.putInt(getString(R.string.mode), R.style.Theme6).commit();
                    mEditor.putInt(getString(R.string.imageID), R.drawable.sundaymorning).commit();
                    mEditor.putInt(getString(R.string.phase), 3).commit();
                    newMode = R.style.Theme6;
                    newPhase = 3;
                }
                break;
            case "Fri":
                if (Integer.valueOf(splitTime[3].split(":")[0])>=0 && Integer.valueOf(splitTime[3].split(":")[0])<3){
                    mEditor.putInt(getString(R.string.mode), R.style.Theme6).commit();
                    mEditor.putInt(getString(R.string.imageID), R.drawable.sundaymorning).commit();
                    mEditor.putInt(getString(R.string.phase), 3).commit();
                    newMode = R.style.Theme6;
                    newPhase = 3;
                } else if (Integer.valueOf(splitTime[3].split(":")[0])>=3 && Integer.valueOf(splitTime[3].split(":")[0])<9){
                    mEditor.putInt(getString(R.string.mode), R.style.Theme4).commit();
                    mEditor.putInt(getString(R.string.imageID), R.drawable.fridaymorning).commit();
                    mEditor.putInt(getString(R.string.phase), 0).commit();
                    newMode = R.style.Theme4;
                    newPhase = 0;
                } else if (Integer.valueOf(splitTime[3].split(":")[0])>=9 && Integer.valueOf(splitTime[3].split(":")[0])<15){
                    mEditor.putInt(getString(R.string.mode), R.style.Theme4).commit();
                    mEditor.putInt(getString(R.string.imageID), R.drawable.fridayday).commit();
                    mEditor.putInt(getString(R.string.phase), 1).commit();
                    newMode = R.style.Theme4;
                    newPhase = 1;
                } else if (Integer.valueOf(splitTime[3].split(":")[0])>=15 && Integer.valueOf(splitTime[3].split(":")[0])<=20){
                    mEditor.putInt(getString(R.string.mode), R.style.Theme0).commit();
                    mEditor.putInt(getString(R.string.imageID), R.drawable.mondayday).commit();
                    mEditor.putInt(getString(R.string.phase), 2).commit();
                    newMode = R.style.Theme0;
                    newPhase = 2;
                }
                else {
                    mEditor.putInt(getString(R.string.mode), R.style.Theme0).commit();
                    mEditor.putInt(getString(R.string.imageID), R.drawable.mondaymorning).commit();
                    mEditor.putInt(getString(R.string.phase), 3).commit();
                    newMode = R.style.Theme0;
                    newPhase = 3;
                }
                break;
            case "Sat":
                if (Integer.valueOf(splitTime[3].split(":")[0])>=0 && Integer.valueOf(splitTime[3].split(":")[0])<3){
                    mEditor.putInt(getString(R.string.mode), R.style.Theme0).commit();
                    mEditor.putInt(getString(R.string.imageID), R.drawable.mondaymorning).commit();
                    mEditor.putInt(getString(R.string.phase), 3).commit();
                    newMode = R.style.Theme0;
                    newPhase = 3;
                } else if (Integer.valueOf(splitTime[3].split(":")[0])>=3 && Integer.valueOf(splitTime[3].split(":")[0])<9){
                    mEditor.putInt(getString(R.string.mode), R.style.Theme5).commit();
                    mEditor.putInt(getString(R.string.imageID), R.drawable.saturdaymorning).commit();
                    mEditor.putInt(getString(R.string.phase), 0).commit();
                    newMode = R.style.Theme5;
                    newPhase = 0;
                } else if (Integer.valueOf(splitTime[3].split(":")[0])>=9 && Integer.valueOf(splitTime[3].split(":")[0])<15){
                    mEditor.putInt(getString(R.string.mode), R.style.Theme5).commit();
                    mEditor.putInt(getString(R.string.imageID), R.drawable.saturdayday).commit();
                    mEditor.putInt(getString(R.string.phase), 1).commit();
                    newMode = R.style.Theme5;
                    newPhase = 1;
                } else if (Integer.valueOf(splitTime[3].split(":")[0])>=15 && Integer.valueOf(splitTime[3].split(":")[0])<=20){
                    mEditor.putInt(getString(R.string.mode), R.style.Theme1).commit();
                    mEditor.putInt(getString(R.string.imageID), R.drawable.tuesdayday).commit();
                    mEditor.putInt(getString(R.string.phase), 2).commit();
                    newMode = R.style.Theme1;
                    newPhase = 2;
                }
                else {
                    mEditor.putInt(getString(R.string.mode), R.style.Theme1).commit();
                    mEditor.putInt(getString(R.string.imageID), R.drawable.tuesdaymorning).commit();
                    mEditor.putInt(getString(R.string.phase), 3).commit();
                    newMode = R.style.Theme1;
                    newPhase = 3;
                }
                break;
            case "Sun":
                if (Integer.valueOf(splitTime[3].split(":")[0])>=0 && Integer.valueOf(splitTime[3].split(":")[0])<3){
                    mEditor.putInt(getString(R.string.mode), R.style.Theme1).commit();
                    mEditor.putInt(getString(R.string.imageID), R.drawable.tuesdaymorning).commit();
                    mEditor.putInt(getString(R.string.phase), 3).commit();
                    newMode = R.style.Theme1;
                    newPhase = 3;
                } else if (Integer.valueOf(splitTime[3].split(":")[0])>=3 && Integer.valueOf(splitTime[3].split(":")[0])<9){
                    mEditor.putInt(getString(R.string.mode), R.style.Theme6).commit();
                    mEditor.putInt(getString(R.string.imageID), R.drawable.sundaymorning).commit();
                    mEditor.putInt(getString(R.string.phase), 0).commit();
                    newMode = R.style.Theme6;
                    newPhase = 0;
                } else if (Integer.valueOf(splitTime[3].split(":")[0])>=9 && Integer.valueOf(splitTime[3].split(":")[0])<15){
                    mEditor.putInt(getString(R.string.mode), R.style.Theme6).commit();
                    mEditor.putInt(getString(R.string.imageID), R.drawable.sundayday).commit();
                    mEditor.putInt(getString(R.string.phase), 1).commit();
                    newMode = R.style.Theme6;
                    newPhase = 1;
                } else if (Integer.valueOf(splitTime[3].split(":")[0])>=15 && Integer.valueOf(splitTime[3].split(":")[0])<=20){
                    mEditor.putInt(getString(R.string.mode), R.style.Theme2).commit();
                    mEditor.putInt(getString(R.string.imageID), R.drawable.wednesdayday).commit();
                    mEditor.putInt(getString(R.string.phase), 2).commit();
                    newMode = R.style.Theme2;
                    newPhase = 2;
                }
                else {
                    mEditor.putInt(getString(R.string.mode), R.style.Theme2).commit();
                    mEditor.putInt(getString(R.string.imageID), R.drawable.wednesdaymorning).commit();
                    mEditor.putInt(getString(R.string.phase), 3).commit();
                    newMode = R.style.Theme2;
                    newPhase = 3;
                }
                break;
            default:
        }
        if (newPhase != phase || newMode != mode){
            recreate();
        }
    }

    private class PagerStateAdapter extends FragmentStateAdapter {

        public PagerStateAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0){
                return homescreen;
            } else if (position == 1){
                return contentsList;
            } else {
                return quickList;
            }
        }

        @Override
        public int getItemCount() {
            return count;
        }

    }
}

