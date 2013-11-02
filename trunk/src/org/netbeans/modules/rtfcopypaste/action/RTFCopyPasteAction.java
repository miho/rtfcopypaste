package org.netbeans.modules.rtfcopypaste.action;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.openide.util.ImageUtilities;

public abstract class RTFCopyPasteAction extends AbstractAction {

    private static final RTFCopyPasteAction action = new DefaultRTFCopyPasteAction();

//    private static final Icon icon = new ImageIcon(ImageUtilities.loadImage("resource/cpicon.gif"));
//
//    public RTFCopyPasteAction() {
//        super("Copy as RTF", icon);
//    }
//    
    public static RTFCopyPasteAction getDefault() {
        return action;
    }

    public abstract void action();
}
