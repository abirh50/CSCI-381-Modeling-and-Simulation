import java.util.*;

public class myLinkedList {
    Node head;
    Node tail;
    static int totalNode = 0;

    static class Node{
        double data;
        Node nextNodeRef;

        Node(double d){
            data = d;
            nextNodeRef = null;
        }
    }

    //insert data in sorted order
    public static myLinkedList inOrder(myLinkedList list, double data){
        //creating node, inserting data and next node pointer
        Node newNode = new Node(data);
        newNode.nextNodeRef = null;

        if (list.head == null){
            list.head = newNode;
            list.tail = list.head;
            totalNode++;
        }
        else if (data <= list.head.data){
            newNode.nextNodeRef = list.head;
            list.head = newNode;
            totalNode++;
        }
        else if (data >= list.tail.data){
            list.tail.nextNodeRef = newNode;
            list.tail = newNode;
            totalNode++;
        }
        else{
            Node curNode = list.head;
            while(data >= curNode.nextNodeRef.data){
                curNode = curNode.nextNodeRef;
            }
            newNode.nextNodeRef = curNode.nextNodeRef;
            curNode.nextNodeRef = newNode;
            totalNode++;
        }

        return list;
    }

    // Print node data of entire linked list
    public void printList(myLinkedList list) {
        Node curNode = list.head;
         while (curNode != null){
             System.out.println(curNode.data);
             curNode = curNode.nextNodeRef;
         }
    }

    // Prints first 20 values of linked list
    public void printList20(myLinkedList list) {
        Node curNode = list.head;
        int count = 1;
        while (curNode != null && count <= 20){
            System.out.println("#" + count + " : " + curNode.data);
            curNode = curNode.nextNodeRef;
            count++;
        }
    }

    public void freq(myLinkedList list, int min , int max){
        Node curNode = list.head;
        int count = 0;
        double freq;

        if (min == 0){
            while (curNode != null && curNode.data >= min && curNode.data <= max){
                curNode = curNode.nextNodeRef;
                count++;
            }

            freq = (1.0*count/totalNode)*100;

            System.out.println("Frequency of values between " + min + " - " + max + ": " + count + " (" + freq + "%)");
        }
        else {
            while (curNode != null && curNode.data <= min) {
                curNode = curNode.nextNodeRef;
            }

            while (curNode != null && curNode.data > min && curNode.data <= max) {
                curNode = curNode.nextNodeRef;
                count++;
            }

            freq = (1.0 * count / totalNode) * 100;

            System.out.println("Frequency of values between " + min + "+ - " + max + ": " + count + " (" + freq + "%)");
        }
    }
}
