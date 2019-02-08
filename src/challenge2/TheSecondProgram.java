package challenge2;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import lejos.hardware.motor.Motor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

/**
 * Roboticon 2018 Team Name: The Soap Lovers Team Members: Matthew Zhou, Donny
 * Ren, Dan Choi, John Tzoganakis Competition Date: March 18-19 @author(s) 9/10
 * Donny Ren and 1/10 Matthew Zhou
 */

public class TheSecondProgram {

	static boolean done = false;

	public static float TpStart = 90;// starting power
	public static float Tp = TpStart;// don't change
	public static float TpAcc = 2f; // rate of acceleration to TpMax
	public static float TpMax = 250; // max speed

	static Brick brick;
	static Wheel wheel1;
	static Wheel wheel2;
	static Chassis chassis;
	static MovePilot pilot;

	// MAIN/////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) {
		init();
		startColourReadingThread();
		Button.ENTER.waitForPress();
		solveMaze();
	}

	private static void startColourReadingThread() {
		ColourReadingThread cThread = new ColourReadingThread();
		cThread.start();
		ColourPrintingThread cPThread = new ColourPrintingThread();
		cPThread.start();
	}

	static boolean onBlack = true;

	private static void solveMaze() {
		pilot.forward();
		while (!done) {
			String colour = ColourReadingThread.getColourString();
			if (!colour.equals(ColourReadingThread.COLOUR_WHITE)) {
				delay(1000);
				float colourValue = ColourReadingThread.getSmallestRecentValue();
				colour = ColourReadingThread.getColourString(colourValue);
				if (colour.equals(ColourReadingThread.COLOUR_GREY)) {
					if (onBlack) {
						turnRight();
						System.out.println("Right G " + colourValue);
					} else {
						turnLeft();
						System.out.println("Left G " + colourValue);
					}
					onBlack = !onBlack;
					pilot.forward();
				} else if (colour.equals(ColourReadingThread.COLOUR_BLACK)) {
					if (onBlack) {
						turnLeft();
						System.out.println("Left B " + colourValue);
					} else {
						turnRight();
						System.out.println("Right B " + colourValue);
					}
					onBlack = !onBlack;
					pilot.forward();
				}
			}
		}
	}

	public static void turnRight() {
		pilot.rotate(90);
	}

	public static void turnLeft() {
		pilot.rotate(-90);
	}

	static public void delay(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void init() {
		brick = BrickFinder.getDefault();
		wheel1 = WheeledChassis.modelWheel(Motor.B, 5.7).offset(-8.90);
		wheel2 = WheeledChassis.modelWheel(Motor.C, 5.7).offset(8.90);
		chassis = new WheeledChassis(new Wheel[] { wheel1, wheel2 }, 2);
		pilot = new MovePilot(chassis);
		pilot.setLinearSpeed(3);
		pilot.setLinearAcceleration(20);
		pilot.setAngularSpeed(100);
		pilot.setAngularAcceleration(120);
		Button.ESCAPE.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(Key k) {
				done = true;
			}

			@Override
			public void keyReleased(Key k) {
			}
		});
	}
}