package com.aulaandroid.remember.Model;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface LocaisService {
    @GET("/api/78b414ac12bb48538c406b4636d39bd0/locais")
    Call<List<Locais>> getAllLocais();
    @POST("/api/78b414ac12bb48538c406b4636d39bd0/locais")
    Call<ResponseBody> salvarLocais(
            @Body
                    Locais locais);
    /*@PUT("/api/78b414ac12bb48538c406b4636d39bd0/locais/{id}")
    Call<ResponseBody> alterarLocais(
            @Path("id") String id,
            @Body LocaisPut contatoPut);*/
    @DELETE("/api/378b414ac12bb48538c406b4636d39bd0/locais/{id}")
    Call<ResponseBody> deletarLocais(
            @Path("id") String id);
}
