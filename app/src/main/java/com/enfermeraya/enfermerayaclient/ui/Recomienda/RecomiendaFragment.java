package com.enfermeraya.enfermerayaclient.ui.Recomienda;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.enfermeraya.enfermerayaclient.R;
import com.enfermeraya.enfermerayaclient.app.Modelo;
import com.enfermeraya.enfermerayaclient.comandos.ComandoPerfil;
import com.enfermeraya.enfermerayaclient.customfonts.MyTextView_Poppins_Medium;


public class RecomiendaFragment extends Fragment  implements ComandoPerfil.OnPerfilChangeListener{

    ComandoPerfil comandoPerfil;

    Modelo modelo = Modelo.getInstance();
    private RecomiendaViewModel galleryViewModel;
    MyTextView_Poppins_Medium compartir;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(RecomiendaViewModel.class);
        View root = inflater.inflate(R.layout.fragment_recomienda, container, false);
        // final TextView textView = root.findViewById(R.id.text_gallery);
        compartir = (MyTextView_Poppins_Medium) root.findViewById(R.id.compartir);

        comandoPerfil =  new ComandoPerfil(this);
        comandoPerfil.getUsuario();

        compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String cod =  modelo.usuario.getCodigo();



                String texto = modelo.setting.getMsmcompartir();
                String[] parts = texto.split("_codigo_");
                String part1 = parts[0];


                String s = modelo.setting.getUrlcompartir()+" \n"+parts[0]+" "+ cod + " "+parts[1];


                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "\n" +
                        "Compartir");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, s);
                startActivity(Intent.createChooser(sharingIntent, "Compartir información a través de"));

            }
        });

        return root;
    }

    @Override
    public void cargoUSuario() {
        String cod = modelo.usuario.getCodigo();

    }

    @Override
    public void setUsuarioListener() {

    }

    @Override
    public void errorSetUsuario() {

    }
}
