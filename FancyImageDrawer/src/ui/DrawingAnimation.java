package ui;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class DrawingAnimation extends AnimationTimer {

	private double lineLength = 25;
	private double graden = 5;
	private double hoeken = 1;
	private double plus = 2;
	private long limit = 9999;
	private long linesDrawn = 0;

	private double currentX;
	private double currentY;

	private boolean doAnimate;

	private Canvas canvas;
	private GraphicsContext gc;

	public void start(Canvas canvas, long limit, double lineLength, double hoeken, double plus, boolean doAnimate) {

		this.canvas = canvas;
		this.gc = this.canvas.getGraphicsContext2D();
		
		this.limit = limit;
		this.lineLength = lineLength;
		this.hoeken = hoeken;
		this.plus = plus;
		
		this.doAnimate = doAnimate;
		
		// set the start position to the center of the canvas
		currentX = canvas.getWidth() / 2;
		currentY = canvas.getHeight() / 2;

		super.start();
	}

	@Override
	public void handle(long now) {
		
		while (true) {
			if (linesDrawn < limit) {
				vooruit(lineLength);
				rechts(graden);
				graden += hoeken;
				hoeken += plus;
				
				++linesDrawn;
			} else {
				this.stop();
				break;
			}

			if (doAnimate == true)
				break;
		}
	}
	
	public void stop() {
		graden = 5;
		hoeken = 1;
		plus = 2;
		linesDrawn = 0;
		
		super.stop();
	}
	
	/**
	 * Calculates the next point and draw a line to this next point
	 * This is done by calculating the x and y positions of a circle's circumference around the point with lineLength as the circle's radius.
	 * Calculation source: https://stackoverflow.com/a/839931
	 * 
	 * @param pixelsForwards
	 */
	private void vooruit(double pixelsForwards) {
		double nextX = currentX + lineLength * Math.cos(graden);
		double nextY = currentY + lineLength * Math.sin(graden);

		gc.strokeLine(currentX, currentY, nextX, nextY);

		currentX = nextX;
		currentY = nextY;
	}

	private void rechts(double degreesToTurn) {
		graden += degreesToTurn;

		if (graden >= 360)
			graden %= 360;
	}
}
