package drawingbot.files.json.presets;

import drawingbot.files.json.AbstractPresetLoader;
import drawingbot.files.json.PresetData;
import drawingbot.files.json.PresetType;
import drawingbot.javafx.GenericPreset;
import drawingbot.registry.MasterRegistry;

public class PresetUISettingsLoader extends AbstractPresetLoader<PresetData> {

    public PresetUISettingsLoader(PresetType presetType) {
        super(PresetData.class, presetType, "ui_presets.json");
        setDefaultManager(new PresetUISettingsManager(this));
    }

    @Override
    public GenericPreset<PresetData> getDefaultPreset() {
        return MasterRegistry.INSTANCE.getDefaultPreset(this, "Default");
    }

    @Override
    protected PresetData getPresetInstance(GenericPreset<PresetData> preset) {
        return new PresetData();
    }
}
