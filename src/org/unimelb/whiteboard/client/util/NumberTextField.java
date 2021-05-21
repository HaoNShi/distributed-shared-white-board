package org.unimelb.whiteboard.client.util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class NumberTextField extends PlainDocument {
    private final int lengthLimit;
    private final Boolean isNumOnly;

    public NumberTextField(int lengthLimit, Boolean isNumOnly) {
        super();
        this.lengthLimit = lengthLimit;
        this.isNumOnly = isNumOnly;
    }

    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null) {
            return;
        }
        char[] s = str.toCharArray();
        // Only allow number input
        int length = 0;
        for (int i = 0; i < s.length; i++) {
            if (getLength() < lengthLimit) {
                if (!isNumOnly || (s[i] >= '0') && (s[i] <= '9')) {
                    s[length++] = s[i];
                }
            }
            // Insert text
            super.insertString(offset, new String(s, 0, length), attr);
        }
    }
}
