package adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import br.com.raveline.redinfunusers.R;
import de.hdodenhof.circleimageview.CircleImageView;
import model.Usuario;

public class AdapterUsuarios extends RecyclerView.Adapter<AdapterUsuarios.myViewHolder> {
    private List<Usuario> lista;
    private Context c;

    public AdapterUsuarios(List<Usuario> lista, Context c) {
        this.lista = lista;
        this.c = c;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_usuarios,parent,false);
        return new AdapterUsuarios.myViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        //exibe os itens configurados
        //recupera os usuarios do Firebase
        Usuario usuario = lista.get(position);
        holder.nome.setText(usuario.getNome());
        if (usuario.getCaminhoFoto() != null){
            Uri url = Uri.parse(usuario.getCaminhoFoto());
            Glide.with(c).load(url).circleCrop().into(holder.foto);
        }else{
            holder.foto.setImageResource(R.drawable.perfilfoto);

        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        final CircleImageView foto;
        final TextView nome;


        myViewHolder(@NonNull View itemView) {

            super(itemView);
            foto = itemView.findViewById(R.id.foto_perfil_id_adapter_usuarios_layout);
            nome = itemView.findViewById(R.id.nome_usuario_id_adapter_usuarios_layout);
        }
    }

}
