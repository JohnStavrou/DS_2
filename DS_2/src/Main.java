public class Main
{
    public static void main(String[] args)
    {
        Table table = new Table();
        
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