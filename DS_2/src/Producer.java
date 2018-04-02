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
        
        // Θέτω ονόματα στα threads ανάλογα με το ρόλο τους.
        if (id == 1)
            setName("Μάγειρας: ");
        else
            setName("Μπουφετζής: ");
    }

    public int GetId() { return id; }
    
    public void run()
    {
        int food;
        for (int i = 0; i < 20; i++)
        {
            food = RandValue(id);
            try                         
            {
                // Χρόνος που ξοδεύει ο παραγωγός για να ετοιμάσει κάτι κάθε φορά.
                sleep((int)(Math.random() * 1000));
                System.out.println(getName() + "ετοίμασε " + food + ".");
            }
            catch (InterruptedException e) { }
            
            table.put(this, food);
        }
    }
    
   /* Ανάλογα με το ρόλο του παραγωγού, παράγω τυχαία ένα από τα 2 προϊόντα του.
      ~ Αν είναι μάγειρας: 1-Μακαρονάδα ή 2-Τοστ.
      ~ Αν είναι μπουφετζής: 3-Καφές ή 4-Χυμός. */
    public int RandValue(int id)
    {
        if(id == 1)
            return random.nextInt(2) + 1;
        else
            return random.nextInt(2) + 3;
    }
}