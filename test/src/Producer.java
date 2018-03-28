public class Producer extends Thread
{
    private CubbyHole cubbyhole;
    private int id;
    
    public Producer(CubbyHole c, int id)
    {
        cubbyhole = c;
        this.id = id;
    }
     
    public void run()
    {
        for (int i = 0; i < 10; i++)
        {
            cubbyhole.put(i);
            System.out.println("Producer #" + this.id + " put: " + i);
            try
            {
                sleep((int)(Math.random() * 1000));
            }
            catch (InterruptedException e)
            { }
        }
    }
}