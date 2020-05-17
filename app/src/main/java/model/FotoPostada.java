package model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import helper.ConfiguracaoFirebase;
import helper.UsuarioFirebase;

public class FotoPostada implements Serializable {

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
    private Usuario usuario;

    public FotoPostada() {

        //recuperando o id da postagem
        DatabaseReference databaseReference = ConfiguracaoFirebase.getReferenciaDatabase();
        DatabaseReference fotoPostadaRef = databaseReference.child("fotosPostadas");
        //gerar identificador unitario dentro de foto postada
        String idFotoPostada = fotoPostadaRef.push().getKey();
        setIdFotoPostada(idFotoPostada);


    }

    public Map<String,Object> converterToMap(){
        HashMap<String,Object> hashMapUsuario = new HashMap<>();
        hashMapUsuario.put("idUsuario",getIdUsuario());
        hashMapUsuario.put("idFotoPostada",getIdFotoPostada());
        hashMapUsuario.put("caminhoFotoPostada",getCaminhoFotoPostada());
        hashMapUsuario.put("descricao",getDescricaoFotoPostada());
        return hashMapUsuario;

    }

    //utilizar estratégia FenOut de espalhamento
    public boolean salvarFotoPostada(DataSnapshot seguidoresSnapShot) {

        //objeto para atualização
        Map objeto = new HashMap();
        Usuario usuarioLogado = UsuarioFirebase.getUsuarioLogado();

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getReferenciaDatabase();

        //referencia postagens
       // firebaseRef.child("postagens").child(getIdUsuario());
        String combinacaodeId = "/"+getIdUsuario()+"/"+getIdFotoPostada();
        objeto.put("/fotosPostadas"+combinacaodeId,this);
        //exemplo "/postagens/idusuario/iddafotopostada cada barra separa cada filho

     //referencia para o feed
        for (DataSnapshot dsSeguidores:seguidoresSnapShot.getChildren()){
 /*            exemplo da estrutura de feed
                feed
                    idSeguidor
                        postagem (feita por)

        montar objeto para salvar
*/
             //recuperar chave id seguidor
             String idSeguidor = dsSeguidores.getKey();
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("fotoPostada", getCaminhoFotoPostada());
            hashMap.put("descricaoFotoPostada", getDescricaoFotoPostada());
            hashMap.put("idFotoPostada", getIdFotoPostada());
             //dados do usuario necessarios
            hashMap.put("nomeUsuario", usuarioLogado.getNome());
            hashMap.put("caminhoFotoUsuario", usuarioLogado.getCaminhoFoto());
             //salvar Objeto
            String caminhoAtualizacao = "/"+idSeguidor+"/"+getIdFotoPostada();
            objeto.put("/feed" + caminhoAtualizacao, hashMap);

        }

        firebaseRef.updateChildren(objeto);
        return true;
    }




    public String getIdFotoPostada() {
        return idFotoPostada;
    }

    private void setIdFotoPostada(String idFotoPostada) {
        this.idFotoPostada = idFotoPostada;
    }

    private String getIdUsuario() {
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
