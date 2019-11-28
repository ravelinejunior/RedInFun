package model;

public class HomeFeed {
    private String nomeUsuario;
    private String fotoUsuario;
    private String idUsuario;
    private String descricaoPostagem;
    private String fotoPostagem;

    public HomeFeed() {

    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getFotoUsuario() {
        return fotoUsuario;
    }

    public void setFotoUsuario(String fotoUsuario) {
        this.fotoUsuario = fotoUsuario;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDescricaoPostagem() {
        return descricaoPostagem;
    }

    public void setDescricaoPostagem(String descricaoPostagem) {
        this.descricaoPostagem = descricaoPostagem;
    }

    public String getFotoPostagem() {
        return fotoPostagem;
    }

    public void setFotoPostagem(String fotoPostagem) {
        this.fotoPostagem = fotoPostagem;
    }
}
