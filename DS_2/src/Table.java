import java.util.ArrayList;
import java.util.Iterator;

public class Table
{
    /* Για πάγκο χρησιμοποίησα μια ArrayList, όπου
       ~> Τα καινούρια προϊόντα τοποθετούνται στο τέλος της.
       ~> Οι καταναλωτές αφαιρούν από την αρχή της. */
    private ArrayList<Integer> table = new ArrayList<>();
    
    /* Χρησιμοποίησα μια ArrayList για να βάζω με μια σειρά μεσα τα threads που περιμένουν,
       έτσι ώστε να μπορώ να "ξυπνάω" το σωστό thread κάθε φορά, δηλαδή αυτό που μπήκε
       πρώτο σε αναμονή. */
    private ArrayList<Thread> waitList = new ArrayList<>();
    
    private boolean isEmpty = true;
    private boolean isFull = false;
    Iterator<Thread> iterator;;
    
    public synchronized int get(Consumer c)
    {
        Wait(c); // Ο καταναλωτής περιμένει αν ο πάγκος είναι άδειος.
        
        int value = table.get(0); // Βλέπει το προϊόν που είναι προς σερβίρισμα.
        if(c.GetId() == 1) // Αν ο καταναλωτής είναι φαγητού...
            while(value != 1 && value != 2) // Αλλα το προϊόν δεν είναι του ρόλου του...
                try
                {
                    System.out.println(c.getName() + "waits until Drink Consumer gets the drink from the table...");
                    Add(c); // Πρόσθεσε το thread σε μια λίστα αναμονής.
                    myNotify(c, 1); // Ενημέρωσε τον άλλον καταναλωτή.
                    wait(); // Και περίμενε.
                    Wait(c); // Ο καταναλωτής περιμένει αν ο πάγκος είναι άδειος.
                    value = table.get(0); // Βλέπει το προϊόν που είναι προς σερβίρισμα.
                }
                catch (InterruptedException e) { }
        else // Αν ο καταναλωτής είναι ροφήματος...
            while(value != 3 && value != 4) // Αλλα το προϊόν δεν είναι του ρόλου του...
                try
                {
                    System.out.println(c.getName() + "waits until Food Consumer gets the food from the table...");
                    Add(c); // Πρόσθεσε το thread σε μια λίστα αναμονής.
                    myNotify(c, 1); // Ενημέρωσε τον άλλον καταναλωτή.
                    wait(); // Και περίμενε.
                    Wait(c); // Ο καταναλωτής περιμένει αν ο πάγκος είναι άδειος.
                    value = table.get(0); // Βλέπει το προϊόν που είναι προς σερβίρισμα.
                }
                catch (InterruptedException e) { }

        // Αφαιρεί από τον πάγκο το προϊόν που είναι προς σερβίρισμα.
        System.out.println(c.getName() + "removes value " + value + " from the table");
        table.remove(0);
        isFull = false;
        
        if (table.isEmpty())
        {
            isEmpty = true;
            System.out.println("The table is empty!");
        }
        
        myNotify(c, 0); // Ενημερώνει όλα τα threads οτι τελείωσε.
        return value;
    } 
 
    public synchronized void put(Producer p, int value)
    {
        Wait(p); // Ο παραγωγός περιμένει αν ο πάγκος είναι γεμάτος.
        
        // Προσθέτει στο τέλος του πάγκου το νέο προϊόν.
        System.out.println(p.getName() + "puts " + value + " on the table");
        isEmpty = false;
        table.add(value);
        
        if (table.size() == 3)
        {
            isFull = true;
            System.out.println("The table is full!");
        }
        
        // Εκτυπώνει το τρέχον περιεχόμενο του πάγκου.
        for(int i : table)
            System.out.print(i + " ");
        System.out.println();

        myNotify(p, 1); // Ενημερώνει τους καταναλωτές οτι το τραπέζει έχει προϊόντα.
    }
    
    /* Η συνάρτηση Wait δέχεται σαν όρισμα ενα thread και ανάλογα με το είδος του
       ενημερώνει την αντίστοιχη μεταβλητή (isFull/isEmpty) και ενημερώνει το αντίστοιχο
       thread να περιμένει. */
    public synchronized void Wait(Thread thread)
    {
        if(thread instanceof Consumer)
            while(isEmpty)
                try
                {
                    System.out.println(thread.getName() + "waits until the table is not empty...");
                    Add(thread);
                    wait();
                }
                catch (InterruptedException e) { }
        else
            while(isFull)
                try
                {
                    System.out.println(thread.getName() + "waits until the table is not full...");
                    Add(thread);
                    wait();
                }
                catch (InterruptedException e) { }
    }
    
    /* Η συνάρτηση Add δέχεται σαν παράμετρο ένα thread, κι αν δεν υπάρχει ήδη στη
       λίστα αναμονής των thread, το προσθέτει και κατόπιν εκτυπώνει με τη σειρά
       το ένα κάτω από το άλλο τα threads που βρίσκονται σε αναμονή. */
    public void Add(Thread thread)
    {
        if(waitList.contains(thread))
            return;
        waitList.add(thread);
        for(Thread t : waitList)
            System.out.println("*" + t.getName() + "*");
    }
    
    /* Η συνάρτηση myNotify είναι μια custom συνάρτηση για να κανω notify κάθε φορά
       το thread που πρέπει. */
    public void myNotify(Thread thread, int toNotify)
    {
        iterator = waitList.iterator();
        while(iterator.hasNext())
        {
            Thread t = iterator.next();
            // Αν το thread είναι Producer ενημερώνει μόνο καταναλωτές.
            if(thread instanceof Producer)
            {
                if(t instanceof Consumer)
                    synchronized(t)
                    {
                        iterator.remove();
                        System.out.println(thread.getName() + "notified " + t.getName());
                        t.notify();
                    }
            }
            /* Αν το thread είναι Consumer ενημερώνει ανάλογα με τη μεταβλητή toNotify
               που δέχεται σαν όρισμα. */
            else
            {
                // Αν η toNotify είναι 0 ενημερώνει όλα τα threads που είναι σε αναμονη.
                if(toNotify == 0)
                {
                    synchronized(t)
                    {
                        iterator.remove();
                        System.out.println(thread.getName() + " notified " + t.getName());
                        t.notify();
                    }
                }
                /* Αν η toNotify είναι 1, έχει καλεστεί από καταναλωτή που περιμένει
                   επειδή δεν μπορεί να παραλάβει προϊόν που δεν του αντιστοιχεί.
                   Γι'αυτό ενημερώνει τον άλλον καταναλωτη να παραλάβει το προϊόν του. */
                else
                {
                    if(t instanceof Consumer && !t.getName().equals(thread.getName()))
                        synchronized(t)
                        {
                            iterator.remove();
                            System.out.println(thread.getName() + " notified " + t.getName());
                            t.notify();
                        }
                }
            }
        }
    }
}