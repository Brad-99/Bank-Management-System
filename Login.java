import java.sql.*;
import javax.swing.*;

public class Login extends JFrame {

    // Components Declaration
    private JPanel jPanel1;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin;
    private JButton btnCancel;

    public Login() {
        initComponents();
    }

    private void initComponents() {
        // Component Initialization and Layout Code Here...
        // ...

        // Set Action Listeners for buttons
        btnLogin.addActionListener(e -> loginAction());
        btnCancel.addActionListener(e -> cancelAction());
    }

    private void loginAction() {
        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return authenticateUser(txtUser.getText(), new String(txtPass.getPassword()));
            }

            @Override
            protected void done() {
                try {
                    if (get()) {
                        MainMenu mainMenu = new MainMenu();

                        Login.this.setVisible(false);
                        mainMenu.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(Login.this, "Username and password do not match");
                        txtUser.setText("");
                        txtPass.setText("");
                        txtUser.requestFocus();
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // Replace with better error handling
                }
            }
        };
        worker.execute();
    }

    private void cancelAction() {
        // Handle cancel action
    }

    private boolean authenticateUser(String username, String password) {
        String query = "SELECT * FROM user WHERE username = ? AND password = SHA2(?, 256)";
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/customer", "root", "");
                PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, username);
            pst.setString(2, password);

            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Replace with better error handling
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}
