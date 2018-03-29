public class Consumer extends Thread
{
    private Table table;
    private int id;
    
    public Consumer(Table table, int id)
    {
        this.table = table;
        this.id = id;
        
        // Θέτω ονόματα στα threads ανάλογα με το ρόλο τους.
        if (id == 1)
            setName("Food Consumer: ");
        else
            setName("Drink Consumer: ");
    }
    
    public int GetId() { return id; }
    
    public void run()
    {
        for (int i = 0; i < 20; i++)
            try
            {
                /* Χρόνος που ξοδεύει ο καταναλωτής για να σερβίρει κάτι και να επιστρέψει
                   στον πάγκο. */
                sleep((int)(Math.random() * 1000));
                System.out.println(i + " " + getName() + "served " + table.get(this) + " and came back");
            }
            catch (InterruptedException e) { }      
    }
} 