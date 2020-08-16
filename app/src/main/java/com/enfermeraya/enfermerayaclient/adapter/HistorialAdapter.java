package com.enfermeraya.enfermerayaclient.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.enfermeraya.enfermerayaclient.R;
import com.enfermeraya.enfermerayaclient.app.Modelo;
import com.enfermeraya.enfermerayaclient.clases.Historial;
import com.enfermeraya.enfermerayaclient.views.DetalleHistorial;

import java.util.ArrayList;
import java.util.List;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.HistorialViewHolder> implements Filterable{

    private Context context;
    private List<Historial> nameList;
    private List<Historial> filteredNameList;
    Modelo modelo = Modelo.getInstance();

    public HistorialAdapter(Context context, List<Historial> nameList) {
        super();
        this.context = context;
        this.nameList = nameList;
        this.filteredNameList = nameList;


    }

    @NonNull
    @Override
    public HistorialAdapter.HistorialViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardviewhistorial, viewGroup, false);
        return new HistorialAdapter.HistorialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistorialAdapter.HistorialViewHolder holder, final int position) {
        String fecha = filteredNameList.get(position).getFecha();
        String servicio = filteredNameList.get(position).getTipoServicio();
        String nombre = filteredNameList.get(position).getNombre();
        String direccion = filteredNameList.get(position).getDireccion();
        String estado = filteredNameList.get(position).getEstado();
        double calificacion = filteredNameList.get(position).getCalificaion();

        holder.txtfecha.setText(fecha);
        holder.txtservicio.setText(servicio);
        holder.txtnombre.setText(nombre);
        holder.txtdireccion.setText(direccion);

        if(calificacion != 0){
            holder.txt_pendiente.setVisibility(View.GONE);
            holder.layoutcalificacion.setVisibility(View.VISIBLE);
            holder.txt_calificacion.setText(""+calificacion);
        }

        holder.btnestado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                modelo.historial =  filteredNameList.get(position);
                Intent i = new Intent(context, DetalleHistorial.class);
                i.putExtra("historial","historial");
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return filteredNameList.size();
    }

    /**
     * <p>Returns a filter that can be used to constrain data with a filtering
     * pattern.</p>
     *
     * <p>This method is usually implemented by {@link RecyclerView.Adapter}
     * classes.</p>
     *
     * @return a filter used to constrain data
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSequenceString = constraint.toString();
                if (charSequenceString.isEmpty()) {
                    filteredNameList = nameList;
                } else {
                    List<Historial> filteredList = new ArrayList<>();
                    for (Historial name : nameList) {
                        if (name.getDireccion().toLowerCase().contains(charSequenceString.toLowerCase())) {
                            filteredList.add(name);
                        }
                        filteredNameList = filteredList;
                    }

                }
                FilterResults results = new FilterResults();
                results.values = filteredNameList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredNameList = (List<Historial>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    class HistorialViewHolder extends RecyclerView.ViewHolder {

        private TextView txtfecha;
        private TextView txtservicio;
        private TextView txtnombre;
        private TextView txtdireccion;
        private Button btnestado;
        private Button txt_pendiente;
        private LinearLayout layoutcalificacion;
        private TextView txt_calificacion;


        HistorialViewHolder(@NonNull View itemView) {
            super(itemView);
            txtfecha = itemView.findViewById(R.id.txtfecha);
            txtservicio = itemView.findViewById(R.id.txtservicio);
            txtnombre = itemView.findViewById(R.id.txtnombre);
            txtdireccion = itemView.findViewById(R.id.txtdireccion);
            btnestado = itemView.findViewById(R.id.btnestado);
            txt_pendiente = itemView.findViewById(R.id.txt_pendiente);
            layoutcalificacion = itemView.findViewById(R.id.layoutcalificacion);
            txt_calificacion = itemView.findViewById(R.id.txt_calificacion);

        }
    }

}