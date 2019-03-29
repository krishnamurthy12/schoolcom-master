package com.krish.assignment.fragments;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.krish.assignment.R;
import com.krish.assignment.adapters.TabAdapter;
import com.krish.assignment.api_responses.details.Article;
import com.krish.assignment.api_responses.details.DetailedResponse;
import com.krish.assignment.utils.OnResponseListener;
import com.krish.assignment.utils.Utilities;
import com.krish.assignment.utils.WebServices;

import java.util.ArrayList;
import java.util.List;

import static com.krish.assignment.MainActivity.sourceList;
import static com.krish.assignment.utils.Utilities.showSnackBar;
import static com.krish.assignment.utils.Utilities.showToast;
import static com.krish.assignment.utils.Utilities.toggleVisibility;


public class FragmentOne extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnResponseListener {

    List<Article> mList=new ArrayList<>();
    RecyclerView mRecyclerView;
    TabAdapter listAdapter;

    ProgressBar mProgressBar;
    SwipeRefreshLayout mRefreshPage;

    Context mContext;
    View view;
    int SOURCE_POS;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getBundle();
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_one, container, false);
            initializeViews();

        } else {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        return view;

    }
    private void getBundle() {
       Bundle bundle = getArguments();
        if (null != bundle) {
            if (bundle.containsKey("SOURCE_POS")) {
                SOURCE_POS = bundle.getInt("SOURCE_POS");
            }
        }
    }

    private void initializeViews() {

        mProgressBar=view.findViewById(R.id.progressbar_fragment_one);

        mRefreshPage=view.findViewById(R.id.swipeRefreshLayout_fragment_one);

        mRecyclerView=view.findViewById(R.id.vR_recyclerview_fragment_one);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mRefreshPage.setOnRefreshListener(this);

        callGetDetailsAPI();

    }

    public void callGetDetailsAPI() {
        if (Utilities.isConnectedToInternet(mContext)) {
            toggleVisibility(true, mProgressBar);
            WebServices<DetailedResponse> webServices = new WebServices<DetailedResponse>(this);
            webServices.callgetDetails(Utilities.getBaseURL(), WebServices.ApiType.details,sourceList.get(SOURCE_POS).getId());

        } else {
            showToast(mContext, getResources().getString(R.string.err_msg_nointernet));

        }

    }

    @Override
    public void onRefresh() {
        mRefreshPage.setColorSchemeColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.greenlight));
        if(mProgressBar.getVisibility()==View.VISIBLE)
        {
            showSnackBar(mContext,"Wait untill the current process to finish");
        }
        else {
            refreshContent();
        }


    }

    public void refreshContent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callGetDetailsAPI();
                mRefreshPage.setRefreshing(false);

            }
        },3000);
    }

    @Override
    public void onResponse(Object response, WebServices.ApiType URL, boolean isSucces, int code) {
        switch (URL)
        {
            case details:
                toggleVisibility(false, mProgressBar);
                if(isSucces)
                {
                    DetailedResponse exampleResponse= (DetailedResponse) response;
                    if(exampleResponse!=null)
                    {
                        if(exampleResponse.getArticles()!=null)
                        {
                            //Success case
                            mList=exampleResponse.getArticles();

                            listAdapter=new TabAdapter(mContext,mList,mRecyclerView);
                            mRecyclerView.setAdapter(listAdapter);
                            listAdapter.notifyDataSetChanged();
                        }else {
                            //List is null
                            showSnackBar(mContext, "List is empty");
                        }
                    }
                    else {
                        //Null Response
                        showSnackBar(mContext, "Something went wrong please try again");
                    }

                }
                else {
                    //API Call Failed
                    showSnackBar(mContext, "API call failed");
                }
                break;
        }
    }
}
