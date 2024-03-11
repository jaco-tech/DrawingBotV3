package drawingbot.render.renderer;

import drawingbot.render.modes.DisplayModeBase;
import drawingbot.render.viewport.Viewport;
import javafx.beans.property.*;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;

/**
 * Defines a renderer which may utilise either JavaFX or OpenGL to render elements e.g. image, drawing etc.
 * Renderer instances will be generated by the {@link Viewport}
 */
public abstract class RendererBase {

    public RendererBase() {}

    /**
     * Called by the {@link Viewport} when the renderer is first created, it should be used to allocate resources and create scene elements
     */
    public abstract void initRenderer();

    /**
     * Activates the renderer, called by the {@link Viewport} when a {@link DisplayModeBase} which requires this renderer is activated
     * It should add any required scene elements to the {@link Viewport}, such as setting the {@link Viewport#rendererNodeProperty()} which will be placed in the zoomable viewport
     */
    public abstract void activateRenderer();

    /**
     * Deactivates the renderer, any elements added during {@link #activateRenderer()} must be removed
     * The renderer shouldn't be disposed and should instead remain ready to be reactivated
     */
    public abstract void deactivateRenderer();

    /**
     * Called by the {@link Viewport} for every rendered frame or "tick"
     * It should be used for updating the render if required e.g. render the image/drawing, or clear the canvas if theres nothing to render
     */
    public abstract void doRender();

    /**
     * @return true if this renderer is a pure JavaFX implementation
     */
    public abstract boolean isJavaFXRenderer();

    /**
     * @return true if this renderer is pure OpenGL implementation
     */
    public abstract boolean isOpenGLRenderer();

    /**
     * Render -> Canvas Scaling
     * Converts from the expected size of the canvas to the actual size of the drawn canvas
     * It's up to the renderer itself to bind this property as required
     *
     * For example if an image with resolution 640 x 480 is to be drawn and this is beneath the minTextureSize
     * The actual canvas will use the minTextureSize and this scale value will reflect the amount to rescale the image by to draw within the canvas element
     */
    public final DoubleProperty renderScale = new SimpleDoubleProperty(1);

    public double getRenderScale() {
        return renderScale.get();
    }

    public ReadOnlyDoubleProperty renderScaleProperty() {
        return renderScale;
    }

    private void setRenderScale(double renderScale) {
        this.renderScale.set(renderScale);
    }

    public abstract double calculateRenderScale();

    /**
     * To prevent performance/quality issues the canvas' size is limited between the min and max texture sizes
     * @param viewport the viewport the canvas will be disabled within
     * @param minTextureSize the minimum texture size of the canvas: default 2048
     * @param maxTextureSize the maximum texture size of the canvas: default 4096
     * @return the render scale to use on the canvas
     */
    public static double calculateRenderScale(Viewport viewport, int minTextureSize, int maxTextureSize){
        if(viewport == null){
            return 1;
        }
        double width = viewport.getCanvasScaledWidth();
        double height = viewport.getCanvasScaledHeight();

        if(width > maxTextureSize || height > maxTextureSize){
            return maxTextureSize / Math.max(width, height);
        }
        if(width < minTextureSize && height < maxTextureSize){
            return minTextureSize / Math.max(width, height);
        }
        return 1D;
    }

    public void updateCanvasToSceneTransform(Affine dst){
        if(getViewport() == null){
            return;
        }
    }

    ////////////////////////////////////////////////////////

    private Viewport viewport = null;

    public Viewport getViewport() {
        return viewport;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

    ////////////////////////////////////////////////////////

    /**
     * The current active display mode, will match the display mode in {@link Viewport#getDisplayMode()}
     */
    public ObjectProperty<DisplayModeBase> displayMode = new SimpleObjectProperty<>();

    public DisplayModeBase getDisplayMode() {
        return displayMode.get();
    }

    public ObjectProperty<DisplayModeBase> displayModeProperty() {
        return displayMode;
    }

    public void setDisplayMode(DisplayModeBase displayMode) {
        this.displayMode.set(displayMode);
    }

    ////////////////////////////////////////////////////////

    /**
     * JFX Transformation from scene to renderer, it can take a point mapped to the scene and convert it to a local position in the drawing
     */
    private final ObjectProperty<Transform> sceneToRendererTransform = new SimpleObjectProperty<>();

    public Transform getSceneToRendererTransform() {
        return sceneToRendererTransform.get();
    }

    public ObjectProperty<Transform> sceneToRendererTransformProperty() {
        return sceneToRendererTransform;
    }

    private void setSceneToRendererTransform(Transform sceneToRendererTransform) {
        this.sceneToRendererTransform.set(sceneToRendererTransform);
    }

    ////////////////////////////////////////////////////////

    /**
     * JFX Transfomration from renderer to scene, it can take a point relative to the drawing and convert it to scene coordinates
     */
    private final ObjectProperty<Transform> rendererToSceneTransform = new SimpleObjectProperty<>();

    public Transform getRendererToSceneTransform() {
        return rendererToSceneTransform.get();
    }

    public ObjectProperty<Transform> rendererToSceneTransformProperty() {
        return rendererToSceneTransform;
    }

    public void setRendererToSceneTransform(Transform rendererToSceneTransform) {
        this.rendererToSceneTransform.set(rendererToSceneTransform);
    }

    ////////////////////////////////////////////////////////

    private final ObjectProperty<Transform> sceneToCanvasTransform = new SimpleObjectProperty<>();



}