package com.empresa.rh.controller;

import com.empresa.rh.model.Departamento;
import com.empresa.rh.model.DepartamentoDAO;
import java.sql.SQLException;
import java.util.List;

/**
 * CONTROLLER de Departamentos - UC01: Cadastrar Departamentos.
 * confere as regras e só então pede ao DAO para gravar no banco.
 */
public class DepartamentoController {

    // O DAO é quem conversa com o banco de dados (tabela de departamentos).
    private final DepartamentoDAO departamentoDAO;

    public DepartamentoController() {
        this.departamentoDAO = new DepartamentoDAO();
    }

    // CREATE: valida os dados e cadastra um departamento novo.
    public Departamento cadastrarDepartamento(String nome, double orcamento) throws SQLException {
        validar(nome, orcamento);                                          // confere as regras antes de gravar
        Departamento departamento = new Departamento(0, nome.trim(), orcamento); // id 0: o banco gera o id
        departamentoDAO.salvar(departamento);                              // grava no banco
        return departamento;
    }

    // READ: devolve todos os departamentos cadastrados.
    public List<Departamento> listarDepartamentos() throws SQLException {
        return departamentoDAO.listarTodos();
    }

    // READ: busca um departamento pelo id (devolve null se não existir).
    public Departamento buscarDepartamento(int id) throws SQLException {
        return departamentoDAO.buscarPorId(id);
    }

    // UPDATE: altera o nome e/ou o orçamento, passando pelas mesmas regras do cadastro.
    public boolean atualizarDepartamento(int id, String nome, double orcamento) throws SQLException {
        validar(nome, orcamento);
        return departamentoDAO.atualizar(new Departamento(id, nome.trim(), orcamento));
    }

    // DELETE: exclui um departamento.
    // O banco não deixa excluir um departamento que tem funcionários (relacionamento 1:N).
    
    public boolean deletarDepartamento(int id) throws SQLException {
        try {
            return departamentoDAO.deletar(id);
        } catch (SQLException e) {
            if (e.getErrorCode() == 1451) {
                throw new IllegalArgumentException("Não é possível excluir: existem funcionários nesse departamento.");
            }
            throw e; // qualquer outro erro do banco segue adiante
        }
    }

    // Regras do departamento, usadas no cadastro e na alteração.
    private void validar(String nome, double orcamento) {
        // O nome é obrigatório (pelo menos 2 letras).
        if (nome == null || nome.trim().length() < 2) {
            throw new IllegalArgumentException("Informe o nome do departamento.");
        }
        // O orçamento não pode ser negativo.
        if (orcamento < 0) {
            throw new IllegalArgumentException("O orçamento não pode ser negativo.");
        }
    }
}