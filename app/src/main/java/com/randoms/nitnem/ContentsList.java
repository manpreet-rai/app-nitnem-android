package com.randoms.nitnem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ContentsList extends Fragment {
    String [] BaniNamesEnglish;
    LinearLayoutManager linearLayoutManager;
    RecyclerView contentsListView;
    ContentsListAdapter contentsListAdapter;

    ArrayList<Integer> unfinishedVisibility, updatedUnfinished;
    Gson gson;
    Type type;
    String json;

    SharedPreferences mPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BaniNamesEnglish = new String[]{"Japji Sahib", "Jaap Sahib", "Tav Prasad Sawaiye", "Chaupai Sahib", "Anand Sahib", "Rehraas Sahib", "Rakhya Shabad", "Kirtan Sohila", "Shabad Hazaare", "Barah Maha Maaj",
                "Shabad Hazaare Paatshahi 10", "Swaiye Deenan", "Aarti", "Ardaas", "Sukhmani Sahib", "Asa Di Vaar", "Dakhnee Oankaar", "Sidh Gosat", "Baavan Akhree", "Jaitsari Di Vaar",
                "Ramkali Ki Vaar", "Basant Ki Vaar", "Barah Maha Tukhari", "Laavan", "Salok Mehal 9", "Kuchaji", "Suchaji", "Gunvanti", "Raag Maala", "Akaal Ustat Chaupai", "Chandi Di Vaar"};

        View view = inflater.inflate(R.layout.contentslist, container, false);

        contentsListView = view.findViewById(R.id.contentsListView);
        contentsListAdapter = new ContentsListAdapter();
        linearLayoutManager = new LinearLayoutManager(getActivity());

        new Thread(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        contentsListView.setLayoutManager(linearLayoutManager);
                        mPreferences = getActivity().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
                        unfinishedVisibility = new ArrayList<Integer>();
                        updatedUnfinished = new ArrayList<Integer>();

                        gson= new Gson();
                        type = new TypeToken<ArrayList<Integer>>() {}.getType();
                        json = mPreferences.getString(getString(R.string.unfinishedVisibility), "[4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4]");
                        unfinishedVisibility = gson.fromJson(json, type);
                        contentsListView.setAdapter(contentsListAdapter);
                    }
                });
            }
        }).start();

        return view;
    }

    public class ContentsListAdapter extends RecyclerView.Adapter<ContentsListAdapter.ListViewHolder>{
        @NonNull
        @Override
        public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contents_layout, parent, false);
            return new ListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
            holder.contentIndexTextView.setText(""+(position+1));
            holder.contentTextView.setText(BaniNamesEnglish[position]);
            holder.unfinishedIndicatorView.setVisibility(unfinishedVisibility.get(position));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = contentsListView.getChildAdapterPosition(view);
                    Intent intent = new Intent(getActivity(), SundarGutkaActivity.class);
                    SharedPreferences mPreferences = getActivity().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
                    intent.putExtra("theme", mPreferences.getInt(getString(R.string.mode), R.style.Theme0));
                    intent.putExtra("loadBaniIndex", pos);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return BaniNamesEnglish.length;
        }

        public class ListViewHolder extends RecyclerView.ViewHolder{
            View contentsView, unfinishedIndicatorView;
            TextView contentIndexTextView, contentTextView;
            public ListViewHolder(@NonNull View itemView) {
                super(itemView);
                contentsView = itemView.findViewById(R.id.contentsView);
                contentIndexTextView = itemView.findViewById(R.id.contentIndexTextView);
                contentTextView = itemView.findViewById(R.id.contentTextView);
                unfinishedIndicatorView = itemView.findViewById(R.id.unfinishedIndicatorView);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences mPreferences = getActivity().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Integer>>() {}.getType();

        unfinishedVisibility = new ArrayList<Integer>();
        updatedUnfinished = new ArrayList<Integer>();

        String json = mPreferences.getString(getString(R.string.unfinishedVisibility), "[4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4]");
        updatedUnfinished = gson.fromJson(json, type);
        if (!unfinishedVisibility.equals(updatedUnfinished)){
            unfinishedVisibility = updatedUnfinished;
            contentsListAdapter.notifyDataSetChanged();
        }
    }
}
