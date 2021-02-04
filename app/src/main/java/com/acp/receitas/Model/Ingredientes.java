package com.acp.receitas.Model;

public class Ingredientes {
    private String Quantidade;
    private String Medida;
    private String Ingrediente;

    public Ingredientes() {
    }

    public Ingredientes(String quantidade, String medida, String ingrediente) {
        Quantidade = quantidade;
        Medida = medida;
        Ingrediente = ingrediente;
    }

    public String getQuantidade() {
        return Quantidade;
    }

    public void setQuantidade(String quantidade) {
        Quantidade = quantidade;
    }

    public String getMedida() {
        return Medida;
    }

    public void setMedida(String medida) {
        Medida = medida;
    }

    public String getIngrediente() {
        return Ingrediente;
    }

    public void setIngrediente(String ingrediente) {
        Ingrediente = ingrediente;
    }
}
