import java.util.ArrayList;

public class Table
{
    /* Για πάγκο χρησιμοποίησα μια ArrayList, όπου
       ~> Τα καινούρια προϊόντα τοποθετούνται στο τέλος της.
       ~> Οι καταναλωτές αφαιρούν από την αρχή της. */
    private ArrayList<Integer> table = new ArrayList<>();
    
    private boolean isEmpty = true;
    private boolean isFull = false;
    
    public synchronized int get(Consumer c)
    {
        while(isEmpty) // Ο καταναλωτής περιμένει όσο ο πάγκος είναι άδειος.
            try
            {
                System.out.println(c.getName() + "περιμένει να ετοιμαστεί κάποιο προϊόν...");
                wait();
            }
            catch (InterruptedException e) { }
            
        int value = table.get(0); // Βλέπει το προϊόν που είναι προς σερβίρισμα.
        // Ελέγχει αν το προϊόν που είναι προς σερβίρισμα είναι της αρμοδιότητας του.
        while(!c.getType(c.GetId(), value)) 
            try
            {
                System.out.println(c.getName() + "απομακρύνεται από τον πάγκο μέχρι να πάρει ο άλλος σερβιτόρος το προϊόν του από τον πάγκο...");
                wait((int)(Math.random() * 1000));
                
                while(isEmpty) // Αφού επιστρέψει στον πάγκο ξαναελέγχει αν είναι άδειος.
                    try
                    {
                        System.out.println(c.getName() + "περιμένει να ετοιμαστεί κάποιο προϊόν...");
                        wait();
                    }
               catch (InterruptedException e) { }
               value = table.get(0); // Βλέπει το προϊόν που είναι προς σερβίρισμα.
            }
            catch (InterruptedException e) { }

        // Αφαιρεί από τον πάγκο το προϊόν που είναι προς σερβίρισμα.
        System.out.println(c.getName() + "πήρε το " + value + " από τον πάγκο.");
        table.remove(0);
        isFull = false;
        
        if (table.isEmpty())
        {
            isEmpty = true;
            System.out.println("Ο πάγκος είναι άδειος!");
        }
        else
            DisplayTable();
        
        notifyAll();
        
        return value;
    } 
 
    public synchronized void put(Producer p, int value)
    {
        while(isFull) // Ο παραγωγός περιμένει όσο ο πάγκος είναι γεμάτος.
            try
            {
                System.out.println(p.getName() + "περιμένει να ετοιμαστεί κάποιο προϊόν...");
                wait();
            }
            catch (InterruptedException e) { }
        
        // Προσθέτει στο τέλος του πάγκου το νέο προϊόν.
        System.out.println(p.getName() + "προσθέτει το " + value + " στον πάγκο.");
        isEmpty = false;
        table.add(value);
        
        if (table.size() == 10)
        {
            isFull = true;
            System.out.println("Ο πάγκος είναι γεμάτος!");
        }
        
        DisplayTable();
        
        notifyAll();
    }
    
    // Εκτυπώνει το περιχόμενο του πάγκου.
    public void DisplayTable()
    {
        System.out.print("~ Πάγκος: ");
        for(int p : table)
            System.out.print(p + " ");
        System.out.println();
    }
}