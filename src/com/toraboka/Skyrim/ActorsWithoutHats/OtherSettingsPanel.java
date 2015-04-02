package com.toraboka.Skyrim.ActorsWithoutHats;

import lev.gui.LCheckBox;
import skyproc.gui.SPMainMenuPanel;
import skyproc.gui.SPSettingPanel;
import skyproc.gui.SUMGUI;

/**
 *
 * @author Martin Rudat
 */
public class OtherSettingsPanel extends SPSettingPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = -536996971901639220L;

    LCheckBox importOnStartup;

    public OtherSettingsPanel(SPMainMenuPanel parent_) {
	super(parent_, "Other Settings", ActorsWithoutHats.headerColor);
    }

    @Override
    protected void initialize() {
	super.initialize();

	importOnStartup = new LCheckBox("Import Mods on Startup",
		ActorsWithoutHats.settingsFont, ActorsWithoutHats.settingsColor);
	importOnStartup.tie(YourSaveFile.Settings.IMPORT_AT_START,
		ActorsWithoutHats.save, SUMGUI.helpPanel, true);
	importOnStartup.setOffset(2);
	importOnStartup.addShadow();
	setPlacement(importOnStartup);
	AddSetting(importOnStartup);

	alignRight();

    }
}
