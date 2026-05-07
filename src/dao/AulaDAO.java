package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import model.Aula;
import util.Conexao;

public class AulaDAO {

    public ArrayList<Aula> listarAulas() {
        ArrayList<Aula> lista = new ArrayList<>();
        String sql = "SELECT * FROM aulas ORDER BY id";

        try (
            Connection conn = Conexao.ligar();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
        ) {
            while (rs.next()) {
                Aula a = new Aula();
                a.setId(rs.getInt("id"));
                a.setNome(rs.getString("nome"));
                a.setDataAula(rs.getString("data_aula"));
                a.setHoraAula(rs.getString("hora_aula"));
                a.setVagas(rs.getInt("vagas"));
                lista.add(a);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean inserir(Aula a) {
        String sql = "INSERT INTO aulas (nome, data_aula, hora_aula, vagas) VALUES (?, ?, ?, ?)";

        try (
            Connection conn = Conexao.ligar();
            PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            ps.setString(1, a.getNome());
            ps.setString(2, a.getDataAula());
            ps.setString(3, a.getHoraAula());
            ps.setInt(4, a.getVagas());

            int linhas = ps.executeUpdate();
            return linhas > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean atualizar(Aula a) {
        String sql = "UPDATE aulas SET nome = ?, data_aula = ?, hora_aula = ?, vagas = ? WHERE id = ?";

        try (
            Connection conn = Conexao.ligar();
            PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            ps.setString(1, a.getNome());
            ps.setString(2, a.getDataAula());
            ps.setString(3, a.getHoraAula());
            ps.setInt(4, a.getVagas());
            ps.setInt(5, a.getId());

            int linhas = ps.executeUpdate();
            return linhas > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean apagar(int id) {
        String apagarReservas = "DELETE FROM reservas WHERE id_aula = ?";
        String apagarAula = "DELETE FROM aulas WHERE id = ?";

        try (Connection conn = Conexao.ligar()) {

            conn.setAutoCommit(false);

            try (
                PreparedStatement ps1 = conn.prepareStatement(apagarReservas);
                PreparedStatement ps2 = conn.prepareStatement(apagarAula);
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
    
    public Aula buscarAulaPorId(int id) {
        String sql = "SELECT * FROM aulas WHERE id = ?";

        try (Connection conn = Conexao.ligar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Aula a = new Aula();
                a.setId(rs.getInt("id"));
                a.setNome(rs.getString("nome"));
                a.setDataAula(rs.getString("data_aula"));
                a.setHoraAula(rs.getString("hora_aula"));
                a.setVagas(rs.getInt("vagas"));
                return a;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean atualizarParcial(Aula a) {
        String sql = "UPDATE aulas SET nome = ?, data_aula = ?, hora_aula = ?, vagas = ? WHERE id = ?";

        try (Connection conn = Conexao.ligar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, a.getNome());
            ps.setString(2, a.getDataAula());
            ps.setString(3, a.getHoraAula());
            ps.setInt(4, a.getVagas());
            ps.setInt(5, a.getId());

            int linhas = ps.executeUpdate();
            return linhas > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
}