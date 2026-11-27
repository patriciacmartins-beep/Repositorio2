package com.empresa.rh.controller;

import com.empresa.rh.model.Cargo;
import com.empresa.rh.model.CargoDAO;
import java.sql.SQLException;
import java.util.List;

/**
 * CONTROLLER de Cargos - UC01: Cadastrar Cargos (RF01, RN01).
  * confere as regras de negócio e só então pede ao DAO para gravar no banco.
 */
public class CargoController {

    // RN01: salário mínimo vigente. Nenhum cargo pode ter salário base menor que este valor.
    public static final double SALARIO_MINIMO = 1621.00;

    // O DAO é quem conversa com o banco de dados (tabela de cargos).
    private final CargoDAO cargoDAO;

    public CargoController() {
        this.cargoDAO = new CargoDAO();
    }

    // CREATE: valida os dados e cadastra um cargo novo (RF01).
    public Cargo cadastrarCargo(String titulo, double salarioBase) throws SQLException {
        validar(titulo, salarioBase);                              // confere as regras antes de gravar
        Cargo cargo = new Cargo(0, salarioBase, titulo.trim());    // id 0: o banco gera o id (AUTO_INCREMENT)
        cargoDAO.salvar(cargo);                                    // grava no banco
        return cargo;
    }

    // READ: devolve todos os cargos cadastrados.
    public List<Cargo> listarCargos() throws SQLException {
        return cargoDAO.listarTodos();
    }

    // READ: busca um cargo pelo id (devolve null se não existir).
    public Cargo buscarCargo(int id) throws SQLException {
        return cargoDAO.buscarPorId(id);
    }

    // UPDATE: altera o título e/ou o salário, passando pelas mesmas regras do cadastro.
    public boolean atualizarCargo(int id, String titulo, double salarioBase) throws SQLException {
        validar(titulo, salarioBase);
        return cargoDAO.atualizar(new Cargo(id, salarioBase, titulo.trim()));
    }

    // DELETE: exclui um cargo.
    // RNF02: o banco não deixa excluir um cargo que tem funcionários ou vagas ligados a ele.
    // Nesse caso o MySQL devolve o erro 1451 (chave estrangeira), que vira uma mensagem clara.
    public boolean deletarCargo(int id) throws SQLException {
        try {
            return cargoDAO.deletar(id);
        } catch (SQLException e) {
            if (e.getErrorCode() == 1451) {
                throw new IllegalArgumentException("Não é possível excluir: existem funcionários ou vagas com esse cargo (RNF02).");
            }
            throw e; // qualquer outro erro do banco segue adiante
        }
    }

    // Regras de negócio do cargo, usadas no cadastro e na alteração.
    private void validar(String titulo, double salarioBase) {
        // O título é obrigatório (pelo menos 2 letras).
        if (titulo == null || titulo.trim().length() < 2) {
            throw new IllegalArgumentException("Informe o título do cargo.");
        }
        // RN01: o salário base não pode ser menor que o salário mínimo vigente.
        if (salarioBase < SALARIO_MINIMO) {
            throw new IllegalArgumentException(String.format(
                    "O salário base não pode ser menor que o salário mínimo vigente (R$ %.2f) (RN01).", SALARIO_MINIMO));
        }
    }
}