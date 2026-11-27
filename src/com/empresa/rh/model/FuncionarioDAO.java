package com.empresa.rh.model;

import com.empresa.rh.util.ConnectionFactory;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// Acesso aos dados da tabela TB_SRH_FUNCIONARIO (CRUD)
public class FuncionarioDAO {

    // SELECT com INNER JOIN: o funcionário junto com o cargo e o departamento
    private static final String SELECT_FUNCIONARIO =
            "SELECT f.id_funcionario, f.nm_nome, f.nr_cpf, f.dt_admissao, "
            + "c.id_cargo, c.nm_titulo, c.vl_salario_base, "
            + "d.id_departamento, d.nm_departamento, d.vl_orcamento "
            + "FROM TB_SRH_FUNCIONARIO f "
            + "INNER JOIN TB_SRH_CARGO c ON c.id_cargo = f.id_cargo "
            + "INNER JOIN TB_SRH_DEPARTAMENTO d ON d.id_departamento = f.id_departamento ";

    public void salvar(Funcionario funcionario) throws SQLException {
        String sql = "INSERT INTO TB_SRH_FUNCIONARIO (id_departamento, id_cargo, nm_nome, nr_cpf, dt_admissao) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, funcionario.getDepartamento().getId());
            stmt.setInt(2, funcionario.getCargo().getId());
            stmt.setString(3, funcionario.getNome());
            stmt.setString(4, funcionario.getCpf());
            stmt.setDate(5, Date.valueOf(funcionario.getAdmissao()));
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    funcionario.setId(rs.getInt(1));
                }
            }
        }
    }

    // UC04 - Consultar Quadro Funcional
    public List<Funcionario> listarTodos() throws SQLException {
        List<Funcionario> funcionarios = new ArrayList<>();
        String sql = SELECT_FUNCIONARIO + "ORDER BY f.nm_nome";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                funcionarios.add(montarFuncionario(rs));
            }
        }
        return funcionarios;
    }

    // Painel de Gestão de Pessoas com filtro por departamento
    public List<Funcionario> listarPorDepartamento(int idDepartamento) throws SQLException {
        List<Funcionario> funcionarios = new ArrayList<>();
        String sql = SELECT_FUNCIONARIO + "WHERE f.id_departamento = ? ORDER BY f.nm_nome";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idDepartamento);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    funcionarios.add(montarFuncionario(rs));
                }
            }
        }
        return funcionarios;
    }

    public Funcionario buscarPorId(int id) throws SQLException {
        String sql = SELECT_FUNCIONARIO + "WHERE f.id_funcionario = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montarFuncionario(rs);
                }
            }
        }
        return null;
    }

    // RN02: confere no banco se o CPF já está cadastrado
    public boolean existeCpf(String cpf) throws SQLException {
        String sql = "SELECT COUNT(*) FROM TB_SRH_FUNCIONARIO WHERE nr_cpf = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    public boolean atualizar(Funcionario funcionario) throws SQLException {
        String sql = "UPDATE TB_SRH_FUNCIONARIO SET id_departamento = ?, id_cargo = ?, nm_nome = ? "
                + "WHERE id_funcionario = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, funcionario.getDepartamento().getId());
            stmt.setInt(2, funcionario.getCargo().getId());
            stmt.setString(3, funcionario.getNome());
            stmt.setInt(4, funcionario.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deletar(int id) throws SQLException {
        String sql = "DELETE FROM TB_SRH_FUNCIONARIO WHERE id_funcionario = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // Transforma uma linha do SELECT em um objeto Funcionario (com o Cargo e o Departamento dentro)
    private Funcionario montarFuncionario(ResultSet rs) throws SQLException {
        Cargo cargo = new Cargo(rs.getInt("id_cargo"), rs.getDouble("vl_salario_base"), rs.getString("nm_titulo"));
        Departamento departamento = new Departamento(rs.getInt("id_departamento"),
                rs.getString("nm_departamento"), rs.getDouble("vl_orcamento"));
        return new Funcionario(rs.getInt("id_funcionario"), rs.getString("nm_nome"), rs.getString("nr_cpf"),
                rs.getDate("dt_admissao").toLocalDate(), cargo, departamento);
    }
}
