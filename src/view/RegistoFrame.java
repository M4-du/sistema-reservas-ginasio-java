package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;

import dao.UtilizadorDAO;
import model.Utilizador;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class RegistoFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtNome;
    private JTextField txtEmail;
    private JPasswordField txtPassword;

    public RegistoFrame() {
        setTitle("Registo de Novo Aluno");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 420, 280);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(40, 40, 80, 25);
        contentPane.add(lblNome);

        txtNome = new JTextField();
        txtNome.setBounds(140, 40, 200, 25);
        txtNome.setColumns(10);
        contentPane.add(txtNome);

        txtNome.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetter(c) && !Character.isWhitespace(c)
                        && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    e.consume();
                }
            }
        });

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(40, 80, 80, 25);
        contentPane.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(140, 80, 200, 25);
        txtEmail.setColumns(10);
        contentPane.add(txtEmail);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(40, 120, 80, 25);
        contentPane.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(140, 120, 200, 25);
        contentPane.add(txtPassword);

        JButton btnRegistar = new JButton("Registar");
        btnRegistar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registarUtilizador();
            }
        });
        btnRegistar.setBounds(140, 170, 100, 30);
        contentPane.add(btnRegistar);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        btnFechar.setBounds(250, 170, 90, 30);
        contentPane.add(btnFechar);
    }

    private void registarUtilizador() {
        String nome = txtNome.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (nome.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
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
        boolean sucesso = dao.registar(u);

        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Aluno registado com sucesso.");
            limparCampos();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Esse email já existe ou ocorreu um erro.");
        }
    }

    private boolean validarNome(String nome) {
        return nome.matches("[A-Za-zÀ-ÿ ]+");
    }

    private boolean validarEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    private void limparCampos() {
        txtNome.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
    }
}