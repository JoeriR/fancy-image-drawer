package ui;
import java.util.ArrayList;
import java.util.Random;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logic.Algorithm;
import logic.Point;

public class GuiStage extends Stage {
	
	// Declare GUI elements
	private Scene scene;
	private GridPane gridPane;
	private VBox sideMenuVBox;
	
	private Canvas canvas;
	private GraphicsContext gc;
	
	private Button startAnimationButton;
	private NumberFieldWrapper animationLinesPerFrameWrapper;
	
	private NumberFieldWrapper linesLimitWrapper;
	private NumberFieldWrapper lineLengthWrapper;
	private NumberFieldWrapper startAngleWrapper;
	private NumberFieldWrapper hoekenWrapper;
	private NumberFieldWrapper plusWrapper;
	
	private CheckBox showAnimationCheckBox;
	private CheckBox randomizerCheckBox;
	
	private Button clearCanvasButton;
	
	private AnimationTimer animation = null;
	
	//private DrawingAnimation animation;
	
	/**
	 * Initialiases all GUI elements and adds them to itself (Window)
	 */
	public GuiStage() {
		
		canvas = new Canvas(800, 800);
		gc = canvas.getGraphicsContext2D();
		animation = new DrawingAnimation();
		
		startAnimationButton = new Button("Draw Lines");
		animationLinesPerFrameWrapper = new NumberFieldWrapper("Lines/Frame: ", "10");
		animationLinesPerFrameWrapper.setDisable(true);
		
		linesLimitWrapper = new NumberFieldWrapper("Max Lines: ", "500");
		lineLengthWrapper = new NumberFieldWrapper("Line Length: ", "50");
		startAngleWrapper = new NumberFieldWrapper("Start Angle: " , "5");
		hoekenWrapper = new NumberFieldWrapper("Hoeken: ", "10");
		plusWrapper = new NumberFieldWrapper("plus: ", "10");
		
		showAnimationCheckBox = new CheckBox("Draw Animation");
		showAnimationCheckBox.setTextFill(Color.WHITE);
		
		randomizerCheckBox = new CheckBox("Randomize");
		randomizerCheckBox.setTextFill(Color.WHITE);
		
		clearCanvasButton = new Button("Clear Drawing");
		
		sideMenuVBox = new VBox(startAnimationButton, showAnimationCheckBox, animationLinesPerFrameWrapper, randomizerCheckBox, linesLimitWrapper, lineLengthWrapper, startAngleWrapper, hoekenWrapper, plusWrapper, clearCanvasButton);
		sideMenuVBox.setSpacing(10);
		sideMenuVBox.setStyle("-fx-background-color: #333333;");
		sideMenuVBox.setPadding(new Insets(10, 10, 10, 10));
		sideMenuVBox.setMinWidth(sideMenuVBox.getWidth());
		
		gridPane = new GridPane();
		gridPane.add(sideMenuVBox, 0, 0);
		gridPane.add(canvas, 1, 0);
		
		scene = new Scene(gridPane, 1100, 800);
		
		this.setScene(scene);
		this.setTitle("Joeri's fancy image drawer");
		
		this.setMinWidth(700);
		this.setMinHeight(500);
		
		createEventHandlers();
	}
	
	
	private void createEventHandlers() {
		
		// this will start the drawing process when the user presses "ENTER" while they have focus on any of sideMenuVBox's child elements
		sideMenuVBox.setOnKeyPressed(event -> {
			String code = event.getCode().toString();
			
			if (code.equals("ENTER"))
				startAnimationButton.fire();
			if (code.equals("UP")) { // bugged doesn't get detected
				System.out.println("up pressed");
				try { new Robot().keyPress(KeyEvent.VK_UP); } catch (AWTException e) { }
			}
				
		});
		
		startAnimationButton.setOnAction(event -> {
			
			gc.clearRect(0, 0, getWidth(), getHeight());
			
			if (randomizerCheckBox.isSelected()) {
				Random random = new Random();
				hoekenWrapper.setFieldText("" + random.nextInt());
				plusWrapper.setFieldText("" + random.nextInt());
			}
			
			long amountOfLines = linesLimitWrapper.getLongValue();
			double lineLength = lineLengthWrapper.getDoubleValue();
			double hoeken = hoekenWrapper.getDoubleValue();
			double startAngle = startAngleWrapper.getDoubleValue();
			double plus = plusWrapper.getDoubleValue();
			boolean doAnimate = showAnimationCheckBox.isSelected();
			
			double startX = canvas.getWidth() / 2;
			double startY = canvas.getHeight() / 2;
			
			if (animation != null) {
				animation.stop();
			}
			
			// TODO: pas de DrawingAnimation class aan
			// Task<Void> task = new Task<Void>() {

			// Object synchronizeObject = new Object();

			// @Override
			// protected synchronized Void call() throws Exception {

			Algorithm algorithm = new Algorithm();
			ArrayList<Point> points = algorithm.calculatePoints(0, 0, amountOfLines, lineLength, startAngle, hoeken,
					plus);
			
			algorithm.setCenter(points, startX, startY);

			// animation.start(canvas, amountOfLines, lineLength, hoeken, plus, doAnimate);

			if (doAnimate) {

				long linesPerFrame = animationLinesPerFrameWrapper.getLongValue();
				
				animation = new AnimationTimer() {
					int index = 0;
					int currentFrameLineCount = 0;
					int linesToBeDrawn = points.size() - 1;
					
					@Override
					public void handle(long now) {

						while (currentFrameLineCount < linesPerFrame) {

							if (index < linesToBeDrawn) {
								Point point1 = points.get(index);
								Point point2 = points.get(index + 1);
								gc.strokeLine(point1.getX(), point1.getY(), point2.getX(), point2.getY());

								++index;
								++currentFrameLineCount;
							} 
							else {
								stop();
								break;
							}
						}
						currentFrameLineCount = 0;
					}
				};
				
				animation.start();

				// return null; // Stops this event handler
			} else {

				int lastIndex = points.size() - 1;

				for (int i = 0; i < points.size(); ++i) {
					if (i != lastIndex) {
						Point point1 = points.get(i);
						Point point2 = points.get(i + 1);
						gc.strokeLine(point1.getX(), point1.getY(), point2.getX(), point2.getY());

					}
				}

				// return null;
			}

		});
		// };

		// new Thread((Runnable) task).start();
		// });

		clearCanvasButton.setOnAction(event -> {
			if (animation != null)
				animation.stop();
			
			animation = null;
			gc.clearRect(0, 0, getWidth(), getHeight());
		});
		
		
		showAnimationCheckBox.setOnAction(event -> {
			boolean isSelected = showAnimationCheckBox.isSelected();
			animationLinesPerFrameWrapper.setDisable(!isSelected);
		});
		
		randomizerCheckBox.setOnAction(event -> {
			if (randomizerCheckBox.isSelected()) {
				hoekenWrapper.setNumberFieldDisable(true);
				plusWrapper.setNumberFieldDisable(true);
			} 
			else {
				hoekenWrapper.setNumberFieldDisable(false);
				plusWrapper.setNumberFieldDisable(false);
			}
		});
		
		// adds width and height listeners to the scene which allow the Canvas to scale with the window size
		scene.widthProperty().addListener( (event, oldValue, newValue) -> {
			canvas.setWidth(newValue.doubleValue() - sideMenuVBox.getWidth());
		});
		
		scene.heightProperty().addListener( (event, oldValue, newValue) -> {
				canvas.setHeight(newValue.doubleValue());
		});
		
	}
	
	public void allignWrappers() {
		System.out.println("ALLLIGN WRAPPERS NOT IMPLEMENTED YET");
	}
	
}
