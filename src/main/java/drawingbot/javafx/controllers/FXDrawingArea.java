package drawingbot.javafx.controllers;

import drawingbot.files.json.presets.PresetDrawingArea;
import drawingbot.javafx.FXHelper;
import drawingbot.javafx.GenericPreset;
import drawingbot.plotting.canvas.ObservableCanvas;
import drawingbot.registry.Register;
import drawingbot.utils.EnumScalingMode;
import drawingbot.utils.UnitsLength;
import drawingbot.utils.Utils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.NumberStringConverter;

public class FXDrawingArea {

    public final SimpleObjectProperty<ObservableCanvas> drawingArea = new SimpleObjectProperty<>();
    public final SimpleStringProperty drawingAreaPaddingGangValue = new SimpleStringProperty("0");

    ////////////////////////////////////////////////////////

    public ComboBox<GenericPreset<PresetDrawingArea>> comboBoxDrawingAreaPreset = null;
    public MenuButton menuButtonDrawingAreaPresets = null;

    /////SIZING OPTIONS
    public CheckBox checkBoxOriginalSizing = null;
    public ChoiceBox<UnitsLength> choiceBoxDrawingUnits = null;
    public Pane paneDrawingAreaCustom = null;
    public TextField textFieldDrawingWidth = null;
    public TextField textFieldDrawingHeight = null;
    public Button buttonRotate = null;
    public TextField textFieldPaddingLeft = null;
    public TextField textFieldPaddingRight = null;
    public TextField textFieldPaddingTop = null;
    public TextField textFieldPaddingBottom = null;
    public CheckBox checkBoxGangPadding = null;

    public ChoiceBox<EnumScalingMode> choiceBoxScalingMode = null;

    public CheckBox checkBoxOptimiseForPrint = null;
    public TextField textFieldPenWidth = null;

    public ColorPicker colorPickerCanvas = null;

