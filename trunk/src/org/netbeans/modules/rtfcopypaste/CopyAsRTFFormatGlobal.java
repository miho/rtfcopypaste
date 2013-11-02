package org.netbeans.modules.rtfcopypaste;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.netbeans.modules.rtfcopypaste.action.RTFCopyPasteAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;

@ActionID(id = "org.netbeans.modules.rtfcopypaste.CopyAsRTFFormatGlobal", category = "Edit")
@ActionRegistration(displayName = "#CTL_CopyAsRTFFormatGlobal", iconBase = "resource/cpicon.gif")
@ActionReference(path = "Menu/Edit", position = 987)
public final class CopyAsRTFFormatGlobal implements ActionListener {
    
    @Override
    public void actionPerformed(ActionEvent event) {

        RTFCopyPasteAction.getDefault().action();
    }
}
