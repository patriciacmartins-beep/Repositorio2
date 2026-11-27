package com.empresa.rh.model;

import com.empresa.rh.util.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// Acesso aos dados da tabela TB_SRH_VAGA (CRUD)
public class VagaDAO {

    // SELECT com INNER JOIN para trazer o nome do cargo junto com a vaga
    private static final String SELECT_VAGA =
            "SELECT v.id_vaga, v.vl_quantidade, v.st_vaga, c.id_cargo, c.nm_titulo, c.vl_salario_base "
            + "FROM TB_SRH_VAGA v "
            + "INNER JOIN TB_SRH_CARGO c ON c.id_cargo = v.id_cargo ";

    public void salvar(Vaga vaga) throws SQLException {
        String sql = "INSERT INTO TB_SRH_VAGA (id_cargo, vl_quantidade, st_vaga) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, vaga.getCargo().getId());
            stmt.setInt(2, vaga.getQuantidade());
            stmt.setString(3, vaga.getStatus());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    vaga.setVaga(rs.getInt(1));
                }
            }
        }
    }

    public List<Vaga> listarTodas() throws SQLException {
        return listar(SELECT_VAGA + "ORDER BY v.id_vaga");
    }

    // Portal de vagas abertas: só as vagas livres (LV)
    public List<Vaga> listarAbertas() throws SQLException {
        return listar(SELECT_VAGA + "WHERE v.st_vaga = 'LV' ORDER BY v.id_vaga");
    }

    public Vaga buscarPorId(int id) throws SQLException {
        String sql = SELECT_VAGA + "WHERE v.id_vaga = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montarVaga(rs);
                }
            }
        }
        return null;
    }

    public boolean atualizar(Vaga vaga) throws SQLException {
        String sql = "UPDATE TB_SRH_VAGA SET id_cargo = ?, vl_quantidade = ?, st_vaga = ? WHERE id_vaga = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vaga.getCargo().getId());
            stmt.setInt(2, vaga.getQuantidade());
            stmt.setString(3, vaga.getStatus());
            stmt.setInt(4, vaga.getVaga());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deletar(int id) throws SQLException {
        String sql = "DELETE FROM TB_SRH_VAGA WHERE id_vaga = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private List<Vaga> listar(String sql) throws SQLException {
        List<Vaga> vagas = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                vagas.add(montarVaga(rs));
            }
        }
        return vagas;
    }

    // Transforma uma linha do SELECT em um objeto Vaga (com o Cargo dentro)
    private Vaga montarVaga(ResultSet rs) throws SQLException {
        Cargo cargo = new Cargo(rs.getInt("id_cargo"), rs.getDouble("vl_salario_base"), rs.getString("nm_titulo"));
        return new Vaga(rs.getInt("vl_quantidade"), cargo, rs.getInt("id_vaga"), rs.getString("st_vaga"));
    }
}
