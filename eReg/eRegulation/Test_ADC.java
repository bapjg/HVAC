package eRegulation;

public class Test_ADC
{

	public static void main(String[] args)
	{
		Control 		Me 							= new Control();
		System.loadLibrary("Interfaces");
		ADC				adc							= new ADC();
		
		System.out.println("Starting test");
		
		int i;
		Float voltage;
		
		for (i = 0; i < 20; i++)
		{
			voltage = adc.read();
			System.out.println("Iteration : " + i + " voltage : " + voltage);
			Global.waitSeconds(1);
		}
	}
}
