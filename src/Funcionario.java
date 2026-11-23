import java.time.LocalDate;
import java.util.List;

public class Funcionario {

    private int id;
    private String nome;
    private String cpf;
    private LocalDate admissao;
    private Cargo cargo;
    private Departamento departamento;

    // Construtor corrigido com tipos e parâmetros corretos e sem duplicações
    public Funcionario(int id, String nome, String cpf, LocalDate admissao, Cargo cargo, Departamento departamento) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.admissao = admissao;
        this.cargo = cargo;
        this.departamento = departamento;
    }

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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getAdmissao() {
        return admissao;
    }

    public void setAdmissao(LocalDate admissao) {
        this.admissao = admissao;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    // Alocação dupla - RN03: O colaborador deve estar vinculado simultaneamente a
    // um cargo e um departamento no momento da admissão
    public void alocacaoDupla() {
        if (this.cargo != null) {
            System.out.println("Cargo Vinculado: " + this.cargo.getTitulo() + " (Salário Base: R$ "
                    + this.cargo.getSalarioBase() + ")");
        }

        if (this.departamento != null) {
            System.out.println("Departamento Vinculado: " + this.departamento.getNome());
        }
    }

    // RN02 - Garantir que não haja cadastro de CPF duplicado
    public void validarCpfUnico(List<Funcionario> funcionariosCadastrados) {
        boolean cpfExiste = false;

        for (Funcionario f : funcionariosCadastrados) {
            if (f.getCpf().equals(this.cpf) && f.getId() != this.id) {
                cpfExiste = true;
                break;
            }
        }

        if (cpfExiste) {
            System.out.println("Erro: O CPF " + this.cpf + " já está registado no sistema!");
        } else {
            System.out.println("Sucesso: O CPF " + this.cpf + " é único e válido.");
        }
    }
}