    @FXML
    public void initialize(){

        drawingArea.addListener((observable, oldValue, newValue) -> {
            if(oldValue != null){
                checkBoxOriginalSizing.selectedProperty().unbindBidirectional(oldValue.useOriginalSizing);
                choiceBoxDrawingUnits.valueProperty().unbindBidirectional(oldValue.inputUnits);

                textFieldDrawingWidth.textProperty().unbindBidirectional(oldValue.width);
                textFieldDrawingHeight.textProperty().unbindBidirectional(oldValue.height);

                textFieldPaddingLeft.textProperty().unbindBidirectional(oldValue.drawingAreaPaddingLeft);
                textFieldPaddingRight.textProperty().unbindBidirectional(oldValue.drawingAreaPaddingRight);
                textFieldPaddingTop.textProperty().unbindBidirectional(oldValue.drawingAreaPaddingTop);
                textFieldPaddingBottom.textProperty().unbindBidirectional(oldValue.drawingAreaPaddingBottom);
                checkBoxGangPadding.selectedProperty().unbindBidirectional(oldValue.drawingAreaGangPadding);

                choiceBoxScalingMode.valueProperty().unbindBidirectional(oldValue.scalingMode);

                checkBoxOptimiseForPrint.selectedProperty().unbindBidirectional(oldValue.optimiseForPrint);
                textFieldPenWidth.textProperty().unbindBidirectional(oldValue.targetPenWidth);

                colorPickerCanvas.valueProperty().unbindBidirectional(oldValue.canvasColor);
            }

            if(newValue != null){
                checkBoxOriginalSizing.selectedProperty().bindBidirectional(newValue.useOriginalSizing);
                choiceBoxDrawingUnits.valueProperty().bindBidirectional(newValue.inputUnits);

                textFieldDrawingWidth.textProperty().bindBidirectional(newValue.width, new NumberStringConverter(Utils.defaultDF));
                textFieldDrawingHeight.textProperty().bindBidirectional(newValue.height, new NumberStringConverter(Utils.defaultDF));

                textFieldPaddingLeft.textProperty().bindBidirectional(newValue.drawingAreaPaddingLeft, new NumberStringConverter());
                textFieldPaddingRight.textProperty().bindBidirectional(newValue.drawingAreaPaddingRight, new NumberStringConverter());
                textFieldPaddingTop.textProperty().bindBidirectional(newValue.drawingAreaPaddingTop, new NumberStringConverter());
                textFieldPaddingBottom.textProperty().bindBidirectional(newValue.drawingAreaPaddingBottom, new NumberStringConverter());
                checkBoxGangPadding.selectedProperty().bindBidirectional(newValue.drawingAreaGangPadding);

                choiceBoxScalingMode.valueProperty().bindBidirectional(newValue.scalingMode);

                checkBoxOptimiseForPrint.selectedProperty().bindBidirectional(newValue.optimiseForPrint);
                textFieldPenWidth.textProperty().bindBidirectional(newValue.targetPenWidth, new NumberStringConverter());

                colorPickerCanvas.valueProperty().bindBidirectional(newValue.canvasColor);
                updatePaddingBindings(newValue.drawingAreaGangPadding.get());
            }
        });

        colorPickerCanvas.setValue(Color.WHITE);

        comboBoxDrawingAreaPreset.setItems(Register.PRESET_LOADER_DRAWING_AREA.presets);
        comboBoxDrawingAreaPreset.setValue(Register.PRESET_LOADER_DRAWING_AREA.getDefaultPreset());
        comboBoxDrawingAreaPreset.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                Register.PRESET_LOADER_DRAWING_AREA.applyPreset(newValue);
            }
        });

        FXHelper.setupPresetMenuButton(Register.PRESET_LOADER_DRAWING_AREA, menuButtonDrawingAreaPresets, false, comboBoxDrawingAreaPreset::getValue, (preset) -> {
            comboBoxDrawingAreaPreset.setValue(preset);

            ///force update rendering
            comboBoxDrawingAreaPreset.setItems(Register.PRESET_LOADER_DRAWING_AREA.presets);
            comboBoxDrawingAreaPreset.setButtonCell(new ComboBoxListCell<>());
        });


        /////SIZING OPTIONS

        paneDrawingAreaCustom.disableProperty().bind(checkBoxOriginalSizing.selectedProperty());
        choiceBoxDrawingUnits.disableProperty().bind(checkBoxOriginalSizing.selectedProperty());

        choiceBoxDrawingUnits.getItems().addAll(UnitsLength.values());
        choiceBoxDrawingUnits.setValue(UnitsLength.MILLIMETRES);

        textFieldDrawingWidth.textFormatterProperty().setValue(new TextFormatter<>(new FloatStringConverter(), 0F));
        textFieldDrawingHeight.textFormatterProperty().setValue(new TextFormatter<>(new FloatStringConverter(), 0F));

        buttonRotate.setOnAction(e -> {
            String width = textFieldDrawingWidth.getText();
            String height = textFieldDrawingHeight.getText();
            textFieldDrawingWidth.setText(height);
            textFieldDrawingHeight.setText(width);
        });

        textFieldPaddingLeft.textFormatterProperty().setValue(new TextFormatter<>(new FloatStringConverter(), 0F));
        textFieldPaddingRight.textFormatterProperty().setValue(new TextFormatter<>(new FloatStringConverter(), 0F));
        textFieldPaddingTop.textFormatterProperty().setValue(new TextFormatter<>(new FloatStringConverter(), 0F));
        textFieldPaddingBottom.textFormatterProperty().setValue(new TextFormatter<>(new FloatStringConverter(), 0F));

        checkBoxGangPadding.selectedProperty().addListener((observable, oldValue, newValue) -> updatePaddingBindings(newValue));
        updatePaddingBindings(checkBoxGangPadding.isSelected());

        choiceBoxScalingMode.getItems().addAll(EnumScalingMode.values());
        choiceBoxScalingMode.setValue(EnumScalingMode.CROP_TO_FIT);

        textFieldPenWidth.textFormatterProperty().setValue(new TextFormatter<>(new FloatStringConverter(), 0.3F));
        textFieldPenWidth.disableProperty().bind(checkBoxOptimiseForPrint.selectedProperty().not());
    }

    public void updatePaddingBindings(boolean ganged){
        if(ganged){
            drawingAreaPaddingGangValue.set("0");
            textFieldPaddingLeft.textProperty().bindBidirectional(drawingAreaPaddingGangValue);
            textFieldPaddingRight.textProperty().bindBidirectional(drawingAreaPaddingGangValue);
            textFieldPaddingTop.textProperty().bindBidirectional(drawingAreaPaddingGangValue);
            textFieldPaddingBottom.textProperty().bindBidirectional(drawingAreaPaddingGangValue);
        }else{
            textFieldPaddingLeft.textProperty().unbindBidirectional(drawingAreaPaddingGangValue);
            textFieldPaddingRight.textProperty().unbindBidirectional(drawingAreaPaddingGangValue);
            textFieldPaddingTop.textProperty().unbindBidirectional(drawingAreaPaddingGangValue);
            textFieldPaddingBottom.textProperty().unbindBidirectional(drawingAreaPaddingGangValue);
            if(drawingArea.get() != null){
                textFieldPaddingLeft.textProperty().bindBidirectional(drawingArea.get().drawingAreaPaddingLeft, new NumberStringConverter());
                textFieldPaddingRight.textProperty().bindBidirectional(drawingArea.get().drawingAreaPaddingRight, new NumberStringConverter());
                textFieldPaddingTop.textProperty().bindBidirectional(drawingArea.get().drawingAreaPaddingTop, new NumberStringConverter());
                textFieldPaddingBottom.textProperty().bindBidirectional(drawingArea.get().drawingAreaPaddingBottom, new NumberStringConverter());
            }
        }
    }

}
