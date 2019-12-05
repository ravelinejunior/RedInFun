package model;

import com.google.firebase.database.DatabaseReference;


import helper.ConfiguracaoFirebase;

public class Comentarios {

    private String idComentario;
    private String nome;
    private String caminhoFoto;
    private String idFotoPostada;
    private String comentario;
    private String idUsuario;

    public Comentarios() {

    }

    public boolean salvarComentario(){
        /*
            nó comentarios
                Comentarios
                    idFotoPostada
                        idComentario -> comentario
         */
        DatabaseReference comentariosRef = ConfiguracaoFirebase.getReferenciaDatabase()
                .child("comentarios")
                .child(getIdFotoPostada());

        //gerando chave gerada para id comentario
        String keyIdComentario = comentariosRef.push().getKey();
        setIdComentario(keyIdComentario);
        //salvando nó para id comentario
        comentariosRef.child(getIdComentario()).setValue(this);

        return true;
    }



    public String getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(String idComentario) {
        this.idComentario = idComentario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

    public String getIdFotoPostada() {
        return idFotoPostada;
    }

    public void setIdFotoPostada(String idFotoPostada) {
        this.idFotoPostada = idFotoPostada;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }




}
