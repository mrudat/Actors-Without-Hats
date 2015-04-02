package com.toraboka.Skyrim.ActorsWithoutHats;

import lev.gui.LTextPane;
import skyproc.gui.SPMainMenuPanel;
import skyproc.gui.SPSettingPanel;

/**
 *
 * @author Martin Rudat
 */
public class WelcomePanel extends SPSettingPanel {

    private static final long serialVersionUID = 1L;

    LTextPane introText;

    public WelcomePanel(SPMainMenuPanel parent_) {
	super(parent_, ActorsWithoutHats.myPatchName,
		ActorsWithoutHats.headerColor);
    }

    @Override
    protected void initialize() {
	super.initialize();

	introText = new LTextPane(settingsPanel.getWidth() - 40, 400,
		ActorsWithoutHats.settingsColor);
	introText.setText(ActorsWithoutHats.welcomeText);
	introText.setEditable(false);
	introText.setFont(ActorsWithoutHats.settingsFont);
	introText.setCentered();
	setPlacement(introText);
	Add(introText);

	alignRight();
    }
}
