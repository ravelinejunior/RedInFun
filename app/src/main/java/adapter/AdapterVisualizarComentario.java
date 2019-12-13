package adapter;

import android.content.Context;
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
import model.Comentarios;
import model.Usuario;

public class AdapterVisualizarComentario extends RecyclerView.Adapter<AdapterVisualizarComentario.MyViewHolder> {
    private List<Comentarios> listaComentarios;
    private Context context;

    public AdapterVisualizarComentario(List<Comentarios> listaComentarios, Context context) {
        this.listaComentarios = listaComentarios;
        this.context = context;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_visualizar_comentarios,parent,false);
        return new AdapterVisualizarComentario.MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Comentarios comentario = listaComentarios.get(position);
        holder.nomeUsuario.setText(comentario.getNome());
        holder.comentario.setText(comentario.getComentario());
        Glide.with(context).load(comentario.getCaminhoFoto()).into(holder.imagemPerfil);

    }

    @Override
    public int getItemCount() {
        return listaComentarios.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView imagemPerfil;
        TextView nomeUsuario;
        TextView comentario;

        public MyViewHolder(View itemView){
            super(itemView);
            imagemPerfil = itemView.findViewById(R.id.foto_usuario_adapter_visualizar_comentario);
            nomeUsuario = itemView.findViewById(R.id.nome_usuario_id_adapter_visualizar_comentarios);
            comentario = itemView.findViewById(R.id.comentario_id_adapter_visualizar_comentarios);

        }
    }

}
