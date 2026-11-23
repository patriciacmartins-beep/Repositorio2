import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("     SISTEMA DE RECURSOS HUMANOS - SRH     ");
        System.out.println("==================================================");

        List<Funcionario> listaFuncionarios = new ArrayList<>();

        //Validação de Salário Mínimo (RN01)
        System.out.println("\n--- TESTE 1: Validação de Salário Mínimo ---");
        Cargo cargoInvalido = new Cargo(1, 1200.00, "Assistente Júnior");
        cargoInvalido.validarSalario(1200.00);

        Cargo cargoDev = new Cargo(2, 3500.00, "Desenvolvedor Back-End");
        cargoDev.validarSalario(3500.00);

        // Gestão de Departamentos
        System.out.println("\n--- TESTE 2: Registo e Orçamento de Departamento ---");
        Departamento depTI = new Departamento(1, "Tecnologia da Informação", 150000.00);
        System.out.println("Setor Criado: " + depTI.getNome() + " | Orçamento Alocado: R$ " + depTI.getOrcamento());

        // Abertura de Quadro de Vagas (RN04)
        System.out.println("\n--- TESTE 3: Abertura de Quadro de Vagas ---");
        Vaga vagaDev = new Vaga(3, cargoDev, 1, "Aberta");
        vagaDev.abrirQuadroVagas();

        // Admissão e Alocação Dupla (UC03 / RN03)
        System.out.println("\n--- TESTE 4: Admissão e Alocação Simultânea ---");
        LocalDate dataAdmissao = LocalDate.of(2026, 3, 10);
        String cpfCarlos = "111.222.333-44";

        Funcionario func1 = new Funcionario(1, "Carlos Silva", cpfCarlos, dataAdmissao, cargoDev, depTI);
        System.out.println("Colaborador Admitido: " + func1.getNome() + " (CPF: " + func1.getCpf() + ")");
        func1.alocacaoDupla();
        listaFuncionarios.add(func1);

        // Validação de CPF Único (RN02)
        System.out.println("\n--- TESTE 5: Validação de Segurança - CPF Duplicado ---");
        String cpfDuplicado = "111.222.333-44";
        
        Funcionario funcAna = new Funcionario(2, "Ana Souza", cpfDuplicado, LocalDate.now(), cargoDev, depTI);
        System.out.println("A tentar registar colaborador '" + funcAna.getNome() + "' com CPF já existente...");
        funcAna.validarCpfUnico(listaFuncionarios);

        System.out.println("\n==================================================");
        System.out.println("   DEMONSTRAÇÃO FINALIZADA                           ");
        System.out.println("==================================================");
    }
}