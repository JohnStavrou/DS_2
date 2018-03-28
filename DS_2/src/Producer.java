import java.util.Random;

public class Producer extends Thread
{
    private Table table;
    private int id;
    
    Random random = new Random();
    
    public Producer(Table table, int id)
    {
        this.table = table;
        this.id = id;
        
        if (id == 1)
            setName("Food Producer: ");
        else
            setName("Drink Producer: ");
    }

    public int GetId() { return id; }
    
    public void run()
    {
        int value;
        for (int i = 0; i < 5; i++)
        {
            value = RandValue(id);
            try
            {
                int ms = (int)(Math.random() * 1000);
                sleep(ms);
                System.out.println(getName() + "prepared " + value);
            }
            catch (InterruptedException e) { }
            
            table.put(this, value);
        }
    }
    
    public int RandValue(int id)
    {
        if(id == 1)
            return random.nextInt(2) + 1;
        else
            return random.nextInt(2) + 3;
    }
}