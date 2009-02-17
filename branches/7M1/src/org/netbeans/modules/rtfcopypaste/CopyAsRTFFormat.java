package org.netbeans.modules.rtfcopypaste;

import org.netbeans.modules.rtfcopypaste.action.RTFCopyPasteAction;
import org.openide.cookies.EditorCookie;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;


public final class CopyAsRTFFormat extends CookieAction {

    @Override
    protected final void performAction(final Node[] activatedNodes) {
        RTFCopyPasteAction.getDefault().action();
    }

    @Override
    protected final int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }

    @Override
    public final String getName() {
        return NbBundle.getMessage(CopyAsRTFFormat.class, "CTL_CopyAsRTFFormat");
    }

    @Override
    protected final Class[] cookieClasses() {
        return new Class[]{
                    EditorCookie.class
                };
    }

    @Override
    protected final void initialize() {
        super.initialize();
        putValue("noIconInMenu", Boolean.TRUE);
    }

    @Override
    public final HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected final boolean asynchronous() {
        return false;
    }
}
