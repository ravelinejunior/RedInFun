package model;

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

    public void atualizarDados(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getReferenciaDatabase();
        Map objetoHash = new HashMap();
        objetoHash.put("/usuarios/"+getId()+"nome",getNome());
        objetoHash.put("/usuarios/"+getId()+"caminhoFoto",getCaminhoFoto());

        firebaseRef.updateChildren(objetoHash);
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
}
