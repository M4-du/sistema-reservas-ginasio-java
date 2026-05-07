package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;

import dao.AulaDAO;
import dao.ReservaDAO;
import model.Aula;
import model.Utilizador;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class ClienteFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtIdAula;
    private JTextArea txtDisponiveis;
    private JTextArea txtReservadas;
    private Utilizador utilizador;

    public ClienteFrame(Utilizador utilizador) {
        this.utilizador = utilizador;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 750, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        setTitle("Área do Cliente - " + utilizador.getNome());

        JLabel lblDisponiveis = new JLabel("Aulas Disponíveis");
        lblDisponiveis.setBounds(30, 20, 150, 20);
        contentPane.add(lblDisponiveis);

        txtDisponiveis = new JTextArea();
        txtDisponiveis.setBounds(30, 45, 300, 220);
        contentPane.add(txtDisponiveis);

        JLabel lblReservadas = new JLabel("Aulas Reservadas");
        lblReservadas.setBounds(390, 20, 150, 20);
        contentPane.add(lblReservadas);

        txtReservadas = new JTextArea();
        txtReservadas.setBounds(390, 45, 300, 220);
        contentPane.add(txtReservadas);

        JButton btnListar = new JButton("Atualizar Listas");
        btnListar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listarAulasDisponiveis();
                listarAulasReservadas();
            }
        });
        btnListar.setBounds(30, 290, 150, 30);
        contentPane.add(btnListar);

        JLabel lblIdAula = new JLabel("ID Aula:");
        lblIdAula.setBounds(30, 350, 60, 25);
        contentPane.add(lblIdAula);

        txtIdAula = new JTextField();
        txtIdAula.setBounds(100, 350, 100, 25);
        contentPane.add(txtIdAula);
        txtIdAula.setColumns(10);

        JButton btnReservar = new JButton("Reservar");
        btnReservar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int idAula = Integer.parseInt(txtIdAula.getText());

                    ReservaDAO dao = new ReservaDAO();
                    boolean sucesso = dao.reservar(utilizador.getId(), idAula);

                    if (sucesso) {
                        JOptionPane.showMessageDialog(null, "Reserva feita com sucesso");
                        listarAulasDisponiveis();
                        listarAulasReservadas();
                    } else {
                        JOptionPane.showMessageDialog(null, "Não foi possível reservar. Verifica o ID, vagas ou se já reservaste essa aula.");
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Introduce um ID válido.");
                }
            }
        });
        btnReservar.setBounds(230, 348, 120, 30);
        contentPane.add(btnReservar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int idAula = Integer.parseInt(txtIdAula.getText());

                    ReservaDAO dao = new ReservaDAO();
                    boolean sucesso = dao.cancelar(utilizador.getId(), idAula);

                    if (sucesso) {
                        JOptionPane.showMessageDialog(null, "Reserva cancelada com sucesso");
                        listarAulasDisponiveis();
                        listarAulasReservadas();
                    } else {
                        JOptionPane.showMessageDialog(null, "Não foi possível cancelar. Verifica se essa aula está reservada.");
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Introduce um ID válido.");
                }
            }
        });
        btnCancelar.setBounds(380, 348, 120, 30);
        contentPane.add(btnCancelar);

        listarAulasDisponiveis();
        listarAulasReservadas();
    }

    private void listarAulasDisponiveis() {
        AulaDAO dao = new AulaDAO();
        ArrayList<Aula> lista = dao.listarAulas();

        txtDisponiveis.setText("");

        for (Aula a : lista) {
            txtDisponiveis.append("ID: " + a.getId()
                    + " | " + a.getNome()
                    + " | " + a.getDataAula()
                    + " | " + a.getHoraAula()
                    + " | Vagas: " + a.getVagas() + "\n");
        }
    }

    private void listarAulasReservadas() {
        ReservaDAO dao = new ReservaDAO();
        ArrayList<Aula> lista = dao.listarReservadas(utilizador.getId());

        txtReservadas.setText("");

        for (Aula a : lista) {
            txtReservadas.append("ID: " + a.getId()
                    + " | " + a.getNome()
                    + " | " + a.getDataAula()
                    + " | " + a.getHoraAula() + "\n");
        }
    }
}