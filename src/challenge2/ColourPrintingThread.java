package challenge2;

import lejos.hardware.lcd.LCD;

public class ColourPrintingThread extends Thread {
	public ColourPrintingThread() {
	}
	
	// RUN
	// METHOD////////////////////////////////////////////////////////////////////////
	@Override
	public void run() {
		while(TheSecondProgram.done == false){
			LCD.drawString(" " + ColourReadingThread.colourValue, 9, 4);
			LCD.drawString(" " + ColourReadingThread.getColourString(), 9, 5);
			LCD.drawString(" " + TheSecondProgram.onBlack, 9, 6);
			TheSecondProgram.delay(200);
		}
	}
}