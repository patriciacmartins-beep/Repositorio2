package com.empresa.rh.controller;

import com.empresa.rh.model.Cargo;
import com.empresa.rh.model.CargoDAO;
import com.empresa.rh.model.Departamento;
import com.empresa.rh.model.DepartamentoDAO;
import com.empresa.rh.model.Funcionario;
import com.empresa.rh.model.FuncionarioDAO;
import com.empresa.rh.util.ValidadorCpf;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * CONTROLLER de Funcionários - o caso de uso principal do sistema:
 * UC03 - Admitir Funcionário (RF02, RF03, RF05, RN02, RN03, RN05)
 * UC04 - Consultar Quadro Funcional (RF03).
 * Fica entre a View (menu) e o Model (os DAOs): confere todas as regras
 */
public class FuncionarioController {

    // Três DAOs: o de funcionários grava a admissão;
    // os de cargos e departamentos conferem se o que foi escolhido existe.
    private final FuncionarioDAO funcionarioDAO;
    private final CargoDAO cargoDAO;
    private final DepartamentoDAO departamentoDAO;

    public FuncionarioController() {
        this.funcionarioDAO = new FuncionarioDAO();
        this.cargoDAO = new CargoDAO();
        this.departamentoDAO = new DepartamentoDAO();
    }

    // CREATE: fluxo principal do UC03 - Admitir Funcionário.
    // As regras são conferidas na mesma ordem do fluxo descrito no caso de uso.
    public Funcionario admitirFuncionario(String nome, String cpf, LocalDate admissao,
        int idDepartamento, int idCargo) throws SQLException {
        // Passo 1: o operador informa os dados do candidato. O nome é obrigatório.
        if (nome == null || nome.trim().length() < 3) {
            throw new IllegalArgumentException("Informe o nome completo do funcionário.");
        }

        // RNF01: confere o formato (000.000.000-00) e os dígitos verificadores do CPF.
        if (!ValidadorCpf.formatoValido(cpf)) {
            throw new IllegalArgumentException("CPF fora do formato 000.000.000-00 (RNF01).");
        }
        if (!ValidadorCpf.digitosValidos(cpf)) {
            throw new IllegalArgumentException("CPF inválido: os dígitos verificadores não conferem (RNF01).");
        }

        // Passo 2 - RN02 (RF02): não pode haver dois funcionários com o mesmo CPF.
        if (funcionarioDAO.existeCpf(cpf)) {
            throw new IllegalArgumentException("Já existe um funcionário com o CPF " + cpf + " (RN02).");
        }

        // Passo 3 - RN03 (RF03): o funcionário precisa de um departamento E de um cargo ao mesmo tempo.
        Departamento departamento = departamentoDAO.buscarPorId(idDepartamento);
        Cargo cargo = cargoDAO.buscarPorId(idCargo);
        if (departamento == null || cargo == null) {
            throw new IllegalArgumentException("Selecione um departamento e um cargo existentes (RN03).");
        }

        // Passo 4 - RN05 (RF05): a data de admissão não pode ser depois de hoje.
        if (admissao == null || admissao.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data de admissão não pode ser futura (RN05).");
        }

        // Passo 5: passou em todas as regras, então salva o colaborador no banco.
        Funcionario funcionario = new Funcionario(0, nome.trim(), cpf, admissao, cargo, departamento);
        funcionarioDAO.salvar(funcionario);
        return funcionario; // pós-condição: funcionário alocado no quadro ativo
    }

    // READ: UC04 - Consultar Quadro Funcional (todos os funcionários).
    public List<Funcionario> listarFuncionarios() throws SQLException {
        return funcionarioDAO.listarTodos();
    }

    // READ: Painel de Gestão de Pessoas com filtro por departamento.
    public List<Funcionario> listarPorDepartamento(int idDepartamento) throws SQLException {
        return funcionarioDAO.listarPorDepartamento(idDepartamento);
    }

    // READ: busca um funcionário pelo id (devolve null se não existir).
    public Funcionario buscarFuncionario(int id) throws SQLException {
        return funcionarioDAO.buscarPorId(id);
    }

    // UPDATE: troca o cargo e/ou o departamento do funcionário.
    // Continua valendo a RN03: o novo departamento e o novo cargo têm que existir.
    public boolean alterarLotacao(int idFuncionario, int idDepartamento, int idCargo) throws SQLException {
        Funcionario funcionario = funcionarioDAO.buscarPorId(idFuncionario);
        if (funcionario == null) {
            return false; // funcionário não encontrado
        }
        Departamento departamento = departamentoDAO.buscarPorId(idDepartamento);
        Cargo cargo = cargoDAO.buscarPorId(idCargo);
        if (departamento == null || cargo == null) {
            throw new IllegalArgumentException("Selecione um departamento e um cargo existentes (RN03).");
        }
        funcionario.setDepartamento(departamento);
        funcionario.setCargo(cargo);
        return funcionarioDAO.atualizar(funcionario);
    }

    // DELETE: desliga (exclui) o funcionário do quadro.
    public boolean desligarFuncionario(int id) throws SQLException {
        return funcionarioDAO.deletar(id);
    }
}