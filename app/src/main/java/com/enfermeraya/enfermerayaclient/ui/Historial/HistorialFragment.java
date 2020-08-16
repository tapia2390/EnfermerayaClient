package com.enfermeraya.enfermerayaclient.ui.Historial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.enfermeraya.enfermerayaclient.R;
import com.enfermeraya.enfermerayaclient.adapter.HistorialAdapter;
import com.enfermeraya.enfermerayaclient.adapter.ServicioAdapter2;
import com.enfermeraya.enfermerayaclient.app.Modelo;
import com.enfermeraya.enfermerayaclient.clases.Historial;
import com.enfermeraya.enfermerayaclient.clases.Servicios;
import com.enfermeraya.enfermerayaclient.comandos.ComandoHistorial;
import com.enfermeraya.enfermerayaclient.comandos.ComandoSercicio;
import com.enfermeraya.enfermerayaclient.models.utility.Utility;
import com.enfermeraya.enfermerayaclient.ui.MisServicios.MisServiciosViewModel;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class HistorialFragment extends Fragment implements ComandoHistorial.OnHistorialChangeListener {

    private HistorialViewModel galleryViewModel;
    Utility utility;
    ComandoHistorial comandoHistorial;
    HistorialAdapter historialAdapter;
    List<Historial> hisList = new ArrayList<>();
    Modelo modelo = Modelo.getInstance();
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(HistorialViewModel.class);
        View root = inflater.inflate(R.layout.fragment_historial, container, false);



        final SearchView search2 = (SearchView) root.findViewById(R.id.search_view);
        recyclerView = root.findViewById(R.id.recycler_view2);
        utility = new Utility();
        comandoHistorial = new ComandoHistorial(this);

        if (utility.estado(getActivity())) {
            comandoHistorial.getListaHistorico();


        }else{
            alerta("Sin Internet","Valide la conexión a internet");
        }



        search2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String queryString) {


                historialAdapter.getFilter().filter(queryString);


                return false;
            }

            @Override
            public boolean onQueryTextChange(String queryString) {

                historialAdapter.getFilter().filter(queryString);

                return false;
            }
        });

        return root;
    }


    public void alerta(String titulo, String descripcion){
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(titulo)
                .setContentText(descripcion)
                .setConfirmText("Aceptar")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        // reuse previous dialog instance

                        sDialog.hide();
                    }
                })
                .show();
    }


    @Override
    public void errorHistorial() {

    }

    @Override
    public void okHistorial() {

        hisList = modelo.listHistorial;
        historialAdapter = new HistorialAdapter(getActivity(), hisList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(historialAdapter);
    }
}
