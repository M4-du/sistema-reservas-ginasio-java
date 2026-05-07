package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JPasswordField;

import dao.UtilizadorDAO;
import model.Utilizador;

import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GerirAlunosFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextArea textArea;
    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtEmail;
    private JPasswordField txtPassword;

    public GerirAlunosFrame() {
        setTitle("Gestão de Alunos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 700, 500);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(24, 20, 620, 150);
        contentPane.add(scrollPane);

        textArea = new JTextArea();
        textArea.setEditable(false);
        scrollPane.setViewportView(textArea);

        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(24, 200, 45, 13);
        contentPane.add(lblId);

        txtId = new JTextField();
        txtId.setBounds(100, 197, 140, 22);
        txtId.setColumns(10);
        contentPane.add(txtId);

        txtId.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    e.consume();
                }
            }
        });

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(24, 235, 45, 13);
        contentPane.add(lblNome);

        txtNome = new JTextField();
        txtNome.setBounds(100, 232, 220, 22);
        txtNome.setColumns(10);
        contentPane.add(txtNome);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(24, 270, 45, 13);
        contentPane.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(100, 267, 220, 22);
        txtEmail.setColumns(10);
        contentPane.add(txtEmail);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(24, 305, 66, 13);
        contentPane.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(100, 302, 220, 22);
        contentPane.add(txtPassword);

        JButton btnListar = new JButton("Listar Alunos");
        btnListar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listarAlunos();
            }
        });
        btnListar.setBounds(380, 190, 150, 30);
        contentPane.add(btnListar);

        JButton btnAdicionar = new JButton("Adicionar");
        btnAdicionar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                adicionarAluno();
            }
        });
        btnAdicionar.setBounds(380, 230, 150, 30);
        contentPane.add(btnAdicionar);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                atualizarAluno();
            }
        });
        btnAtualizar.setBounds(380, 270, 150, 30);
        contentPane.add(btnAtualizar);

        JButton btnApagar = new JButton("Apagar");
        btnApagar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                apagarAluno();
            }
        });
        btnApagar.setBounds(380, 310, 150, 30);
        contentPane.add(btnApagar);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        btnFechar.setBounds(380, 350, 150, 30);
        contentPane.add(btnFechar);

        listarAlunos();
    }

    private void listarAlunos() {
        try {
            UtilizadorDAO dao = new UtilizadorDAO();
            ArrayList<Utilizador> lista = dao.listarAlunos();

            textArea.setText("");

            if (lista.isEmpty()) {
                textArea.setText("Nenhum aluno encontrado.");
                return;
            }

            for (Utilizador u : lista) {
                textArea.append("ID: " + u.getId()
                        + " | Nome: " + u.getNome()
                        + " | Email: " + u.getEmail()
                        + " | Tipo: " + u.getTipo() + "\n");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao listar alunos.");
            e.printStackTrace();
        }
    }

    private void adicionarAluno() {
        try {
            String nome = txtNome.getText().trim();
            String email = txtEmail.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();

            if (nome.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha nome, email e password.");
                return;
            }

            if (!validarNome(nome)) {
                JOptionPane.showMessageDialog(this, "Nome inválido. Use apenas letras e espaços.");
                return;
            }

            if (!validarEmail(email)) {
                JOptionPane.showMessageDialog(this, "Email inválido.");
                return;
            }

            Utilizador u = new Utilizador();
            u.setNome(nome);
            u.setEmail(email);
            u.setPassword(password);
            u.setTipo("cliente");

            UtilizadorDAO dao = new UtilizadorDAO();
            boolean sucesso = dao.adicionarAluno(u);

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Aluno adicionado com sucesso.");
                limparCampos();
                listarAlunos();
            } else {
                JOptionPane.showMessageDialog(this, "Não foi possível adicionar. O email pode já existir.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar aluno.");
            e.printStackTrace();
        }
    }

    private void atualizarAluno() {
        try {
            String idTexto = txtId.getText().trim();

            if (idTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o ID do aluno.");
                return;
            }

            int id = Integer.parseInt(idTexto);

            UtilizadorDAO dao = new UtilizadorDAO();
            Utilizador alunoAtual = dao.buscarAlunoPorId(id);

            if (alunoAtual == null) {
                JOptionPane.showMessageDialog(this, "Aluno não encontrado.");
                return;
            }

            String nome = txtNome.getText().trim();
            String email = txtEmail.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();

            if (!nome.isEmpty()) {
                alunoAtual.setNome(nome);
            }

            if (!email.isEmpty()) {
                alunoAtual.setEmail(email);
            }

            if (!password.isEmpty()) {
                alunoAtual.setPassword(password);
            }

            boolean sucesso = dao.atualizarAlunoParcial(alunoAtual);

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Aluno atualizado com sucesso.");
                limparCampos();
                listarAlunos();
            } else {
                JOptionPane.showMessageDialog(this, "Não foi possível atualizar. Verifique se o email já existe.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "O ID deve ser numérico.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar aluno.");
            e.printStackTrace();
        }
    }

    private void apagarAluno() {
        try {
            String idTexto = txtId.getText().trim();

            if (idTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o ID do aluno.");
                return;
            }

            if (!validarID(idTexto)) {
                JOptionPane.showMessageDialog(this, "O ID deve ser numérico.");
                return;
            }

            int id = Integer.parseInt(idTexto);

            int confirmacao = JOptionPane.showConfirmDialog(
                    this,
                    "Tem a certeza que deseja apagar este aluno?",
                    "Confirmar apagamento",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmacao == JOptionPane.YES_OPTION) {
                UtilizadorDAO dao = new UtilizadorDAO();
                boolean sucesso = dao.apagarAluno(id);

                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Aluno apagado com sucesso.");
                    limparCampos();
                    listarAlunos();
                } else {
                    JOptionPane.showMessageDialog(this, "Não foi possível apagar esse aluno.");
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao apagar aluno.");
            e.printStackTrace();
        }
    }

    private boolean validarID(String idTexto) {
        return idTexto.matches("\\d+");
    }

    private boolean validarNome(String nome) {
        return nome.matches("[A-Za-zÀ-ÿ ]+");
    }

    private boolean validarEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
    }
}