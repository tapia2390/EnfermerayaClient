package com.enfermeraya.enfermerayaclient.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.enfermeraya.enfermerayaclient.R;
import com.enfermeraya.enfermerayaclient.app.Modelo;
import com.enfermeraya.enfermerayaclient.clases.Servicios;
import com.enfermeraya.enfermerayaclient.comandos.ComandoSercicio;
import com.enfermeraya.enfermerayaclient.notificacion.APIService;
import com.enfermeraya.enfermerayaclient.notificacion.Client;
import com.enfermeraya.enfermerayaclient.notificacion.Data;
import com.enfermeraya.enfermerayaclient.notificacion.MyResponse;
import com.enfermeraya.enfermerayaclient.notificacion.NotificationSender;
import com.enfermeraya.enfermerayaclient.views.DetalleHistorial;
import com.enfermeraya.enfermerayaclient.views.MainActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServicioAdapter2 extends RecyclerView.Adapter<ServicioAdapter2.ServicioViewHolder> implements Filterable, ComandoSercicio.OnSercicioChangeListener {

    private Context context;
    private List<Servicios> nameList;
    private List<Servicios> filteredNameList;
    ComandoSercicio comandoSercicio;
    Modelo modelo = Modelo.getInstance();

    private APIService apiService;

    public ServicioAdapter2(Context context, List<Servicios> nameList) {
        super();
        this.context = context;
        this.nameList = nameList;
        this.filteredNameList = nameList;

        comandoSercicio = new ComandoSercicio(this);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);


    }

    @NonNull
    @Override
    public ServicioViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_service, viewGroup, false);
        return new ServicioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicioViewHolder holder, final int position) {
        String direccion = filteredNameList.get(position).getDireccion();
        String titlo = filteredNameList.get(position).getInformacion();
        String fecha = filteredNameList.get(position).getFecha();
        holder.text_dir.setText(direccion);
        holder.text_nombre.setText(titlo);
        holder.text_fecha.setText(fecha);

        if(filteredNameList.get(position).getEstado().equals("false")){

            if(modelo.vistaservice ==0){
                holder.btnaceptservice.setText("Aceptar");
                holder.btnaceptservice.setVisibility(View.VISIBLE);
                holder.txt_estado.setVisibility(View.GONE);
            }else{
                holder.btnaceptservice.setVisibility(View.GONE);
                holder.txt_estado.setText("Pendiente");
                holder.txt_estado.setVisibility(View.VISIBLE);
            }

        }
        else if(filteredNameList.get(position).getEstado().equals("Aceptado")){

            if(modelo.vistaservice ==0){
                holder.btnaceptservice.setText("Detalle");
                holder.btnaceptservice.setVisibility(View.VISIBLE);
                holder.txt_estado.setVisibility(View.GONE);
            }else{
                holder.btnaceptservice.setVisibility(View.GONE);
                holder.txt_estado.setText("Detalle");
                holder.txt_estado.setVisibility(View.VISIBLE);
            }

        }
        else if(filteredNameList.get(position).getEstado().equals("Terminado")){
            holder.btnaceptservice.setVisibility(View.GONE);
            holder.txt_estado.setText("Terminado");
            holder.txt_estado.setVisibility(View.VISIBLE);
        }
        else{
            holder.btnaceptservice.setVisibility(View.GONE);
            holder.txt_estado.setText("Finalizado");
            holder.txt_estado.setVisibility(View.VISIBLE);
        }

        holder.btnaceptservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(holder.btnaceptservice.getText().equals("Aceptar")){
                    comandoSercicio.servicoAceptadouidCliente(modelo.uid,filteredNameList.get(position).getKey(),"Aceptado");
                    sendNotifications(filteredNameList.get(position).getToken(),"Servico","servicio aceptado");
                    holder.btnaceptservice.setText("Detalle");

                }else{

                    /*comandoSercicio.servicoAceptadouidCliente(modelo.uid,filteredNameList.get(position).getKey(),"Terminado");
                    sendNotifications(filteredNameList.get(position).getToken(),"Servico","servicio Terminado");
                    holder.btnaceptservice.setVisibility(View.GONE);
                    holder.txt_estado.setText("Terminado");
                    holder.txt_estado.setVisibility(View.VISIBLE);*/
                    filteredNameList.get(position).setEstado("Aceptado");
                    modelo.servicios = filteredNameList.get(position);

                    Intent i = new Intent(context, DetalleHistorial.class);
                    context.startActivity(i);
                }



            }
        });

        if(filteredNameList.get(position).getEstado().equals("true")){
            holder.image_fav.setBackgroundResource(R.drawable.start);
        }else{
            holder.image_fav.setBackgroundResource(R.drawable.start2);
        }


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
                    List<Servicios> filteredList = new ArrayList<>();
                    for (Servicios name : nameList) {
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
                filteredNameList = (List<Servicios>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public void getTipoServicio() {

    }

    @Override
    public void getServicio() {


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

    class ServicioViewHolder extends RecyclerView.ViewHolder {

        private ImageView image_fav;
        private TextView text_dir;
        private TextView text_nombre;
        private TextView text_fecha;
        private TextView txt_estado;
        private LinearLayout layuotdata;
        private Button btnaceptservice;

        ServicioViewHolder(@NonNull View itemView) {
            super(itemView);
            image_fav = itemView.findViewById(R.id.image_fav);
            text_dir = itemView.findViewById(R.id.text_dir);
            text_nombre = itemView.findViewById(R.id.text_nombre);
            text_fecha = itemView.findViewById(R.id.text_fecha);
            txt_estado = itemView.findViewById(R.id.txt_estado);
            layuotdata = itemView.findViewById(R.id.layuotdata);
            btnaceptservice = itemView.findViewById(R.id.btnaceptservice);
        }
    }


    //puch
    public void sendNotifications(String usertoken, String title, String message) {
        Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(context, "Failed ", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(context, "servicio aceptado ", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(context, "Error code" + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }
}