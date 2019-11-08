package model;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

import helper.ConfiguracaoFirebase;

public class FotoPostada {

    /*
    MODELO DE BANCO DE DADOS E ESTRUTURA DA POSTAGEM
        fotoPostada
            idUsuario
                idFotoPostada
                    fotoPostada
                    descrição
                    idUsuario

     */

    private String idFotoPostada;
    private String idUsuario;
    private String caminhoFotoPostada;
    private String descricaoFotoPostada;

    public FotoPostada() {

        //recuperando o id da postagem
        DatabaseReference databaseReference = ConfiguracaoFirebase.getReferenciaDatabase();
        DatabaseReference fotoPostadaRef = databaseReference.child("fotosPostadas");
        //gerar identificador unitario dentro de foto postada
        String idFotoPostada = fotoPostadaRef.push().getKey();
        setIdFotoPostada(idFotoPostada);
        converterToMap();

    }

    public Map<String,Object> converterToMap(){
        HashMap<String,Object> hashMapUsuario = new HashMap<>();
        hashMapUsuario.put("idUsuario",getIdUsuario());
        hashMapUsuario.put("idFotoPostada",getIdFotoPostada());
        hashMapUsuario.put("caminhoFotoPostada",getCaminhoFotoPostada());
        hashMapUsuario.put("descricao",getDescricaoFotoPostada());
        return hashMapUsuario;

    }

    public boolean salvarFotoPostada() {
        DatabaseReference databaseRef = ConfiguracaoFirebase.getReferenciaDatabase();
        DatabaseReference fotosPostadasRef = databaseRef.child("fotosPostadas")
                .child(getIdUsuario())
                .child(getIdFotoPostada());
        fotosPostadasRef.setValue(this);
        converterToMap();
        return true;
    }




    public String getIdFotoPostada() {
        return idFotoPostada;
    }

    public void setIdFotoPostada(String idFotoPostada) {
        this.idFotoPostada = idFotoPostada;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCaminhoFotoPostada() {
        return caminhoFotoPostada;
    }

    public void setCaminhoFotoPostada(String caminhoFotoPostada) {
        this.caminhoFotoPostada = caminhoFotoPostada;
    }

    public String getDescricaoFotoPostada() {
        return descricaoFotoPostada;
    }

    public void setDescricaoFotoPostada(String descricaoFotoPostada) {
        this.descricaoFotoPostada = descricaoFotoPostada;
    }
}