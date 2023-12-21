import java.sql.*; // Imports the SQL package for database operations.
import javax.swing.*; // Imports Swing components for the GUI.
import DatabaseConnector;

public class Login extends JFrame { // Declares a class named Login that inherits from JFrame.

    // Components Declaration
    private JPanel jPanel1; // Declares a JPanel to group other GUI components.
    private JLabel jLabel1; // Declares a JLabel for displaying text for the username field.
    private JLabel jLabel2; // Declares a JLabel for displaying text for the password field.
    private JTextField txtUser; // Declares a JTextField for inputting the username.
    private JPasswordField txtPass; // Declares a JPasswordField for inputting the password.
    private JButton btnLogin; // Declares a JButton to trigger the login action.
    private JButton btnCancel; // Declares a JButton to trigger the cancel action.

    public Login() { // Constructor for the Login class.
        initComponents(); // Calls the initComponents method to set up the GUI components.
    }

    private void initComponents() { // Method to initialize and configure the GUI components.
        // Component Initialization and Layout Code Here...
        // ...
        jPanel1 = new JPanel();
        jPanel1.setLayout(new BoxLayout(jPanel1, BoxLayout.Y_AXIS)); // Stack components vertically

        // Initialize labels
        jLabel1 = new JLabel("Username:");
        jLabel2 = new JLabel("Password");

        // Initialize text fields
        txtUser = new JTextField(20); // Width of 20 columns
        txtPass = new JPasswordField(20);

        // Initialize buttons
        btnLogin = new JButton("Login");
        btnCancel = new JButton("Cancel");

        // Add components to the panel
        jPanel1.add(jLabel1);
        jPanel1.add(txtUser);
        jPanel1.add(jLabel2);
        jPanel1.add(txtPass);
        jPanel1.add(btnLogin);
        jPanel1.add(btnCancel);

        // Add the panel to the frame
        this.add(jPanel1);

        // Set Action Listeners for buttons
        btnLogin.addActionListener(e -> loginAction()); // Adds an action listener to the login button.
        btnCancel.addActionListener(e -> cancelAction()); // Adds an action listener to the cancel button.

        this.setTitle("Bank Management System - Login");
        this.setSize(300, 200); // Set the initial size of the frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set the default close operation

    }

    private void loginAction() { // Method that defines the login action.
        SwingWorker<Boolean, Void> worker = new SwingWorker<>() { // Creates a new SwingWorker to perform background
                                                                  // operations.
            @Override
            protected Boolean doInBackground() throws Exception { // The method that runs in the background thread.
                return authenticateUser(txtUser.getText(), new String(txtPass.getPassword())); // Calls authenticateUser
                                                                                               // with the username and
                                                                                               // password.
            }

            @Override
            protected void done() { // This method is called when the background processing is complete.
                try {
                    if (get()) { // Checks the result of the doInBackground method.
                        MainMenu mainMenu = new MainMenu(); // If successful, creates a new instance of MainMenu.

                        Login.this.setVisible(false); // Hides the Login window.
                        mainMenu.setVisible(true); // Shows the MainMenu window.
                    } else {
                        JOptionPane.showMessageDialog(Login.this, "Username and password do not match"); // Shows an
                                                                                                         // error
                                                                                                         // message.
                        txtUser.setText(""); // Resets the username field.
                        txtPass.setText(""); // Resets the password field.
                        txtUser.requestFocus(); // Sets the focus back to the username field.
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // Prints the stack trace for any exceptions that occurred.
                }
            }
        };
        worker.execute(); // Starts the SwingWorker by calling its execute method.
    }

    private void cancelAction() { // Method that defines the cancel action.
        // Handle cancel action
    }

    private boolean authenticateUser(String username, String password) { // Method to authenticate the user against the
                                                                         // database.
        String query = "SELECT * FROM user WHERE username = ? AND password = SHA2(?, 256)"; // SQL query to check
                                                                                            // username and hashed
                                                                                            // password.
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/customer", "root", ""); // Establishes
                                                                                                          // a database
                                                                                                          // connection.
                PreparedStatement pst = con.prepareStatement(query)) { // Prepares the SQL statement.

            pst.setString(1, username); // Sets the username parameter in the SQL query.
            pst.setString(2, password); // Sets the password parameter in the SQL query.

            try (ResultSet rs = pst.executeQuery()) { // Executes the query and gets the result set.
                return rs.next(); // Returns true if the query found a match, otherwise false.
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Prints the stack trace for any SQL exceptions that occurred.
            return false; // Returns false if an exception occurred.
        }
    }

    public static void main(String[] args) { // The main method, the entry point of the application.
        SwingUtilities.invokeLater(() -> new Login().setVisible(true)); // Ensures the GUI is created in the Event
                                                                        // Dispatch Thread.
    }
}
