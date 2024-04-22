package com.randoms.nitnem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ModifyActivity extends AppCompatActivity {
    ImageButton backButton;
    TextView titleText;
    Button saveButton;
    View saveButtonBackView;

    String [] BaniNamesEnglish;

    LinearLayoutManager linearLayoutManager;
    RecyclerView gurbaniSelectView;
    SelectListAdapter selectListAdapter;

    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;

    int phase;
    ArrayList<String> morningList, dayList, eveningList, nightList, listToShow;

    GradientDrawable saveButtonBackViewGradient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setTheme(intent.getIntExtra("theme", R.style.Theme0));
        setContentView(R.layout.activity_modify);

        backButton = findViewById(R.id.backButton);
        titleText = findViewById(R.id.titleText);
        gurbaniSelectView = findViewById(R.id.gurbaniSelectView);
        saveButton = findViewById(R.id.saveButton);
        saveButtonBackView = findViewById(R.id.saveButtonBackView);

        titleText.setText(intent.getStringExtra("modifierHeader"));

        BaniNamesEnglish = new String[]{"Japji Sahib", "Jaap Sahib", "Tav Prasad Sawaiye", "Chaupai Sahib", "Anand Sahib", "Rehraas Sahib", "Rakhya Shabad", "Kirtan Sohila", "Shabad Hazaare", "Barah Maha Maaj",
                "Shabad Hazaare Paatshahi 10", "Swaiye Deenan", "Aarti", "Ardaas", "Sukhmani Sahib", "Asa Di Vaar", "Dakhnee Oankaar", "Sidh Gosat", "Baavan Akhree", "Jaitsari Di Vaar",
                "Ramkali Ki Vaar", "Basant Ki Vaar", "Barah Maha Tukhari", "Laavan", "Salok Mehal 9", "Kuchaji", "Suchaji", "Gunvanti", "Raag Maala", "Akaal Ustat Chaupai", "Chandi Di Vaar"};

        mPreferences = this.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();

        phase = mPreferences.getInt(getString(R.string.phase), 0);

        morningList = new ArrayList<>();
        dayList = new ArrayList<>();
        eveningList = new ArrayList<>();
        nightList = new ArrayList<>();
        listToShow = new ArrayList<>();

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        String json = mPreferences.getString(getString(R.string.morningList), "[0,1,2,3,4,15]");
        morningList = gson.fromJson(json, type);
        json = mPreferences.getString(getString(R.string.dayList), "[8,14,17]");
        dayList = gson.fromJson(json, type);
        json = mPreferences.getString(getString(R.string.dayList), "[8,14,17]");
        dayList = gson.fromJson(json, type);
        json = mPreferences.getString(getString(R.string.eveningList), "[5,12]");
        eveningList = gson.fromJson(json, type);
        json = mPreferences.getString(getString(R.string.nightList), "[7]");
        nightList = gson.fromJson(json, type);

        switch (phase){
            case 0:
                listToShow = morningList;
                break;
            case 1:
                listToShow = dayList;
                break;
            case 2:
                listToShow = eveningList;
                break;
            case 3:
                listToShow = nightList;
                break;
            default:
        }

        float radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, this.getResources().getDisplayMetrics());

        saveButtonBackViewGradient = new GradientDrawable();
        saveButtonBackViewGradient.setShape(GradientDrawable.RECTANGLE);
        saveButtonBackViewGradient.setColor(Color.argb(230,255,255, 255));
        saveButtonBackViewGradient.setCornerRadii(new float[]{radius, radius, radius, radius, 0, 0, 0, 0});
        saveButtonBackView.setBackground(saveButtonBackViewGradient);

        linearLayoutManager = new LinearLayoutManager(this);
        selectListAdapter = new SelectListAdapter();
        gurbaniSelectView.setLayoutManager(linearLayoutManager);
        gurbaniSelectView.setAdapter(selectListAdapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                String json = gson.toJson(listToShow);

                switch (phase){
                    case 0:
                        mEditor.putString(getString(R.string.morningList), json).commit();
                        break;
                    case 1:
                        mEditor.putString(getString(R.string.dayList), json).commit();
                        break;
                    case 2:
                        mEditor.putString(getString(R.string.eveningList), json).commit();
                        break;
                    case 3:
                        mEditor.putString(getString(R.string.nightList), json).commit();
                        break;
                    default:
                }
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public class SelectListAdapter extends RecyclerView.Adapter<SelectListAdapter.ListViewHolder>{
        @NonNull
        @Override
        public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gurbani_select_view, parent, false);
            return new ListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ListViewHolder holder, final int position) {
            if (position != BaniNamesEnglish.length){
                if(listToShow.contains(""+position)){
                    holder.checkMark.setActivated(true);
                } else {
                    holder.checkMark.setActivated(false);
                }
                holder.selectGurbaniName.setText(BaniNamesEnglish[position]);
                holder.selectGurbaniCell.setVisibility(View.VISIBLE);
                holder.selectGurbaniEmptyCell.setVisibility(View.GONE);
            } else {
                holder.selectGurbaniCell.setVisibility(View.GONE);
                holder.selectGurbaniEmptyCell.setVisibility(View.VISIBLE);
            }

            holder.checkMark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int click = gurbaniSelectView.getChildAdapterPosition(holder.itemView);

                    if (holder.checkMark.isActivated()){
                        holder.checkMark.setActivated(false);
                        listToShow.remove(listToShow.indexOf(""+click));
                    } else {
                        holder.checkMark.setActivated(true);
                        listToShow.add(""+click);
                    }
                }
            });

            holder.selectGurbaniName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int click = gurbaniSelectView.getChildAdapterPosition(holder.itemView);

                    if (holder.checkMark.isActivated()){
                        holder.checkMark.setActivated(false);
                        listToShow.remove(listToShow.indexOf(""+click));
                    } else {
                        holder.checkMark.setActivated(true);
                        listToShow.add(""+click);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return BaniNamesEnglish.length+1;
        }

        public class ListViewHolder extends RecyclerView.ViewHolder{
            View selectGurbaniCell, selectGurbaniEmptyCell;
            ImageView checkMark;
            TextView selectGurbaniName;
            public ListViewHolder(@NonNull View itemView) {
                super(itemView);
                selectGurbaniEmptyCell = itemView.findViewById(R.id.selectGurbaniEmptyCell);
                selectGurbaniCell = itemView.findViewById(R.id.selectGurbaniCell);
                selectGurbaniName = itemView.findViewById(R.id.selectGurbaniName);
                checkMark = itemView.findViewById(R.id.checkMark);
            }
        }
    }
}