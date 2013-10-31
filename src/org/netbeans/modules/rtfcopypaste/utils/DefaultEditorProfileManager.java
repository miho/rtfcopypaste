package org.netbeans.modules.rtfcopypaste.utils;

import java.util.Set;
import org.netbeans.modules.editor.settings.storage.api.EditorSettings;

public class DefaultEditorProfileManager extends EditorProfileManager {

    @Override
    public Set<String> getFontAndColorsProfiles() {
        return EditorSettings.getDefault().getFontColorProfiles();
    }

    @Override
    public String getCurrentFontAndColorsProfile() {
        return EditorSettings.getDefault().getCurrentFontColorProfile();
    }

    @Override
    public void setCurrentFontAndColorProfile(String profile) {       
        EditorSettings.getDefault().setCurrentFontColorProfile(profile);
    }
}
