package com.empresa.rh.model;

import com.empresa.rh.util.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// Acesso aos dados da tabela TB_SRH_CARGO (CRUD)
public class CargoDAO {

    public void salvar(Cargo cargo) throws SQLException {
        String sql = "INSERT INTO TB_SRH_CARGO (nm_titulo, vl_salario_base) VALUES (?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cargo.getTitulo());
            stmt.setDouble(2, cargo.getSalarioBase());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    cargo.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<Cargo> listarTodos() throws SQLException {
        List<Cargo> cargos = new ArrayList<>();
        String sql = "SELECT id_cargo, nm_titulo, vl_salario_base FROM TB_SRH_CARGO ORDER BY id_cargo";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                cargos.add(new Cargo(
                        rs.getInt("id_cargo"),
                        rs.getDouble("vl_salario_base"),
                        rs.getString("nm_titulo")));
            }
        }
        return cargos;
    }

    public Cargo buscarPorId(int id) throws SQLException {
        String sql = "SELECT id_cargo, nm_titulo, vl_salario_base FROM TB_SRH_CARGO WHERE id_cargo = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Cargo(
                            rs.getInt("id_cargo"),
                            rs.getDouble("vl_salario_base"),
                            rs.getString("nm_titulo"));
                }
            }
        }
        return null;
    }

    public boolean atualizar(Cargo cargo) throws SQLException {
        String sql = "UPDATE TB_SRH_CARGO SET nm_titulo = ?, vl_salario_base = ? WHERE id_cargo = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cargo.getTitulo());
            stmt.setDouble(2, cargo.getSalarioBase());
            stmt.setInt(3, cargo.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deletar(int id) throws SQLException {
        String sql = "DELETE FROM TB_SRH_CARGO WHERE id_cargo = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}
