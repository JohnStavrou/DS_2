import java.util.ArrayList;

public class Table
{
    private ArrayList<Integer> table = new ArrayList<>();
    private ArrayList<Thread> waitList = new ArrayList<>();
    private boolean isEmpty = true;
    private boolean isFull = false;
    
    public synchronized int get(Consumer c)
    {
        while(isEmpty)
            try
            {
                System.out.println(c.getName() + "waits until the table is not empty...");
                Add(c);
                wait();        
            }
            catch (InterruptedException e) { }
        
        int value = table.get(0);
        if(c.GetId() == 1)
            while(value != 1 && value != 2)
                try
                {
                    System.out.println(c.getName() + "waits until Drink Consumer gets the drink from the table... - Notify Drink Consumer");
                    Add(c);
                    myNotify(c);
                    wait();
                    value = table.get(0);
                }
                catch (InterruptedException e) { }
        else
            while(value != 3 && value != 4)
                try
                {
                    System.out.println(c.getName() + "waits until Food Consumer gets the food from the table... - Notify Food Consumer");
                    Add(c);
                    myNotify(c);
                    wait();   
                    value = table.get(0);
                }
                catch (InterruptedException e) { }

        System.out.println(c.getName() + "removes value " + value + " from the table - Notify Everyone");
        table.remove(0);
        isFull = false;
        
        if (table.isEmpty())
        {
            isEmpty = true;
            System.out.println("The table is empty!");
        }
        
        myNotify();
        return value;
    } 
 
    public synchronized void put(Producer p, int value)
    {
        while(isFull)
            try
            {
                System.out.println(p.getName() + "waits until the table is not full...");
                Add(p);
                wait();
            }
            catch (InterruptedException e) { }
        
        System.out.println(p.getName() + "puts " + value + " on the table - Notify Consumers");
        isEmpty = false;
        table.add(value);
        
        if (table.size() == 3)
        {
            isFull = true;
            System.out.println("The table is full!");
        }
        
        for(int i : table)
            System.out.print(i + " ");
        System.out.println();

        myNotify(1);
    }
    
    public void Add(Thread t)
    {
        waitList.add(t);
        for(Thread t1 : waitList)
            System.out.println("*" + t1.getName() + "*");
    }
    
    public void myNotify()
    {
        for(Thread t : waitList)
            if(t.getState() == Thread.State.WAITING)
                synchronized(t)
                {
                    waitList.remove(t);
                    t.notify();                        
                    System.out.println(t.getName() + "notified!");
                }
    }
    
    public void myNotify(int toNotify)
    {
        if(toNotify == 1)
            for(Thread t : waitList)
                if(t instanceof Consumer && t.getState() == Thread.State.WAITING)
                    synchronized(t)
                    {
                        waitList.remove(t);
                        t.notify();
                        System.out.println(t.getName() + "notified!");
                    }
    }
    
    public void myNotify(Thread thread)
    {
        for(Thread t : waitList)
            if(!t.getName().equals(thread.getName()) && thread.getState() == Thread.State.WAITING)
                synchronized(t)
                {
                    waitList.remove(t);
                    t.notify();
                    System.out.println(t.getName() + "notified!");
                }
    }
}