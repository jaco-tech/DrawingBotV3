package drawingbot.javafx.preferences;

import static drawingbot.javafx.GenericSetting.*;

import drawingbot.DrawingBotV3;
import drawingbot.api.ISettings;
import drawingbot.files.json.PresetType;
import drawingbot.image.blend.EnumBlendMode;
import drawingbot.javafx.GenericPreset;
import drawingbot.javafx.GenericSetting;
import drawingbot.javafx.settings.*;
import drawingbot.javafx.util.PropertyUtil;
import drawingbot.plotting.canvas.ObservableCanvas;
import drawingbot.registry.Register;
import drawingbot.utils.EnumClippingMode;
import drawingbot.utils.EnumRescaleMode;
import drawingbot.utils.UnitsLength;
import drawingbot.utils.UnitsTime;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBPreferences implements ISettings {

    public final static DBPreferences INSTANCE = new DBPreferences();

    ///////////////////////////////////////////////

    public static final String CATEGORY_GENERAL = "General";
    public static final String CATEGORY_ADVANCED = "Advanced";
    public static final String CATEGORY_OPTIMISATION = "Path Optimisation";
    public static final String CATEGORY_NOTIFICATIONS = "Notifications";
    public static final String CATEGORY_SVG = "SVG";
    public static final String CATEGORY_IMAGE = "Image";
    public static final String CATEGORY_ANIMATION = "Animation";
    public static final String CATEGORY_USER_INTERFACE = "User Interface";

    public final List<GenericSetting<?, ?>> settings = new ArrayList<>();
    public final ObservableList<GenericSetting<?, ?>> observableList;

    private DBPreferences(){
        observableList = PropertyUtil.createPropertiesListFromSettings(settings);
    }

    public <T extends GenericSetting<?, ?>> T register(T add){
        settings.add(add);
        add.createDefaultGetterAndSetter();
        return add;
    }

    ///////////////////////////////////////////////

    public final IntegerSetting<?> maxTextureSize = register(createRangedIntSetting(DBPreferences.class, CATEGORY_ADVANCED, "maxTextureSize", -1, -1, 8096));
    public final BooleanSetting<?> disableOpenGLRenderer = register(createBooleanSetting(DBPreferences.class, CATEGORY_ADVANCED, "disableOpenGLRenderer", false));

    ///////////////////////////////////////////////

    //// DRAWING AREA \\\\
    public final StringSetting<?> defaultPFM = register(createStringSetting(DBPreferences.class, CATEGORY_GENERAL, "defaultPFM", "Sketch Lines PFM"));
    public final ColourSetting<?> defaultCanvasColour = register(createColourSetting(DBPreferences.class, CATEGORY_GENERAL, "defaultCanvasColour", Color.WHITE));
    public final ColourSetting<?> defaultBackgroundColour = register(createColourSetting(DBPreferences.class, CATEGORY_GENERAL, "defaultBackgroundColour", ObservableCanvas.backgroundColourDefault));
    public final OptionSetting<?, EnumClippingMode> defaultClippingMode = register(createOptionSetting(DBPreferences.class, EnumClippingMode.class, CATEGORY_GENERAL, "defaultClippingMode", FXCollections.observableArrayList(EnumClippingMode.values()), EnumClippingMode.DRAWING));
    public final FloatSetting<?> defaultPenWidth = (FloatSetting<?>) register(createFloatSetting(DBPreferences.class, CATEGORY_GENERAL, "defaultPenWidth", 0.3F).setValidator(Math::abs));
    public final OptionSetting<?, EnumRescaleMode> defaultRescalingMode = register(createOptionSetting(DBPreferences.class, EnumRescaleMode.class, CATEGORY_GENERAL, "defaultRescaleNode", FXCollections.observableArrayList(EnumRescaleMode.values()), EnumRescaleMode.HIGH_QUALITY));
    public final BooleanSetting<?> defaultRangeExport = register(createBooleanSetting(DBPreferences.class, CATEGORY_GENERAL, "defaultRangeExport", true));
    public final OptionSetting<?, EnumBlendMode> defaultBlendMode = register(createOptionSetting(DBPreferences.class, EnumBlendMode.class, CATEGORY_GENERAL, "defaultBlendMode", FXCollections.observableArrayList(EnumBlendMode.values()), EnumBlendMode.NORMAL));

    ///////////////////////////////////////////////

    //// PATH OPTIMISATION \\\\

    public final BooleanSetting<?> pathOptimisationEnabled = register(createBooleanSetting(DBPreferences.class, CATEGORY_OPTIMISATION, "pathOptimisationEnabled", true));

    public final BooleanSetting<?> lineSimplifyEnabled = register(createBooleanSetting(DBPreferences.class, CATEGORY_OPTIMISATION, "lineSimplifyEnabled", true));
    public final DoubleSetting<?> lineSimplifyTolerance = register(createRangedDoubleSetting(DBPreferences.class, CATEGORY_OPTIMISATION, "lineSimplifyTolerance", 0.1D, 0.1D, 100D));
    public final OptionSetting<?, UnitsLength> lineSimplifyUnits = register(createOptionSetting(DBPreferences.class, UnitsLength.class, CATEGORY_OPTIMISATION, "lineSimplifyUnits", FXCollections.observableArrayList(UnitsLength.values()), UnitsLength.MILLIMETRES));

    public final BooleanSetting<?> lineMergingEnabled = register(createBooleanSetting(DBPreferences.class, CATEGORY_OPTIMISATION, "lineMergingEnabled", true));
    public final DoubleSetting<?> lineMergingTolerance = register(createRangedDoubleSetting(DBPreferences.class, CATEGORY_OPTIMISATION, "lineMergingTolerance", 0.5D, 0.1D, 100D));
    public final OptionSetting<?, UnitsLength> lineMergingUnits = register(createOptionSetting(DBPreferences.class, UnitsLength.class, CATEGORY_OPTIMISATION, "lineMergingUnits", FXCollections.observableArrayList(UnitsLength.values()), UnitsLength.MILLIMETRES));

    public final BooleanSetting<?> lineFilteringEnabled = register(createBooleanSetting(DBPreferences.class, CATEGORY_OPTIMISATION, "lineFilteringEnabled", false));
    public final DoubleSetting<?> lineFilteringTolerance = register(createRangedDoubleSetting(DBPreferences.class, CATEGORY_OPTIMISATION, "lineFilteringTolerance", 0.5D, 0.1D, 100D));
    public final OptionSetting<?, UnitsLength> lineFilteringUnits = register(createOptionSetting(DBPreferences.class, UnitsLength.class, CATEGORY_OPTIMISATION, "lineFilteringUnits", FXCollections.observableArrayList(UnitsLength.values()), UnitsLength.MILLIMETRES));

    public final BooleanSetting<?> lineSortingEnabled = register(createBooleanSetting(DBPreferences.class, CATEGORY_OPTIMISATION, "lineSortingEnabled", false));
    public final DoubleSetting<?> lineSortingTolerance = register(createRangedDoubleSetting(DBPreferences.class, CATEGORY_OPTIMISATION, "lineSortingTolerance", 1D, 0.1D, 100D));
    public final OptionSetting<?, UnitsLength> lineSortingUnits = register(createOptionSetting(DBPreferences.class, UnitsLength.class, CATEGORY_OPTIMISATION, "lineSortingUnits", FXCollections.observableArrayList(UnitsLength.values()), UnitsLength.MILLIMETRES));

    public final BooleanSetting<?> multipassEnabled = register(createBooleanSetting(DBPreferences.class, CATEGORY_OPTIMISATION, "multipassEnabled", false));
    public final IntegerSetting<?> multipassCount = register(createRangedIntSetting(DBPreferences.class, CATEGORY_OPTIMISATION, "multipassCount", 1, 1, 100));

    ///////////////////////////////////////////////

    //// OVERLAYS \\\\
    public final BooleanSetting<?> rulersEnabled = register(createBooleanSetting(DBPreferences.class, CATEGORY_NOTIFICATIONS, "rulersEnabled", true));
    public final BooleanSetting<?> drawingBordersEnabled = register(createBooleanSetting(DBPreferences.class, CATEGORY_NOTIFICATIONS, "drawingBordersEnabled", false));
    public final ColourSetting<?> drawingBordersColor = register(createColourSetting(DBPreferences.class, CATEGORY_NOTIFICATIONS, "drawingBordersColor", Color.BLACK));

    ///////////////////////////////////////////////

    //// NOTIFICATIONS \\\\

    public final BooleanSetting<?> notificationsEnabled = register(createBooleanSetting(DBPreferences.class, CATEGORY_NOTIFICATIONS, "notificationsEnabled", true));
    public final IntegerSetting<?> notificationsScreenTime = register(createRangedIntSetting(DBPreferences.class, CATEGORY_OPTIMISATION, "notificationsScreenTime", 7, 0, 1000));

    ///////////////////////////////////////////////

    //// SVG SETTINGS \\\\

    public final StringSetting<?> svgLayerNaming = register(createStringSetting(DBPreferences.class, CATEGORY_SVG, "svgLayerNaming", "%NAME%"));
    public final BooleanSetting<?> exportSVGBackground = register(createBooleanSetting(DBPreferences.class, CATEGORY_SVG, "exportSVGBackground", false));

    ///////////////////////////////////////////////

    //// IMAGE SETTINGS \\\\

    public final DoubleSetting<?> exportDPI = register(createRangedDoubleSetting(DBPreferences.class, CATEGORY_IMAGE, "exportDPI", 300D, 1D, Short.MAX_VALUE));

    ///////////////////////////////////////////////

    //// ANIMATION SETTINGS \\\\

    public final IntegerSetting<?> framesPerSecond = register(createIntSetting(DBPreferences.class, CATEGORY_ANIMATION, "framesPerSecond", 25));
    public final IntegerSetting<?> duration = register(createIntSetting(DBPreferences.class, CATEGORY_ANIMATION, "duration", 5));
    public final IntegerSetting<?> frameHoldStart = register(createIntSetting(DBPreferences.class, CATEGORY_ANIMATION, "frameHoldStart", 1));
    public final IntegerSetting<?> frameHoldEnd = register(createIntSetting(DBPreferences.class, CATEGORY_ANIMATION, "frameHoldEnd", 1));
    public final OptionSetting<?, UnitsTime> durationUnits = register(createOptionSetting(DBPreferences.class, UnitsTime.class, CATEGORY_ANIMATION, "durationUnits", UnitsTime.OBSERVABLE_LIST, UnitsTime.SECONDS));

    ///////////////////////////////////////////////

    //// OPEN GL RENDERER \\\\

    public final BooleanSetting<?> darkTheme = register(createBooleanSetting(DBPreferences.class, CATEGORY_USER_INTERFACE, "darkTheme", false));

    ///////////////////////////////////////////////

    //// PRESET SETTINGS \\\\

    private final MapSetting<?, String, String> defaultPresets = register(new MapSetting<>(DBPreferences.class, String.class, String.class, CATEGORY_ADVANCED, "defaultPresets", FXCollections.observableMap(new HashMap<>())));
    public BooleanProperty flagDefaultPresetChange = new SimpleBooleanProperty(false);

    ///////////////////////////////////////////////

    public String getDefaultPreset(String key){
        return defaultPresets.get().get(key);
    }

    public void setDefaultPreset(GenericPreset<?> preset){
        setDefaultPreset(preset.presetType, preset.presetSubType, preset.presetName);
    }

    public void setDefaultPreset(PresetType type, String subtype, String value){
        if(type.defaultsPerSubType){
            setDefaultPreset(type.id + ":" + subtype, value);
        }else{
            setDefaultPreset(type.id, value);
        }
    }

    public void setDefaultPreset(String key, String value){
        defaultPresets.get().put(key, value);
        Register.PRESET_LOADER_CONFIGS.markDirty();
        flagDefaultPresetChange.set(!flagDefaultPresetChange.get());
    }

    public void clearDefaultPreset(PresetType type, String subtype){
        if(type.defaultsPerSubType){
            clearDefaultPreset(type.id + ":" + subtype);
        }else{
            clearDefaultPreset(type.id);
        }
    }


    public void clearDefaultPreset(String key){
        defaultPresets.get().remove(key);
        Register.PRESET_LOADER_CONFIGS.markDirty();
        flagDefaultPresetChange.set(!flagDefaultPresetChange.get());
    }

    ////

    public int getFrameCount(){
        return (int)(framesPerSecond.get() * durationUnits.get().toSeconds(duration.get()));
    }

    public int getFrameHoldStartCount(){
        return (int)(framesPerSecond.get() * durationUnits.get().toSeconds(frameHoldStart.get()));
    }

    public int getFrameHoldEndCount(){
        return (int)(framesPerSecond.get() * durationUnits.get().toSeconds(frameHoldEnd.get()));
    }

    public int getGeometriesPerFrame(int count){
        return Math.max(1, count / getFrameCount());
    }

    public long getVerticesPerFrame(long count){
        return Math.max(1, count / getFrameCount());
    }

    public void postInit(){
        defaultCanvasColour.addListener(observable -> {
            DrawingBotV3.project().getDrawingArea().canvasColor.set(defaultCanvasColour.getValue());
        });
        defaultBackgroundColour.addListener(observable -> {
            DrawingBotV3.project().getDrawingArea().backgroundColor.set(defaultBackgroundColour.getValue());
        });
        defaultPenWidth.addListener(observable -> {
            DrawingBotV3.project().getDrawingArea().targetPenWidth.set(defaultPenWidth.getValue());
        });
        defaultClippingMode.addListener(observable -> {
            DrawingBotV3.project().getDrawingArea().clippingMode.set(defaultClippingMode.getValue());
        });
        defaultRescalingMode.addListener(observable -> {
            DrawingBotV3.project().getDrawingArea().rescaleMode.set(defaultRescalingMode.getValue());
        });
        defaultBlendMode.addListener(observable -> {
            DrawingBotV3.project().blendMode.set(defaultBlendMode.getValue());
        });
        defaultRangeExport.addListener(observable -> {
            DrawingBotV3.project().exportRange.set(defaultRangeExport.getValue());
        });
    }

    @Override
    public ObservableList<GenericSetting<?, ?>> getObservables() {
        return observableList;
    }
}