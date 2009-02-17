package org.netbeans.modules.rtfcopypaste;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.netbeans.modules.rtfcopypaste.action.RTFCopyPasteAction;

public final class CopyAsRTFFormatGlobal implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent event) {

        RTFCopyPasteAction.getDefault().action();
    }
}
