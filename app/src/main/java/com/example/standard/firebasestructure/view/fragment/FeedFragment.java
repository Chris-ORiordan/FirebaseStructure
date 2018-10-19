package com.example.standard.firebasestructure.view.fragment;

import android.arch.lifecycle.*;
import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.*;
import android.support.v7.widget.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.example.standard.firebasestructure.*;
import com.example.standard.firebasestructure.R;
import com.example.standard.firebasestructure.model.*;
import com.example.standard.firebasestructure.model.adapters.*;
import com.example.standard.firebasestructure.view.*;
import com.example.standard.firebasestructure.viewmodel.*;

import java.util.List;

public class FeedFragment extends Fragment {

    private static final String TAG = FeedFragment.class.getSimpleName();

    private RecyclerView recyclerViewOutGoers;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager recyclerLayoutManager;

    private UserViewModel userViewModel;
    private OutGoerViewModel outGoerViewModel;

    public FeedFragment() {
        // Required empty public constructor
    }

    public static FeedFragment newInstance() {
        return new FeedFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_feed, container, false);

        ((MainActivity) getActivity()).setActionBarTitle(R.string.feed);
        ((MainActivity) getActivity()).setDisplayHomeAsUpEnabled(false);

        recyclerViewOutGoers = fragmentView.findViewById(R.id.recyclerViewOutGoers);
        recyclerViewOutGoers.setHasFixedSize(true);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        outGoerViewModel = ViewModelProviders.of(this).get(OutGoerViewModel.class);

        if(outGoerViewModel != null){
            LiveData<List<OutGoer>> outGoerLiveData = outGoerViewModel.getOutGoerLiveData(MainApplication.getCurrentUser().getUserId());

            outGoerLiveData.observe(FeedFragment.this, new Observer<List<OutGoer>>() {
                @Override
                public void onChanged(@Nullable List<OutGoer> outGoers) {
                    outGoers = Utils.sortOutGoersByTime(outGoers);
                    recyclerLayoutManager = new LinearLayoutManager(getContext());
                    recyclerViewOutGoers.setLayoutManager(recyclerLayoutManager);
                    recyclerAdapter = new OutGoerRecyclerAdapter(getContext(), outGoers, new OnItemClickListener() {
                        @Override
                        public void onItemClick(Venue venue) {
                            //null
                        }

                        @Override
                        public void onItemClick(User friend) {
                            //null
                        }

                        @Override
                        public void onItemClick(OutGoer outGoer) {
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("outGoer", outGoer);
                            FeedDetailFragment feedDetailFragment= FeedDetailFragment.newInstance();
                            feedDetailFragment.setArguments(bundle);
                            fragmentTransaction.replace(R.id.framelayoutContainer, feedDetailFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    });
                    recyclerViewOutGoers.setAdapter(recyclerAdapter);
                }
            });
        }
        return fragmentView;
    }
}
