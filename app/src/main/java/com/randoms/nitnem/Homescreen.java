package com.randoms.nitnem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class Homescreen extends Fragment {
    String [] BaniNamesEnglish;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recommendationListView;
    RecommendationListAdapter recommendationListAdapter;
    GradientDrawable headerGradient, normalGradient, customizeGradient;
    ImageView imageView;

    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;

    int imageID, phase;
    String modifierHeader;

    ArrayList<String> morningList, dayList, eveningList, nightList, listToShow, messageList, updatedList, BaniNamesArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BaniNamesEnglish = new String[]{"Japji Sahib", "Jaap Sahib", "Tav Prasad Sawaiye", "Chaupai Sahib", "Anand Sahib", "Rehraas Sahib", "Rakhya Shabad", "Kirtan Sohila", "Shabad Hazaare", "Barah Maha Maaj",
                "Shabad Hazaare Paatshahi 10", "Swaiye Deenan", "Aarti", "Ardaas", "Sukhmani Sahib", "Asa Di Vaar", "Dakhnee Oankaar", "Sidh Gosat", "Baavan Akhree", "Jaitsari Di Vaar",
                "Ramkali Ki Vaar", "Basant Ki Vaar", "Barah Maha Tukhari", "Laavan", "Salok Mehal 9", "Kuchaji", "Suchaji", "Gunvanti", "Raag Maala", "Akaal Ustat Chaupai", "Chandi Di Vaar"};

        mPreferences = getActivity().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();

        imageID = mPreferences.getInt(getString(R.string.imageID), R.drawable.mondaymorning);
        phase = mPreferences.getInt(getString(R.string.phase), 0);

        morningList = new ArrayList<>();
        dayList = new ArrayList<>();
        eveningList = new ArrayList<>();
        nightList = new ArrayList<>();
        listToShow = new ArrayList<>();
        messageList = new ArrayList<>();

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
        json = mPreferences.getString(getString(R.string.messageList), "[\"A Pleasant Morning\",\"A Beautiful Day\",\"A Delightful Evening\",\"A Peaceful Night\"]");
        messageList = gson.fromJson(json, type);

        float radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getActivity().getResources().getDisplayMetrics());

        headerGradient = new GradientDrawable();
        normalGradient = new GradientDrawable();
        customizeGradient = new GradientDrawable();

        headerGradient.setShape(GradientDrawable.RECTANGLE);
        headerGradient.setColor(Color.argb(240,255,255, 255));

        normalGradient.setShape(GradientDrawable.RECTANGLE);
        normalGradient.setColor(Color.argb(240,255,255, 255));

        customizeGradient.setShape(GradientDrawable.RECTANGLE);
        customizeGradient.setColor(Color.argb(240,255,255, 255));

        headerGradient.setCornerRadii(new float[]{radius, radius, radius, radius, 0, 0, 0, 0});
        customizeGradient.setCornerRadii(new float[]{0, 0, 0, 0, radius, radius, radius, radius});

        View view = inflater.inflate(R.layout.homescreen, container, false);

        imageView = view.findViewById(R.id.imageView);
        recommendationListView = view.findViewById(R.id.recommendationsListView);

        recommendationListAdapter = new RecommendationListAdapter();
        linearLayoutManager = new LinearLayoutManager(getActivity());

        switch (phase){
            case 0:
                listToShow = morningList;
                modifierHeader = "Morning Recommendations";
                break;
            case 1:
                listToShow = dayList;
                modifierHeader = "Day Recommendations";
                break;
            case 2:
                listToShow = eveningList;
                modifierHeader = "Evening Recommendations";
                break;
            case 3:
                listToShow = nightList;
                modifierHeader = "Night Recommendations";
                break;
            default:
        }

        recommendationListView.setLayoutManager(linearLayoutManager);
        recommendationListView.setAdapter(recommendationListAdapter);

        BaniNamesArrayList = new ArrayList<String>(Arrays.asList(BaniNamesEnglish));

        new Thread(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageDrawable(getActivity().getDrawable(imageID));
                    }
                });
            }
        }).start();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences mPreferences = getActivity().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        String json;

        switch (phase){
            case 0:
                json = mPreferences.getString(getString(R.string.morningList), "[0,1,2,3,4,15]");
                updatedList = gson.fromJson(json, type);
                break;
            case 1:
                json = mPreferences.getString(getString(R.string.dayList), "[8,14,17]");
                updatedList = gson.fromJson(json, type);
                break;
            case 2:
                json = mPreferences.getString(getString(R.string.eveningList), "[5,12]");
                updatedList = gson.fromJson(json, type);
                break;
            case 3:
                json = mPreferences.getString(getString(R.string.nightList), "[7]");
                updatedList = gson.fromJson(json, type);
                break;
            default:
        }

        if (!updatedList.equals(listToShow)){
            listToShow = updatedList;
            recommendationListAdapter.notifyDataSetChanged();
        }
    }

    public class RecommendationListAdapter extends RecyclerView.Adapter<RecommendationListAdapter.ListViewHolder>{
        @NonNull
        @Override
        public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommendations_layout, parent, false);
            return new ListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ListViewHolder holder, final int position) {
            if (position == 0){
                holder.emptyView.setVisibility(View.VISIBLE);
                holder.headerView.setVisibility(View.GONE);
                holder.recommendedView.setVisibility(View.GONE);
                holder.customizeView.setVisibility(View.GONE);

            } else if (position == 1){
                holder.emptyView.setVisibility(View.GONE);
                holder.headerView.setVisibility(View.VISIBLE);
                holder.recommendedView.setVisibility(View.GONE);
                holder.customizeView.setVisibility(View.GONE);

                holder.timeTextView.setText(messageList.get(phase));

            } else if (position == (listToShow.size()+2)){
                holder.emptyView.setVisibility(View.GONE);
                holder.headerView.setVisibility(View.GONE);
                holder.recommendedView.setVisibility(View.GONE);
                holder.customizeView.setVisibility(View.VISIBLE);

            } else {
                holder.emptyView.setVisibility(View.GONE);
                holder.headerView.setVisibility(View.GONE);
                holder.recommendedView.setVisibility(View.VISIBLE);
                holder.recommendedTextView.setText(BaniNamesEnglish[Integer.valueOf(listToShow.get(position-2))]);
                holder.customizeView.setVisibility(View.GONE);
            }

            holder.recommendedView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), SundarGutkaActivity.class);
                    intent.putExtra("theme", mPreferences.getInt(getString(R.string.mode), R.style.Theme0));
                    intent.putExtra("loadBaniIndex", Integer.valueOf(listToShow.get(position-2)));
                    startActivity(intent);
                }
            });

            holder.customizeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), ModifyActivity.class);
                    intent.putExtra("theme", mPreferences.getInt(getString(R.string.mode), R.style.Theme0));
                    intent.putExtra("modifierHeader", modifierHeader);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return listToShow.size() + 3;
        }

        public class ListViewHolder extends RecyclerView.ViewHolder{
            View emptyView, headerView, recommendedView, customizeView;
            TextView recommendedTextView, timeTextView;
            Button customizeButton;

            public ListViewHolder(@NonNull View itemView) {
                super(itemView);
                emptyView = itemView.findViewById(R.id.emptyView);
                headerView = itemView.findViewById(R.id.headerView);
                headerView.setBackground(headerGradient);
                recommendedView = itemView.findViewById(R.id.recommendedView);
                recommendedView.setBackground(normalGradient);
                recommendedTextView = itemView.findViewById(R.id.recommendedTextView);
                customizeView = itemView.findViewById(R.id.customizeView);
                customizeView.setBackground(customizeGradient);
                customizeButton = itemView.findViewById(R.id.customizeButton);
                timeTextView = itemView.findViewById(R.id.timeTextView);
            }
        }
    }
}
