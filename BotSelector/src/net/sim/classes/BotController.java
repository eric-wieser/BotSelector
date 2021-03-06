package net.sim.classes;

import java.util.Random;

import net.sim.interfaces.BotKeyboardListener;
import net.sim.interfaces.BotMouseListener;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

/**
 * Intended as the presenter/controller mechanism for bots. Each new bot is
 * constructed with this as a parameter.
 * 
 * @author mrh2
 * 
 */
public class BotController implements BotMouseListener, BotKeyboardListener {

	private static final int INITIAL_BOT_POPULATION = 20;
	
	private Random mRandom;
	private int xMax, yMax, thetaMax, sizeMax, sizeMin;
	
	// Keyboard variables
	private boolean altHeld;
	
	//Variables for structured game programming
	private GameBoard mGameBoard;
	private BotRegister mBotRegister;

	public BotController() throws LWJGLException {
		mGameBoard = new GameBoard(this, this);
		
		altHeld = false;
		
		mRandom = new Random();
		xMax = Display.getWidth();
		yMax = Display.getHeight();
		thetaMax = 360;
		sizeMax = 20;
		sizeMin = 5;

		mBotRegister = new BotRegister();
		for (int i = 0; i < INITIAL_BOT_POPULATION; i++) {
			new Bot(this,
					mRandom.nextInt(xMax),
					mRandom.nextInt(yMax),
					Math.toRadians(mRandom.nextInt(thetaMax) - (thetaMax/2 -1)),
					mRandom.nextInt(sizeMax - sizeMin) + sizeMin + 1);
		}
	}
	
	public void start(){
		while (!Display.isCloseRequested()) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			mBotRegister.update();
			mGameBoard.update();
		}
	}

	public void register(Bot bot) {
		mBotRegister.registerBot(bot);
	}

	@Override
	public void keyPressed(int key) {
		switch (key) {
		case Keyboard.KEY_UP:
			mBotRegister.setSelectedMovingForward(true);
			break;
		case Keyboard.KEY_RIGHT:
			mBotRegister.setSelectedRotatingClockwise(true);
			break;
		case Keyboard.KEY_LEFT:
			mBotRegister.setSelectedRotatingAntiClockwise(true);
			break;
		case Keyboard.KEY_LMENU:
			altHeld = true;
			System.out.println("Alt pressed");
			break;
//		case Keyboard.KEY_F:
//			System.out.println("F pressed and alt is held down: " + altHeld);
//			if (altHeld)
//				try {
//					if (Display.isFullscreen()) mGameBoard.setSmall();
//					else mGameBoard.setFullScreen();
//					mBotRegister.updateScreenSize();
//				} catch (Exception e) {
//					e.printStackTrace();
//					//Tired of eclipse moaning about exceptions.
//				}
//				
//			break;
//		default:
//			System.out.printf("Key pressed: %s\n", Keyboard.getKeyName(key));
		}
//		System.out.printf("Key pressed: %s\n", Keyboard.getKeyName(key));
	}

	@Override
	public void keyReleased(int key) {
		switch (key) {
		case Keyboard.KEY_UP:
			mBotRegister.setSelectedMovingForward(false);
			break;
		case Keyboard.KEY_RIGHT:
			mBotRegister.setSelectedRotatingClockwise(false);
			break;
		case Keyboard.KEY_LEFT:
			mBotRegister.setSelectedRotatingAntiClockwise(false);
			break;
		case Keyboard.KEY_LMENU:
			System.out.println("Alt released");
			altHeld = false;
			break;
		}
//		System.out.printf("Key released: %s\n", Keyboard.getKeyName(key));
	}

	@Override
	public void keyTyped(int key) {
		// No op
	}

	@Override
	public void leftButtonClicked(int x, int y) {
		mBotRegister.pickupBotNearest(x,y);
//		System.out.printf("Left mouse button clicked at (%d,%d)\n", x,y);
	}

	@Override
	public void leftButtonReleased(int x, int y) {
		mBotRegister.release();
	}

	@Override
	public void rightButtonClicked(int x, int y) {
//		System.out.printf("Right mouse button clicked at (%d,%d)\n", x,y);
	}

	@Override
	public void rightButtonReleased(int x, int y) {
//		System.out.printf("Right mouse button released at (%d,%d)\n", x,y);
		
	}

	@Override
	public void leftDragged(int x, int y) {
		mBotRegister.dragSelected(x,y);
	}

	@Override
	public void rightDragged(int x, int y) {
//		System.out.printf("Right mouse button dragged to (%d,%d)\n", x,y);
	}
	
	public static void main(String[] args) throws LWJGLException {
		BotController botController = new BotController();
		botController.start();
	}

}
