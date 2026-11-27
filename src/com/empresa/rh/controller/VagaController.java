package com.empresa.rh.controller;

import com.empresa.rh.model.Cargo;
import com.empresa.rh.model.CargoDAO;
import com.empresa.rh.model.Vaga;
import com.empresa.rh.model.VagaDAO;
import java.sql.SQLException;
import java.util.List;

/**
 * CONTROLLER de Vagas - UC02: Publicar Quadro de Vagas (RF04, RN04).
 * confere as regras e só então pede ao DAO para gravar no banco.
 */
public class VagaController {

    // Dois DAOs: o de vagas grava a vaga, o de cargos confere se o cargo existe.
    private final VagaDAO vagaDAO;
    private final CargoDAO cargoDAO;

    public VagaController() {
        this.vagaDAO = new VagaDAO();
        this.cargoDAO = new CargoDAO();
    }

    // CREATE: publica uma vaga nova para um cargo (RF04).
    public Vaga publicarVaga(int idCargo, int quantidade) throws SQLException {
        // RN04: a quantidade de posições tem que ser um inteiro maior que zero.
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade de posições tem que ser maior que zero (RN04).");
        }
        // Toda vaga pertence a um cargo (relacionamento Cargo 1:N Vaga): o cargo tem que existir.
        Cargo cargo = cargoDAO.buscarPorId(idCargo);
        if (cargo == null) {
            throw new IllegalArgumentException("Cargo " + idCargo + " não encontrado.");
        }
        Vaga vaga = new Vaga(quantidade, cargo, 0, "LV"); // toda vaga nova nasce livre (LV)
        vagaDAO.salvar(vaga);                             // grava no banco
        return vaga;
    }

    // READ: devolve todas as vagas, abertas ou não.
    public List<Vaga> listarVagas() throws SQLException {
        return vagaDAO.listarTodas();
    }

    // READ: Portal de Vagas abertas (tela do roteiro de navegação).
    public List<Vaga> listarVagasAbertas() throws SQLException {
        return vagaDAO.listarAbertas();
    }

    // UPDATE: muda o status da vaga.
    // LV = livre para ser preenchida, EP = em processo de recrutamento.
    public boolean alterarStatus(int idVaga, String novoStatus) throws SQLException {
        // Aceita "lv" ou "ep" em minúsculas e tira os espaços.
        String status = novoStatus == null ? "" : novoStatus.trim().toUpperCase();
        if (!status.equals("LV") && !status.equals("EP")) {
            throw new IllegalArgumentException("Status inválido. Use LV (livre) ou EP (em processo).");
        }
        Vaga vaga = vagaDAO.buscarPorId(idVaga);
        if (vaga == null) {
            return false; // vaga não encontrada
        }
        vaga.setStatus(status);
        return vagaDAO.atualizar(vaga);
    }

    // DELETE: exclui uma vaga.
    // Se houver registros ligados a ela, o MySQL devolve o erro 1451 (chave estrangeira).
    public boolean deletarVaga(int id) throws SQLException {
        try {
            return vagaDAO.deletar(id);
        } catch (SQLException e) {
            if (e.getErrorCode() == 1451) {
                throw new IllegalArgumentException("Não é possível excluir: existem candidaturas para essa vaga.");
            }
            throw e; // qualquer outro erro do banco segue adiante
        }
    }
}