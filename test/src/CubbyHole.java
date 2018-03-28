public class CubbyHole
{
    private int[] contents = {0,0,0,0,0};
    private boolean bufferEmpty = true;
    private boolean bufferFull = false;
	 
    private final int size = 5;
	private int counter = 0;
	
	public synchronized int get()
	{
		while (bufferEmpty == true)
		{
			try
			{
				wait();
			}
			catch (InterruptedException e) { }
		}
		counter--;
		int value=contents[counter];
		System.out.println("The consumer removes the value : " + value + " by the cubbyhole");
		bufferFull=false;
		if (counter==0)
		{
			bufferEmpty = true;
			System.out.println("The buffer is empty");
		}
		// αφύπνιση του παραγωγού που πιθανόν να έχει εμποδιστεί
                notifyAll();
        return value;
	} 
 
    public synchronized void put(int value)
	{
		while (bufferFull == true)
		{
			try
			{
				wait();
			}
			catch (InterruptedException e) { }
		}
		bufferEmpty=false;
		System.out.println("The producer adds the value " + value + " in the cubbyhole");
		contents[counter] = value;
                counter++;  
        // ελέγχει αν έχει γεμίσει το cubbyhole
		if (counter==size)
		{
			bufferFull = true;
			System.out.println("The cubbyhole is full");
		}
		// αφύπνισε τον καταναλωτή που πιθανόν να περιμένει
        notifyAll();
	}
}