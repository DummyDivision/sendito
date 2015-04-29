package org.dummydivision.sendito.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.BadLocationException;
import org.dummydivision.sendito.shared.SenditoBasicClient;
import org.dummydivision.sendito.shared.SenditoBasicServer;
import org.dummydivision.sendito.shared.message.Message;

/**
 * Window to browse history.
 */
public class JHistoryFrame extends JFrame implements SenditoBasicClient, ActionListener {

    // Interaction with the main server
    private final SenditoBasicServer server;
    // Message display area
    private final JMessagePane messagePane;
    // Combobox to select time
    private final JComboBox timeList;
    // Available times
    private final String[] times = {"Show all", "Last month", "This month", "Yesterday", "Last 24 hours", "Today", "Last hour"};

    /**
     * Creates a new history browser. The server will be stored and cannot be
     * changed!
     *
     * @param server A class that provides the services defined by
     * @throws HeadlessException Inherited from JFrame
     */
    public JHistoryFrame(SenditoBasicServer server) throws HeadlessException {
        super("Sendito - History");
        this.server = server;

        setPreferredSize(new Dimension(640, 480));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // GUI controls for date selection
        JPanel historyPanel = new JPanel();
        historyPanel.add(new JLabel("Time:"));
        timeList = new JComboBox(times);
        timeList.setEditable(false);
        timeList.setSelectedIndex(6);
        timeList.addActionListener(this);
        historyPanel.add(timeList);
        add(historyPanel, BorderLayout.PAGE_START);

        // Message display area
        messagePane = new JMessagePane(server);
        add(messagePane, BorderLayout.CENTER);

        // Dedicated close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                setVisible(false);
            }
        });
        add(closeButton, BorderLayout.PAGE_END);
        pack();
    }

    /**
     * {@inheritDoc}
     */
    public void onLostConnection() {
        setTitle("Sendito - History [No Connection]");

        timeList.setEnabled(false);
    }

    /**
     * Notifies the window of a restored connection. This will re-enable
     * functionality
     */
    public void onRestoreConnection() {
        // Change the title back to normal
        setTitle("Sendito - History [" + server.getSelf() + "]");

        // Re-Enable the GUI
        timeList.setEnabled(true);

        // Reload the list
        actionPerformed(null);
    }

    /**
     * {@inheritDoc}
     */
    public void onAddMessage(String id) {
        messagePane.addMessage(server.getMessage(id));
    }

    /**
     * Ignored.
     *
     * @param id ignored
     */
    public void onAddServer(String id) {
        // We ignore this
    }

    /*
     * Clears the diaplay, then displays Messages sent after date.
     * @param date Messages after this will be displayed
     */
    private void loadFromDate(Date date) {
        try {
            // Clear messages
            messagePane.getStyledDocument().remove(0, messagePane.getStyledDocument().getLength());

            // Load the correct messages
            List<Message> messages = server.getAllMessages();
            for (Message m : messages) {
                if (m.getDateSent().after(date)) {
                    messagePane.addMessage(m);
                }
            }
        } catch (BadLocationException ex) {
            // Should never occur as we delete the entire document
            System.out.println(ex);
        }
    }

    /*
     * Convert a selection index to a meaningful date.
     * @param index Selection index
     * @return Meaningful Date
     */
    private Date indexToDate(int index) {
        Calendar calendar;
        // If we had lambdas, this could easily be avoided
        switch (index) {
            case 0:
                // Show all
                return new Date(0);
            case 1:
                // Last month
                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.DATE, 1);
                calendar.add(Calendar.MONTH, -1);
                return calendar.getTime();
            case 2:
                // This month
                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.DATE, 1);
                return calendar.getTime();
            case 3:
                // Yesterday
                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.add(Calendar.DATE, -1);
                return calendar.getTime();
            case 4:
                // Last 24 hours
                calendar = Calendar.getInstance();
                calendar.add(Calendar.HOUR_OF_DAY, -24);
                return calendar.getTime();
            case 5:
                // Today
                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                return calendar.getTime();
            case 6:
                // Last hour
                calendar = Calendar.getInstance();
                calendar.add(Calendar.HOUR_OF_DAY, -1);
                return calendar.getTime();
            default:
                // Error
                return null;
        }
    }

    /**
     * Invoked when a date is selected
     *
     * @param ae ignored
     */
    public void actionPerformed(ActionEvent ae) {
        // Grab messages
        loadFromDate(indexToDate(timeList.getSelectedIndex()));
    }
}
