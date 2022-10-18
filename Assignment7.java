//         Name: Osman Servin
//  Description: This program will allow user to draw a circle, rectangle, or line 
//               within a canvas. It will also allow the user to erase/undo the last figure drawn.

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Assignment7 extends Application
{
    public static final int WINSIZE_X = 800, WINSIZE_Y = 800;
    private final String WINTITLE = "Sketchy";
    @Override
    public void start(Stage stage) throws Exception
    {
        SketchPane rootPane = new SketchPane();
        rootPane.setPrefSize(WINSIZE_X, WINSIZE_Y);
        Scene scene = new Scene(rootPane, WINSIZE_X, WINSIZE_Y);
        stage.setTitle(WINTITLE);
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Technically this is not needed for JavaFX applications. Added just in case.
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        launch(args);
    }
}