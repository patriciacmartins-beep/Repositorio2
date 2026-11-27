package com.empresa.rh.model;

import com.empresa.rh.util.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// Acesso aos dados da tabela TB_SRH_DEPARTAMENTO (CRUD)
public class DepartamentoDAO {

    public void salvar(Departamento departamento) throws SQLException {
        String sql = "INSERT INTO TB_SRH_DEPARTAMENTO (nm_departamento, vl_orcamento) VALUES (?, ?)";
        // Statement.RETURN_GENERATED_KEYS avisa ao MySQL para retornar o ID gerado pelo AUTO_INCREMENT
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, departamento.getNome());
            stmt.setDouble(2, departamento.getOrcamento());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    departamento.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<Departamento> listarTodos() throws SQLException {
        List<Departamento> departamentos = new ArrayList<>();
        String sql = "SELECT id_departamento, nm_departamento, vl_orcamento FROM TB_SRH_DEPARTAMENTO ORDER BY id_departamento";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                departamentos.add(new Departamento(
                        rs.getInt("id_departamento"),
                        rs.getString("nm_departamento"),
                        rs.getDouble("vl_orcamento")));
            }
        }
        return departamentos;
    }

    public Departamento buscarPorId(int id) throws SQLException {
        String sql = "SELECT id_departamento, nm_departamento, vl_orcamento FROM TB_SRH_DEPARTAMENTO WHERE id_departamento = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Departamento(
                            rs.getInt("id_departamento"),
                            rs.getString("nm_departamento"),
                            rs.getDouble("vl_orcamento"));
                }
            }
        }
        return null;
    }

    public boolean atualizar(Departamento departamento) throws SQLException {
        String sql = "UPDATE TB_SRH_DEPARTAMENTO SET nm_departamento = ?, vl_orcamento = ? WHERE id_departamento = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, departamento.getNome());
            stmt.setDouble(2, departamento.getOrcamento());
            stmt.setInt(3, departamento.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deletar(int id) throws SQLException {
        String sql = "DELETE FROM TB_SRH_DEPARTAMENTO WHERE id_departamento = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}
