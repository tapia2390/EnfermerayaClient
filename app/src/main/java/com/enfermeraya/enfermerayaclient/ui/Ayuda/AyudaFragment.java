package com.enfermeraya.enfermerayaclient.ui.Ayuda;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.enfermeraya.enfermerayaclient.R;
import com.enfermeraya.enfermerayaclient.app.Modelo;
import com.enfermeraya.enfermerayaclient.views.Contactanos;
import com.enfermeraya.enfermerayaclient.views.InfoApp;
import com.enfermeraya.enfermerayaclient.views.PreguntasFrecuetes;
import com.enfermeraya.enfermerayaclient.views.Teminos;
import com.google.firebase.database.annotations.Nullable;


public class AyudaFragment extends Fragment {

    private AyudaViewModel galleryViewModel;
    Modelo modelo = Modelo.getInstance();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(AyudaViewModel.class);
        View root = inflater.inflate(R.layout.fragment_ayuda, container, false);



        final LinearLayout lypreguntas = (LinearLayout) root.findViewById(R.id.lypreguntas);
        final LinearLayout lycontacto = (LinearLayout) root.findViewById(R.id.lycontacto);
        final LinearLayout lycondiciones = (LinearLayout) root.findViewById(R.id.lycondiciones);
        final LinearLayout lyinfo = (LinearLayout) root.findViewById(R.id.lyinfo);

        final ImageView fb = (ImageView) root.findViewById(R.id.fb);
        final ImageView ins = (ImageView) root.findViewById(R.id.ins);
        final ImageView tw = (ImageView) root.findViewById(R.id.tw);
        final ImageView in = (ImageView) root.findViewById(R.id.in);



        lypreguntas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), PreguntasFrecuetes.class);
                startActivity(i);
            }
        });

        lycontacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Contactanos.class);
                startActivity(i);
            }
        });


        lycondiciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Teminos.class);
                startActivity(i);
            }
        });

        lyinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), InfoApp.class);
                startActivity(i);
            }
        });


        //redes
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWebPage(modelo.setting.getUrl_face());
            }
        });

        ins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWebPage(modelo.setting.getUrl_instagra());
            }
        });

        tw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWebPage(modelo.setting.getUrl_twitter());
            }
        });

        in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWebPage(modelo.setting.getUrl_in());
            }
        });

        return root;
    }

    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
