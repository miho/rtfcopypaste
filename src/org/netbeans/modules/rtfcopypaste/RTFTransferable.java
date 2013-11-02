package org.netbeans.modules.rtfcopypaste;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class RTFTransferable implements Transferable {

    private Object data = null;
    private final DataFlavor flavor = new DataFlavor("text/rtf", "Rich Text Format");

    public RTFTransferable(Object o) {
        this.data = o;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] ret = {flavor};
        return ret;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor df) {
        return flavor.isMimeTypeEqual(df);
    }

    @Override
    public Object getTransferData(DataFlavor df) throws UnsupportedFlavorException, IOException {
        if (!df.isMimeTypeEqual(flavor)) {
            throw new UnsupportedFlavorException(df);
        }
        return data;
    }
}
