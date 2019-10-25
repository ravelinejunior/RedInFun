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

public class AdapterPesquisar extends RecyclerView.Adapter<AdapterPesquisar.myViewHolder> {

    private List<Usuario> listaUsuarios;
    private Context context;

    public AdapterPesquisar(List<Usuario> lista, Context c) {
        this.listaUsuarios = lista;
        this.context = c;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pesquisar_usuario_layout,parent,false);
      return new myViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int  position) {

        //exibe os itens configurados
        //recupera os usuarios do Firebase
        Usuario usuario = listaUsuarios.get(position);
        holder.nome.setText(usuario.getNome());
        if (usuario.getCaminhoFoto() != null){
            Uri url = Uri.parse(usuario.getCaminhoFoto());
            Glide.with(context).load(url).circleCrop().into(holder.foto);
        }else{
            holder.foto.setImageResource(R.drawable.perfilfoto);

        }

    }

    @Override
    public int getItemCount() {
        //retorna o tamanho da lista
        return listaUsuarios.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        CircleImageView foto;
        TextView nome;


        public myViewHolder(@NonNull View itemView) {

            super(itemView);
            foto = itemView.findViewById(R.id.foto_perfil_id_adapter_pesquisar_layout);
            nome = itemView.findViewById(R.id.nome_usuario_id_adapter_pesquisar_layout);
        }
    }

}
