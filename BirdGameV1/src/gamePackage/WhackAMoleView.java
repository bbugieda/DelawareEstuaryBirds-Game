package gamePackage;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.Timer;
import javax.swing.ImageIcon;


@SuppressWarnings("serial")
public class WhackAMoleView extends View {
	//necessary images needed for WhackAMoleView
	private Image background;
	private Image left;
	private Image right;
	private Image up;
	private Image down;
	private Image stick;
	private Image highlightedStick;
	
	//flags for key presses and keyStates
	private int keyState = 0;
	private boolean isWhackView;
	private boolean drawUp = false;
	private boolean drawDown = false;
	private boolean drawLeft = false;
	private boolean drawRight = false;
	
	//Timer and action listener to draw highlighted the game pattern stick pattern
	private BufferedImage highlightStickBuffer;
	private Timer highlightStickTimer;
	private int highlightTimerDelay = 1000;
	private ActionListener highlightStickListener;
	
	//Timer to draw the normal sticks over the highlighted sticks, essentially makes the game pattern blink 
	private BufferedImage normalStickBuffer;
	private Timer normalTimer;
	private int normalStickTimerDelay = 1100;
	private ActionListener normalStickListener;
	
	//scaled image size
	private int scaledImageWidth = Model.scaledImageWidth;
	private int scaledImageHeight = Model.scaledImageHeight;

	
	//Transparent colors which is used to indicate key presses on screen
	Color OPAQUE_GREEN = new Color(.75f, 1f, 0f, .75f);	//75% opaque
	Color OPAQUE_RED = new Color(.75f, 0f, 0f, .75f);	//75% opaque
	
	//MUST FIX
	WhackAMoleModel whackModel = new WhackAMoleModel();
	
	
	public WhackAMoleView() {
		super();
		this.loadImage();
		//setDoubleBuffered(false);
		
	}
	

	/**
	 * initTimers()
	 * If the game state is in the Whack A Mole view, then the two action listeners and the two timers are initialized. The two timers are started. Their delays are offset to 
	 * achieve a blinking effect for the game pattern. Essentially the highlightStickMethod() is called after every delay interval.
	 */
	public void initTimers() {
		if (isWhackView) {
		
			//timer and action listener for the highlighted stick images
			highlightStickBuffer = new BufferedImage(scaledImageWidth, scaledImageHeight, BufferedImage.TYPE_INT_ARGB);
			highlightStickListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (whackModel.gamePattern.size() < 4) {
						highlightStickMethod(highlightStickBuffer.getGraphics());
						repaint();
					}
				}
			};
			highlightStickTimer = new Timer(highlightTimerDelay, highlightStickListener);
			highlightStickTimer.start();

