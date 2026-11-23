public class Vaga {

    private int vaga;
    private int quantidade;
    private String status;

    public Vaga(int quantidade, Cargo cargoDev, int vaga, String status) {
        this.quantidade = quantidade;
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

public void abrirQuadroVagas(){

    if(quantidade > 0){

        System.out.println("Quadro de vagas aberto com sucesso! Total de vagas: " + this.quantidade);
        } else {
            System.out.println("Não foi possível abrir o quadro de vagas. A quantidade é inválida.");
        }
    }
}



