package com.randoms.nitnem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class SundarGutkaActivity extends AppCompatActivity implements Serializable {
    String [] BaniNames;

    DrawerLayout sundarGutkaDrawer;
    View settingsViewHolder, settingsAreaView;
    ImageButton backButton, sundarGuktaGoToBtn, settingsBtn, closeSettingsViewBtn;
    TextView titleText, fontSizeSeekBarProgress, sundarGutkaHeadingText, sundarGutkaBaniText;
    Button lightThemeBtn, darkThemeBtn, slimFontStyleBtn, regularFontStyleBtn, mediumFontStyleBtn, boldFontStyleBtn;
    SeekBar fontSizeSeekBar;

    RecyclerView gurbaniScreenRV, gotoRV;

    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;

    Boolean lightMode;
    int loadBaniIndex, fontStyle, fontSize, textColorUse, backColorUse, navBarColor, renderedPosition;

    GradientDrawable settingsViewGradient;

    ArrayList<ArrayList<String>> GurbaniArrayList;
    ArrayList<ArrayList<Boolean>> HeadingArrayList;
    ArrayList<ArrayList<Integer>> JumpToIndexArrayList;
    ArrayList<ArrayList<String>> JumpToLabelsArrayList;

    ArrayList<Integer> unfinishedVisibility, sundarGutkaPosition;

    LinearLayoutManager linearLayoutManagerQ;
    LinearLayoutManagerWithSmoothScroller linearLayoutManager2;

    GoToAdapter goToAdapter;
    GurbaniViewAdapter gurbaniViewAdapter;

    Gson gson;
    String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setTheme(intent.getIntExtra("theme", R.style.Theme0));
        setContentView(R.layout.activity_sundar_gutka);

        BaniNames = new String[]{"jpujI swihb", "jwpu swihb", "qÍpRswid sÍXy", "cOpeI swihb", "AnMdu swihb", "rhrwis swihb", "r`iKAw Sbd", "kIrqn soihlw", "Sbd hjwry", "bwrh mwhw mWJ",
                "Sbd hjwry pw 10", "sÍXy dInn", "AwrqI", "Ardws", "suKmnI swihb", "Awsw dI vwr", "dKxI EAMkwr", "isD gosit", "bwvn AKrI", "jYqsrI dI vwr",
                "rwmklI kI vwr", "bsMq kI vwr", "bwrh mwhw quKwrI", "lwvW", "slok mhlw 9", "kucjI", "sucjI", "guxvMqI", "rwgmwlw", "Akwl ausqq cOpeI", "cMfI dI vwr"};

        GurbaniArrayList = new ArrayList<ArrayList<String>>();
        HeadingArrayList = new ArrayList<ArrayList<Boolean>>();
        JumpToIndexArrayList = new ArrayList<ArrayList<Integer>>();
        JumpToLabelsArrayList = new ArrayList<ArrayList<String>>();

        unfinishedVisibility = new ArrayList<Integer>();
        sundarGutkaPosition = new ArrayList<Integer>();

        InputStream inputStream;
        ObjectInputStream ois;
        try {
            AssetManager assetManager = this.getAssets();
            inputStream = assetManager.open("g0");
            ois = new ObjectInputStream(inputStream);
            GurbaniArrayList = (ArrayList<ArrayList<String>>) ois.readObject();
            inputStream.close();

            inputStream = assetManager.open("g1");
            ois = new ObjectInputStream(inputStream);
            HeadingArrayList = (ArrayList<ArrayList<Boolean>>) ois.readObject();
            inputStream.close();

            inputStream = assetManager.open("g2");
            ois = new ObjectInputStream(inputStream);
            JumpToIndexArrayList = (ArrayList<ArrayList<Integer>>) ois.readObject();
            inputStream.close();

            inputStream = assetManager.open("g3");
            ois = new ObjectInputStream(inputStream);
            JumpToLabelsArrayList = (ArrayList<ArrayList<String>>) ois.readObject();
            inputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(SundarGutkaActivity.this, "Error Occurred, Try Again Later", Toast.LENGTH_LONG).show();
            finish();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(SundarGutkaActivity.this, "Error Occurred, Try Again Later", Toast.LENGTH_LONG).show();
            finish();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(SundarGutkaActivity.this, "Error Occurred, Try Again Later", Toast.LENGTH_LONG).show();
            finish();
        }

        mPreferences = this.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();

        loadBaniIndex = intent.getIntExtra("loadBaniIndex", 0);

        gson= new Gson();
        Type type = new TypeToken<ArrayList<Integer>>() {}.getType();
        json = mPreferences.getString(getString(R.string.unfinishedVisibility), "[4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4]");
        unfinishedVisibility = gson.fromJson(json, type);
        json = mPreferences.getString(getString(R.string.sundarGutkaPosition), "[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]");
        sundarGutkaPosition = gson.fromJson(json, type);

        sundarGutkaDrawer = findViewById(R.id.sundarGutkaDrawer);
        gurbaniScreenRV = findViewById(R.id.gurbaniScreenRV);
        gotoRV = findViewById(R.id.gotoRV);
        settingsViewHolder = findViewById(R.id.settingsViewHolder);
        settingsAreaView = findViewById(R.id.settingsAreaView);
        backButton = findViewById(R.id.backButton);
        sundarGuktaGoToBtn = findViewById(R.id.sundarGuktaGoToBtn);
        settingsBtn = findViewById(R.id.settingsBtn);
        closeSettingsViewBtn = findViewById(R.id.closeSettingsViewBtn);

        sundarGutkaHeadingText = findViewById(R.id.sundarGutkaHeadingText);
        sundarGutkaBaniText = findViewById(R.id.sundarGutkaBaniText);

        titleText = findViewById(R.id.titleText);
        fontSizeSeekBar = findViewById(R.id.fontSizeSeekBar);
        fontSizeSeekBarProgress = findViewById(R.id.fontSizeSeekBarProgress);

        lightThemeBtn = findViewById(R.id.lightThemeBtn);
        darkThemeBtn = findViewById(R.id.darkThemeBtn);
        slimFontStyleBtn = findViewById(R.id.slimFontStyleBtn);
        regularFontStyleBtn = findViewById(R.id.regularFontStyleBtn);
        mediumFontStyleBtn = findViewById(R.id.mediumFontStyleBtn);
        boldFontStyleBtn = findViewById(R.id.boldFontStyleBtn);

        titleText.setText(BaniNames[loadBaniIndex]);

        lightMode = mPreferences.getBoolean(getString(R.string.lightMode), true);
        fontStyle = mPreferences.getInt(getString(R.string.fontStyle), R.layout.list_gurbani_layout);
        fontSize = mPreferences.getInt(getString(R.string.fontSize), 19);

        fontSizeSeekBar.setMax(48);
        fontSizeSeekBar.setProgress(fontSize);
        fontSizeSeekBarProgress.setText(""+fontSize);

        renderedPosition = (96*GurbaniArrayList.get(loadBaniIndex).size())/100;

        linearLayoutManager2 = new LinearLayoutManagerWithSmoothScroller(this);
        gurbaniViewAdapter = new GurbaniViewAdapter();
        gurbaniScreenRV.setLayoutManager(linearLayoutManager2);
        gurbaniScreenRV.setAdapter(gurbaniViewAdapter);
        gurbaniScreenRV.smoothScrollToPosition(sundarGutkaPosition.get(loadBaniIndex));

        linearLayoutManagerQ = new LinearLayoutManager(this);
        goToAdapter = new GoToAdapter();
        gotoRV.setLayoutManager(linearLayoutManagerQ);
        gotoRV.setAdapter(goToAdapter);

        if(lightMode){
            lightThemeBtn.setSelected(true);
            darkThemeBtn.setSelected(false);
            textColorUse = Color.parseColor("#2C2C2C");
            backColorUse = Color.parseColor("#FFFFFF");
            navBarColor = Color.parseColor("#F2F2F2");
        } else {
            lightThemeBtn.setSelected(false);
            darkThemeBtn.setSelected(true);
            textColorUse = Color.parseColor("#B7B7B7");
            backColorUse = Color.parseColor("#000000");
            navBarColor = Color.parseColor("#000000");
        }

        gurbaniScreenRV.setKeepScreenOn(true);
        gurbaniScreenRV.setBackgroundColor(backColorUse);
        getWindow().setNavigationBarColor(navBarColor);

        switch(fontStyle){
            case R.layout.list_gurbani_layout_slim:
                slimFontStyleBtn.setSelected(true);
                regularFontStyleBtn.setSelected(false);
                mediumFontStyleBtn.setSelected(false);
                boldFontStyleBtn.setSelected(false);
                break;
            case R.layout.list_gurbani_layout:
                slimFontStyleBtn.setSelected(false);
                regularFontStyleBtn.setSelected(true);
                mediumFontStyleBtn.setSelected(false);
                boldFontStyleBtn.setSelected(false);
                break;
            case R.layout.list_gurbani_layout_medium:
                slimFontStyleBtn.setSelected(false);
                regularFontStyleBtn.setSelected(false);
                mediumFontStyleBtn.setSelected(true);
                boldFontStyleBtn.setSelected(false);
                break;
            case R.layout.list_gurbani_layout_bold:
                slimFontStyleBtn.setSelected(false);
                regularFontStyleBtn.setSelected(false);
                mediumFontStyleBtn.setSelected(false);
                boldFontStyleBtn.setSelected(true);
                break;
            default:
        }

        float radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, this.getResources().getDisplayMetrics());

        settingsViewGradient = new GradientDrawable();
        settingsViewGradient.setShape(GradientDrawable.RECTANGLE);
        settingsViewGradient.setColor(Color.argb(250,255,255, 255));
        settingsViewGradient.setCornerRadii(new float[]{radius, radius, radius, radius, 0, 0, 0, 0});
        settingsAreaView.setBackground(settingsViewGradient);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        closeSettingsViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsAreaView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(settingsAreaView.getHeight()).alpha(0.0f).setDuration(220);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        settingsAreaView.setVisibility(View.INVISIBLE);
                        settingsViewHolder.setVisibility(View.INVISIBLE);
                    }
                }, 220);
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(settingsAreaView.getVisibility() != View.VISIBLE){
                    settingsAreaView.setTranslationY(settingsAreaView.getHeight());
                    settingsAreaView.setAlpha(0.0f);
                    settingsAreaView.setVisibility(View.VISIBLE);
                    settingsViewHolder.setVisibility(View.VISIBLE);
                    settingsAreaView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(0).alpha(1.0f).setDuration(220);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            settingsAreaView.setAlpha(1.0f);
                        }
                    }, 220);
                }
            }
        });

        sundarGuktaGoToBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sundarGutkaDrawer.openDrawer(GravityCompat.END);
            }
        });

        lightThemeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!lightThemeBtn.isActivated()){
                    lightThemeBtn.setSelected(true);
                    darkThemeBtn.setSelected(false);
                    textColorUse = Color.parseColor("#2C2C2C");
                    backColorUse = Color.parseColor("#FFFFFF");
                    navBarColor = Color.parseColor("#F2F2F2");
                    gurbaniScreenRV.setBackgroundColor(backColorUse);
                    getWindow().setNavigationBarColor(navBarColor);
                    gurbaniScreenRV.setAdapter(null);
                    gurbaniScreenRV.setAdapter(gurbaniViewAdapter);
                    mEditor.putBoolean(getString(R.string.lightMode), true).commit();
                }
            }
        });

        darkThemeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!lightThemeBtn.isActivated()){
                    lightThemeBtn.setSelected(false);
                    darkThemeBtn.setSelected(true);
                    textColorUse = Color.parseColor("#B7B7B7");
                    backColorUse = Color.parseColor("#000000");
                    navBarColor = Color.parseColor("#000000");
                    gurbaniScreenRV.setBackgroundColor(backColorUse);
                    getWindow().setNavigationBarColor(navBarColor);
                    gurbaniScreenRV.setAdapter(null);
                    gurbaniScreenRV.setAdapter(gurbaniViewAdapter);
                    mEditor.putBoolean(getString(R.string.lightMode), false).commit();
                }
            }
        });

        slimFontStyleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slimFontStyleBtn.setSelected(true);
                regularFontStyleBtn.setSelected(false);
                mediumFontStyleBtn.setSelected(false);
                boldFontStyleBtn.setSelected(false);
                fontStyle = R.layout.list_gurbani_layout_slim;
                gurbaniScreenRV.setAdapter(null);
                gurbaniScreenRV.setAdapter(gurbaniViewAdapter);
                mEditor.putInt(getString(R.string.fontStyle), R.layout.list_gurbani_layout_slim).commit();
            }
        });

        regularFontStyleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slimFontStyleBtn.setSelected(false);
                regularFontStyleBtn.setSelected(true);
                mediumFontStyleBtn.setSelected(false);
                boldFontStyleBtn.setSelected(false);
                fontStyle = R.layout.list_gurbani_layout;
                gurbaniScreenRV.setAdapter(null);
                gurbaniScreenRV.setAdapter(gurbaniViewAdapter);
                mEditor.putInt(getString(R.string.fontStyle), R.layout.list_gurbani_layout).commit();
            }
        });

        mediumFontStyleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slimFontStyleBtn.setSelected(false);
                regularFontStyleBtn.setSelected(false);
                mediumFontStyleBtn.setSelected(true);
                boldFontStyleBtn.setSelected(false);
                fontStyle = R.layout.list_gurbani_layout_medium;
                gurbaniScreenRV.setAdapter(null);
                gurbaniScreenRV.setAdapter(gurbaniViewAdapter);
                mEditor.putInt(getString(R.string.fontStyle), R.layout.list_gurbani_layout_medium).commit();
            }
        });

        boldFontStyleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slimFontStyleBtn.setSelected(false);
                regularFontStyleBtn.setSelected(false);
                mediumFontStyleBtn.setSelected(false);
                boldFontStyleBtn.setSelected(true);
                fontStyle = R.layout.list_gurbani_layout_bold;
                gurbaniScreenRV.setAdapter(null);
                gurbaniScreenRV.setAdapter(gurbaniViewAdapter);
                mEditor.putInt(getString(R.string.fontStyle), R.layout.list_gurbani_layout_bold).commit();
            }
        });

        fontSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                fontSizeSeekBarProgress.setText(""+i);
                fontSize = i;
                gurbaniScreenRV.setAdapter(null);
                gurbaniScreenRV.setAdapter(gurbaniViewAdapter);
                mEditor.putInt(getString(R.string.fontSize), i).commit();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if(sundarGutkaDrawer.isDrawerOpen(GravityCompat.END)){
            sundarGutkaDrawer.closeDrawer(GravityCompat.END);
            return;
        }

        if(settingsAreaView.getVisibility()==View.VISIBLE){
            settingsAreaView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(settingsAreaView.getHeight()).alpha(0.0f).setDuration(220);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    settingsAreaView.setVisibility(View.INVISIBLE);
                    settingsViewHolder.setVisibility(View.INVISIBLE);
                }
            }, 220);
            return;
        }

        if (linearLayoutManager2.findLastVisibleItemPosition()<renderedPosition && linearLayoutManager2.findFirstCompletelyVisibleItemPosition()>1){
            sundarGutkaPosition.set(loadBaniIndex, linearLayoutManager2.findFirstCompletelyVisibleItemPosition());
            unfinishedVisibility.set(loadBaniIndex, 0);
        } else {
            sundarGutkaPosition.set(loadBaniIndex, 0);
            unfinishedVisibility.set(loadBaniIndex, 4);
        }

        json = gson.toJson(sundarGutkaPosition);
        mEditor.putString(getString(R.string.sundarGutkaPosition), json).commit();
        json = gson.toJson(unfinishedVisibility);
        mEditor.putString(getString(R.string.unfinishedVisibility), json).commit();

        super.onBackPressed();
    }

    public class GoToAdapter extends RecyclerView.Adapter<GoToAdapter.ListViewHolder>{
        @NonNull
        @Override
        public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_goto_layout, parent, false);
            return new ListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
            holder.gotoBani.setText(JumpToLabelsArrayList.get(loadBaniIndex).get(position));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = gotoRV.getChildAdapterPosition(view);
                    gurbaniScreenRV.smoothScrollToPosition(JumpToIndexArrayList.get(loadBaniIndex).get(pos));
                    sundarGutkaDrawer.closeDrawer(GravityCompat.END);
                }
            });
        }

        @Override
        public int getItemCount() {
            return JumpToIndexArrayList.get(loadBaniIndex).size();
        }

        public class ListViewHolder extends RecyclerView.ViewHolder{
            TextView gotoBani;

            public ListViewHolder(@NonNull View itemView) {
                super(itemView);
                gotoBani = itemView.findViewById(R.id.gotoBani);
            }
        }
    }

    public class GurbaniViewAdapter extends RecyclerView.Adapter<GurbaniViewAdapter.ListViewHolder>{
        @NonNull
        @Override
        public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(fontStyle, parent, false);
            return new ListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
            if(HeadingArrayList.get(loadBaniIndex).get(position)){
                holder.sundarGutkaHeadingText.setText(GurbaniArrayList.get(loadBaniIndex).get(position));
                holder.sundarGutkaHeadingText.setVisibility(View.VISIBLE);
                holder.sundarGutkaBaniText.setVisibility(View.GONE);
            } else {
                holder.sundarGutkaBaniText.setText(GurbaniArrayList.get(loadBaniIndex).get(position));
                holder.sundarGutkaBaniText.setVisibility(View.VISIBLE);
                holder.sundarGutkaHeadingText.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return GurbaniArrayList.get(loadBaniIndex).size();
        }

        public class ListViewHolder extends RecyclerView.ViewHolder{
            TextView sundarGutkaHeadingText;
            TextView sundarGutkaBaniText;

            public ListViewHolder(@NonNull View itemView) {
                super(itemView);
                sundarGutkaHeadingText = itemView.findViewById(R.id.sundarGutkaHeadingText);
                sundarGutkaBaniText = itemView.findViewById(R.id.sundarGutkaBaniText);

                sundarGutkaBaniText.setTextColor(textColorUse);
                sundarGutkaHeadingText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fontSize);
                sundarGutkaBaniText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fontSize);
            }
        }
    }
}