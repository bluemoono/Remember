package com.aulaandroid.remember.Model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class LocaisViewModel extends AndroidViewModel {
    private LocaisRepository contatoRepository;
    private LiveData<List<Locais>> contatosResponseLiveData;
    private LiveData<Boolean> salvoComSucessoLiveData;
    public LocaisViewModel(@NonNull Application application) {
        super(application);
        Log.d("RESPOSTA","CRIACAO DA VIEWMODEL");
        contatoRepository = new LocaisRepository();
        contatosResponseLiveData = contatoRepository.getAllLocais();
        salvoComSucessoLiveData = contatoRepository.getSalvoSucesso();
    }
    public void getLocais() {
        contatoRepository.getLocais();
    }
    public LiveData<List<Locais>> getLocaisResponseLiveData() {
        return contatosResponseLiveData;
    }
    public LiveData<Boolean> getSalvoSucesso() {
        return salvoComSucessoLiveData;
    }
    public void salvarLocais(Locais locais){
        contatoRepository.salvarLocais(locais);
    }

    public void insert(Locais locaisCorrente) {
    }

    /*public void alterarLocais(Locais locais){
        contatoRepository.alterarContato(contato);
    }*/
}
