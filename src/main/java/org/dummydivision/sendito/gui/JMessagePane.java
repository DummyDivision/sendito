package org.dummydivision.sendito.gui;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import org.dummydivision.sendito.shared.SenditoBasicServer;
import org.dummydivision.sendito.shared.message.Message;

public class JMessagePane extends JTextPane {

    final private SenditoBasicServer server;
    final private Style bodyStyle;
    final private Style senderStyle;
    final private Style recieverStyle;

    public JMessagePane(SenditoBasicServer server) {
        super();

        this.server = server;
        this.setEditable(false);

        StyleContext sc = new StyleContext();
        this.setStyledDocument(new DefaultStyledDocument(sc));

        // Setup styles
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

    public void addMessage(Message m) {
        try {
            StyledDocument doc = this.getStyledDocument();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss, dd.MM.yyyy");
            doc.insertString(doc.getLength(), String.format("%s (%s):\n", m.getSender(), simpleDateFormat.format(m.getDateSent())), (m.getSender().equals(server.getSelf()) ? senderStyle : recieverStyle));
            doc.insertString(doc.getLength(), m.getBody() + '\n', bodyStyle);
        } catch (BadLocationException ex) {
            System.out.println(ex);
        }
    }
}
