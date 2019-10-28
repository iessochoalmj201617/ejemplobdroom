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
    private List<Contacto> mContactos; // Cached copy of words

    @NonNull
    @Override
    public ContactoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.contacto_item, parent, false);
        return new ContactoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactoViewHolder holder, int position) {
        if (mContactos != null) {
            Contacto current = mContactos.get(position);
            holder.itemView.setText(current.getNombre());
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
