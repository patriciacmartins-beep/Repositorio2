package com.empresa.rh.model;

public class Cargo {

    private int id;
    private String titulo;
    private double salarioBase;
    private double salarioMinimo = 1621.00;

    public Cargo(int id, double salarioBase, String titulo) {
        this.id = id;
        this.salarioBase = salarioBase;
        this.titulo = titulo;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public double getSalarioBase() {
        return salarioBase;
    }

    public double getSalarioMinimo() {
        return salarioMinimo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setSalarioBase(double salarioBase) {
        this.salarioBase = salarioBase;
    }

    public void setSalarioMinimo(double salarioMinimo) {
        this.salarioMinimo = salarioMinimo;
    }

    // RN01: só aceita o novo salário se ele não for menor que o salário mínimo vigente
    public void validarSalario(double novoSalario) {
        if (novoSalario >= salarioMinimo) {
            this.salarioBase = novoSalario; // Atribui corretamente ao atributo da classe
            System.out.println("Salário válido e atualizado com sucesso!");
        } else {
            System.out.println("O salário do cargo não pode ser menor que o Salário Mínimo Vigente (1621.00).");
        }
    }

    public void verificarSalarioBanco(double salarioInformado, double salarioDoBanco) {
        if (salarioInformado == salarioDoBanco) {
            System.out.println("Sucesso: O salário confere exatamente com o registrado no banco de dados!");
        } else {
            System.out.println("Erro: O salário informado (R$ " + salarioInformado
                    + ") é diferente do salário base cadastrado no banco (R$ " + salarioDoBanco + ")!");
        }
    }

    @Override
    public String toString() {
        return String.format("ID: %-4d | Cargo: %-28s | Salário base: R$ %,.2f", id, titulo, salarioBase);
    }
}
