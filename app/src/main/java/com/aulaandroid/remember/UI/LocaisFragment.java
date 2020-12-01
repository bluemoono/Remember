package com.aulaandroid.remember.UI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aulaandroid.remember.Model.Locais;
import com.aulaandroid.remember.Model.LocaisViewModel;
import com.aulaandroid.remember.Model.Usuario;
import com.aulaandroid.remember.Model.UsuarioViewModel;
import com.aulaandroid.remember.R;
import com.orhanobut.hawk.Hawk;

import java.util.Date;
import java.util.List;

public class LocaisFragment extends Fragment{
    public static final String LOCAIS_FRAGMENT_TAG = "locais_fragment" ;
    private LocaisViewModel locaisViewModel;
    private Locais locaisCorrente;
    private double latitudeAtual;
    private double longitudeAtual;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private static int GPS_PERMISSION_CODE = 1001;
    private EditText tituloLocais;
    private EditText descricaoLocais;
    private EditText hojeLocais;
    private TextView lonlatLocais;
    private Button buttonLocais;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Boolean mParam1;
    private String mParam2;

    public LocaisFragment() {

    }


    public static LocaisFragment newInstance(Boolean param1, String param2) {
        LocaisFragment fragment = new LocaisFragment();
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
        return inflater.inflate(R.layout.fragment_locais, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lonlatLocais = view.findViewById(R.id.lonlatLocais);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                latitudeAtual = lat;
                longitudeAtual = lon;
                lonlatLocais.setText(String.format("Lat: %f, Long: %f", lat, lon));
            }
        };

        tituloLocais = view.findViewById(R.id.tituloLocais);
        descricaoLocais = view.findViewById(R.id.descricaoLocais);
        hojeLocais = view.findViewById(R.id.dataLocais);
        lonlatLocais = view.findViewById(R.id.lonlatLocais);
        buttonLocais = view.findViewById(R.id.buttonLocais);

        buttonLocais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarLocais();
            }
        });

        locaisViewModel = new ViewModelProvider(getActivity()).get(LocaisViewModel.class);
        locaisViewModel.getSalvoSucesso().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable final Boolean sucesso) {
                if (sucesso) {
                    Toast.makeText(getActivity(), "Local salvo com sucesso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Falha ao salvar local", Toast.LENGTH_SHORT).show();
                }
            }
        });

        lonlatLocais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri =
                        Uri.parse(String.format("geo:%f,%f?q=",
                                latitudeAtual, longitudeAtual));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    5000, 10, locationListener);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    GPS_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == GPS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            5000, 10, locationListener);
                }
            } else {
                Toast.makeText(getActivity(), "GPS necessário", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }

    /*private void updateView(Locais locais){
        if(locais != null && locais.getId() > 0){
            locaisCorrente = locais;
            tituloLocais.setText(locais.getTitulo());
            descricaoLocais.setText(locais.getDescricao());
            hojeLocais.setText(locais.getHoje());
        }
    }*/

    public void salvarLocais() {
        if (locaisCorrente == null) {
            locaisCorrente = new Locais();
        }
        locaisCorrente.setTitulo(tituloLocais.getText().toString());
        locaisCorrente.setDescricao(descricaoLocais.getText().toString());
        locaisCorrente.setHoje(hojeLocais.getText().toString());
        locaisCorrente.setLatLon(lonlatLocais.getText().toString());

        locaisViewModel.salvarLocais(locaisCorrente);
        Toast.makeText(getActivity(), "Local salvo com sucesso", Toast.LENGTH_SHORT).show();

    }

}