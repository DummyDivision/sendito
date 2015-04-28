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
import org.dummydivision.sendito.shared.LoginVerifier;

public class JLoginDialog extends JDialog {

    private JTextField username;
    private JTextField password;
    private boolean success = false;

    public JLoginDialog(String title) {
        this(null, title, ModalityType.TOOLKIT_MODAL);
    }

    public JLoginDialog(JFrame parent, String title, ModalityType mt) throws HeadlessException {
        super(parent, title, mt);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        getContentPane().add(buildLoginPanel(), BorderLayout.CENTER);
        getContentPane().add(buildButtonPanel(), BorderLayout.PAGE_END);
        pack();

        setResizable(false);
        setLocationRelativeTo(parent); // Respect parent's location
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
                success = true;
                setVisible(false);
            }
        });
        buttonPanel.add(btnLogin);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        buttonPanel.add(btnCancel);

        return buttonPanel;
    }

    public boolean showDialog(String username) {
        this.username.setText(username);
        setVisible(true);
        return success;
    }

    public String getUsername() {
        return username.getText();
    }

    public String getPassword() {
        return password.getText();
    }
}
