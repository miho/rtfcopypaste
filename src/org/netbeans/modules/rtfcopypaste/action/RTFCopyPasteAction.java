package org.netbeans.modules.rtfcopypaste.action;

public abstract class RTFCopyPasteAction {

    private static final RTFCopyPasteAction action = new DefaultRTFCopyPasteAction();

    public static RTFCopyPasteAction getDefault() {
        return action;
    }

    public abstract void action();
}
