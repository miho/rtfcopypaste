package org.netbeans.modules.rtfcopypaste.action;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import org.netbeans.modules.rtfcopypaste.RTFTransferable;
import org.netbeans.modules.rtfcopypaste.converters.RTFConverter;
import org.netbeans.modules.rtfcopypaste.converters.SwapCurrentProfileRTFConverter;
import org.openide.cookies.EditorCookie;
import org.openide.util.ImageUtilities;
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
                if (pane != null && pane.isShowing()) {
                    try {
                        RTFConverter converter = new SwapCurrentProfileRTFConverter();
                        String formattedText = converter.convertContentToRTF(pane);
                        setRtfContest(new ByteArrayInputStream(formattedText.getBytes()));
                        return;
                    } catch (final Throwable e) {
                        org.openide.ErrorManager.getDefault().notify(e);
                    }
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("xxx");
    }
}
