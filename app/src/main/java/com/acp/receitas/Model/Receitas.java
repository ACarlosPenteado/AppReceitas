package com.acp.receitas.Model;

import java.util.ArrayList;

public class Receitas {
    private String id;
    private String tipo;
    private String descrição;
    private String tempoPreparo;
    private String rendimento;
    private String imagem;
    private ArrayList<IncLisIngItem> ingredientes;
    private ArrayList<IncLisPreItem> modoPreparo;

    public Receitas() {
    }

    public Receitas(String id, String tipo, String descrição, String tempoPreparo,
                    String rendimento, String imagem, ArrayList<IncLisIngItem> ingredientes,
                    ArrayList<IncLisPreItem> modoPreparo) {
        this.id = id;
        this.tipo = tipo;
        this.descrição = descrição;
        this.tempoPreparo = tempoPreparo;
        this.rendimento = rendimento;
        this.imagem = imagem;
        this.ingredientes = ingredientes;
        this.modoPreparo = modoPreparo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescrição() {
        return descrição;
    }

    public void setDescrição(String descrição) {
        this.descrição = descrição;
    }

    public String getTempoPreparo() {
        return tempoPreparo;
    }

    public void setTempoPreparo(String tempoPreparo) {
        this.tempoPreparo = tempoPreparo;
    }

    public String getRendimento() {
        return rendimento;
    }

    public void setRendimento(String rendimento) {
        this.rendimento = rendimento;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public ArrayList<IncLisIngItem> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(ArrayList<IncLisIngItem> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public ArrayList<IncLisPreItem> getModoPreparo() {
        return modoPreparo;
    }

    public void setModoPreparo(ArrayList<IncLisPreItem> modoPreparo) {
        this.modoPreparo = modoPreparo;
    }
}
