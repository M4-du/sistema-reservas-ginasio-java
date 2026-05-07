package view;

import java.awt.EventQueue;
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

public class LoginFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtEmail;
    private JPasswordField txtPassword;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LoginFrame frame = new LoginFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public LoginFrame() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 420, 280);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(50, 40, 80, 25);
        contentPane.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(140, 40, 200, 25);
        contentPane.add(txtEmail);
        txtEmail.setColumns(10);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(50, 80, 80, 25);
        contentPane.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(140, 80, 200, 25);
        contentPane.add(txtPassword);

        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fazerLogin();
            }
        });
        btnEntrar.setBounds(140, 130, 95, 30);
        contentPane.add(btnEntrar);

        JButton btnRegistar = new JButton("Registar");
        btnRegistar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RegistoFrame registo = new RegistoFrame();
                registo.setVisible(true);
            }
        });
        btnRegistar.setBounds(245, 130, 95, 30);
        contentPane.add(btnRegistar);
    }

    private void fazerLogin() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha email e password.");
            return;
        }

        UtilizadorDAO dao = new UtilizadorDAO();
        Utilizador u = dao.login(email, password);

        if (u != null) {
            JOptionPane.showMessageDialog(this, "Login com sucesso.");

            if (u.getTipo().equals("admin")) {
                AdminFrame admin = new AdminFrame(u);
                admin.setVisible(true);
            } else {
                ClienteFrame cliente = new ClienteFrame(u);
                cliente.setVisible(true);
            }

            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Credenciais inválidas.");
        }
    }
}