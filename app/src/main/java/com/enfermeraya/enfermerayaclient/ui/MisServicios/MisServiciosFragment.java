package com.enfermeraya.enfermerayaclient.ui.MisServicios;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.enfermeraya.enfermerayaclient.R;
import com.enfermeraya.enfermerayaclient.adapter.ServicioAdapter2;
import com.enfermeraya.enfermerayaclient.app.Modelo;
import com.enfermeraya.enfermerayaclient.clases.Servicios;
import com.enfermeraya.enfermerayaclient.comandos.ComandoSercicio;
import com.enfermeraya.enfermerayaclient.models.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MisServiciosFragment extends Fragment implements ComandoSercicio.OnSercicioChangeListener{

    Utility utility;
    private MisServiciosViewModel slideshowViewModel;
    ComandoSercicio comandoSercicio;
    ServicioAdapter2 servicioAdapter;
    List<Servicios> serList = new ArrayList<>();
    Modelo modelo = Modelo.getInstance();
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel = ViewModelProviders.of(this).get(MisServiciosViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mis_servicios, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);


        final SearchView search2 = (SearchView) root.findViewById(R.id.search_view);
        recyclerView = root.findViewById(R.id.recycler_view2);
        utility = new Utility();
        comandoSercicio = new ComandoSercicio(this);

        modelo.vistaservice =  1;
        if (utility.estado(getActivity())) {
            comandoSercicio.getListServicio();


        }else{
            alerta("Sin Internet","Valide la conexión a internet");
        }



        search2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String queryString) {


                servicioAdapter.getFilter().filter(queryString);


                return false;
            }

            @Override
            public boolean onQueryTextChange(String queryString) {

                servicioAdapter.getFilter().filter(queryString);

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
    public void getTipoServicio() {

    }

    @Override
    public void getServicio() {
        serList = modelo.listServicios;
        servicioAdapter = new ServicioAdapter2(getActivity(), serList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(servicioAdapter);
    }

    @Override
    public void cargoServicio() {

    }

    @Override
    public void errorServicio() {

    }

    @Override
    public void actualizarFavorito() {

    }
}
