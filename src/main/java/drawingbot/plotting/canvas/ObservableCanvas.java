package drawingbot.plotting.canvas;

import drawingbot.api.ICanvas;
import drawingbot.api.IProperties;
import drawingbot.utils.EnumScalingMode;
import drawingbot.javafx.util.PropertyUtil;
import drawingbot.utils.UnitsLength;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

public class ObservableCanvas implements ICanvas, IProperties {

    private static final float defaultWidth = 210, defaultHeight = 297; //DEFAULT - A4 Paper

    public final SimpleBooleanProperty useOriginalSizing = new SimpleBooleanProperty(true);
    public final SimpleObjectProperty<EnumScalingMode> scalingMode = new SimpleObjectProperty<>(EnumScalingMode.CROP_TO_FIT);
    public final SimpleObjectProperty<UnitsLength> inputUnits = new SimpleObjectProperty<>(UnitsLength.MILLIMETRES);

    public final SimpleFloatProperty width = new SimpleFloatProperty(0);
    public final SimpleFloatProperty height = new SimpleFloatProperty(0);
    public final SimpleFloatProperty drawingAreaPaddingLeft = new SimpleFloatProperty(0);
    public final SimpleFloatProperty drawingAreaPaddingRight = new SimpleFloatProperty(0);
    public final SimpleFloatProperty drawingAreaPaddingTop = new SimpleFloatProperty(0);
    public final SimpleFloatProperty drawingAreaPaddingBottom = new SimpleFloatProperty(0);
    public final SimpleBooleanProperty drawingAreaGangPadding = new SimpleBooleanProperty(true);

    public final SimpleBooleanProperty optimiseForPrint = new SimpleBooleanProperty(true);
    public final SimpleFloatProperty targetPenWidth = new SimpleFloatProperty(0.3F);

    //not saved
    public final SimpleObjectProperty<Color> canvasColor = new SimpleObjectProperty<>(Color.WHITE);

    public final ObservableList<Property<?>> observables = PropertyUtil.createPropertiesList(useOriginalSizing, scalingMode, inputUnits, width, height, drawingAreaPaddingLeft, drawingAreaPaddingRight, drawingAreaPaddingTop, drawingAreaPaddingBottom, drawingAreaGangPadding, optimiseForPrint, targetPenWidth, canvasColor);

    public ObservableCanvas(){}

    @Override
    public UnitsLength getUnits() {
        return inputUnits.get();
    }

    @Override
    public EnumScalingMode getScalingMode() {
        return scalingMode.get();
    }

    @Override
    public float getPlottingScale(){
        return optimiseForPrint.get() ? 1F / targetPenWidth.get() : 1;
    }

    @Override
    public float getWidth(){
        if(width.getValue() > 0){
            return width.getValue();
        }
        return defaultWidth;
    }

    @Override
    public float getHeight(){
        if(height.getValue() > 0){
            return height.getValue();
        }
        return defaultHeight;
    }

    @Override
    public float getDrawingWidth(){
        return width.getValue() - drawingAreaPaddingLeft.get() - drawingAreaPaddingRight.get();
    }

    @Override
    public float getDrawingHeight(){
        return height.getValue() - drawingAreaPaddingTop.get() - drawingAreaPaddingBottom.get();
    }

    @Override
    public float getDrawingOffsetX(){
        return drawingAreaPaddingLeft.get();
    }

    @Override
    public float getDrawingOffsetY(){
        return drawingAreaPaddingTop.get();
    }

    @Override
    public boolean useOriginalSizing(){
        return useOriginalSizing.get();
    }

    @Override
    public boolean optimiseForPrint(){
        return optimiseForPrint.get();
    }

    public ObservableCanvas copy(){
        ObservableCanvas copy = new ObservableCanvas();
        copy.useOriginalSizing.set(useOriginalSizing.get());
        copy.scalingMode.set(scalingMode.get());
        copy.inputUnits.set(inputUnits.get());

        copy.width.set(width.get());
        copy.height.set(height.get());
        copy.drawingAreaPaddingLeft.set(drawingAreaPaddingLeft.get());
        copy.drawingAreaPaddingRight.set(drawingAreaPaddingRight.get());
        copy.drawingAreaPaddingTop.set(drawingAreaPaddingTop.get());
        copy.drawingAreaPaddingBottom.set(drawingAreaPaddingBottom.get());
        copy.drawingAreaGangPadding.set(drawingAreaGangPadding.get());

        copy.optimiseForPrint.set(optimiseForPrint.get());
        copy.targetPenWidth.set(targetPenWidth.get());
        copy.canvasColor.set(canvasColor.get());
        return copy;
    }

    @Override
    public ObservableList<Property<?>> getProperties() {
        return observables;
    }
}
