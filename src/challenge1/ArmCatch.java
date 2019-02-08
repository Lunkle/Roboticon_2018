package challenge1;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.robotics.SampleProvider;

public class ArmCatch extends Thread {
	private static EV3IRSensor irSensor = new EV3IRSensor(SensorPort.S4);
	final SampleProvider sp = irSensor.getDistanceMode();
	int distanceValue = 0;
	float[] sample = new float[sp.sampleSize()];

	public ArmCatch() {
	}

	@Override
	public void run() {
		init();
		while (TheProgram.done == false) {
			sp.fetchSample(sample, 0);
			distanceValue = (int) sample[0];
			if (distanceValue < 9) {
				TheProgram.armMotor.rotateTo(500);
			} else {
				TheProgram.armMotor.rotateTo(320);
			}
			waitSomeTime(100);
		}
		TheProgram.armMotor.rotateTo(0);
	}

	private void init() {
		TheProgram.armMotor.setSpeed(1000);
	}

	// So code does not look cluttered.
	public static void waitSomeTime(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
		}
	}
}