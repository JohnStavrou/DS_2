public class Consumer extends Thread
{
    private Table table;
    private int id;
    
    public Consumer(Table table, int id)
    {
        this.table = table;
        this.id = id;
        
        if (id == 1)
            setName("Food Consumer: ");
        else
            setName("Drink Consumer: ");
    }
    
    public int GetId() { return id; }
    
    public void run()
    {
        for (int i = 0; i < 5; i++)
            try
            {
                int ms = (int)(Math.random() * 1000);
                sleep(ms);
                System.out.println(i + " " + getName() + "served " + table.get(this) + " and came back");
            }
            catch (InterruptedException e) { }      
    }
} 