package com.toraboka.Skyrim.ActorsWithoutHats;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import lev.gui.LSaveFile;
import skyproc.ARMA;
import skyproc.ARMO;
import skyproc.BodyTemplate;
import skyproc.BodyTemplate.BodyTemplateType;
import skyproc.FormID;
import skyproc.GRUP;
import skyproc.GRUP_TYPE;
import skyproc.Mod;
import skyproc.ModListing;
import skyproc.SPDatabase;
import skyproc.SPGlobal;
import skyproc.SkyProcSave;
import skyproc.genenums.FirstPersonFlags;
import skyproc.genenums.Gender;
import skyproc.genenums.Perspective;
import skyproc.gui.SPMainMenuPanel;
import skyproc.gui.SUM;
import skyproc.gui.SUMGUI;

import com.toraboka.Skyrim.ActorsWithoutHats.YourSaveFile.Settings;

public class ActorsWithoutHats implements SUM {

    public static String authorName = "Martin Rudat";

    public static String descriptionToShowInSUM = "Makes hats (and other hair-concealing head-wear) invisible.";

    public static Color headerColor = new Color(66, 181, 184); // Teal

    public static String myPatchName = "Actors Without Hats";

    public static SkyProcSave save = new YourSaveFile();

    public static Color settingsColor = new Color(72, 179, 58); // Green

    public static Font settingsFont = new Font("Serif", Font.BOLD, 15);

    public static String version = "0.0.1";

    public static String welcomeText = "Makes hats (and other hair-concealing head-wear) invisible.";

    public static void main(String[] args) {
	try {
	    SPGlobal.createGlobalLog();
	    SUMGUI.open(new ActorsWithoutHats(), args);
	} catch (Exception e) {
	    // If a major error happens, print it everywhere and display a
	    // message box.
	    System.err.println(e.toString());
	    SPGlobal.logException(e);
	    JOptionPane.showMessageDialog(null,
		    "There was an exception thrown during program execution: '"
			    + e
			    + "'  Check the debug logs or contact the author.");
	    SPGlobal.closeDebug();
	}
    }

    GRUP_TYPE[] dangerousRecordTypes = new GRUP_TYPE[] { GRUP_TYPE.ARMO };

    GRUP_TYPE[] importRequests = new GRUP_TYPE[] { GRUP_TYPE.ARMA,
	    GRUP_TYPE.ARMO };

    private Mod merger;

    private Mod patch;

    private FormID invisibleArmourAddOn;

    @Override
    public GRUP_TYPE[] dangerousRecordReport() {
	return dangerousRecordTypes;
    }

    @Override
    public String description() {
	return descriptionToShowInSUM;
    }

    @Override
    public Mod getExportPatch() {
	Mod out = new Mod(getListing());
	out.setAuthor(authorName);
	return out;
    }

    @Override
    public Color getHeaderColor() {
	return headerColor;
    }

    @Override
    public ModListing getListing() {
	return new ModListing(getName(), false);
    }

