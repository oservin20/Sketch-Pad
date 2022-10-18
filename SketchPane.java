//         Name: Osman Servin
//  Description: This program will allow user to draw a circle, rectangle, or line 
//               within a canvas. It will also allow the user to erase/undo the last figure drawn.


import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class SketchPane extends BorderPane 
{
	//Task 1: Declare all instance variables listed in UML diagram
	ArrayList<Shape> shapeList = new ArrayList<Shape>() ;
	ArrayList<Shape> tempList = new ArrayList<Shape>() ;
	
	private Button undoButton;
	private Button eraseButton;
	
	private Label fillColorLabel;
	private Label strokeColorLabel;
	private Label strokeWidthLabel;
	
	ComboBox<String> fillColorCombo;
	ComboBox<String> strokeColorCombo;
	ComboBox<String> strokeWidthCombo;
	
	private RadioButton radioButtonLine;
	private RadioButton radioButtonRectangle;
	private RadioButton radioButtonCircle;
	
	Pane sketchCanvas = new Pane();
	HBox topHBox;
	HBox bottomHBox;
	
	private Color[] colors;
	
	private String[] strokeWidth;
	private String[] colorLabels;
	
	private Color currentStrokeColor;
	private Color currentFillColor;
	
	private int currentStrokeWidth;
	
	Line line = null;
	Circle circle = null;
	Rectangle rectangle = null;
	
	private double x1;
	private double y1;
	
	BorderPane borderPane = new BorderPane();
	FlowPane botFlowPane = new FlowPane();
	FlowPane topFlowPane = new FlowPane();
	
	//Task 2: Implement the constructor
	public SketchPane() 
	{
		// Colors, labels, and stroke widths that are available to the user
		colors = new Color[] {Color.BLACK, Color.GREY, Color.YELLOW, 
							  Color.GOLD, Color.ORANGE, Color.DARKRED,
							  Color.PURPLE, Color.HOTPINK, Color.TEAL, 
							  Color.DEEPSKYBLUE, Color.LIME} ;
		colorLabels = new String[] {"black", "grey", "yellow", "gold", "orange", 
									"dark red", "purple", "hot pink", "teal", 
									"deep sky blue", "lime"};
        fillColorLabel = new Label("Fill Color:");
        strokeWidthLabel = new Label("Stroke Width:");
        strokeColorLabel = new Label("Stroke Color:");
        strokeWidth = new String[] {"1", "3", "5", "7", "9", "11", "13"};   
        
        
        // ADDING AVAILABLE COLORS TO THE COMBOBOXES AND ADDING LABELS/COMBOBOXES TO topHBox
        topHBox = new HBox(20);
        topHBox.setMinSize(20, 40);
        topHBox.setAlignment(Pos.CENTER);
        topHBox.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, null, null)));
        
        //topHBox.getChildren().add(fillColorLabel);
        fillColorCombo = new ComboBox<String>();
        fillColorCombo.setValue("black");
		fillColorCombo.getItems().addAll(colorLabels);
    	fillColorCombo.setOnAction(new ColorHandler());
    	//topHBox.getChildren().add(fillColorCombo);
    	
    	//topHBox.getChildren().add(strokeWidthLabel);
    	strokeWidthCombo = new ComboBox<String>();
    	strokeWidthCombo.setValue("1");
		strokeWidthCombo.setOnAction(new WidthHandler());
    	strokeWidthCombo.getItems().addAll(strokeWidth);
    	//topHBox.getChildren().add(strokeWidthCombo);
    	
    	//topHBox.getChildren().add(strokeColorLabel);
    	strokeColorCombo = new ComboBox<String>();
    	strokeColorCombo.setValue("black");
		strokeColorCombo.setOnAction(new ColorHandler());
		strokeColorCombo.getItems().addAll(colorLabels);
    	//topHBox.getChildren().add(strokeColorCombo);
    	
		topHBox.getChildren().addAll(fillColorLabel,fillColorCombo, strokeWidthLabel, strokeWidthCombo, 
										strokeColorLabel, strokeColorCombo);
		
    	// ADDS THE radioButtons INTO A GROUP SO THAT ONLY ONE CAN BE CHOSEN
    	radioButtonLine = new RadioButton("Line");
    	radioButtonRectangle = new RadioButton("Rectangle");
    	radioButtonCircle = new RadioButton("Circle");
    	
    	ToggleGroup group = new ToggleGroup();
		radioButtonLine.setToggleGroup(group);
	    radioButtonRectangle.setToggleGroup(group);
	    radioButtonCircle.setToggleGroup(group);
	    //group.getSelectedToggle();
	    
	    // ADD radioButtons AND labels  TO THE BOTTOM HBOX
	    bottomHBox = new HBox(20);
	    bottomHBox.setMinSize(20, 40);
	    bottomHBox.setAlignment(Pos.CENTER);
	    bottomHBox.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, null, null)));
	    
	    // REGISTERING THE undoButton AND eraseButton TO THE BUTTONHANDLER
	    shapeList = tempList;
		undoButton = new Button("Undo");
	    undoButton.setOnAction(new ButtonHandler());
	    eraseButton = new Button("Erase");
	    eraseButton.setOnAction(new ButtonHandler());
	    
	    bottomHBox.getChildren().addAll(radioButtonLine, radioButtonRectangle, radioButtonCircle, 
	    									undoButton, eraseButton);
	    //bottomHBox.getChildren().add(radioButtonLine);
	    //bottomHBox.getChildren().add(radioButtonRectangle);
	    //bottomHBox.getChildren().add(radioButtonCircle);
	    //bottomHBox.getChildren().add(undoButton);
	    //bottomHBox.getChildren().add(eraseButton);

    	// ADDS THE HBOXES TO THE PANE AND MAKES THE CANVAS WHITE
	    //BorderPane borderPane = new BorderPane();
	    sketchCanvas.setStyle("-fx-background-color: white;");
		borderPane.setCenter(sketchCanvas);
		borderPane.setTop(topHBox);
		borderPane.setBottom(bottomHBox);
		this.getChildren().addAll(topHBox, bottomHBox);
		
		// REGISTERS THE sketchCanvas TO THE HANDLER
		sketchCanvas.setOnMousePressed(new MouseHandler());
		sketchCanvas.setOnMouseDragged(new MouseHandler());
		sketchCanvas.setOnMouseReleased(new MouseHandler());
		
	}
	
	private class MouseHandler implements EventHandler<MouseEvent> 
	{
		@Override
		public void handle(MouseEvent event) 
		{
			// TASK 3: Implement the mouse handler for Circle and Line
			
			// THIS WILL DRAW A CIRCLE
			if (radioButtonCircle.isSelected()) 
			{
				//Mouse is pressed
				if (event.getEventType() == MouseEvent.MOUSE_PRESSED) 
				{
					x1 = event.getX();
					y1 = event.getY();
					circle = new Circle();
					circle.setCenterX(x1);
					circle.setCenterY(y1);
					//circle.setRadius(event.getX());
					//circle.setRadius(event.getY());
					tempList.add(circle);
					circle.setFill(Color.WHITE);
					circle.setStroke(Color.BLACK);
					sketchCanvas.getChildren().add(circle);
				}
				//Mouse is dragged
				else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) 
				{
					if (event.getX() - x1 <0) 
					{
						circle.setRadius(Math.abs(event.getX() - x1));
					}
					if (event.getY() - y1 <0) 
					{
						circle.setRadius(Math.abs(event.getY() - y1));
					}
				}
				//Mouse is released
				else if (event.getEventType() == MouseEvent.MOUSE_RELEASED)
				{
					circle.setFill(currentFillColor);
					circle.setStroke(currentStrokeColor);
					circle.setStrokeWidth(currentStrokeWidth);
				}
			}
			
			
			// THIS WILL DRAW A LINE
			if (radioButtonLine.isSelected()) 
			{
				//Mouse is pressed
				if (event.getEventType() == MouseEvent.MOUSE_PRESSED) 
				{
					x1 = event.getX();
					y1 = event.getY();
					line = new Line();
					line.setStartX(x1);
					line.setStartY(y1);
					line.setEndX(x1);
					line.setEndY(y1);
					tempList.add(line);
					line.setFill(Color.WHITE);
					line.setStroke(Color.BLACK);
					sketchCanvas.getChildren().add(line);
				}
				//Mouse is dragged
				else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) 
				{
					if (event.getX() - x1 <0) 
					{
						line.setEndX(event.getX() - x1);
					}
					if (event.getY() - y1 <0) 
					{
						line.setEndY(event.getY() - y1);
					}
				}
				//Mouse is released
				else if (event.getEventType() == MouseEvent.MOUSE_RELEASED)
				{
					line.setFill(currentFillColor);
					line.setStroke(currentStrokeColor);
					line.setStrokeWidth(currentStrokeWidth);
				}
			}
			
			
			// Rectange Example given!
			if (radioButtonRectangle.isSelected()) 
			{
				//Mouse is pressed
				if (event.getEventType() == MouseEvent.MOUSE_PRESSED) 
				{
					x1 = event.getX();
					y1 = event.getY();
					rectangle = new Rectangle();
					rectangle.setX(x1);
					rectangle.setY(y1);
					tempList.add(rectangle);
					rectangle.setFill(Color.WHITE);
					rectangle.setStroke(Color.BLACK);
					sketchCanvas.getChildren().add(rectangle);
				}
				//Mouse is dragged
				else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) 
				{
					if (event.getX() - x1 <0) 
					{
						rectangle.setX(event.getX());
					}
					if (event.getY() - y1 <0) 
					{
						rectangle.setY(event.getY());
					}
					rectangle.setWidth(Math.abs(event.getX() - x1));
					rectangle.setHeight(Math.abs(event.getY() - y1));
				}

				//Mouse is released
				else if (event.getEventType() == MouseEvent.MOUSE_RELEASED)
				{
					rectangle.setFill(currentFillColor);
					rectangle.setStroke(currentStrokeColor);
					rectangle.setStrokeWidth(currentStrokeWidth);
				}
			}
		}
	}

	
	
	private class ButtonHandler implements EventHandler<ActionEvent> 
	{
		@Override
		public void handle(ActionEvent event) 
		{
			// TASK 4: Implement the button handler
			Object whichButton = event.getSource();
			
			if (whichButton == undoButton)
			{
				if (shapeList.size() > 0)
				{
					sketchCanvas.getChildren().remove(shapeList.size());
					shapeList.remove(shapeList.size() - 1);
				}
				else if (shapeList.size() == 0)
				{
					shapeList = tempList;
					tempList.clear();
					sketchCanvas.getChildren().addAll(shapeList);
				}
			}
			// IF eraseButton IS PRESSED
			else if (whichButton == eraseButton && shapeList.size() > 0)
			{
				tempList.clear();
				sketchCanvas.getChildren().clear();
			}	
		}
	}

	
	private class ColorHandler implements EventHandler<ActionEvent> 
	{
		@Override
		public void handle(ActionEvent event) 
		{
			// TASK 5: Implement the color handler
			int i = fillColorCombo.getSelectionModel().getSelectedIndex();
			currentFillColor = colors[i];
			
			int j = strokeColorCombo.getSelectionModel().getSelectedIndex();
			currentStrokeColor = colors[j];
			
		}
	}

	
	private class WidthHandler implements EventHandler<ActionEvent> 
	{
		@Override
		public void handle(ActionEvent event)
		{
			// TASK 6: Implement the stroke width handler
			int stroke = Integer.parseInt(strokeWidthCombo.getValue());
			currentStrokeWidth = stroke;
		}
	}

	
	// Get the Euclidean distance between (x1,y1) and (x2,y2)
    private double getDistance(double x1, double y1, double x2, double y2)  
    {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}
