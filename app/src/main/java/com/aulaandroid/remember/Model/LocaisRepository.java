package com.aulaandroid.remember.Model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class LocaisRepository {
    private static final String CONTATOS_SERVICE_BASE_URL = "https://crudcrud.com";
    private LocaisService localService;
    private MutableLiveData<List<Locais>> locaisResponseMutableLiveData;
    private MutableLiveData<Boolean> salvoSucessoMutableLiveData;
    public LocaisRepository() {
        locaisResponseMutableLiveData = new MutableLiveData<>();
        salvoSucessoMutableLiveData = new MutableLiveData<>();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        localService = new retrofit2.Retrofit.Builder()
                .baseUrl(CONTATOS_SERVICE_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LocaisService.class);
    }
    public void getLocais() {
        localService.getAllLocais()
                .enqueue(new Callback<List<Locais>>() {
                    @Override
                    public void onResponse(Call<List<Locais>> call, Response<List<Locais>> response) {
                        if (response.body() != null) {
                            Log.d("RESPOSTA", "tenho resultato-->"+response.body());
                            locaisResponseMutableLiveData.postValue(response.body());
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Locais>> call, Throwable t) {
                        Log.e("RESPOSTA", "FALHOU->"+t.getMessage());
                        locaisResponseMutableLiveData.postValue(null);
                    }
                });
    }
    public LiveData<List<Locais>> getAllLocais() {
        return locaisResponseMutableLiveData;
    }
    public LiveData<Boolean> getSalvoSucesso() {
        return salvoSucessoMutableLiveData;
    }
    public void salvarLocais(Locais locais){
        localService.salvarLocais(locais)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.body() != null) {
                            Log.d("RESPOSTA", "tenho resultato-->"+response.body());
                            salvoSucessoMutableLiveData.postValue(new Boolean(true));
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("RESPOSTA", "FALHOU->"+t.getMessage());
                        salvoSucessoMutableLiveData.postValue(new Boolean(false));
                    }
                });
    }
    /*public void alterarContato(Locais locais){
        LocaisPut contatoPut = new LocaisPut(locais.getTitulo(),locais.getDescricao(),
                locais.getHoje(), locais.getLatLon());
        localService.alterarContato(locais.getId(),contatoPut)
                .enqueue(new Callback<ResponseBody>() {
                    @Override

                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if (response.body() != null) {
                            Log.d("RESPOSTA", "tenho resultato-->"+response.body());
                            salvoSucessoMutableLiveData.postValue(new Boolean(true));
                        }
                    }
                    @Override

                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                        Log.e("RESPOSTA", "FALHOU->"+t.getMessage());
                        salvoSucessoMutableLiveData.postValue(new Boolean(false));
                    }
                });
    }*/
    public Call<ResponseBody> deletarLocais(Locais locais){
        return localService.deletarLocais(locais.getId());
    }

}
