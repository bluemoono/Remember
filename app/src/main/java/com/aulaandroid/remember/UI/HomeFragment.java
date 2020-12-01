package com.aulaandroid.remember.UI;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aulaandroid.remember.Model.Locais;
import com.aulaandroid.remember.Model.LocaisViewModel;
import com.aulaandroid.remember.R;

import java.util.List;

public class HomeFragment extends Fragment {
    private LocaisViewModel locaisViewModel;
    private List<Locais> locais;
    private LocaisAdapter adapter;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomeFragment() {

    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        adapter = new LocaisAdapter();
        locaisViewModel = new ViewModelProvider(this).get(LocaisViewModel.class);
        locaisViewModel.getLocaisResponseLiveData().observe(this, new Observer<List<Locais>>() {
            @Override
            public void onChanged(List<Locais> locaisList) {
                if (locaisList != null) {
                    adapter.setResults(locaisList);
                }
            }
        });
        adapter.setOnItemClickListener(new LocaisAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position, Locais locais) {
//                replaceFragment(R.id.frameLayout,
//                        LocaisFragment.newInstance(false, locais),
//                        LocaisFragment.LOCAIS_FRAGMENT_TAG,
//                        "contato_click");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewContatos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }
    @Override
    public void onResume(){
        super.onResume();
        locaisViewModel.getLocais();
    }
    protected void replaceFragment(@IdRes int containerViewId,
                                   @NonNull Fragment fragment,
                                   @NonNull String fragmentTag,
                                   @Nullable String backStackStateName) {

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(containerViewId, fragment, fragmentTag)
                .addToBackStack(backStackStateName)
                .commit();
    }
}