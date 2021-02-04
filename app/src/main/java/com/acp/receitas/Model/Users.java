package com.acp.receitas.Model;

import com.google.type.DateTime;
import java.security.KeyStore;

public class Users {

    private String id;
    private String nome;
    private String fone;
    private String email;
    private String data;
    private String imagem;

    public Users() {
    }

    public Users(String id, String nome, String fone, String email, String data, String imagem) {
        this.id = id;
        this.nome = nome;
        this.fone = fone;
        this.email = email;
        this.data = data;
        this.imagem = imagem;
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

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }
}
