package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import model.Aula;
import util.Conexao;

public class ReservaDAO {

    public boolean reservar(int idUtilizador, int idAula) {
        String verificarUtilizador = "SELECT id FROM utilizadores WHERE id = ?";
        String verificarReserva = "SELECT * FROM reservas WHERE id_utilizador = ? AND id_aula = ?";
        String verificarVagas = "SELECT vagas FROM aulas WHERE id = ?";
        String inserirReserva = "INSERT INTO reservas (id_utilizador, id_aula) VALUES (?, ?)";
        String atualizarVagas = "UPDATE aulas SET vagas = vagas - 1 WHERE id = ? AND vagas > 0";

        try (Connection conn = Conexao.ligar()) {

            // Verifica se o utilizador ainda existe
            try (PreparedStatement psUtilizador = conn.prepareStatement(verificarUtilizador)) {
                psUtilizador.setInt(1, idUtilizador);
                ResultSet rsUtilizador = psUtilizador.executeQuery();

                if (!rsUtilizador.next()) {
                    return false;
                }
            }

            // Verifica se já existe reserva para essa aula
            try (PreparedStatement psReservaExistente = conn.prepareStatement(verificarReserva)) {
                psReservaExistente.setInt(1, idUtilizador);
                psReservaExistente.setInt(2, idAula);
                ResultSet rsReserva = psReservaExistente.executeQuery();

                if (rsReserva.next()) {
                    return false;
                }
            }

            // Verifica se a aula existe e se tem vagas
            try (PreparedStatement psVerifica = conn.prepareStatement(verificarVagas)) {
                psVerifica.setInt(1, idAula);
                ResultSet rs = psVerifica.executeQuery();

                if (rs.next()) {
                    int vagas = rs.getInt("vagas");
                    if (vagas <= 0) {
                        return false;
                    }
                } else {
                    return false; // aula não existe
                }
            }

            // Insere a reserva
            try (PreparedStatement psInserir = conn.prepareStatement(inserirReserva)) {
                psInserir.setInt(1, idUtilizador);
                psInserir.setInt(2, idAula);
                psInserir.executeUpdate();
            }

            // Atualiza vagas
            try (PreparedStatement psUpdate = conn.prepareStatement(atualizarVagas)) {
                psUpdate.setInt(1, idAula);
                int linhasAfetadas = psUpdate.executeUpdate();

                if (linhasAfetadas <= 0) {
                    return false;
                }
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cancelar(int idUtilizador, int idAula) {
        String verificarUtilizador = "SELECT id FROM utilizadores WHERE id = ?";
        String apagarReserva = "DELETE FROM reservas WHERE id_utilizador = ? AND id_aula = ?";
        String aumentarVagas = "UPDATE aulas SET vagas = vagas + 1 WHERE id = ?";

        try (Connection conn = Conexao.ligar()) {

            // Verifica se o utilizador ainda existe
            try (PreparedStatement psUtilizador = conn.prepareStatement(verificarUtilizador)) {
                psUtilizador.setInt(1, idUtilizador);
                ResultSet rsUtilizador = psUtilizador.executeQuery();

                if (!rsUtilizador.next()) {
                    return false;
                }
            }

            // Apaga a reserva
            try (PreparedStatement psDelete = conn.prepareStatement(apagarReserva)) {
                psDelete.setInt(1, idUtilizador);
                psDelete.setInt(2, idAula);

                int linhas = psDelete.executeUpdate();

                if (linhas > 0) {
                    // Devolve a vaga
                    try (PreparedStatement psUpdate = conn.prepareStatement(aumentarVagas)) {
                        psUpdate.setInt(1, idAula);
                        psUpdate.executeUpdate();
                    }
                    return true;
                }
            }

            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Aula> listarReservadas(int idUtilizador) {
        ArrayList<Aula> lista = new ArrayList<>();

        String sql = "SELECT a.id, a.nome, a.data_aula, a.hora_aula, a.vagas " +
                     "FROM aulas a " +
                     "INNER JOIN reservas r ON a.id = r.id_aula " +
                     "WHERE r.id_utilizador = ?";

        try (Connection conn = Conexao.ligar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUtilizador);
            ResultSet rs = ps.executeQuery();

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
}