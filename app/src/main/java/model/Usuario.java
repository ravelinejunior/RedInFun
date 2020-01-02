package model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import helper.ConfiguracaoFirebase;

public class Usuario implements Serializable {
    private String id;
    private String nome;
    private String email;
    private String senha;
    private String caminhoFoto;
    private String idade;
    private String nomeUsuarioPesquisa;
    private String telefoneUsuario;
    private String localUsuario;
    private String pesoUsuario;
    private String descricaoUsuario;
    private String alturaUsuario;
    private String senhaConfirmadaUsuario;
    private int fotos = 0;
    private int clientes = 0;
    private int fas = 0;


    public Usuario() {
    }

    public void salvarDados(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getReferenciaDatabase();
        DatabaseReference usuariosRef = firebaseRef.child("usuarios").child(getId());
        usuariosRef.setValue(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void atualizarDados(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getReferenciaDatabase();
        DatabaseReference usuariosRef = firebaseRef.child("usuarios").child(getId());
        Map objetoHash = new HashMap();
        objetoHash.put("id",getId());
        objetoHash.put("nome",getNome());
        objetoHash.put("nomeUsuarioPesquisa",getNomeUsuarioPesquisa());
        objetoHash.put("caminhoFoto",getCaminhoFoto());

        usuariosRef.updateChildren(objetoHash);
    }


    public void atualizarDados1(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getReferenciaDatabase();
        DatabaseReference usuariosRef = firebaseRef.child("usuarios").child(getId());
        Map<String,Object> valoresMap = converterToMap();
        usuariosRef.updateChildren(valoresMap);
    }

    public void atualizarFotosPostadas(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getReferenciaDatabase();
        DatabaseReference usuariosRef = firebaseRef.child("usuarios").child(getId());
        HashMap<String,Object> fotosPostadas = new HashMap<>();
        fotosPostadas.put("fotos",getFotos());
        usuariosRef.updateChildren(fotosPostadas);
    }

    private Map<String,Object> converterToMap(){

        HashMap<String,Object> hashMapUsuario = new HashMap<>();
        hashMapUsuario.put("email",getEmail());
        hashMapUsuario.put("senha",getSenha());
        hashMapUsuario.put("caminhoFoto",getCaminhoFoto());
        hashMapUsuario.put("nomeUsuarioPesquisa",getNomeUsuarioPesquisa());
        hashMapUsuario.put("nome",getNome());
        hashMapUsuario.put("idade",getIdade());
        hashMapUsuario.put("id",getId());
        hashMapUsuario.put("fotos",getFotos());
        hashMapUsuario.put("clientes",getClientes());
        hashMapUsuario.put("fas",getFas());
        return hashMapUsuario;

    }

    public int getFotos() {
        return fotos;
    }

    public void setFotos(int fotos) {
        this.fotos = fotos;
    }

    public int getClientes() {
        return clientes;
    }

    public void setClientes(int clientes) {
        this.clientes = clientes;
    }

    public int getFas() {
        return fas;
    }

    public void setFas(int fas) {
        this.fas = fas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    private String getNomeUsuarioPesquisa() {
        nomeUsuarioPesquisa = getNome().toUpperCase();
        return nomeUsuarioPesquisa;
    }

    public void setNomeUsuarioPesquisa(String nomeUsuarioPesquisa) {
        this.nomeUsuarioPesquisa = nomeUsuarioPesquisa.toUpperCase();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //excluir senha para nao ser exibida
    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

    private String getIdade() {
        return idade;
    }

    public void setIdade(String idade) {
        this.idade = idade;
    }

    public String getTelefoneUsuario() {
        return telefoneUsuario;
    }

    public void setTelefoneUsuario(String telefoneUsuario) {
        this.telefoneUsuario = telefoneUsuario;
    }

    public String getLocalUsuario() {
        return localUsuario;
    }

    public void setLocalUsuario(String localUsuario) {
        this.localUsuario = localUsuario;
    }

    public String getPesoUsuario() {
        return pesoUsuario;
    }

    public void setPesoUsuario(String pesoUsuario) {
        this.pesoUsuario = pesoUsuario;
    }

    public String getDescricaoUsuario() {
        return descricaoUsuario;
    }

    public void setDescricaoUsuario(String descricaoUsuario) {
        this.descricaoUsuario = descricaoUsuario;
    }

    public String getAlturaUsuario() {
        return alturaUsuario;
    }

    public void setAlturaUsuario(String alturaUsuario) {
        this.alturaUsuario = alturaUsuario;
    }

    public String getSenhaConfirmadaUsuario() {
        return senhaConfirmadaUsuario;
    }

    public void setSenhaConfirmadaUsuario(String senhaConfirmadaUsuario) {
        this.senhaConfirmadaUsuario = senhaConfirmadaUsuario;
    }
}
