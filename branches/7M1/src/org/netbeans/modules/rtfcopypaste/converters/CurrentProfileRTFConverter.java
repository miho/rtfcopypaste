package org.netbeans.modules.rtfcopypaste.converters;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.JEditorPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;
import org.netbeans.editor.Coloring;
import org.netbeans.modules.editor.lib2.highlighting.HighlightingManager;
import org.netbeans.modules.rtfcopypaste.utils.CurrentCopyPasteProfile;
import org.netbeans.spi.editor.highlighting.HighlightsContainer;
import org.netbeans.spi.editor.highlighting.HighlightsSequence;
import org.openide.util.Exceptions;

public class CurrentProfileRTFConverter extends RTFConverter {

    private Map<Color, Integer> positions = new HashMap<Color, Integer>();
    private Vector<Color> tableclr = new Vector<Color>();

    private void processColorToken(Color color, StringBuilder sb) {
        if (!positions.containsKey(color)) {
            tableclr.add(color);
            positions.put(color, new Integer(tableclr.size()));
            sb.append(colorToRTF(color) + ";");
        }
    }

    private String createColorTable(JEditorPane pane) {
        HighlightsContainer highlights = HighlightingManager.getInstance().getHighlights(pane, null);
        HighlightsSequence hs = highlights.getHighlights(pane.getSelectionStart(), pane.getSelectionEnd());
        final StringBuilder sb = new StringBuilder();

        Color foreground = pane.getForeground();
        processColorToken(foreground, sb);

        while (hs.moveNext()) {
            AttributeSet attr = hs.getAttributes();
            final Color color = (Color) attr.getAttribute(StyleConstants.Foreground);
            if (color != null) {
                processColorToken(color, sb);
            }
        }

        return "{\\colortbl;" + sb.toString() + "}";
    }

    private final void processToken(final String token,
            final AttributeSet attr, final StringBuilder sb) {
        String tokenRtf = getRtfContent(token);

        if (attr == null) {
            emit(sb, tokenRtf);
        } else {
            final Color fg = (Color) attr.getAttribute(StyleConstants.Foreground);
            final Font fontName = Coloring.fromAttributeSet(attr).getFont();
            if (fontName != null) {
                if (fontName.getStyle() == Font.BOLD) {
                    tokenRtf = "{\\b " + tokenRtf + "}";
                }
            }
            if (fg != null) {
                int pos = positions.get(fg).intValue();
                sb.append("{\\cf" + pos + " " + tokenRtf + "}");
            } else {
                int pos = 1;
                sb.append("{\\cf" + pos + " " + tokenRtf + "}");
            //emit(sb, tokenRtf);
            }
        }
    }

    @Override
    public String convertContentToRTF(JEditorPane pane) {
        String colortable = createColorTable(pane);
        String fonttable = createFontTable(pane);

        HighlightsContainer highlights = HighlightingManager.getInstance().getHighlights(pane, null);
        HighlightsSequence hs = highlights.getHighlights(pane.getSelectionStart(), pane.getSelectionEnd());

        final StringBuilder sb = new StringBuilder();

        while (hs.moveNext()) {
            try {
                processToken(pane.getText(hs.getStartOffset(), hs.getEndOffset() - hs.getStartOffset()), hs.getAttributes(), sb);
            } catch (BadLocationException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        Integer fontsize = CurrentCopyPasteProfile.getCurrentCopyPasteFontSize() * 2;
        return buildRTF(fonttable, colortable, sb.toString());
    }
}
