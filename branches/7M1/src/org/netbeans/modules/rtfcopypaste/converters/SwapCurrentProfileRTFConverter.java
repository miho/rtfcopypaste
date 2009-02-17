package org.netbeans.modules.rtfcopypaste.converters;

import javax.swing.JEditorPane;
import org.netbeans.modules.rtfcopypaste.utils.CurrentCopyPasteProfile;
import org.netbeans.modules.rtfcopypaste.utils.EditorProfileManager;

public class SwapCurrentProfileRTFConverter extends RTFConverter {

    @Override
    public String convertContentToRTF(JEditorPane pane) {
        String rtftext = null;
        String current = EditorProfileManager.getDefault().getCurrentFontAndColorsProfile();
        String currentCopyPaste = CurrentCopyPasteProfile.getCurrentCopyPasteProfile();
        RTFConverter rtfConverter;
        if (CurrentCopyPasteProfile.getCurrentCopyPasteOption().equals("HIGHTLIGHTS")) {
            rtfConverter = new CurrentProfileRTFConverter();
        } else {
            rtfConverter = new DefaultRTFConverter();
        }

        if (current.equals(currentCopyPaste)) {
            rtftext = rtfConverter.convertContentToRTF(pane);
        } else {
            EditorProfileManager.getDefault().setCurrentFontAndColorProfile(currentCopyPaste);
            rtftext = rtfConverter.convertContentToRTF(pane);
            EditorProfileManager.getDefault().setCurrentFontAndColorProfile(current);
        }
        return rtftext;
    }
}
