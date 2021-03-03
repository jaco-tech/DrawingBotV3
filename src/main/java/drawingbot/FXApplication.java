package drawingbot;

import drawingbot.api.API;
import drawingbot.api_impl.DrawingBotV3API;
import drawingbot.drawing.DrawingRegistry;
import drawingbot.files.ConfigFileHandler;
import drawingbot.javafx.FXController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jfree.fx.FXGraphics2D;

import java.io.*;
import java.lang.reflect.Method;
import java.util.logging.Level;

public class FXApplication extends Application {

    public static Stage primaryStage;
    public static Scene primaryScene;

    public Animation animation;
    public float frameRate = 60;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        DrawingBotV3.logger.setLevel(Level.ALL);
        DrawingBotV3.logger.entering("FXApplication", "start");
        FXApplication.primaryStage = primaryStage;

        ///////////////////////////////////////////////////////////////////////////////////////////////////////

        //// PRE-INIT

        DrawingBotV3.logger.info("Init DrawingBotV3");
        DrawingBotV3.INSTANCE = new DrawingBotV3();

        DrawingBotV3.logger.info("Init DrawingRegistry");
        DrawingRegistry.init();

        DrawingBotV3.logger.info("Loading API");
        API.INSTANCE = new DrawingBotV3API();

        DrawingBotV3.logger.info("Loading configuration");
        ConfigFileHandler.init();

        ///////////////////////////////////////////////////////////////////////////////////////////////////////

        //// INIT GUI
        Canvas canvas = new Canvas(500, 500);

        DrawingBotV3.INSTANCE.controller = new FXController();
        DrawingBotV3.INSTANCE.canvas = canvas;
        DrawingBotV3.INSTANCE.graphicsFX = canvas.getGraphicsContext2D();
        DrawingBotV3.INSTANCE.graphicsAWT = new FXGraphics2D(canvas.getGraphicsContext2D());

        FXMLLoader loader = new FXMLLoader(FXApplication.class.getResource("/fxml/userinterface.fxml")); // abs path to fxml file
        loader.setController(DrawingBotV3.INSTANCE.controller);

        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        FXApplication.primaryScene = new Scene(loader.load(), visualBounds.getWidth()/1.2, visualBounds.getHeight()/1.2, false, SceneAntialiasing.BALANCED);
        FXApplication.primaryScene.setOnKeyPressed(DrawingBotV3.INSTANCE::keyPressed);
        FXApplication.primaryScene.setOnKeyReleased(DrawingBotV3.INSTANCE::keyReleased);
        primaryStage.setScene(primaryScene);

        primaryStage.setTitle(DrawingBotV3.appName + ", Version: " + DrawingBotV3.appVersion);
        primaryStage.setResizable(true);
        applyDBIcon(primaryStage);
        primaryStage.show();

        // set up main drawing loop
        KeyFrame keyFrame = new KeyFrame(Duration.millis(1000), event -> DrawingBotV3.INSTANCE.draw());
        animation = new Timeline(keyFrame);
        animation.setCycleCount(Animation.INDEFINITE);
        animation.setRate(-frameRate);// setting rate to negative so that event fires at the start of the key frame and first frame is drawn immediately
        animation.play();

        ///////////////////////////////////////////////////////////////////////////////////////////////////////

        DrawingBotV3.logger.exiting("FXApplication", "start");
    }

    public static void applyDBIcon(Stage primaryStage){
        InputStream stream = FXApplication.class.getResourceAsStream("/images/icon.png");
        if(stream != null){
            primaryStage.getIcons().add(new Image(stream));
        }
    }
}