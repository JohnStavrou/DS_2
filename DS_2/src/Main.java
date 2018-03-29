// Σταύρου Ιωάννης - icsd14190

public class Main
{
    public static void main(String[] args)
    {
        Table table = new Table();
        
        /* Το δεύτερο όρισμα που δέχονται οι constructors δείχνει αν ο παραγωγός
           ή καταναλωτής έχει ρόλο 1-Φαγητό ή 2-Ρόφημα. */
        
        Producer FoodP = new Producer(table, 1);
        Producer DrinkP = new Producer(table, 2);
        Consumer FoodC = new Consumer(table, 1);
        Consumer DrinkC = new Consumer(table, 2);
        
        FoodP.start();
        DrinkP.start();
        FoodC.start();
        DrinkC.start();
    }
}