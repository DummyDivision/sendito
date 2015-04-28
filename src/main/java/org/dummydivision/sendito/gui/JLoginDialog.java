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
import javax.swing.JTextField;

public class JLoginDialog extends JDialog {

    JTextField username;
    JTextField password;

    public JLoginDialog(JFrame parent) throws HeadlessException {
        super(parent, "Sendito - Login", ModalityType.TOOLKIT_MODAL);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        setupGUI();
        pack();

        setResizable(false);
        setLocationRelativeTo(null); // Center this window
    }

    private JPanel buildLoginPanel() {
        JPanel loginPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblUsername = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        loginPanel.add(lblUsername, gbc);

        username = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(username, gbc);

        JLabel lblPassword = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        loginPanel.add(lblPassword, gbc);

        password = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        loginPanel.add(password, gbc);

        return loginPanel;
    }

    private JPanel buildButtonPanel() {
        JPanel buttonPanel = new JPanel();

        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        buttonPanel.add(btnLogin);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        buttonPanel.add(btnCancel);

        return buttonPanel;
    }

    private void setupGUI() {
        getContentPane().add(buildLoginPanel(), BorderLayout.CENTER);
        getContentPane().add(buildButtonPanel(), BorderLayout.PAGE_END);
    }

    public String getUsername() {
        return username.getText();
    }

    public String getPassword() {

        return password.getText();
    }

}