    @Override
    public URL getLogo() {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getName() {
	return myPatchName;
    }

    @Override
    public LSaveFile getSave() {
	return save;
    }

    // This is where you add panels to the main menu.
    // First create custom panel classes (as shown by YourFirstSettingsPanel),
    // Then add them here.
    @Override
    public SPMainMenuPanel getStandardMenu() {
	SPMainMenuPanel settingsMenu = new SPMainMenuPanel(getHeaderColor());

	settingsMenu.setWelcomePanel(new WelcomePanel(settingsMenu));
	settingsMenu.addMenu(new OtherSettingsPanel(settingsMenu), false, save,
		Settings.OTHER_SETTINGS);

	return settingsMenu;
    }

    @Override
    public String getVersion() {
	return version;
    }

    // Usually false unless you want to make your own GUI
    @Override
    public boolean hasCustomMenu() {
	return false;
    }

    @Override
    public boolean hasLogo() {
	return false;
    }

    @Override
    public boolean hasSave() {
	return true;
    }

    @Override
    public boolean hasStandardMenu() {
	return true;
    }

    @Override
    public boolean importAtStart() {
	return false;
    }

    @Override
    public GRUP_TYPE[] importRequests() {
	return importRequests;
    }

    @Override
    public boolean needsPatching() {
	save.setStrings(Settings.LAST_MOD_LIST, SPDatabase.getModListDates());
	try {
	    FileTime temp = Files.getLastModifiedTime(FileSystems.getDefault()
		    .getPath("Actors Without Hats.jar"));
	    save.setStr(Settings.JAR_LAST_MOD, temp.toString());
	} catch (IOException e) {
	    SPGlobal.logException(e);
	}
	return false;
    }

    @Override
    public void onExit(boolean patchWasGenerated) throws Exception {
    }

    @Override
    public void onStart() throws Exception {

    }

    @Override
    public JFrame openCustomMenu() {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<ModListing> requiredMods() {
	return new ArrayList<>(0);
    }

    @Override
    public void runChangesToPatch() {

	patch = SPGlobal.getGlobalPatch();
	patch.setAuthor(authorName);

	merger = new Mod(getName() + "_TEMP", false);
	merger.addAsOverrides(SPGlobal.getDB());

	GRUP<ARMO> armours = merger.getArmors();

	int count = 0;

	FirstPersonFlags[] flags = new FirstPersonFlags[] {
		FirstPersonFlags.HAIR, FirstPersonFlags.EARS,
		FirstPersonFlags.HEAD, FirstPersonFlags.LONG_HAIR };

	SUMGUI.progress.setMax(merger.getArmors().size(), "Processing Armour");
	for (ARMO armour : armours) {
	    if (!armour.getTemplate().equals(FormID.NULL)) {
		count++;
		SUMGUI.progress.setBar(count);
		continue;
	    }

	    BodyTemplate bt = armour.getBodyTemplate();
	    // if (bt.get(GeneralFlags.ModulatesVoice)) ...
	    // ArmorType at = bt.getArmorType(BodyTemplateType.Biped);
	    // if (at == ArmorType.LIGHT) ...

	    boolean isCirclet = bt.get(BodyTemplateType.Biped,
		    FirstPersonFlags.CIRCLET);

	    boolean isCovering = false;
	    for (FirstPersonFlags fpf : flags) {
		if (bt.get(BodyTemplateType.Biped, fpf)) {
		    isCovering = true;
		    break;
		}
	    }

	    boolean process = isCirclet && isCovering;

	    if (!process) {
		count++;
		SUMGUI.progress.setBar(count);
		continue;
	    }

	    patch.addRecord(armour);

	    for (FirstPersonFlags fpf : flags) {
		bt.set(BodyTemplateType.Biped, fpf, false);
	    }

	    for (FormID armourAddOn : new ArrayList<FormID>(
		    armour.getArmatures())) {
		armour.removeArmature(armourAddOn);
	    }

	    armour.addArmature(getInvisibleArmorAddOn());

	    count++;
	    SUMGUI.progress.setBar(count);
	}
    }

    private FormID getInvisibleArmorAddOn() {
	if (invisibleArmourAddOn != null) {
	    return invisibleArmourAddOn;
	}
	ARMA armourAddOn = merger.getArmatures().get("Circlet01AA")
		.copy("ActorsWithoutHatsHatAA");

	for (Gender gender : Gender.values()) {
	    for (Perspective perspective : Perspective.values()) {
		armourAddOn.setModelPath(null, gender, perspective);
	    }
	}

	armourAddOn.setWeaponAdjust(0);

	// shouldn't the be on the armour itself?
	armourAddOn.setDetectionSoundValue(0);

	invisibleArmourAddOn = armourAddOn.getForm();
	return invisibleArmourAddOn;
    }
}
