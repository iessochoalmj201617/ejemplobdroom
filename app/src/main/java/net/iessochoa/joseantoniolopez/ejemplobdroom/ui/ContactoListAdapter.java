package net.iessochoa.joseantoniolopez.ejemplobdroom.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.iessochoa.joseantoniolopez.ejemplobdroom.R;
import net.iessochoa.joseantoniolopez.ejemplobdroom.model.Contacto;

import java.util.List;

public class ContactoListAdapter extends RecyclerView.Adapter<ContactoListAdapter.ContactoViewHolder> {
    private final LayoutInflater mInflater;
    private List<Contacto> mContactos; //
    //definimos la interface para el control del click
    private onItemClickListener listener;
    public interface onItemClickListener{
        void onItemClick(Contacto contacto);
    }
    //nos permite asignar el listener creado en la actividad
    public void setOnClickListener(onItemClickListener listener)
    {
        this.listener=listener;
    }

    @NonNull
    @Override
    public ContactoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.contacto_item, parent, false);
        return new ContactoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactoViewHolder holder, int position) {
        if (mContactos != null) {
            final Contacto contacto = mContactos.get(position);
            holder.itemView.setText(contacto.toString());
//asignamos el listener
            if(listener!=null){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onItemClick(contacto);
                    }
                });
            }

        } else {
            // Covers the case of data not being ready yet.
            holder.itemView.setText("Sin contactos");

        }
    }

    @Override
    public int getItemCount() {
        if (mContactos != null)
            return mContactos.size();
        else return 0;
    }
    void setContactos(List<Contacto> contactos){
        mContactos=contactos;
        notifyDataSetChanged();
    }

    public class ContactoViewHolder extends RecyclerView.ViewHolder {
        private  TextView itemView;
        public ContactoViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView.findViewById(R.id.textView);
        }
    }
    ContactoListAdapter(Context context){
        mInflater=LayoutInflater.from(context);
    }
}
