package challenge2;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class ColourReadingThread extends Thread {
	public static final String COLOUR_WHITE = "W";
	public static final String COLOUR_GREY = "G";
	public static final String COLOUR_BLACK = "B";
	public static final String COLOUR_UNKNOWN = "-";

	static final float WHITE_VALUE = 0.42f;
	static final float GRAY_VALUE = 0.7f;
	static final float BLACK_VALUE = 0.04f;

//	static final float GRAY_WHITE_THRESHOLD = (GRAY_VALUE + WHITE_VALUE) * 0.3f;
//	static final float BLACK_GRAY_THRESHOLD = (GRAY_VALUE + BLACK_VALUE) * 0.5f;

	static float colourValue = 0;

	static final int NUM_REMEMBERED_VALUES = 200;
	//The smaller the placement of the value it is, the earlier it was added.
	static float[] lastColourValues = new float[NUM_REMEMBERED_VALUES];

	// Initializing color sensor
	static Port s1 = TheSecondProgram.brick.getPort("S1");
	static EV3ColorSensor colorSensor = new EV3ColorSensor(s1);

	public ColourReadingThread() {
	}

	// RUN
	// METHOD////////////////////////////////////////////////////////////////////////
	@Override
	public void run() {
		while (TheSecondProgram.done == false) {
			colourValue = getColorValue();
			for (int i = NUM_REMEMBERED_VALUES - 1; i > 0; i--) {
				lastColourValues[i] = lastColourValues[i - 1];
			}
			lastColourValues[0] = colourValue;
		}
	}
	
	public static float getSmallestRecentValue(){
		float minimum = lastColourValues[0];
		for (int i = 1; i < NUM_REMEMBERED_VALUES; i++) {
			if(minimum > lastColourValues[i]){
				minimum = lastColourValues[i];
			}
		}
		return minimum;
	}

	// Old Code don't change
	// Johnny the grammar police will hate the lack of a comma

	/*
	 * This code tells the light sensor to do its magic-k. It tells it to read
	 * multiple values very quickly, and return the average value.
	 */
	static SampleProvider redMode = colorSensor.getRedMode();
	static int sampleSize = redMode.sampleSize();
	static float[] sample = new float[sampleSize];

	public static float getColorValue() {
		redMode.fetchSample(sample, 0);
		float totalColourValue = 0;
		for (int i = 0; i < sampleSize; i++) {
			totalColourValue += sample[i];
		}
		float averageColourValue = totalColourValue / sampleSize;
		return averageColourValue;
	}

	/*
	 * Remember to change variables at contest. Check colours of map! Add extra
	 * colours variables as needed. This must always be done -- ALWAYS BE DONE
	 * -- immediately after arriving.
	 */
	/*
	 * The following code turns original colour values into the respective
	 * colour strings (Black, White, Gray, etc.)
	 */
	public static String getColourString(float colourValue) {
		if (colourValue < 0) {
			return COLOUR_UNKNOWN;
		} else if (colourValue > 0.20){//GRAY_WHITE_THRESHOLD) {
			return COLOUR_WHITE;
		} else if (colourValue > 0.03){//BLACK_GRAY_THRESHOLD) {
			return COLOUR_GREY;
		} else {
			return COLOUR_BLACK;
		}
	}

	public static String getColourString() {
		return getColourString(colourValue);
	}
}