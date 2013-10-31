package org.netbeans.modules.rtfcopypaste.utils;

import java.util.Set;

public abstract class EditorProfileManager {

    private static final EditorProfileManager manager = new DefaultEditorProfileManager();

    public static EditorProfileManager getDefault() {
        return manager;
    }

    public abstract Set<String> getFontAndColorsProfiles();

    public abstract String getCurrentFontAndColorsProfile();

    public abstract void setCurrentFontAndColorProfile(String profile);
}
