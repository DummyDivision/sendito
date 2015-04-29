package org.dummydivision.sendito.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Generic dialog that asks for username and password.
 */
public class JLoginDialog extends JDialog implements ActionListener {

    // GUI elements
    private JTextField username;
    private JPasswordField password;
    // Determines whether the user clicked cancel or login
    private boolean success = false;

    /**
     * Construct a new dialog with the given title. The dialog will not have a
     * parent and will be shown as top-level window.
     *
     * @param title The title of the login dialog
     */
    public JLoginDialog(String title) {
        // Call the other constructor
        this(null, title, ModalityType.TOOLKIT_MODAL);
    }

    /**
     * Construct a new dialog with the given parent, title and ModalityType.
     *
     * @param parent The parent of the dialog
     * @param title The title of the login dialog
     * @param mt The Modality of the dialog
     * @throws HeadlessException inherited from JDialog
     */
    public JLoginDialog(JFrame parent, String title, ModalityType mt) throws HeadlessException {
        // Call superconstructor
        super(parent, title, mt);
        // When the dialog is closed, it should be disposed
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Build the GUI
        getContentPane().add(buildLoginPanel(), BorderLayout.CENTER);
        getContentPane().add(buildButtonPanel(), BorderLayout.PAGE_END);
        pack();

        setResizable(false);
        setLocationRelativeTo(parent); // Respect parent's location
    }

    /*
     * Construct the username and password fields of the GUI
     * @return The constructed GUI Panel
     */
    private JPanel buildLoginPanel() {
        // We wrap our GUI in a new Panel that we will return in the end
        JPanel loginPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label for username field
        JLabel lblUsername = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        loginPanel.add(lblUsername, gbc);

        // Field for username
        username = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        username.addActionListener(this);
        loginPanel.add(username, gbc);

        // Label for password field
        JLabel lblPassword = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        loginPanel.add(lblPassword, gbc);

        // Field for password
        password = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        password.addActionListener(this);
        loginPanel.add(password, gbc);

        // Return the constructed panel
        return loginPanel;
    }

    /*
     * Construct login and cancel buttons of the GUI
     * @return The constructed GUI Panel
     */
    private JPanel buildButtonPanel() {
        // We wrap our GUI in a new Panel that we will return in the end
        JPanel buttonPanel = new JPanel();

        // Login button
        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(this);
        buttonPanel.add(btnLogin);

        // Cancel button
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // Just hide the dialog
                setVisible(false);
            }
        });
        buttonPanel.add(btnCancel);

        // Return the constructed panel
        return buttonPanel;
    }

    /**
     * Show the dialog. This method will block until the user clicked login or
     * cancels the interaction. Inspect the return value to see if the user
     * clicked on login
     *
     * @param username Initial username to display in the dialog
     * @return True when the user clicked login, false otherwise
     */
    public boolean showDialog(String username) {
        // Display predefined username
        this.username.setText(username);

        // Set the focus to the password field if a username was supplied
        if (username != null) {
            this.password.requestFocus();
        }

        // Display the dialog. This will block execution until the Dialog is closed somehow
        setVisible(true);

        // Return whether we were canceled or not.
        return success;
    }

    /**
     * Get the username that was given by the user.
     *
     * @return Username that was given by the user
     */
    public String getUsername() {
        return username.getText();
    }

    /**
     * Get the password that was given by the user.
     *
     * @return Password that was given by the user
     */
    public String getPassword() {
        return password.getText();
    }

    /**
     * Closes the dialog. Will be interpreted as successful close.
     *
     * @param ae Will be ignored
     */
    public void actionPerformed(ActionEvent ae) {
        success = true;
        setVisible(false);
    }
}
