package com.empresa.rh.model;

public class Vaga {

    private int vaga;
    private int quantidade;
    private String status; // LV = livre para ser preenchida, EP = em processo de recrutamento
    private Cargo cargo;

    public Vaga(int quantidade, Cargo cargo, int vaga, String status) {
        this.quantidade = quantidade;
        this.cargo = cargo;
        this.status = status;
        this.vaga = vaga;
    }

    public int getVaga() {
        return vaga;
    }

    public void setVaga(int vaga) {
        this.vaga = vaga;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public void abrirQuadroVagas() {
        if (quantidade > 0) {
            System.out.println("Quadro de vagas aberto com sucesso! Total de vagas: " + this.quantidade);
        } else {
            System.out.println("Não foi possível abrir o quadro de vagas. A quantidade é inválida.");
        }
    }

    @Override
    public String toString() {
        String descricao = "LV".equals(status) ? "Livre" : "Em processo";
        return String.format("Vaga: %-4d | Cargo: %-28s | Posições: %-3d | Status: %s (%s)",
                vaga, cargo == null ? "-" : cargo.getTitulo(), quantidade, status, descricao);
    }
}
