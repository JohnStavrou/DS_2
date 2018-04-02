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
            setName("Σερβιτόρος Φαγητού: ");
        else
            setName("Σερβιτόρος Ποτού: ");
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
                System.out.println(getName() + "σέρβιρε " + table.get(this) + " και επέστρεψε.");
            }
            catch (InterruptedException e) { }      
    }
    
    /* Ελέγχει αν το προϊόν που δέχεται στο δεύτερο όρισμα είναι αρμοδιότητα του σερβιτόρου
       που δέχεται στο πρώτο όρισμα. */
    public boolean getType(int id, int value)
    {
        if(id == 1)
            return value == 1 || value == 2;
        else
            return value == 3 || value == 4;
    }
} 