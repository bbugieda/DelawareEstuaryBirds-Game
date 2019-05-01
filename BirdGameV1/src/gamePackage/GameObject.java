package gamePackage;
import java.awt.Toolkit;
import java.awt.geom.Point2D;

public class GameObject extends Point2D {
	private final int SPEED = 15;
	protected HitBox GameObjectBox;
	private double xPosition;
	private double yPosition;
	final int imgWidth = 100;
	
	public GameObject(double startingX){
		xPosition = startingX;
		this.GameObjectBox = new HitBox((int)this.getX(), (int)this.getY(), (int)this.xPosition, imgWidth);
		
		int maxWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()- this.GameObjectBox.width;
		int minWidth = 0;
		
		int randY = (int)(Math.random() * (maxWidth - minWidth + 1) + minWidth);
		
		this.yPosition = randY;
		
		this.GameObjectBox = new HitBox((int)this.getX(), (int)this.getY(), (int)this.xPosition, imgWidth);
	}
	

	@Override
	public double getX() {
		// TODO Auto-generated method stub
		return this.xPosition;
	}

	@Override
	public double getY() {
		// TODO Auto-generated method stub
		return this.yPosition;
	}
	
	public void scroll() {
		this.setLocation(this.getX() - SPEED, this.getY());
	}
	
	@Override
	public void setLocation(double x, double y) {
		this.xPosition = x;
		this.yPosition = y;
	}
	
	public int getGameObjectSpeed() {
		return this.SPEED;
	}
}