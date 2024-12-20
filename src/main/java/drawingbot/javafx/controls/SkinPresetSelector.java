package drawingbot.javafx.controls;

import drawingbot.javafx.GenericPreset;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Callback;

public class SkinPresetSelector<TARGET, DATA> extends SkinBase<ControlPresetSelector<TARGET, DATA>> {

    protected ComboBox<GenericPreset<DATA>> comboBox;
    protected MenuButton menuButton;
    protected HBox hBox;

    protected SkinPresetSelector(ControlPresetSelector<TARGET, DATA> control) {
        super(control);
        hBox = new HBox(4);

        comboBox = createComboBox(control);
        hBox.getChildren().add(comboBox);

        if(!control.disablePresetMenu()) {
            menuButton = SkinPresetsButton.createDefaultMenuButton(control);
            hBox.getChildren().add(menuButton);
        }

        HBox.setHgrow(hBox, Priority.ALWAYS);
        getChildren().add(hBox);

        //Update the combo box if the factory is altered
        control.comboBoxFactoryProperty().addListener((observable, oldValue, newValue) -> {
            refreshComboBox();
        });

        //If the preset menu is re-enabled add it back to the HBox
        control.disablePresetMenuProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue && menuButton != null){
                hBox.getChildren().remove(menuButton);
            }
            if(!newValue){
                if(menuButton == null){
                    menuButton = SkinPresetsButton.createDefaultMenuButton(control);
                }
                if(!hBox.getChildren().contains(menuButton)){
                    hBox.getChildren().set(1, menuButton);
                }
            }
        });
    }

    private void refreshComboBox(){
        ControlPresetSelector<TARGET, DATA> control = getSkinnable();

        if(shouldRecreateComboBoxOnRefresh()){
            ComboBox<GenericPreset<DATA>> oldComboBox = comboBox;
            ComboBox<GenericPreset<DATA>> newComboBox = createComboBox(control);

            if(oldComboBox == newComboBox){
                return;
            }

            destroyComboBox(control, oldComboBox);
            hBox.getChildren().set(0, newComboBox);
            comboBox = newComboBox;
        }else{
            //Unbind the data
            comboBox.itemsProperty().unbind();
            comboBox.valueProperty().unbindBidirectional(control.activePresetProperty());

            //Rebing the data
            comboBox.itemsProperty().bind(control.filteredPresetsProperty());
            comboBox.valueProperty().bindBidirectional(control.activePresetProperty());

            //Refresh the button cell
            ListCell<GenericPreset<DATA>> cell = comboBox.getButtonCell();
            comboBox.setButtonCell(null);
            comboBox.setButtonCell(cell);

            //Refresh the list view cells
            Callback<ListView<GenericPreset<DATA>>, ListCell<GenericPreset<DATA>>> cellFactory =  comboBox.getCellFactory();
            comboBox.setCellFactory(null);
            comboBox.setCellFactory(cellFactory);
        }
    }


    private ComboBox<GenericPreset<DATA>> createComboBox(ControlPresetSelector<TARGET, DATA> control){
        ComboBox<GenericPreset<DATA>>  comboBox = control.getComboBoxFactory() != null ? control.getComboBoxFactory().get() : new ComboBox<>();
        if(comboBox.getCellFactory() == null){
            comboBox.setCellFactory(param -> new ComboCellPreset<>());
        }
        if(comboBox.getButtonCell() == null){
            comboBox.setButtonCell(new ComboCellPresetSimple<>());
        }
        if(comboBox.getPromptText() == null){
            comboBox.setPromptText("Preset");
        }
        comboBox.itemsProperty().bind(control.filteredPresetsProperty());
        comboBox.valueProperty().bindBidirectional(control.activePresetProperty());
        comboBox.setPrefWidth(300);
        HBox.setHgrow(comboBox, Priority.ALWAYS);
        return comboBox;
    }

    private void destroyComboBox(ControlPresetSelector<TARGET, DATA> control, ComboBox<GenericPreset<DATA>> comboBox){
        comboBox.valueProperty().unbindBidirectional(control.activePresetProperty());
        comboBox.itemsProperty().unbind();
    }

    public boolean shouldRecreateComboBoxOnRefresh(){
        return false;
    }


    @Override
    public void dispose() {
        super.dispose();
        destroyComboBox(getSkinnable(), comboBox);
    }

    public void refresh(){
        refreshComboBox();
    }
}