			// timer and action listener to draw the original stick images over the highlighted stick images
			normalStickBuffer = new BufferedImage(scaledImageWidth, scaledImageHeight, BufferedImage.TYPE_INT_ARGB);
			normalStickListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					normalStickMethod(normalStickBuffer.getGraphics());
					repaint();
				}
			};
			normalTimer = new Timer(normalStickTimerDelay, normalStickListener);
			normalTimer.start();
				
		}
	}
	
	/**
	 * loadImage()
	 * Loads all necessary images for the WhackAMoleView. Called in the default constructor.
	 */
	public void loadImage() {
		
		//background image
		ImageIcon bg = new ImageIcon("src/images/WhackAMoleBackground.png");
		background = bg.getImage();
			
		//All bird images (bird looking up, down, left, and right)
		ImageIcon l = new ImageIcon("src/images/WhackAMoleArrowLeft.png");
		left = l.getImage();
		
		ImageIcon r = new ImageIcon("src/images/WhackAMoleArrowRight.png");
		right = r.getImage();
		
		ImageIcon u = new ImageIcon("src/images/WhackAMoleArrowUp.png");
		up = u.getImage();
		
		ImageIcon d = new ImageIcon("src/images/WhackAMoleArrowDown.png");
		down = d.getImage();
		
		//stick image
		ImageIcon s = new ImageIcon("src/images/stick.png");
		stick = s.getImage();
		
		//highlighted stick image
		ImageIcon p = new ImageIcon("src/images/pressedStick.png");
		highlightedStick = p.getImage();

	}
	
	/**
	 * highlightStickMethod()
	 * This method is called after every highlightTimerDelay. It generates a randomNum between 1 - 4, forces no numbers to be repeated, and adds the randomNum to the gamePattern ArrayList. For
	 * each switch case, the correct boolean flags are set. (Boolean flags are necessary so the normalStickMethod knows which highlightedStick image is drawn so it knows which image to draw over.
	 * 
	 * @param g, g is a graphics passed through as an argument for highlightStickMethod(). The highlightStickBuffer will always be passed as the Graphics argument, and for each case the correct
	 * image is drawn onto the Buffered Image. (The buffered image is drawn onto screen by the paintComponent() method; allows the game user to see the gamePattern visually on screen).
	 */
	public void highlightStickMethod(Graphics g) {
		int randomNum = (int)(Math.random()*(4) + 1);
		
		while (whackModel.gamePattern.contains(randomNum)) {
			randomNum = (int)(Math.random()*(4) + 1);
		}
		
		whackModel.gamePattern.add(randomNum);
		System.out.println(whackModel.gamePattern);
		
		switch (randomNum) {
			case 1:
				//up
				System.out.println("highlight up");
				g.drawImage(highlightedStick, (scaledImageWidth/2) - 196, 0, null);
				drawUp = true;
				drawDown = false;
				drawLeft = false;
				drawRight = false;
				break;
			case 2:
				//down
				System.out.println("highlight down");
				g.drawImage(highlightedStick, (scaledImageWidth/2) - 196, scaledImageHeight - 360, null);
				//repaint();
				drawUp = false;
				drawDown = true;
				drawLeft = false;
				drawRight = false;
				break;
			case 3:
				//left
				System.out.println("highlight left");
				g.drawImage(highlightedStick, 0, (scaledImageHeight/2) - 180, null);
				//repaint();
				drawUp = false;
				drawDown = false;
				drawLeft = true;
				drawRight = false;
				break;
			case 4:
				//right
				System.out.println("highlight right");
				g.drawImage(highlightedStick, scaledImageWidth - 393, (scaledImageHeight/2) - 180, null);
				//repaint();
				drawUp = false;
				drawDown = false;
				drawLeft = false;
				drawRight = true;
				break;
		}

	}
	
	/**
	 * normalStickMethod()
	 * This method is called after every normalStickTimerDelay. For each switch case, the stick image is added to the normalStickBuffer with correct coordinates based on the boolean flags set
	 * in higlightStickMethod(). 
	 * 
	 * @param g, g is a graphics passed through as an argument for normalStickMethod(). The normalStickBuffer will always be passed as the Graphics argument, and for each switch case the correct
	 * image is drawn onto the Buffered Image. (The buffered image is drawn onto screen by the paintComponent() method; allows the game user to see the gamePattern blink visually on screen).
	 */
	public void normalStickMethod(Graphics g) {
		//up
		if (drawUp) {
			g.drawImage(stick, (scaledImageWidth/2) - 196, 0, null);
			System.out.println("drew original up");
		}
		//down
		else if (drawDown) {
			g.drawImage(stick, (scaledImageWidth/2) - 196, scaledImageHeight - 360, null);
			System.out.println("drew original down");
		}
		//left
		else if (drawLeft) {
			g.drawImage(stick, 0, (scaledImageHeight/2) - 180, null);
			System.out.println("drew original left");
		}
		//right
		else if (drawRight) {
			g.drawImage(stick, scaledImageWidth - 393, (scaledImageHeight/2) - 180, null);
			System.out.println("drew original right");
		}
		
	}
	
	/**
	 * paintComponent(Graphics g)
	 * Overridden paintComponent method from the View class. Paints all necessary images visually on the screen. This method body is only called if isWhackView is set to true.
	 */
	@Override
	public void paintComponent(Graphics g) {
		if (isWhackView) {
		
			super.paintComponent(g);
			g.setColor(OPAQUE_GREEN);
			
			//initial components
				//background
			g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
				//up
			g.drawImage(stick, (scaledImageWidth/2) - 196, 0, null);
				//down
			g.drawImage(stick, (scaledImageWidth/2) - 196, scaledImageHeight - 360, null);
				//left
			g.drawImage(stick, 0, (scaledImageHeight/2) - 180, null);
				//right
			g.drawImage(stick, scaledImageWidth - 393, (scaledImageHeight/2) - 180, null);
			
			
			
			
			//Draws the bird image looking in the correct direction based on key presses
			switch (keyState) {
				case 1:
					g.drawImage(up, (scaledImageWidth/2) - 175, (scaledImageHeight/2) - 150, null);
					//Overlays a transparent green rectangle over the food image when Up is pressed
					g.fillRect((scaledImageWidth/2) - 175, 0, 350, 224);
					break;
		
				case 2:
					g.drawImage(down, (scaledImageWidth/2) - 175, (scaledImageHeight/2) - 150, null);
					//Overlays a transparent green rectangle over the food image when Down is pressed
					g.fillRect((scaledImageWidth/2) - 175, scaledImageHeight - 224, 350, 224);
					break;
				case 3:
					g.drawImage(right, (scaledImageWidth / 2) - 150, (scaledImageHeight / 2) - 150, null);
					//Overlays a transparent green rectangle over the food image when Right is pressed
					g.fillRect(scaledImageWidth - 350, (scaledImageHeight/2) - 112, 350, 224);
					break;
				case 4:
					g.drawImage(left, (scaledImageWidth / 2) - 150, (scaledImageHeight / 2) - 150, null);
					//Overlays a transparent green rectangle over the food image when Left is pressed
					g.fillRect( 0, (scaledImageHeight/2) - 112, 350, 224);
					break;
				}
			
			//view game pattern
			g.drawImage(highlightStickBuffer, 0, 0, this);
			g.drawImage(normalStickBuffer, 0, 0, this);
		
		}

	}
	
	/**
	 * getKeyState()
	 * @return keyState, the correct keyState based on keyBindings in Controller
	 */
	public int getKeyState() {
		return this.keyState;
	}
	
	/**
	 * setKeyState()
	 * sets the keyState whenever a different keyBinding is called
	 * @param keyState, the keyState is based on keyBindings in Controller
	 */
	public void setKeyState(int keyState) {
		this.keyState = keyState;
	}

	/**
	 * Sets the isWhackView boolean flag
	 * @param isView, necessary to know the gameState of WhackAMoleView because the timers must start immediately when WhackAMoleView is on screen.
	 */
	public void setIsWackView(boolean isWhackView) {
		this.isWhackView = isWhackView;
	}
	
	@Override
	public void update(ArrayList<GameObject> list) {
		
	}
	
	
}

