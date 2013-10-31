package org.netbeans.modules.rtfcopypaste.converters;

import java.awt.Color;
import javax.swing.JEditorPane;
import org.netbeans.modules.rtfcopypaste.utils.CurrentCopyPasteProfile;

public abstract class RTFConverter {

    public abstract String convertContentToRTF(JEditorPane pane);
    protected static String eol = System.getProperty("line.separator");

    protected String createFontTable(
            JEditorPane pane) {
        String fonttable = "{\\fonttbl {\\f0 Courier New;}}";
        return fonttable;
    }

    private static String specialReplaceAll(final String oldcontent) {
        String content = oldcontent;
        String ret = "";

        for (char ch : content.toCharArray()) {
            if (ch == '\\') ret += "\\\\";
            else ret += ch;
        }
        return ret;
    }

    public String buildRTF(String fonttable, String colortable, String content) {
        Integer fontsize = CurrentCopyPasteProfile.getCurrentCopyPasteFontSize() * 2;
        
        String rtf_string = "{\\rtf1\\ansi\\deff0 " + eol;
        rtf_string += fonttable + eol;
        rtf_string += colortable + eol;
        rtf_string += "\\f0\\fs" + fontsize + eol;
        //rtf_string += "\\cb" + bc + eol;
        
        rtf_string += content + "}" + eol;
        return rtf_string;
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
                return "\\par"+eol;
            case '\r':
                return "\\par"+eol;
            case '\t':
                return "\\tab";
            case ' ':
                return " ";
            default:
                return content.substring(index, index + 1);
        }
    }
}
