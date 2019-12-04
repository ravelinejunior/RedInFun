package model;

public class HomeFeed {
    private String nomeUsuario;
    private String caminhoFotoUsuario;
    private String idUsuario;
    private String descricaoFotoPostada;
    private String fotoPostada;
    private String idFotoPostada;

    public HomeFeed() {

    }

    public String getIdFotoPostada() {
        return idFotoPostada;
    }

    public void setIdFotoPostada(String idFotoPostada) {
        this.idFotoPostada = idFotoPostada;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getCaminhoFotoUsuario() {
        return caminhoFotoUsuario;
    }

    public void setCaminhoFotoUsuario(String caminhoFotoUsuario) {
        this.caminhoFotoUsuario = caminhoFotoUsuario;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDescricaoFotoPostada() {
        return descricaoFotoPostada;
    }

    public void setDescricaoFotoPostada(String descricaoFotoPostada) {
        this.descricaoFotoPostada = descricaoFotoPostada;
    }

    public String getFotoPostada() {
        return fotoPostada;
    }

    public void setFotoPostada(String fotoPostada) {
        this.fotoPostada = fotoPostada;
    }
}
