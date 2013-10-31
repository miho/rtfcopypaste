package org.netbeans.modules.rtfcopypaste.converters;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JEditorPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;
import org.netbeans.api.editor.mimelookup.MimeLookup;
import org.netbeans.api.editor.mimelookup.MimePath;
import org.netbeans.api.editor.settings.FontColorSettings;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.editor.Coloring;
import org.netbeans.modules.editor.lib2.highlighting.HighlightingManager;
import org.netbeans.spi.editor.highlighting.HighlightsContainer;
import org.netbeans.spi.editor.highlighting.HighlightsSequence;
import org.openide.util.Exceptions;

public class CurrentProfileRTFConverter extends RTFConverter {

    final private Map<String, Integer> colortblRev = new HashMap<String, Integer>();
    final private ArrayList<Color> colortbl = new ArrayList<Color>();
    private int prev_font_style = Font.PLAIN;
    private Color prev_font_color = Color.BLACK;
    private Color default_fcolor = Color.BLACK;
    private Color default_bcolor = Color.WHITE;
    private String rtfContentTmp = "";
    
    private String processColorToken(Color color) {
        String cs = color.toString();
        if (!colortblRev.containsKey(cs)) {
            colortbl.add(color);
            colortblRev.put(cs, new Integer(colortbl.size()));
            return colorToRTF(color) + ";";
        }
        return "";
    }

    private String createColorTable(JEditorPane pane) {
        int start = pane.getSelectionStart();
        int end = pane.getSelectionEnd();
        
        if (start == end) {
            start = 0;
            end = pane.getText().length();
        }
        
        HighlightsContainer bh = HighlightingManager.getInstance(pane).getBottomHighlights();
        //HighlightsContainer th = HighlightingManager.getInstance(pane).getTopHighlights();
        
        HighlightsSequence bhs = bh.getHighlights(start, end);
        //HighlightsSequence ths = th.getHighlights(start, end);        

        final FontColorSettings fcs = MimeLookup.getLookup(
            MimePath.get(pane.getContentType())).lookup(FontColorSettings.class);

        default_fcolor = (Color) fcs.getTokenFontColors("default").getAttribute(StyleConstants.Foreground);
       // default_fcolor = pane.getForeground();
      //  Color test = (Color) pane.getFont().getAttributes().get(StyleConstants.Foreground);

        String colorTblStr = "{\\colortbl;";
        colorTblStr += processColorToken(default_fcolor);
        //colorTblStr += processColorToken(default_bcolor);

        while (bhs.moveNext()) {
            AttributeSet attr = bhs.getAttributes();
            final Color fc = (Color) attr.getAttribute(StyleConstants.Foreground);
          //  final Color bc = (Color) attr.getAttribute(StyleConstants.Background);
            if (fc != null) colorTblStr += processColorToken(fc);
        }

        colorTblStr += "}";
        return colorTblStr;
    }

    private void emitRTF(String rtf, int fs, Color fc, StringBuilder rtfContent) {
        if (fs == Font.BOLD) rtf = "{\\b " + rtf + "}";
        else if (fs == Font.ITALIC) rtf = "{\\i " + rtf + "}";
        else if (fs == Font.BOLD + Font.ITALIC) rtf = "{\\b\\i " + rtf + "}";
        int pos = 0;
        if (fc != null) pos = colortblRev.get(fc.toString()).intValue();
        rtf = "{\\cf" + pos + " " + rtf + "}";
        rtfContent.append(rtf);
    }
    
    private void processToken(final String token,
            final AttributeSet attr, 
            StringBuilder rtfContent) {
        
        String tokenRtf = getRtfContent(token);
        
        Color font_color = prev_font_color;
        int font_style = prev_font_style;
        if (attr != null) {
            font_color = (Color) attr.getAttribute(StyleConstants.Foreground);
            if (font_color == null) font_color = default_fcolor;
            
            final Font font = Coloring.fromAttributeSet(attr).getFont();
            if (font != null) font_style = font.getStyle();
            else font_style = 0;
        }
        String tokentTrim = rtfContentTmp.trim();
        
        if (tokentTrim.length() == 0 || (font_style == prev_font_style && font_color.equals(prev_font_color))) {
            rtfContentTmp += tokenRtf;
            prev_font_style = font_style;
            prev_font_color = font_color;
        } else {
            emitRTF(rtfContentTmp, prev_font_style, prev_font_color, rtfContent);
            
            rtfContentTmp = tokenRtf;
            prev_font_color = font_color;
            prev_font_style = font_style;
        }
    }

    @Override
    public String convertContentToRTF(JEditorPane pane) {
        String colortable = createColorTable(pane);
        String fonttable = createFontTable(pane);

        int start = pane.getSelectionStart();
        int end = pane.getSelectionEnd();
        
        if (start == end) {
            start = 1;
            end = pane.getText().length();
        }
        
        HighlightsContainer bh = HighlightingManager.getInstance(pane).getBottomHighlights();
  //      HighlightsContainer th = HighlightingManager.getInstance(pane).getTopHighlights();
        
        HighlightsSequence bhs = bh.getHighlights(start, end);
//        HighlightsSequence ths = th.getHighlights(start, end);
        
    //    default_bcolor = pane.getBackground();
        
        int token_end_prev = -2;
        StringBuilder rtfContent = new StringBuilder("");
        
        while (bhs.moveNext()) {
            try {
                int token_start = bhs.getStartOffset();
                int token_end = bhs.getEndOffset();
                
                String token = pane.getText(token_start, token_end - token_start);
                
                if (token_start > token_end_prev && token_end_prev >= 0) {
                    String token_miss = pane.getText(token_end_prev, token_start-token_end_prev);
                    processToken(token_miss, null, rtfContent);
                }
                processToken(token, bhs.getAttributes(), rtfContent);
                token_end_prev = token_end;
            } catch (BadLocationException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        if (rtfContentTmp.length() > 0) {
            emitRTF(rtfContentTmp, prev_font_style, prev_font_color, rtfContent);
        }
        
        return buildRTF(fonttable, colortable, rtfContent.toString());
    }
}
