package com.example.icecream.new_job;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.icecream.new_job.entity.PlaceInfo;
import com.example.icecream.new_job.entity.PlaceRequest;
import com.example.icecream.new_job.entity.PlaceResult;
import com.example.icecream.new_job.swipeRefreshLayoutCustom.SwipeRefreshLayoutBottom;
import com.example.icecream.new_job.util.CustomAsyncTask;
import com.example.icecream.new_job.util.JsonParser;

import java.util.ArrayList;

/**
 * Created by IceCream on 2/10/2018.
 */

public class SubCategoryFragment extends Fragment {

    private PlaceRequest placeRequest;
    private String title;
    private ArrayList<PlaceInfo> listResult;
    private ArrayList<String> listNameResult;
    private PlaceResult placeResult;

    private ListView listCategory;
    private SwipeRefreshLayoutBottom swipeLoadMore;
    private boolean isMore;

    public SubCategoryFragment() {

    }

    @SuppressLint("ValidFragment")
    public SubCategoryFragment(PlaceRequest placeRequest, String title) {
        this.placeRequest = placeRequest;
        this.title = title;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subcategory, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        findView();
        getActivity().setTitle(title);
        listResult = new ArrayList<>();
        listNameResult = new ArrayList<>();
        isMore = true;
        //load result
        getResult();
        // set load more result
        setLoadMore();
        // set click result
        setClickItemList();
    }

    private void findView() {
        listCategory = (ListView) getActivity().findViewById(R.id.list_category);
        swipeLoadMore = (SwipeRefreshLayoutBottom) getActivity().findViewById(R.id.swipe_load_more);
    }

    private void getResult() {
        CustomAsyncTask customAsyncTask = new CustomAsyncTask(new CustomAsyncTask.OnCustomEventListener() {
            @Override
            public void onEvent(String s) {
                try {
                    int oldPos = listNameResult.size();
                    placeResult = JsonParser.getListPlace(s);
                    listResult.addAll(placeResult.getListResult());
                    if (listResult.size() != 0) {
                        listNameResult.clear();
                        for (int i = 0; i < listResult.size(); i++) {
                            listNameResult.add(listResult.get(i).getName());
                        }
                    } else {
                        Toast.makeText(getActivity(), "No result", Toast.LENGTH_SHORT).show();
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listNameResult);
                    listCategory.setAdapter(adapter);
                    if (!"".equals(placeResult.getPagetoken())) {
                        placeRequest.setPagetoken(placeResult.getPagetoken());
                        isMore = true;
                    } else {
                        isMore = false;
                    }
                    if (oldPos > 0) {
                        listCategory.setSelection(oldPos - 1);

                    }
                }catch (Exception e)
                {

                }
            }
        });
        customAsyncTask.execute(placeRequest.getUrlRequest());
    }

    private void setLoadMore() {
        swipeLoadMore.setOnRefreshListener(new SwipeRefreshLayoutBottom.OnRefreshListener() {
            @Override
            public void onRefresh() {
                int pos = listNameResult.size();
                if (isMore) {
                    getResult();
                }
                swipeLoadMore.setRefreshing(false);
            }
        });
    }

    private void setClickItemList()
    {
        listCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String origin = placeRequest.getLocation();
                String destination = String.valueOf(placeResult.getListResult().get(i).getLat())+","+ String.valueOf(placeResult.getListResult().get(i).getLng());
                Intent intent = new Intent(getActivity(),MapsActivity.class);
                intent.putExtra("origin",origin);
                intent.putExtra("destination",destination);
                intent.putExtra("name_location",listNameResult.get(i));
                startActivity(intent);

            }
        });
    }


}
