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
import model.Aula;
import model.Utilizador;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class AdminFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtData;
    private JTextField txtHora;
    private JTextField txtVagas;
    private JTextArea textArea;
    
    
    public AdminFrame(Utilizador adminLogado) {
        setTitle("Área do Administrador - " + adminLogado.getNome());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 650, 520);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBounds(24, 21, 580, 150);
        contentPane.add(textArea);

        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(24, 195, 45, 13);
        contentPane.add(lblId);

        txtId = new JTextField();
        txtId.setBounds(150, 192, 96, 19);
        contentPane.add(txtId);
        txtId.setColumns(10);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(24, 232, 80, 13);
        contentPane.add(lblNome);

        txtNome = new JTextField();
        txtNome.setBounds(150, 229, 180, 19);
        contentPane.add(txtNome);
        txtNome.setColumns(10);

        JLabel lblData = new JLabel("Data (dd/MM/yyyy):");
        lblData.setBounds(24, 269, 120, 13);
        contentPane.add(lblData);

        txtData = new JTextField();
        txtData.setBounds(150, 266, 180, 19);
        contentPane.add(txtData);
        txtData.setColumns(10);

        JLabel lblHora = new JLabel("Hora (HH:mm):");
        lblHora.setBounds(24, 307, 100, 13);
        contentPane.add(lblHora);

        txtHora = new JTextField();
        txtHora.setBounds(150, 304, 180, 19);
        contentPane.add(txtHora);
        txtHora.setColumns(10);

        JLabel lblVagas = new JLabel("Vagas:");
        lblVagas.setBounds(24, 345, 45, 13);
        contentPane.add(lblVagas);

        txtVagas = new JTextField();
        txtVagas.setBounds(150, 342, 180, 19);
        contentPane.add(txtVagas);
        txtVagas.setColumns(10);

        JButton btnListar = new JButton("Listar");
        btnListar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listarAulas();
            }
        });
        btnListar.setBounds(380, 191, 120, 30);
        contentPane.add(btnListar);

        JButton btnInserir = new JButton("Inserir");
        btnInserir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inserirAula();
            }
        });
        btnInserir.setBounds(380, 231, 120, 30);
        contentPane.add(btnInserir);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                atualizarAula();
            }
        });
        btnAtualizar.setBounds(380, 271, 120, 30);
        contentPane.add(btnAtualizar);

        JButton btnApagar = new JButton("Apagar");
        btnApagar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                apagarAula();
            }
        });
        btnApagar.setBounds(380, 311, 120, 30);
        contentPane.add(btnApagar);

        JButton btnGerirAlunos = new JButton("Gerir Alunos");
        btnGerirAlunos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GerirAlunosFrame frame = new GerirAlunosFrame();
                frame.setVisible(true);
            }
        });
        btnGerirAlunos.setBounds(380, 351, 140, 30);
        contentPane.add(btnGerirAlunos);

        listarAulas();
    }

    private boolean dataValida(String data) {
        try {
            DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);
            LocalDate.parse(data, formatoData);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean horaValida(String hora) {
        try {
            DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime.parse(hora, formatoHora);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private void inserirAula() {
        try {
            String nome = txtNome.getText().trim();
            String data = txtData.getText().trim();
            String hora = txtHora.getText().trim();
            String vagasTexto = txtVagas.getText().trim();

            if (nome.isEmpty() || data.isEmpty() || hora.isEmpty() || vagasTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
                return;
            }

            if (!dataValida(data)) {
                JOptionPane.showMessageDialog(this, "Data inválida. Use o formato dd/MM/yyyy.");
                return;
            }

            if (!horaValida(hora)) {
                JOptionPane.showMessageDialog(this, "Hora inválida. Use o formato HH:mm.");
                return;
            }

            int vagas = Integer.parseInt(vagasTexto);

            if (vagas < 0) {
                JOptionPane.showMessageDialog(this, "As vagas não podem ser negativas.");
                return;
            }

            Aula a = new Aula();
            a.setNome(nome);
            a.setDataAula(data);
            a.setHoraAula(hora);
            a.setVagas(vagas);

            AulaDAO dao = new AulaDAO();
            dao.inserir(a);

            JOptionPane.showMessageDialog(this, "Aula inserida com sucesso.");
            limparCampos();
            listarAulas();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "O campo vagas deve ser numérico.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao inserir aula.");
            e.printStackTrace();
        }
    }

    private void atualizarAula() {
        try {
            String idTexto = txtId.getText().trim();

            if (idTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o ID da aula.");
                return;
            }

            int id = Integer.parseInt(idTexto);

            AulaDAO dao = new AulaDAO();
            Aula aulaAtual = dao.buscarAulaPorId(id);

            if (aulaAtual == null) {
                JOptionPane.showMessageDialog(this, "Aula não encontrada.");
                return;
            }

            String nome = txtNome.getText().trim();
            String data = txtData.getText().trim();
            String hora = txtHora.getText().trim();
            String vagasTexto = txtVagas.getText().trim();

            if (!nome.isEmpty()) {
                aulaAtual.setNome(nome);
            }

            if (!data.isEmpty()) {
                if (!dataValida(data)) {
                    JOptionPane.showMessageDialog(this, "Data inválida. Use o formato dd/MM/yyyy.");
                    return;
                }
                aulaAtual.setDataAula(data);
            }

            if (!hora.isEmpty()) {
                if (!horaValida(hora)) {
                    JOptionPane.showMessageDialog(this, "Hora inválida. Use o formato HH:mm.");
                    return;
                }
                aulaAtual.setHoraAula(hora);
            }

            if (!vagasTexto.isEmpty()) {
                int vagas = Integer.parseInt(vagasTexto);

                if (vagas < 0) {
                    JOptionPane.showMessageDialog(this, "As vagas não podem ser negativas.");
                    return;
                }

                aulaAtual.setVagas(vagas);
            }

            boolean sucesso = dao.atualizarParcial(aulaAtual);

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Aula atualizada com sucesso.");
                limparCampos();
                listarAulas();
            } else {
                JOptionPane.showMessageDialog(this, "Não foi possível atualizar a aula.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID e vagas devem ser numéricos.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar aula.");
            e.printStackTrace();
        }
    }

    private void apagarAula() {
        try {
            String idTexto = txtId.getText().trim();

            if (idTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o ID da aula.");
                return;
            }

            int id = Integer.parseInt(idTexto);

            int confirmacao = JOptionPane.showConfirmDialog(
                    this,
                    "Tem a certeza que deseja apagar esta aula?",
                    "Confirmar apagamento",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmacao == JOptionPane.YES_OPTION) {
                AulaDAO dao = new AulaDAO();
                boolean sucesso = dao.apagar(id);

                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Aula apagada com sucesso.");
                    limparCampos();
                    listarAulas();
                } else {
                    JOptionPane.showMessageDialog(this, "Não foi possível apagar a aula.");
                }
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "O ID da aula deve ser numérico.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao apagar aula.");
            e.printStackTrace();
        }
    }

    private void listarAulas() {
        try {
            AulaDAO dao = new AulaDAO();
            ArrayList<Aula> lista = dao.listarAulas();

            textArea.setText("");

            if (lista.isEmpty()) {
                textArea.setText("Nenhuma aula encontrada.");
                return;
            }

            for (Aula a : lista) {
                textArea.append("ID: " + a.getId() +
                        " | Nome: " + a.getNome() +
                        " | Data: " + a.getDataAula() +
                        " | Hora: " + a.getHoraAula() +
                        " | Vagas: " + a.getVagas() + "\n");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao listar aulas.");
            e.printStackTrace();
        }
    }

    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
        txtData.setText("");
        txtHora.setText("");
        txtVagas.setText("");
    }
}