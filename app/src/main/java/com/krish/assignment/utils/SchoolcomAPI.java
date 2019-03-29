package com.krish.assignment.utils;

import com.krish.assignment.api_responses.details.DetailedResponse;
import com.krish.assignment.api_responses.sources.SourceSResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SchoolcomAPI {

    @GET("sources")
    Call<SourceSResponse> callGetAllSources();

    @GET("top-headlines")
    Call<DetailedResponse> callgetDetails(@Query("sources") String sources);





}
