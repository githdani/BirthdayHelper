package com.example.birthdayhelper_danielavila;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adaptador extends RecyclerView.Adapter<Adaptador.ViewHolder>{

    private List<Contactos> filCont;
    private Context context;

    public Adaptador(List<Contactos> filCont, Context context) {
        this.context = context;
        this.filCont = filCont;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imagen;
        private TextView txtNomb;
        private TextView txtTel;
        private TextView txtNot;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imagen = (ImageView) itemView.findViewById(R.id.image);
            txtNomb = (TextView) itemView.findViewById(R.id.txtNombre);
            txtTel = (TextView) itemView.findViewById(R.id.txtTelef);
            txtNot = (TextView) itemView.findViewById(R.id.txtNotif);
        }
    }

    @NonNull
    @Override
    public Adaptador.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.contact_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Adaptador.ViewHolder holder, int position) {
        Contactos contacto = filCont.get(position);

        TextView nombre = holder.txtNomb;
        nombre.setText(contacto.getNombre());
        TextView numero = holder.txtTel;
        numero.setText(contacto.getNúmero().get(0));
        ImageView image = holder.imagen;
        image.setImageBitmap(contacto.getFoto());
        TextView tipoNoti = holder.txtNot;
        tipoNoti.setText(contacto.getNotif());

        holder.itemView.setOnClickListener(v -> {



        });
    }

    @Override
    public int getItemCount() {
        return filCont.size();
    }
    public interface OnItemClickListener {
        void onItemClick(Contactos contacto);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    private OnItemClickListener listener;

}
