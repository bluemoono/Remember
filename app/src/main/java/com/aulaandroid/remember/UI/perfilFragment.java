package com.aulaandroid.remember.UI;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aulaandroid.remember.Model.Usuario;
import com.aulaandroid.remember.Model.UsuarioViewModel;
import com.aulaandroid.remember.R;
import com.orhanobut.hawk.Hawk;

public class perfilFragment extends Fragment {
    private UsuarioViewModel usuarioViewModel;
    private Usuario usuarioCorrente;
    private TextView editTextNome;
    private TextView editTextEmail;
    private TextView editTextSenha;
    private Button buttonPerfil;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Boolean mParam1;
    private String mParam2;

    public perfilFragment() {

    }


    public static perfilFragment newInstance(Boolean param1, String param2) {
        perfilFragment fragment = new perfilFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getBoolean(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Hawk.init(getActivity()).build();

        editTextNome = view.findViewById(R.id.nomePerfil);
        editTextEmail = view.findViewById(R.id.emailPerfil);
        editTextSenha = view.findViewById(R.id.senhaPerfil);
        buttonPerfil = view.findViewById(R.id.buttonPerfil);

        buttonPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarCadastro();
            }
        });

        usuarioViewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);
        usuarioViewModel.getUsuario().observe(getActivity(), new Observer<Usuario>() {
            @Override
            public void onChanged(@Nullable final Usuario usuario) {
                updateView(usuario);
            }
        });
    }

    private void updateView(Usuario usuario){
        if(usuario != null && usuario.getId() > 0){
            usuarioCorrente = usuario;
            editTextNome.setText(usuario.getNome());
            editTextEmail.setText(usuario.getEmail());
            editTextSenha.setText(usuario.getSenha());
        }
    }

    public void salvarCadastro() {
        if(usuarioCorrente == null){
            usuarioCorrente = new Usuario();
        }
        usuarioCorrente.setNome(editTextNome.getText().toString());
        usuarioCorrente.setEmail(editTextEmail.getText().toString());
        usuarioCorrente.setSenha(editTextSenha.getText().toString());
        usuarioViewModel.insert(usuarioCorrente);
        Toast.makeText(getActivity(), "Usuário salvo com sucesso", Toast.LENGTH_SHORT).show();

        Hawk.put("tem_cadastro", true);
        if(mParam1){
            getActivity().finish();
        }
    }
}