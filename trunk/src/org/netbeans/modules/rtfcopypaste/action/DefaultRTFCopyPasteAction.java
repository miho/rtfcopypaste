package org.netbeans.modules.rtfcopypaste.action;

import java.awt.Toolkit;
import java.io.ByteArrayInputStream;
import javax.swing.JEditorPane;
import org.netbeans.modules.rtfcopypaste.RTFTransferable;
import org.netbeans.modules.rtfcopypaste.converters.SwapCurrentProfileRTFConverter;
import org.openide.cookies.EditorCookie;
import org.openide.util.Utilities;

public class DefaultRTFCopyPasteAction extends RTFCopyPasteAction {

    private void setRtfContest(
            ByteArrayInputStream bais) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new RTFTransferable(bais), null);
    }

    @Override
    public void action() {
        final EditorCookie editorCookie =
                Utilities.actionsGlobalContext().lookup(EditorCookie.class);
        if (editorCookie != null) {
            for (final JEditorPane pane : editorCookie.getOpenedPanes()) {
                if (pane != null && pane.isShowing() &&
                        pane.getSelectionEnd() > pane.getSelectionStart()) {
                    try {
                        String formattedText = (new SwapCurrentProfileRTFConverter()).convertContentToRTF(pane);
                        System.out.println(formattedText);
                        setRtfContest(new ByteArrayInputStream(formattedText.getBytes()));
                        return;
                    } catch (final Throwable e) {
                        org.openide.ErrorManager.getDefault().notify(e);
                    }
                }
            }
        }
    }
}
