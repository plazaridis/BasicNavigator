package com.threenitas.basicnavigator;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecentFragment extends Fragment {

    public RecentFragment() {
        // Required empty public constructor
    }

    private ListView recentList;
    private View mView;
    private Realm mRealm = Realm.getDefaultInstance();
    private LocInfoAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_recent, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ArrayList<LocInfo> rsList = new ArrayList<>(mRealm.where(LocInfo.class).findAllAsync());

        recentList = (ListView)mView.findViewById(R.id.recentList);
        adapter = new LocInfoAdapter(getContext(), R.layout.adapter_view_layout, rsList);

        recentList.setAdapter(adapter);

        recentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(),rsList.get(position).toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
