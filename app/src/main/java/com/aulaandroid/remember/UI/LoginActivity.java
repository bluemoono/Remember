package com.aulaandroid.remember.UI;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aulaandroid.remember.Model.Usuario;
import com.aulaandroid.remember.Model.UsuarioViewModel;
import com.aulaandroid.remember.R;
import com.orhanobut.hawk.Hawk;

public class LoginActivity extends AppCompatActivity {
    private UsuarioViewModel usuarioViewModel;
    private Usuario usuarioCorrente;
    private TextView editTextEmail;
    private TextView editTextSenha;

    private TextView cadastroLogin;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Hawk.init(this).build();

        editTextEmail = findViewById(R.id.emailLogin);
        editTextSenha = findViewById(R.id.senhaLogin);
        cadastroLogin =findViewById(R.id.cadastroLogin);
        btnLogin = findViewById(R.id.btnLogin);

        usuarioViewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);
        usuarioViewModel.getUsuario().observe(this, new Observer<Usuario>() {
            @Override
            public void onChanged(@Nullable final Usuario usuario) {
                updateUsuario(usuario);
            }
        });
    }

    private void updateUsuario(Usuario usuario){
        usuarioCorrente = usuario;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Hawk.contains("tem_cadastro")){
            if(Hawk.get("tem_cadastro")){
                habilitarLogin();
            } else{
                desabilitarLogin();
            }
        }else{
            desabilitarLogin();
        }
    }

    public void habilitarLogin(){
        cadastroLogin.setVisibility(View.GONE);
        btnLogin.setEnabled(true);
        btnLogin.setBackgroundColor(getColor(R.color.purple_200));
    }

    public void desabilitarLogin(){
    cadastroLogin.setVisibility(View.VISIBLE);
    btnLogin.setEnabled(false);
    btnLogin.setBackgroundColor(getColor(R.color.gray));

    }

    public void cadastro(View view) {
        Intent cadastro = new Intent(this, CadastroActivity.class);
        startActivity(cadastro);
    }

    public void login(View view) {
        if(usuarioCorrente != null){
            if(usuarioCorrente.getEmail().equalsIgnoreCase(editTextEmail.getText().toString())
            && usuarioCorrente.getSenha().equals(editTextSenha.getText().toString())){
                Intent login = new Intent(this, MainActivity.class);
                startActivity(login);
            } else {
                Toast.makeText(this, "Usuário ou senha incorreta", Toast.LENGTH_SHORT).show();
            }
        }
    }
}