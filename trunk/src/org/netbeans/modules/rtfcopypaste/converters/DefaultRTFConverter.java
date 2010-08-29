package org.netbeans.modules.rtfcopypaste.converters;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.JEditorPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.StyleConstants;
import org.netbeans.api.editor.mimelookup.MimeLookup;
import org.netbeans.api.editor.mimelookup.MimePath;
import org.netbeans.api.editor.settings.FontColorSettings;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.editor.Coloring;

public class DefaultRTFConverter extends RTFConverter {

    private Map<String, Integer> positions = new HashMap<String, Integer>();
    private Vector<Color> tableclr = new Vector<Color>();

    private void processColorToken(Color fg, String ID, StringBuilder sb) {

        if (!positions.containsKey(ID.trim())) {
            tableclr.add(fg);
            positions.put(ID.trim(), new Integer(tableclr.size()));
            sb.append(colorToRTF(fg)).append(";");
        }

    }

    private String createColorTable(JEditorPane pane, FontColorSettings fcs) {

        final TokenSequence ts = TokenHierarchy.get(pane.getDocument()).
                tokenSequence();
        final StringBuilder sb = new StringBuilder();
        Color foreground = pane.getForeground();
        processColorToken(foreground, "default", sb);
        ts.move(pane.getSelectionStart());

        while (ts.moveNext() && ts.offset() < pane.getSelectionEnd()) {
            Token token = ts.token();
            String ID = token.id().primaryCategory();
            AttributeSet as = fcs.getTokenFontColors(ID);

            if (as != null) {
                final Color fg = (Color) as.getAttribute(StyleConstants.Foreground);
                if (fg != null) {
                    processColorToken(fg, ID, sb);
                }
            }

        }
        return "{\\colortbl;" + sb.toString() + "}";
    }

    private void processToken(final Token token,
            final FontColorSettings fcs,
            final StringBuilder sb) {
        String tokenrtf = getRtfContent(token.text().toString());

        String ID = token.id().primaryCategory();
        AttributeSet as = fcs.getTokenFontColors(ID);

        if (as == null) {
            emit(sb, tokenrtf);
        } else {
            final Font fontName = Coloring.fromAttributeSet(as).getFont();
            if (fontName != null) {
                if (fontName.getStyle() == Font.BOLD) {
                    tokenrtf = "{\\b " + tokenrtf + "}";
                }

            }
            final Color fg = (Color) as.getAttribute(StyleConstants.Foreground);
            if (fg != null) {
                int pos = positions.get(ID.trim()).intValue();
                sb.append("{\\cf").append(pos).append(" ").append(tokenrtf).append("}");
            } else {
                int pos = 1;
                sb.append("{\\cf").append(pos).append(" ").append(tokenrtf).append("}");
            }

        }
    }

    @Override
    public String convertContentToRTF(
            JEditorPane pane) {
        final FontColorSettings fcs = MimeLookup.getLookup(
                MimePath.get(pane.getContentType())).lookup(FontColorSettings.class);

        String colortable = createColorTable(pane, fcs);
        String fonttable = createFontTable(pane);
        final TokenSequence ts = TokenHierarchy.get(pane.getDocument()).
                tokenSequence();
        final StringBuilder sb = new StringBuilder();

        ts.move(pane.getSelectionStart());

        while (ts.moveNext() && ts.offset() < pane.getSelectionEnd()) {

            processToken(ts.token(), fcs, sb);
        }
        return buildRTF(fonttable, colortable, sb.toString());

    }
}