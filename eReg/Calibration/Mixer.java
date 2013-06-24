package Calibration;

import eRegulation.*;



public class Mixer
{
	public static void main(String[] args)
	{
		Relay mixerDown = new Relay("Floor_Valve_Down", "2", "Down");
		Relay mixerUp = new Relay("Floor_Valve_Up", "1", "Up");

		Buttons buttons = new Buttons();
		
		System.out.println("B1 is stop and exit");
		System.out.println("B2 is stop");

		System.out.println("Relays as indicated");
		System.out.println("Moving Down press B2 when at min position");
		
		// Moving Down : Initial position
		
		mixerDown.on();
		
		while (!buttons.button0 && !buttons.button1)
		{
			try
			{
				Thread.sleep(1000);
			}
	        catch (InterruptedException e)
	        {
	            e.printStackTrace();
	        }
		}
		if (buttons.button0)
		{
			return;
		}
		
		long timeIncrement = 0;
		long timeAverage = 0;
		long timeStart = System.currentTimeMillis();
		
		System.out.println("Moving Up press B2 when at max position");

		// Increment 1 Moving Up
		
		mixerDown.off();
		mixerUp.on();

		while (!buttons.button0 && !buttons.button1)
		{
			try
			{
				Thread.sleep(1000);
			}
	        catch (InterruptedException e)
	        {
	            e.printStackTrace();
	        }
		}
		if (buttons.button0)
		{
			return;
		}

		timeIncrement = System.currentTimeMillis() - timeStart;
		timeAverage += timeIncrement;
		System.out.println("Iteration 1 : " + timeIncrement);

		// Increment 2 Moving Down

		timeStart = System.currentTimeMillis();
		System.out.println("Moving Up press B2 when at max position");

		mixerDown.on();
		mixerUp.off();

		while (!buttons.button0 && !buttons.button1)
		{
			try
			{
				Thread.sleep(1000);
			}
	        catch (InterruptedException e)
	        {
	            e.printStackTrace();
	        }
		}
		if (buttons.button0)
		{
			return;
		}

		timeIncrement = System.currentTimeMillis() - timeStart;
		timeAverage += timeIncrement;
		System.out.println("Iteration 2 : " + timeIncrement);

		// Increment 3 Moving Up
		
		timeStart = System.currentTimeMillis();
		System.out.println("Moving Up press B2 when at max position");
		
		mixerDown.off();
		mixerUp.on();

		while (!buttons.button0 && !buttons.button1)
		{
			try
			{
				Thread.sleep(1000);
			}
	        catch (InterruptedException e)
	        {
	            e.printStackTrace();
	        }
		}
		if (buttons.button0)
		{
			return;
		}

		timeIncrement = System.currentTimeMillis() - timeStart;
		timeAverage += timeIncrement;
		System.out.println("Iteration 3 : " + timeIncrement);

		// Increment 4 Moving Down
		
		timeStart = System.currentTimeMillis();
		System.out.println("Moving Up press B2 when at max position");

		mixerDown.on();
		mixerUp.off();

		while (!buttons.button0 && !buttons.button1)
		{
			try
			{
				Thread.sleep(1000);
			}
	        catch (InterruptedException e)
	        {
	            e.printStackTrace();
	        }
		}
		if (buttons.button0)
		{
			return;
		}

		timeIncrement = System.currentTimeMillis() - timeStart;
		timeAverage += timeIncrement;
		System.out.println("Iteration 4 : " + timeIncrement);

		System.out.println("Average : " + timeAverage/4);
	}
}
