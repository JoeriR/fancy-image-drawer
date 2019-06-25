package logic;

import java.util.ArrayList;

public class Algorithm {
	
	private double graden = 5;
	
	private double currentX;
	private double currentY;
	
	private long pointCounter = 0;
	
	public ArrayList<Point> calculatePoints(double startX, double startY, long limit, double lineLength, double startAngle, double hoeken, double plus) {
		
		this.currentX = startX;
		this.currentY = startY;
		
		this.graden = startAngle;
		
		ArrayList<Point> points = new ArrayList<Point>();
		
		pointCounter = 0;
		
		// add starting point
		points.add(new Point(currentX, currentY));
		
		while (pointCounter < limit) {
			points.add(calcNextPoint(lineLength));
			rechts(graden);
			graden += hoeken;
			hoeken += plus;
			
			++pointCounter;
		}
		
		return points;
	}
	
	public void setCenter(final ArrayList<Point> points, double x, double y) {
		Point currentCenter = calculateCenterOfShape(points);
		
		double deltaX = x - currentCenter.getX();
		double deltaY = y - currentCenter.getY();
		
		System.out.println("offset (" + deltaX + "," + deltaY + ")");
		System.out.println("new center (" + x + "," + y + ")");
		
		for (Point point : points) {
			point.setX(point.getX() + deltaX);
			point.setY(point.getY() + deltaY);
			//System.out.println("(" + point.getX() + "," + point.getY() + ")");
		}
	}
	
	private Point calculateCenterOfShape(ArrayList<Point> points) {
		double smallestX = Double.MAX_VALUE;
		double smallestY = Double.MAX_VALUE;
		double biggestX = Double.MIN_VALUE;
		double biggestY = Double.MIN_VALUE;
		
		for (Point point : points) {
			double x = point.getX();
			double y = point.getY();
			//System.out.println("(" + point.getX() + "," + point.getY() + ")");
			if (x < smallestX)
				smallestX = x;
			if (y < smallestY)
				smallestY = y;
			if (x > biggestX)
				biggestX = x;
			if (y > biggestY)
				biggestY = y;
		}
		
		// The middle of shape = smallestpoint + width / 2
		double centerX = smallestX + (biggestX - smallestX) / 2;
		double centerY = smallestY + (biggestY - smallestY) / 2;
		System.out.println("old center (" + centerX + "," + centerY + ")");
		return new Point(centerX, centerY);
	}
	
	/**
	 * Calculates the next point and draw a line to this next point
	 * This is done by calculating the x and y positions of a circle's circumference around the point with lineLength as the circle's radius.
	 * Calculation source: https://stackoverflow.com/a/839931
	 * 
	 * @param pixelsForwards
	 */
	private Point calcNextPoint(double pointDistance) {
		currentX = currentX + pointDistance * Math.cos(graden);
		currentY = currentY + pointDistance * Math.sin(graden);
		
		Point nextPoint = new Point(currentX, currentY);
		return nextPoint;
	}

	private void rechts(double degreesToTurn) {
		graden += degreesToTurn;

		if (graden >= 360)
			graden %= 360;
	}
	
	
}
