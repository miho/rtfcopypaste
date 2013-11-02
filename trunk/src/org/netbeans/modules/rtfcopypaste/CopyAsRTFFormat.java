package org.netbeans.modules.rtfcopypaste;

import org.netbeans.modules.rtfcopypaste.action.RTFCopyPasteAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.cookies.EditorCookie;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;


@ActionID(id = "org.netbeans.modules.rtfcopypaste.CopyAsRTFFormat", category = "Edit")
@ActionRegistration(lazy = false, displayName = "Copy as RTF")
@ActionReferences(value = {
    @ActionReference(path = "Editors/text/x-ruby/Popup", position = 2000),
    @ActionReference(path = "Editors/text/x-scala/Popup", position = 2000),
    @ActionReference(path = "Editors/text/x-python/Popup", position = 2000),
    @ActionReference(path = "Editors/text/x-httpd-eruby/Popup", position = 2000),
    @ActionReference(path = "Editors/text/x-schema+xml/Popup", position = 2000),
    @ActionReference(path = "Editors/text/xml-dtd/Popup", position = 2000),
    @ActionReference(path = "Editors/text/text/Popup", position = 2000),
    @ActionReference(path = "Editors/text/javascript/Popup", position = 2000),
    @ActionReference(path = "Editors/text/x-ant+xml/Popup", position = 2000),
    @ActionReference(path = "Editors/text/x-c++/Popup", position = 2000),
    @ActionReference(path = "Editors/text/x-c/Popup", position = 2000),
    @ActionReference(path = "Editors/text/x-css/Popup", position = 2000),
    @ActionReference(path = "Editors/text/x-jsf+xml/Popup", position = 2000),
    @ActionReference(path = "Editors/text/x-sql/Popup", position = 2000),
    @ActionReference(path = "Editors/text/x-struts+xml/Popup", position = 2000),
    @ActionReference(path = "Editors/text/x-wsdl+xml/Popup", position = 2000),
    @ActionReference(path = "Editors/text/xml/Popup", position = 2000),
    @ActionReference(path = "Editors/text/x-java/Popup", position = 2000)})
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
    protected final Class<?>[] cookieClasses() {
        return new Class<?>[]{
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
