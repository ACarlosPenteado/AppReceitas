package com.acp.receitas.Model;

import java.util.ArrayList;

public class RecItens {
    private String ID;
    private String Tipo;
    private String Descrição;
    private String TempoPreparo;
    private String Rendimento;
    private String Imagem;

    public RecItens() {
    }

    public RecItens(String ID, String tipo, String descrição, String tempoPreparo,
                    String rendimento, String imagem) {
        this.ID = ID;
        Tipo = tipo;
        Descrição = descrição;
        TempoPreparo = tempoPreparo;
        Rendimento = rendimento;
        Imagem = imagem;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public String getDescrição() {
        return Descrição;
    }

    public void setDescrição(String descrição) {
        Descrição = descrição;
    }

    public String getTempoPreparo() {
        return TempoPreparo;
    }

    public void setTempoPreparo(String tempoPreparo) {
        TempoPreparo = tempoPreparo;
    }

    public String getRendimento() {
        return Rendimento;
    }

    public void setRendimento(String rendimento) {
        Rendimento = rendimento;
    }

    public String getImagem() {
        return Imagem;
    }

    public void setImagem(String imagem) {
        Imagem = imagem;
    }

}
