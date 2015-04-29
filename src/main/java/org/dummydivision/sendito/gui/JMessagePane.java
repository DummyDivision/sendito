package org.dummydivision.sendito.gui;

import java.awt.Color;
import java.text.SimpleDateFormat;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import org.dummydivision.sendito.shared.SenditoBasicServer;
import org.dummydivision.sendito.shared.message.Message;

/**
 * Custom TextPane that displays Message objects.
 */
public class JMessagePane extends JTextPane {

    // Allows us to access sendito's functionality
    private final SenditoBasicServer server;
    // Styles for message bodys and sender / reciever names
    private final Style bodyStyle; // Normal
    private final Style senderStyle; // Blue
    private final Style recieverStyle; // Red

    /**
     * Create a new message pane. The server will be stored and cannot be
     * changed!
     *
     * @param server A class that provides the services defined by
     * SenditoBasicServer
     */
    public JMessagePane(SenditoBasicServer server) {
        super();

        this.server = server;
        // Do not accept user input
        this.setEditable(false);

        // Setup styles
        StyleContext sc = new StyleContext();
        this.setStyledDocument(new DefaultStyledDocument(sc));

        bodyStyle = sc.addStyle("body", null);
        bodyStyle.addAttribute(StyleConstants.Foreground, Color.BLACK);
        bodyStyle.addAttribute(StyleConstants.FontSize, 12);
        bodyStyle.addAttribute(StyleConstants.FontFamily, "Tahoma");
        bodyStyle.addAttribute(StyleConstants.Bold, false);

        senderStyle = sc.addStyle("sender", bodyStyle);
        senderStyle.addAttribute(StyleConstants.Foreground, Color.BLUE);
        senderStyle.addAttribute(StyleConstants.Bold, true);

        recieverStyle = sc.addStyle("reciever", bodyStyle);
        recieverStyle.addAttribute(StyleConstants.Foreground, Color.RED);
        recieverStyle.addAttribute(StyleConstants.Bold, true);
    }

    /**
     * Add a message to the display.
     *
     * @param m The Message that will be added
     */
    public void addMessage(Message m) {
        try {
            StyledDocument doc = this.getStyledDocument();

            // Format date
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss, dd.MM.yyyy");
            String date = simpleDateFormat.format(m.getDateSent());

            // Add header
            String header = String.format("%s (%s):\n", m.getSender(), date);
            boolean outgoingMessage = m.getSender().equals(server.getSelf());
            doc.insertString(doc.getLength(), header, outgoingMessage ? senderStyle : recieverStyle);

            // Add message body
            doc.insertString(doc.getLength(), m.getBody() + '\n', bodyStyle);
        } catch (BadLocationException ex) {
            // This should never occur
            System.out.println(ex);
        }
    }
}
