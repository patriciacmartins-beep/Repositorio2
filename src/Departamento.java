public class Departamento {

    private int id;
    private String nome;
    private double orcamento;  

    //Inserindo o Construtor
    public Departamento(int id, String nome, double orcamento) {
        this.id = id;
        this.nome = nome;
        this.orcamento = orcamento;
    }

    //Inserindo o getters e setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getOrcamento() {
        return orcamento;
    }

    //Atualiza o orçamento de um departamento - basta chamar o método passando o novo valor
    public void setOrcamento(double orcamento) {
        this.orcamento = orcamento;
    }
}