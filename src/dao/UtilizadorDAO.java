package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import model.Utilizador;
import util.Conexao;

public class UtilizadorDAO {

    public Utilizador login(String email, String password) {
        String sql = "SELECT * FROM utilizadores WHERE email = ? AND password = ?";

        try (
            Connection conn = Conexao.ligar();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Utilizador u = new Utilizador();
                    u.setId(rs.getInt("id"));
                    u.setNome(rs.getString("nome"));
                    u.setEmail(rs.getString("email"));
                    u.setPassword(rs.getString("password"));
                    u.setTipo(rs.getString("tipo"));
                    return u;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean registar(Utilizador u) {
        String verificar = "SELECT id FROM utilizadores WHERE email = ?";
        String inserir = "INSERT INTO utilizadores (nome, email, password, tipo) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.ligar()) {
            if (conn == null) {
                return false;
            }

            try (PreparedStatement psVerificar = conn.prepareStatement(verificar)) {
                psVerificar.setString(1, u.getEmail());

                try (ResultSet rs = psVerificar.executeQuery()) {
                    if (rs.next()) {
                        return false;
                    }
                }
            }

            try (PreparedStatement psInserir = conn.prepareStatement(inserir)) {
                psInserir.setString(1, u.getNome());
                psInserir.setString(2, u.getEmail());
                psInserir.setString(3, u.getPassword());
                psInserir.setString(4, u.getTipo());

                int linhas = psInserir.executeUpdate();
                return linhas > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Utilizador> listarAlunos() {
        ArrayList<Utilizador> lista = new ArrayList<>();
        String sql = "SELECT * FROM utilizadores WHERE tipo = 'cliente' ORDER BY id";

        try (
            Connection conn = Conexao.ligar();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Utilizador u = new Utilizador();
                    u.setId(rs.getInt("id"));
                    u.setNome(rs.getString("nome"));
                    u.setEmail(rs.getString("email"));
                    u.setPassword(rs.getString("password"));
                    u.setTipo(rs.getString("tipo"));
                    lista.add(u);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean atualizarAluno(Utilizador u) {
        String verificar = "SELECT id FROM utilizadores WHERE email = ? AND id <> ?";
        String sql = "UPDATE utilizadores SET nome = ?, email = ?, password = ? WHERE id = ? AND tipo = 'cliente'";

        try (Connection conn = Conexao.ligar()) {
            if (conn == null) {
                return false;
            }

            try (PreparedStatement psVerificar = conn.prepareStatement(verificar)) {
                psVerificar.setString(1, u.getEmail());
                psVerificar.setInt(2, u.getId());

                try (ResultSet rs = psVerificar.executeQuery()) {
                    if (rs.next()) {
                        return false;
                    }
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, u.getNome());
                ps.setString(2, u.getEmail());
                ps.setString(3, u.getPassword());
                ps.setInt(4, u.getId());

                int linhas = ps.executeUpdate();
                return linhas > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean apagarAluno(int id) {
        String apagarReservas = "DELETE FROM reservas WHERE id_utilizador = ?";
        String apagarAluno = "DELETE FROM utilizadores WHERE id = ? AND tipo = 'cliente'";

        try (Connection conn = Conexao.ligar()) {
            if (conn == null) {
                return false;
            }

            conn.setAutoCommit(false);

            try (
                PreparedStatement ps1 = conn.prepareStatement(apagarReservas);
                PreparedStatement ps2 = conn.prepareStatement(apagarAluno)
            ) {
                ps1.setInt(1, id);
                ps1.executeUpdate();

                ps2.setInt(1, id);
                int linhas = ps2.executeUpdate();

                conn.commit();
                return linhas > 0;

            } catch (Exception e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean adicionarAluno(Utilizador u) {
        String verificar = "SELECT id FROM utilizadores WHERE email = ?";
        String inserir = "INSERT INTO utilizadores (nome, email, password, tipo) VALUES (?, ?, ?, 'cliente')";

        try (Connection conn = Conexao.ligar()) {
            if (conn == null) {
                return false;
            }

            try (PreparedStatement psVerificar = conn.prepareStatement(verificar)) {
                psVerificar.setString(1, u.getEmail());

                try (ResultSet rs = psVerificar.executeQuery()) {
                    if (rs.next()) {
                        return false;
                    }
                }
            }

            try (PreparedStatement psInserir = conn.prepareStatement(inserir)) {
                psInserir.setString(1, u.getNome());
                psInserir.setString(2, u.getEmail());
                psInserir.setString(3, u.getPassword());

                int linhas = psInserir.executeUpdate();
                return linhas > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean existeUtilizador(int id) {
        String sql = "SELECT id FROM utilizadores WHERE id = ?";

        try (
            Connection conn = Conexao.ligar();
            PreparedStatement ps = conn.prepareStatement(sql); 
        ) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public Utilizador buscarAlunoPorId(int id) {
        String sql = "SELECT * FROM utilizadores WHERE id = ? AND tipo = 'cliente'";

        try (Connection conn = Conexao.ligar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Utilizador u = new Utilizador();
                u.setId(rs.getInt("id"));
                u.setNome(rs.getString("nome"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setTipo(rs.getString("tipo"));
                return u;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean atualizarAlunoParcial(Utilizador u) {
        String verificar = "SELECT id FROM utilizadores WHERE email = ? AND id <> ?";
        String sql = "UPDATE utilizadores SET nome = ?, email = ?, password = ? WHERE id = ? AND tipo = 'cliente'";

        try (Connection conn = Conexao.ligar()) {

            if (conn == null) {
                return false;
            }

            try (PreparedStatement psVerificar = conn.prepareStatement(verificar)) {
                psVerificar.setString(1, u.getEmail());
                psVerificar.setInt(2, u.getId());

                try (ResultSet rs = psVerificar.executeQuery()) {
                    if (rs.next()) {
                        return false;
                    }
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, u.getNome());
                ps.setString(2, u.getEmail());
                ps.setString(3, u.getPassword());
                ps.setInt(4, u.getId());

                int linhas = ps.executeUpdate();
                return linhas > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }   
    
}