package model;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

import helper.ConfiguracaoFirebase;

public class PostagemLike {

    private int qtdLikes = 0;
    private Usuario usuario;
    private HomeFeed homeFeed;



    public PostagemLike() {
    }

    public void salvarLikes(){
        //referenciar nó principal (geralzão)
        DatabaseReference fireBaseRef = ConfiguracaoFirebase.getReferenciaDatabase();

        //objeto Hashmap para recuperar apenas alguns dados
        HashMap<String,Object> dadosUsuarios = new HashMap<>();
        dadosUsuarios.put("nome",usuario.getNome());
        dadosUsuarios.put("caminhoFoto",usuario.getCaminhoFoto());

        //objeto para referenciar o no de postagens like
        DatabaseReference postLikeRef = fireBaseRef.child("postagem-curtidas")
                .child(homeFeed.getIdFotoPostada()) //idpostagem
                .child(usuario.getId()); //idusuariologado

        postLikeRef.setValue(dadosUsuarios);
        //atualizar quantidade
        atualizarQtLikes(1);
    }

    public void removerLikes(){
        //Referenciar o nó principal
        DatabaseReference fireBaseRef = ConfiguracaoFirebase.getReferenciaDatabase();

        //criar objeto para referenciar a quantidade de curtidas
        DatabaseReference postLikeRef = fireBaseRef.child("postagem-curtidas")
                .child(homeFeed.getIdFotoPostada())
                .child(usuario.getId());

        //removendo valores
        postLikeRef.removeValue();

        //atualizar quantidade de curtidas
        atualizarQtLikes(-1);
    }

    public void atualizarQtLikes(int valorLikes){
        //referenciar nó principal (geralzão)
        DatabaseReference fireBaseRef = ConfiguracaoFirebase.getReferenciaDatabase();
        //objeto para referenciar o no de postagens like
        DatabaseReference postLikeRef = fireBaseRef.child("postagem-curtidas")
                .child(homeFeed.getIdFotoPostada()) //idpostagem
                .child("qtdLikes"); //QUANTIDADE DE LIKES

        setQtdLikes(getQtdLikes() + valorLikes);
        //recuperando quantidade de curtidas
        postLikeRef.setValue(getQtdLikes());
}

    public int getQtdLikes() {
        return qtdLikes;
    }

    public void setQtdLikes(int qtdLikes) {
        this.qtdLikes = qtdLikes;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public HomeFeed getHomeFeed() {
        return homeFeed;
    }

    public void setHomeFeed(HomeFeed homeFeed) {
        this.homeFeed = homeFeed;
    }
}
