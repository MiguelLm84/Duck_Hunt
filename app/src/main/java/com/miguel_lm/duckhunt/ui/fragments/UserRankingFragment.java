package com.miguel_lm.duckhunt.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.miguel_lm.duckhunt.R;
import com.miguel_lm.duckhunt.model.User;
import com.miguel_lm.duckhunt.ui.adapter.MyUserRecyclerViewAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserRankingFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    List<User> userList;
    RecyclerView recyclerView;
    MyUserRecyclerViewAdapter adapter;
    FirebaseFirestore db;

    public UserRankingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_ranking_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            db.collection("users").orderBy("ducks", Query.Direction.DESCENDING).limit(10).get()
                    .addOnCompleteListener(task -> {
                        userList = new ArrayList<>();
                        for(DocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                            User userItem = document.toObject(User.class);
                            userList.add(userItem);

                            adapter = new MyUserRecyclerViewAdapter(userList);
                            recyclerView.setAdapter(adapter);
                        }
                    });
        }
        return view;
    }
}