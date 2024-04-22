package com.randoms.nitnem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class QuickList extends Fragment {
    String [] BaniNamesEnglish;
    RecyclerView quickListView;
    QuickListAdapter quickListAdapter;
    LinearLayoutManager linearLayoutManager;
    Button customizeButton;

    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;

    ArrayList<String> quickListArray, updatedList;
    ArrayList<Integer> unfinishedVisibility, updatedUnfinished;

    Gson gson2;
    Type type2;
    String json2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BaniNamesEnglish = new String[]{"Japji Sahib", "Jaap Sahib", "Tav Prasad Sawaiye", "Chaupai Sahib", "Anand Sahib", "Rehraas Sahib", "Rakhya Shabad", "Kirtan Sohila", "Shabad Hazaare", "Barah Maha Maaj",
                "Shabad Hazaare Paatshahi 10", "Swaiye Deenan", "Aarti", "Ardaas", "Sukhmani Sahib", "Asa Di Vaar", "Dakhnee Oankaar", "Sidh Gosat", "Baavan Akhree", "Jaitsari Di Vaar",
                "Ramkali Ki Vaar", "Basant Ki Vaar", "Barah Maha Tukhari", "Laavan", "Salok Mehal 9", "Kuchaji", "Suchaji", "Gunvanti", "Raag Maala", "Akaal Ustat Chaupai", "Chandi Di Vaar"};

        View view = inflater.inflate(R.layout.quicklist, container, false);
        mPreferences = getActivity().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();

        quickListArray = new ArrayList<>();
        updatedList = new ArrayList<>();

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        String json = mPreferences.getString(getString(R.string.quickListArray), "[]");
        quickListArray = gson.fromJson(json, type);

        quickListView = view.findViewById(R.id.quickListView);
        quickListAdapter = new QuickListAdapter();
        linearLayoutManager = new LinearLayoutManager(getActivity());

        customizeButton = view.findViewById(R.id.customizeButton);

        customizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), QuickListModifyActivity.class);
                intent.putExtra("theme", mPreferences.getInt(getString(R.string.mode), R.style.Theme0));
                intent.putExtra("modifierHeader", "Quick List");
                startActivity(intent);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        quickListView.setLayoutManager(linearLayoutManager);
                        mPreferences = getActivity().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
                        unfinishedVisibility = new ArrayList<Integer>();
                        updatedUnfinished = new ArrayList<Integer>();

                        gson2= new Gson();
                        type2 = new TypeToken<ArrayList<Integer>>() {}.getType();
                        json2 = mPreferences.getString(getString(R.string.unfinishedVisibility), "[4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4]");
                        unfinishedVisibility = gson2.fromJson(json2, type2);
                        quickListView.setAdapter(quickListAdapter);
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
        String json = mPreferences.getString(getString(R.string.quickListArray), "[]");
        updatedList = gson.fromJson(json, type);

        if (!updatedList.equals(quickListArray) && quickListArray!=null){
            quickListArray = updatedList;
            quickListAdapter.notifyDataSetChanged();
        }

        Gson gson2= new Gson();
        Type type2 = new TypeToken<ArrayList<Integer>>() {}.getType();
        String json2 = mPreferences.getString(getString(R.string.unfinishedVisibility), "[4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4]");
        updatedUnfinished = gson2.fromJson(json2, type2);

        if (!updatedUnfinished.equals(unfinishedVisibility) && unfinishedVisibility!=null){
            unfinishedVisibility = updatedUnfinished;
            quickListAdapter.notifyDataSetChanged();
        }
    }

    public class QuickListAdapter extends RecyclerView.Adapter<QuickListAdapter.ListViewHolder>{
        @NonNull
        @Override
        public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quick_layout, parent, false);
            return new ListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
            holder.quickIndexTextView.setText(""+(position+1));
            holder.quickTextView.setText(BaniNamesEnglish[Integer.valueOf(quickListArray.get(position))]);

            holder.unfinishedIndicatorView.setVisibility(unfinishedVisibility.get(Integer.valueOf(quickListArray.get(position))));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = quickListView.getChildAdapterPosition(view);
                    Intent intent = new Intent(getActivity(), SundarGutkaActivity.class);
                    intent.putExtra("theme", mPreferences.getInt(getString(R.string.mode), R.style.Theme0));
                    intent.putExtra("loadBaniIndex", Integer.valueOf(quickListArray.get(pos)));
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return quickListArray.size();
        }

        public class ListViewHolder extends RecyclerView.ViewHolder{
            View quickView, unfinishedIndicatorView;
            TextView quickIndexTextView, quickTextView;
            public ListViewHolder(@NonNull View itemView) {
                super(itemView);
                quickView = itemView.findViewById(R.id.quickView);
                quickIndexTextView = itemView.findViewById(R.id.quickIndexTextView);
                quickTextView = itemView.findViewById(R.id.quickTextView);
                unfinishedIndicatorView = itemView.findViewById(R.id.unfinishedIndicatorView);
            }
        }
    }
}
