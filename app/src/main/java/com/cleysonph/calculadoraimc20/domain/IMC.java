package com.cleysonph.calculadoraimc20.domain;

/**
 * Created by Aluno on 20/11/2015.
 */
public class IMC {

    private long id;
    private double peso;
    private double altura;
    private double resultado;

    public IMC(){

    }

    public IMC(long id, double peso, double altura, double resultado){
        this.id = id;
        this.peso = peso;
        this.altura = altura;
        this.resultado = resultado;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public double getResultado() {
        return resultado;
    }

    public void setResultado(double resultado) {
        this.resultado = resultado;
    }
}
