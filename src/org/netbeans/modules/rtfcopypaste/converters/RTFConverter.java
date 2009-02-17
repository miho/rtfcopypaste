package org.netbeans.modules.rtfcopypaste.converters;

import java.awt.Color;
import javax.swing.JEditorPane;
import org.netbeans.modules.rtfcopypaste.utils.CurrentCopyPasteProfile;

public abstract class RTFConverter {

    public abstract String convertContentToRTF(JEditorPane pane);

    protected String createFontTable(
            JEditorPane pane) {
        final StringBuilder sb = new StringBuilder();
        sb.append("{\\f0 Courier New;}");

        return "{\\fonttbl " + sb.toString() + "}";
    }

    private static String specialReplaceAll(final String oldcontent) {
        String content = new String(oldcontent);
        String ret = "";

        for (char ch : content.toCharArray()) {
            if (ch == '\\') {
                ret += "\\\\";
            } else {
                ret += ch;
            }
        }
        return ret;
    }

    public String buildRTF(String fonttable, String colortable, String content) {
        Integer fontsize = CurrentCopyPasteProfile.getCurrentCopyPasteFontSize() * 2;
        return "{\\rtf1\\ansi\\deff0 " + fonttable + colortable + "\\fs" + fontsize + " " + content + "}";  //TODO fontsize
    }

    protected String colorToRTF(Color color) {
        return "\\red" + color.getRed() + "\\green" + color.getGreen() + "\\blue" +
                color.getBlue();
    }

    protected static final void emit(final StringBuilder sb, final String... strings) {
        for (String string : strings) {
            sb.append(string);
        }
    }

    protected static final String getRtfContent(final String oldcontent) {
        String content = specialReplaceAll(oldcontent);
        StringBuilder sb = new StringBuilder(content.length());

        for (int i = 0; i < content.length(); i++) {
            sb.append(escapeCharacter(content, i));
        }

        return sb.toString();
    }

    protected static final String escapeCharacter(String content, int index) {
        switch (content.charAt(index)) {
            case '}':
                return "\\}";
            case '{':
                return "\\{";
            case '\n':
                return "{\\line}";
            case '\t':
                return "{\\tab}";
            case ' ':
                return " ";
            default:
                return content.substring(index, index + 1);
        }
    }
}
