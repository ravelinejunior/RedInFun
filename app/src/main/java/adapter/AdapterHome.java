package adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.List;
import java.util.Objects;

import br.com.raveline.redinfunusers.R;
import br.com.raveline.redinfunusers.VisualizarComentarioActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import helper.ConfiguracaoFirebase;
import helper.UsuarioFirebase;
import model.HomeFeed;
import model.PostagemLike;
import model.Usuario;


public class AdapterHome extends RecyclerView.Adapter<AdapterHome.myViewHolder> {

    private final List<HomeFeed> listaHome;
    private final Context context;

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
        final HomeFeed homeFeed = listaHome.get(position);
        final Usuario usuarioLogado = UsuarioFirebase.getUsuarioLogado();

        holder.descricaoFoto.setText(homeFeed.getDescricaoFotoPostada());
        holder.nomeUsuario.setText(homeFeed.getNomeUsuario());

        //carregando dados no home

            Uri uriFotoUsuario = Uri.parse(homeFeed.getCaminhoFotoUsuario());
            Uri uriFotoPostada = Uri.parse(homeFeed.getFotoPostada());

            //carregando foto
            Glide.with(context).load(uriFotoUsuario).into(holder.fotoPerfilUsuario);
            Glide.with(context).load(uriFotoPostada).into(holder.fotoPostada);

            //Levando para tela de comentarios
            holder.visualizarComentario.setOnClickListener(v -> {
                Intent intent = new Intent(context, VisualizarComentarioActivity.class);
                intent.putExtra("idFotoPostada",homeFeed.getIdFotoPostada());

                context.startActivity(intent);
            });

            /*

            postagem_Curtida
	idPostagem
		idUsuarioQueCurtiu		- nomeUsuario
								- caminhoFoto
		qtCurtidas - valor

                     */
            //recuperar Quantidade de curtidas
        DatabaseReference postLikesRef = ConfiguracaoFirebase.getReferenciaDatabase()
                .child("postagem-likes")
                .child(homeFeed.getIdFotoPostada());

        postLikesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //verificando se existe o nó de curtidas
                int qtdLikes = 0;
                if (dataSnapshot.hasChild("qtdLikes")){
                    PostagemLike postagemLike = dataSnapshot.getValue(PostagemLike.class);
                    qtdLikes = Objects.requireNonNull(postagemLike).getQtdLikes();

                }
                //verificar se ja foi clicado por usuario x
                if (dataSnapshot.hasChild(usuarioLogado.getId())){
                    holder.likeButton.setLiked(true);
                } else{
                    holder.likeButton.setLiked(false);
                }

                //motando objeto postagem curtida
                final PostagemLike like = new PostagemLike();
                like.setHomeFeed(homeFeed);
                like.setUsuario(usuarioLogado);
                like.setQtdLikes(qtdLikes);



                //eventos para o likebutton
                holder.likeButton.setOnLikeListener(new OnLikeListener() {
                    @Override
                    public void liked(LikeButton likeButton) {

                        like.salvarLikes();
                        holder.qtCurtidas.setText(like.getQtdLikes()+" curtida(s)");

                    }

                    @Override
                    public void unLiked(LikeButton likeButton) {

                        like.removerLikes();
                        holder.qtCurtidas.setText(like.getQtdLikes()+" curtida(s)");

                    }
                });
                holder.qtCurtidas.setText(like.getQtdLikes()+" curtida(s)");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        }

    @Override
    public int getItemCount() {
        return listaHome.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        final CircleImageView fotoPerfilUsuario;
        final TextView nomeUsuario;
        final TextView descricaoFoto;
        final TextView qtCurtidas;
        final ImageView visualizarComentario;
        final ImageView fotoPostada;
        final LikeButton likeButton;


        myViewHolder(@NonNull View itemView) {
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


