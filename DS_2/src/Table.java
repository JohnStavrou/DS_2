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
                waitList.add(c);
                wait();            }
            catch (InterruptedException e) { }
        
        int value = table.get(0);
        if(c.GetId() == 1)
            while(value != 1 && value != 2)
                try
                {
                    System.out.println(c.getName() + "waits until Drink Consumer gets the drink from the table...");
                    waitList.add(c);
                    myNotify(c);
                    wait();
                    value = table.get(0);
                }
                catch (InterruptedException e) { }
        else
            while(value != 3 && value != 4)
                try
                {
                    System.out.println(c.getName() + "waits until Food Consumer gets the food from the table...");
                    waitList.add(c);
                    myNotify(c);
                    wait();   
                    value = table.get(0);
                }
                catch (InterruptedException e) { }
        
        System.out.println(c.getName() + "removes value " + value + " from the table");
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
                waitList.add(p);
                myNotify();
                wait();
            }
            catch (InterruptedException e) { }
        isEmpty = false;
        
        table.add(value);
        System.out.println(p.getName() + "puts " + value + " on the table");
        
        if (table.size() == 3)
        {
            isFull = true;
            System.out.println("The table is full!");
        }
        
        for(int i : table)
            System.out.print(i + " ");
        System.out.println();

        myNotify();
    }
    
    public void myNotify()
    {
        for(Thread t : waitList)
            if(t.getState() == Thread.State.WAITING)
                synchronized(t)
                {
                    t.notify();                        
                }
    }
    
    public void myNotify(Thread thread)
    {
        if(thread instanceof Consumer)
        {   
            for(Thread t : waitList)
                if(!t.getName().equals(thread.getName()) && thread.getState() == Thread.State.WAITING)
                    synchronized(t)
                    {
                        t.notify();                        
                    }
        }
        else
        {
            for(Thread t : waitList)
                if(!t.getName().equals(thread.getName()) && thread.getState() == Thread.State.WAITING)
                    synchronized(t)
                    {
                        t.notify();                        
                    }
        }
        
            
    }
    
    
    
    /*public void myNotify(Thread thread, int i)
    {
        for(Thread t : waitList)
            if(i == 0)
            {
                if(t instanceof Producer)
                    synchronized(t)
                    {
                        waitList.remove(t);
                        System.out.println(t.getName() + "**");
                        t.notify();
                    }
            }
            else if(i == 1)
            {
                if(t instanceof Consumer && t.getName().equals(thread.getName()))
                    synchronized(t)
                    {
                        waitList.remove(t);
                        System.out.println(t.getName() + "**");
                        t.notify();
                    }
            }
            else
                synchronized(t)
                {
                    waitList.remove(t);
                    System.out.println(t.getName() + "**");
                    t.notify();
                }
    }*/
}