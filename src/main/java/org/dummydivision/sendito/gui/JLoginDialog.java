package org.dummydivision.sendito.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class JLoginDialog extends JDialog {

    private boolean canceled = true;
    private JTextField username = null;
    private JTextField password = null;

    public JLoginDialog() throws HeadlessException {
        super();
        setTitle("Sendito - Login");
        setModalityType(ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        setupGUI();
        setPreferredSize(new Dimension(800, 600));

        pack();
        setVisible(true);
    }

    private void setupGUI() {
        JPanel mainPanel = buildMainPanel();
        this.add(mainPanel);
    }

    private JPanel buildMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        mainPanel.add(buildInputPanel(), BorderLayout.CENTER);
        mainPanel.add(buildButtonPanel(), BorderLayout.SOUTH);

        return mainPanel;
    }

    private JPanel buildInputPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        inputPanel.add(new JLabel("Username"), constraints);

        username = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        inputPanel.add(username, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        inputPanel.add(new JLabel("Password"), constraints);

        password = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        inputPanel.add(password, constraints);

        return inputPanel;
    }

    private JPanel buildButtonPanel() {
        JPanel buttonPanel = new JPanel(new BorderLayout());

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                canceled = false;
                setVisible(false);
            }
        });
        buttonPanel.add(loginButton, BorderLayout.WEST);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                setVisible(false);
            }
        });
        buttonPanel.add(cancelButton, BorderLayout.EAST);

        return buttonPanel;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public String getUsername() {
        return username.getText();
    }

    public String getPassword() {
        return password.getText();
    }
}
