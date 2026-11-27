package com.empresa.rh.view;

import com.empresa.rh.controller.CargoController;
import com.empresa.rh.controller.DepartamentoController;
import com.empresa.rh.controller.FuncionarioController;
import com.empresa.rh.controller.VagaController;
import com.empresa.rh.model.Cargo;
import com.empresa.rh.model.Departamento;
import com.empresa.rh.model.Funcionario;
import com.empresa.rh.model.Vaga;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

// Interação com o usuário: só exibe os menus e lê o que foi digitado.
// As regras ficam no Controller; o acesso ao banco, no DAO.
public class RHView {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final DepartamentoController departamentoController;
    private final CargoController cargoController;
    private final VagaController vagaController;
    private final FuncionarioController funcionarioController;
    private final Scanner scanner;

    public RHView() {
        this.departamentoController = new DepartamentoController();
        this.cargoController = new CargoController();
        this.vagaController = new VagaController();
        this.funcionarioController = new FuncionarioController();
        this.scanner = new Scanner(System.in);
    }

    public void exibirMenu() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n============ SISTEMA DE RH - SRH ============");
            System.out.println("1. Departamentos");
            System.out.println("2. Cargos e salários");
            System.out.println("3. Vagas");
            System.out.println("4. Funcionários");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            try {
                opcao = Integer.parseInt(scanner.nextLine());
                switch (opcao) {
                    case 1 -> menuDepartamentos();
                    case 2 -> menuCargos();
                    case 3 -> menuVagas();
                    case 4 -> menuFuncionarios();
                    case 0 -> System.out.println("Encerrando a aplicação...");
                    default -> System.out.println("Opção inválida!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Erro: digite apenas números inteiros.");
            }
        }
    }

    // ========================= DEPARTAMENTOS =========================

    private void menuDepartamentos() {
        System.out.println("\n--- Departamentos ---");
        System.out.println("1. Cadastrar departamento");
        System.out.println("2. Listar departamentos");
        System.out.println("3. Atualizar departamento");
        System.out.println("4. Excluir departamento");
        System.out.println("0. Voltar");
        int opcao = lerInteiro("Escolha uma opção: ");
        try {
            switch (opcao) {
                case 1 -> {
                    String nome = lerTexto("Nome do departamento: ");
                    double orcamento = lerDecimal("Orçamento (R$): ");
                    Departamento d = departamentoController.cadastrarDepartamento(nome, orcamento);
                    System.out.println("✓ Departamento cadastrado com o ID " + d.getId());
                }
                case 2 -> mostrarLista(departamentoController.listarDepartamentos(), "Nenhum departamento cadastrado.");
                case 3 -> {
                    int id = lerInteiro("ID do departamento: ");
                    Departamento existente = departamentoController.buscarDepartamento(id);
                    if (existente == null) {
                        System.out.println("Departamento não encontrado.");
                        return;
                    }
                    String nome = lerTextoOuManter("Novo nome", existente.getNome());
                    double orcamento = lerDecimalOuManter("Novo orçamento", existente.getOrcamento());
                    departamentoController.atualizarDepartamento(id, nome, orcamento);
                    System.out.println("✓ Departamento atualizado com sucesso!");
                }
                case 4 -> {
                    int id = lerInteiro("ID do departamento a ser excluído: ");
                    System.out.println(departamentoController.deletarDepartamento(id)
                            ? "✓ Departamento excluído com sucesso!" : "Departamento não encontrado.");
                }
                case 0 -> { }
                default -> System.out.println("Opção inválida!");
            }
        } catch (Exception e) {
            System.out.println("Erro na operação: " + e.getMessage());
        }
    }

    // ========================= CARGOS =========================

    private void menuCargos() {
        System.out.println("\n--- Cargos e salários ---");
        System.out.println("1. Cadastrar cargo");
        System.out.println("2. Listar cargos");
        System.out.println("3. Alterar cargo / salário");
        System.out.println("4. Excluir cargo");
        System.out.println("0. Voltar");
        int opcao = lerInteiro("Escolha uma opção: ");
        try {
            switch (opcao) {
                case 1 -> {
                    String titulo = lerTexto("Título do cargo: ");
                    double salario = lerDecimal("Salário base (R$): ");
                    Cargo c = cargoController.cadastrarCargo(titulo, salario);
                    System.out.println("✓ Cargo cadastrado com o ID " + c.getId());
                }
                case 2 -> mostrarLista(cargoController.listarCargos(), "Nenhum cargo cadastrado.");
                case 3 -> {
                    int id = lerInteiro("ID do cargo: ");
                    Cargo existente = cargoController.buscarCargo(id);
                    if (existente == null) {
                        System.out.println("Cargo não encontrado.");
                        return;
                    }
                    String titulo = lerTextoOuManter("Novo título", existente.getTitulo());
                    double salario = lerDecimalOuManter("Novo salário base", existente.getSalarioBase());
                    cargoController.atualizarCargo(id, titulo, salario);
                    System.out.println("✓ Cargo atualizado com sucesso!");
                }
                case 4 -> {
                    int id = lerInteiro("ID do cargo a ser excluído: ");
                    System.out.println(cargoController.deletarCargo(id)
                            ? "✓ Cargo excluído com sucesso!" : "Cargo não encontrado.");
                }
                case 0 -> { }
                default -> System.out.println("Opção inválida!");
            }
        } catch (Exception e) {
            System.out.println("Erro na operação: " + e.getMessage());
        }
    }

    // ========================= VAGAS =========================

    private void menuVagas() {
        System.out.println("\n--- Vagas ---");
        System.out.println("1. Publicar vaga");
        System.out.println("2. Portal de vagas abertas");
        System.out.println("3. Listar todas as vagas");
        System.out.println("4. Alterar status da vaga (LV / EP)");
        System.out.println("5. Excluir vaga");
        System.out.println("0. Voltar");
        int opcao = lerInteiro("Escolha uma opção: ");
        try {
            switch (opcao) {
                case 1 -> {
                    mostrarLista(cargoController.listarCargos(), "Nenhum cargo cadastrado.");
                    int idCargo = lerInteiro("ID do cargo da vaga: ");
                    int quantidade = lerInteiro("Quantidade de posições: ");
                    Vaga v = vagaController.publicarVaga(idCargo, quantidade);
                    System.out.println("✓ Vaga publicada com o ID " + v.getVaga() + " (status LV)");
                }
                case 2 -> mostrarLista(vagaController.listarVagasAbertas(), "Nenhuma vaga aberta no momento.");
                case 3 -> mostrarLista(vagaController.listarVagas(), "Nenhuma vaga cadastrada.");
                case 4 -> {
                    int id = lerInteiro("ID da vaga: ");
                    String status = lerTexto("Novo status (LV = livre, EP = em processo): ");
                    System.out.println(vagaController.alterarStatus(id, status)
                            ? "✓ Status atualizado com sucesso!" : "Vaga não encontrada.");
                }
                case 5 -> {
                    int id = lerInteiro("ID da vaga a ser excluída: ");
                    System.out.println(vagaController.deletarVaga(id)
                            ? "✓ Vaga excluída com sucesso!" : "Vaga não encontrada.");
                }
                case 0 -> { }
                default -> System.out.println("Opção inválida!");
            }
        } catch (Exception e) {
            System.out.println("Erro na operação: " + e.getMessage());
        }
    }

    // ========================= FUNCIONÁRIOS =========================

    private void menuFuncionarios() {
        System.out.println("\n--- Funcionários ---");
        System.out.println("1. Admitir funcionário");
        System.out.println("2. Quadro funcional (todos)");
        System.out.println("3. Painel por departamento");
        System.out.println("4. Alterar cargo / departamento do funcionário");
        System.out.println("5. Desligar (excluir) funcionário");
        System.out.println("0. Voltar");
        int opcao = lerInteiro("Escolha uma opção: ");
        try {
            switch (opcao) {
                case 1 -> {
                    String nome = lerTexto("Nome: ");
                    String cpf = lerTexto("CPF (000.000.000-00): ");
                    LocalDate admissao = lerData("Data de admissão (dd/mm/aaaa): ");
                    mostrarLista(departamentoController.listarDepartamentos(), "Nenhum departamento cadastrado.");
                    int idDepartamento = lerInteiro("ID do departamento: ");
                    mostrarLista(cargoController.listarCargos(), "Nenhum cargo cadastrado.");
                    int idCargo = lerInteiro("ID do cargo: ");
                    Funcionario f = funcionarioController.admitirFuncionario(nome, cpf, admissao, idDepartamento, idCargo);
                    System.out.println("✓ Funcionário admitido com o ID " + f.getId());
                }
                case 2 -> mostrarLista(funcionarioController.listarFuncionarios(), "Nenhum funcionário cadastrado.");
                case 3 -> {
                    mostrarLista(departamentoController.listarDepartamentos(), "Nenhum departamento cadastrado.");
                    int idDepartamento = lerInteiro("ID do departamento para filtrar: ");
                    mostrarLista(funcionarioController.listarPorDepartamento(idDepartamento),
                            "Nenhum funcionário nesse departamento.");
                }
                case 4 -> {
                    int id = lerInteiro("ID do funcionário: ");
                    Funcionario existente = funcionarioController.buscarFuncionario(id);
                    if (existente == null) {
                        System.out.println("Funcionário não encontrado.");
                        return;
                    }
                    System.out.println(existente);
                    mostrarLista(departamentoController.listarDepartamentos(), "Nenhum departamento cadastrado.");
                    int idDepartamento = lerInteiro("ID do novo departamento: ");
                    mostrarLista(cargoController.listarCargos(), "Nenhum cargo cadastrado.");
                    int idCargo = lerInteiro("ID do novo cargo: ");
                    funcionarioController.alterarLotacao(id, idDepartamento, idCargo);
                    System.out.println("✓ Funcionário atualizado com sucesso!");
                }
                case 5 -> {
                    int id = lerInteiro("ID do funcionário a ser desligado: ");
                    System.out.println(funcionarioController.desligarFuncionario(id)
                            ? "✓ Funcionário desligado com sucesso!" : "Funcionário não encontrado.");
                }
                case 0 -> { }
                default -> System.out.println("Opção inválida!");
            }
        } catch (Exception e) {
            System.out.println("Erro na operação: " + e.getMessage());
        }
    }

    // ========================= LEITURA DO TECLADO =========================

    private <T> void mostrarLista(List<T> lista, String mensagemVazia) {
        if (lista.isEmpty()) {
            System.out.println(mensagemVazia);
        } else {
            // Sintaxe de Method Reference (System.out::println)
            lista.forEach(System.out::println);
        }
    }

    private String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine();
    }

    private String lerTextoOuManter(String mensagem, String atual) {
        System.out.print(mensagem + " (" + atual + "): ");
        String texto = scanner.nextLine();
        return texto.isBlank() ? atual : texto;
    }

    // Repete a pergunta até a pessoa digitar um número inteiro
    private int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Erro: digite apenas números inteiros.");
            }
        }
    }

    // Aceita vírgula ou ponto (3500,50 ou 3500.50)
    private double lerDecimal(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                return Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Erro: digite um valor numérico, por exemplo 3500,00.");
            }
        }
    }

    private double lerDecimalOuManter(String mensagem, double atual) {
        while (true) {
            System.out.print(mensagem + String.format(" (%.2f): ", atual));
            String texto = scanner.nextLine().trim();
            if (texto.isBlank()) {
                return atual;
            }
            try {
                return Double.parseDouble(texto.replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Erro: digite um valor numérico, por exemplo 3500,00.");
            }
        }
    }

    private LocalDate lerData(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                return LocalDate.parse(scanner.nextLine().trim(), FORMATO_DATA);
            } catch (DateTimeParseException e) {
                System.out.println("Erro: use o formato dd/mm/aaaa, por exemplo 10/03/2026.");
            }
        }
    }
}
