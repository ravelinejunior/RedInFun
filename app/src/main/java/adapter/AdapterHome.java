package adapter;

import android.content.Context;
import android.net.Uri;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.like.LikeButton;
import java.util.List;

import br.com.raveline.redinfunusers.R;
import de.hdodenhof.circleimageview.CircleImageView;
import model.HomeFeed;


public class AdapterHome extends RecyclerView.Adapter<AdapterHome.myViewHolder> {

    private List<HomeFeed> listaHome;
    private Context context;

    public AdapterHome(List<HomeFeed> listaHome, Context context) {
        this.listaHome = listaHome;
        this.context = context;
    }






    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_feed,parent,false);
        return new AdapterHome.myViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        HomeFeed homeFeed = listaHome.get(position);

        //carregando dados no home

            Uri uriFotoUsuario = Uri.parse(homeFeed.getFotoUsuario());
            Uri uriFotoPostada = Uri.parse(homeFeed.getFotoPostagem());

            //carregando foto
            Glide.with(context).load(uriFotoUsuario).into(holder.fotoPerfilUsuario);
            Glide.with(context).load(uriFotoPostada).into(holder.fotoPostada);

            holder.descricaoFoto.setText(homeFeed.getDescricaoPostagem());
            holder.nomeUsuario.setText(homeFeed.getNomeUsuario());
        }

    @Override
    public int getItemCount() {
        return listaHome.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        CircleImageView fotoPerfilUsuario;
        TextView nomeUsuario;
        TextView descricaoFoto;
        TextView qtCurtidas;
        ImageView visualizarComentario;
        ImageView fotoPostada;
        LikeButton likeButton;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            fotoPerfilUsuario = itemView.findViewById(R.id.foto_perfil_usuario_visualizar_foto);
            fotoPostada = itemView.findViewById(R.id.imagem_selecionada_visualizar_postagem);
            nomeUsuario = itemView.findViewById(R.id.nome_usuario_visualizar_postagem);
            qtCurtidas = itemView.findViewById(R.id.numero_curtidas_visualizar_postagem);
            descricaoFoto = itemView.findViewById(R.id.descricao_foto_visualizar_postagem);
            visualizarComentario = itemView.findViewById(R.id.comentario_id_home_adapter);
            likeButton = itemView.findViewById(R.id.botao_like_home_adapter);


        }
    }


}


