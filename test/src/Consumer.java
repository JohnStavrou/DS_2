public class Consumer extends Thread
{
    private CubbyHole cubbyhole;
    private int id;
    
    public Consumer(CubbyHole c, int id)
    {
        cubbyhole = c;
        this.id = id;
    } 
 
    public void run()
    {
        int value = 0;
        for (int i = 0; i < 10; i++)
        {
            value = cubbyhole.get();
            System.out.println("Consumer #" + this.id + " got: " + value);
        }
    }
} 