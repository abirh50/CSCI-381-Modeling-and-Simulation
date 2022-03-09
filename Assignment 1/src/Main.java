import java.util.*;

public class Main {
    public static void main(String [] args){
        myLinkedList list = new myLinkedList();
        Random rand = new Random();
        double num;

        // **** 50,000 values ****

        // 30% of them are from a uniform double precision value from 0-10
        for(int i = 0; i < 15000; i++){
            num = rand.nextDouble() * 10;
            list = myLinkedList.inOrder(list, num);
        }

        // 60% of the time from  a uniform double precision value from 0-100
        for(int i = 0; i < 30000; i++){
            num = rand.nextDouble() * 100;
            list = myLinkedList.inOrder(list, num);
        }

        // 10% of the time from  a uniform double precision value from 0-1000
        for(int i = 0; i < 5000; i++){
            num = rand.nextDouble() * 1000;
            list = myLinkedList.inOrder(list, num);
        }


        /*
        // prints entire linked list
        list.printList(list);
         */

        // Prints first 20 values of linked list
        list.printList20(list);

        list.freq(list, 0, 10);
        list.freq(list, 10, 100);
        list.freq(list, 100, 1000);

    }
}
