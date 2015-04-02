package org.dummydivision.sendito.gui;

import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import org.dummydivision.sendito.shared.Message;

public class JMessagePane extends JTextPane {

    final private String self;
    final private Style bodyStyle;
    final private Style senderStyle;
    final private Style recieverStyle;

    public JMessagePane(String self) {
        super();

        this.self = self;
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
        senderStyle.addAttribute(StyleConstants.Foreground, Color.RED);
        senderStyle.addAttribute(StyleConstants.Bold, true);

        recieverStyle = sc.addStyle("reciever", bodyStyle);
        recieverStyle.addAttribute(StyleConstants.Foreground, Color.BLUE);
        recieverStyle.addAttribute(StyleConstants.Bold, true);
    }

    public void addMessage(Message m) {
        try {
            StyledDocument doc = this.getStyledDocument();
            doc.insertString(doc.getLength(), String.format("%s (%s):\n", m.getSender(), m.getDateSent()), (m.getSender().equals(self) ? senderStyle : recieverStyle));
            doc.insertString(doc.getLength(), m.getBody() + '\n', bodyStyle);
        } catch (BadLocationException ex) {
            System.out.println(ex);
        }
    }
}
