/* ******************************************************************************
 *	Final Project: OutPatientSimulation                                         *
 *	Group #4: Abir Haque, Ahmed Hossain, Shunuan Hu, Weiwei Ye, Yangfan Zhou    *
 *	Note:                                                                       *
 ******************************************************************************* */

import java.util.Random;

public class OutPatientSimulation {
    public static class Event{
        public double time;
        public long num;
        public String type;
        public String name;
        public Event previous;
        public Event next;

        //constructor
        public Event(double time, String type, String name, long num){
            this.time = time;
            this.num = num;
            this.type = type;
            this.name = name;
            this.previous = null;
            this.next = null;
        }

        //toString
        public String toString() {
            return "Patient#" + this.num + " type " + this.name + ": " + this.type + " " + this.time + "\n";
        }
    }
    // LinkedList for OutPatient
    public static class OutPatientLinkedList{
        public Event head;
        public int size;

        //constructor
        public OutPatientLinkedList() {
            this.size = 0;
            this.head = null;
        }

        //adding with sort
        public void insert(Event node) {
            if (this.head == null) {
                this.head = node;
            } else {
                Event event = this.head;
                if (node.time >= event.time) {
                    while (event.next != null) {
                        if (event.next.time <= node.time) {
                            event = event.next;
                        } else {
                            break;
                        }
                    }
                    if (event.next != null) {
                        Event temp = event.next;
                        temp.previous = node;
                        node.next = temp;
                    }
                    event.next = node;
                    node.previous = event;
                } else {
                    while (event.previous != null) {
                        if (event.previous.time > node.time) {
                            event = event.previous;
                        } else {
                            break;
                        }
                    }
                    if(event.previous == null) {
                        event.previous = node;
                        node.next = event;
                        this.head = node;
                    } else {
                        Event temp = event.previous;
                        temp.next = node;
                        node.previous = temp;
                        event.previous = node;
                        node.next = event;
                    }
                }
            }
            this.size++;
        }
        //toString
        public String toString() {
            StringBuilder s = new StringBuilder();
            Event temp = this.head;
            while (temp != null) {
                s.append(temp.toString());
                temp = temp.next;
            }
            return s.toString();
        }

        //if empty
        public boolean isEmpty() {
            return this.size==0;
        }

        //get size
        public int getSize() {
            return this.size;
        }

        //remove head
        public Event remove() {
            Event temp = this.head;
            if (this.head.next == null) {
                this.head = null;
            } else {
                this.head.next.previous = null;
                this.head = this.head.next;
                temp.next = null;
            }
            this.size--;
            return temp;
        }

        public double nextTime() {
            return this.head.time;
        }
    }

    //compute distribution-exponential
    public static double exponential(double lambda) {
        Random rand = new Random();
        return Math.log(1 - rand.nextDouble()) / (-lambda);
    }

    public static void main(String[] args) {
        double lambdaA = 6.0; // arrival rate for A
        double lambdaB = 8.0; // arrival rate for B
        double lambdaC = 10.0; // arrival rate for C

        double muA = 8.0; // service rate for A
        double muB = 10.0; // service rate for B
        double muC = 12.0; // service rate for C
        double muR = 30.0; // service rate for Reception

        //fel list and reception list and all del lists
        OutPatientLinkedList fel = new OutPatientLinkedList();
        OutPatientLinkedList delReception = new OutPatientLinkedList();
        OutPatientLinkedList delA = new OutPatientLinkedList();
        OutPatientLinkedList delB = new OutPatientLinkedList();
        OutPatientLinkedList delC = new OutPatientLinkedList();

        //data collect for avg length and avg idle period
        int[] totalLength = {0, 0, 0, 0};
        int[] totalPatient = {0, 0, 0, 0};
        double[] idleTimeSum = {0.0, 0.0, 0.0, 0.0};
        int[] totalidelPatient = {0, 0, 0, 0};

        int count = 0;
        //pretime is for update data of idle period
        double pretimeA = 0.0;
        double pretimeB = 0.0;
        double pretimeC = 0.0;
        double pretimeR = 0.0;

        double clockTime = 0.0;
        long patientCount = 0;
        double nextArrival;

        //busy flags
        boolean isBusyA = false;
        boolean isBusyB = false;
        boolean isBusyC = false;
        boolean isBusyR = false;

        while (patientCount < 1000000) {  // simulate until the patient reaches 1000000
            // if future event list and all delayed lists are empty
            if (fel.isEmpty() && delReception.isEmpty() && delA.isEmpty() && delB.isEmpty() && delC.isEmpty()) {
                //initial start
                pretimeA = clockTime;
                pretimeB = clockTime;
                pretimeC = clockTime;
                pretimeR = clockTime;

                //preset three type patients
                nextArrival = clockTime + exponential(lambdaA);
                patientCount++;
                fel.insert(new Event(nextArrival, "will arrive at", "A", patientCount));

                nextArrival = clockTime + exponential(lambdaB);
                patientCount++;
                fel.insert(new Event(nextArrival, "will arrive at", "B", patientCount));

                nextArrival = clockTime + exponential(lambdaC);
                patientCount++;
                fel.insert(new Event(nextArrival, "will arrive at", "C", patientCount));
            } else if (!fel.isEmpty()) {  // if future event list is not empty
                //get first event from fel, update the clock time
                Event servingPatient = fel.remove();
                clockTime = servingPatient.time;

                //event type: arrival
                if (servingPatient.type.equals("will arrive at")) { // if event is arrival
                    //check the reception status
                    if(isBusyR) {
                        //add event to the delR
                        servingPatient.type = "wait for registering at";
                        delReception.insert(servingPatient);
                        totalLength[0] += delReception.getSize();
                        totalPatient[0]++;
                    } else {
                        //upate the idle period
                        if(clockTime - pretimeR > 0.0) {
                            idleTimeSum[0] += clockTime - pretimeR;
                            totalidelPatient[0]++;
                        }
                        //add finish registering event to fel
                        totalPatient[0] ++;
                        servingPatient.time = clockTime + exponential(muR);
                        servingPatient.type = "will finish registering at";
                        fel.insert(servingPatient);
                        isBusyR = true;
                    }
                    // add next event to the fel
                    switch(servingPatient.name) {
                        case "A": 	nextArrival = clockTime + exponential(lambdaA);
                            patientCount++;
                            fel.insert(new Event(nextArrival, "will arrive at", "A", patientCount));
                            break;
                        case "B": 	nextArrival = clockTime + exponential(lambdaB);
                            patientCount++;
                            fel.insert(new Event(nextArrival, "will arrive at", "B", patientCount));
                            break;
                        case "C": 	nextArrival = clockTime + exponential(lambdaC);
                            patientCount++;
                            fel.insert(new Event(nextArrival, "will arrive at", "C", patientCount));
                            break;
                        default:	System.out.println("Wrong type patient!");
                            break;
                    }
                } else if (servingPatient.type.equals("will finish registering at")) { // if event is f
                    pretimeR = clockTime;
                    if(delReception.isEmpty()) {
                        isBusyR = false;
                    } else {
                        Event nextone = delReception.remove();
                        nextone.time = clockTime + exponential(muR);
                        nextone.type = "will finish registering at";
                        fel.insert(nextone);
                    }
                    //check for type of the patient
                    if (servingPatient.name.equals("A")) {   // if event goes for type A
                        if (isBusyA) {     // if server A is busy
                            servingPatient.type = "start waiting at";
                            delA.insert(servingPatient);
                            totalLength[1] += delA.getSize();
                            totalPatient[1]++;
                        } else {    // if server A is free
                            if(clockTime - pretimeA > 0.0) {
                                idleTimeSum[1] += clockTime - pretimeA;
                                totalidelPatient[1] ++;
                            }
                            totalPatient[1] ++;
                            servingPatient.time = clockTime + exponential(muA);
                            servingPatient.type = "will end service at";
                            fel.insert(servingPatient);
                            isBusyA = true;
                        }
                    } else if (servingPatient.name.equals("B")) {    // if it is type B
                        if (isBusyB) {     // if server B is busy
                            servingPatient.type = "start waiting at";
                            delB.insert(servingPatient);
                            totalLength[2] += delB.getSize();
                            totalPatient[2] ++;
                        } else {    // if server B is free
                            if(clockTime - pretimeB > 0.0) {
                                idleTimeSum[2] += clockTime - pretimeB;
                                totalidelPatient[2] ++;
                            }
                            totalPatient[2] ++;
                            servingPatient.time = clockTime + exponential(muB);
                            servingPatient.type = "will end service at";
                            fel.insert(servingPatient);
                            isBusyB = true;
                        }
                    } else {    // if it is type C
                        totalLength[3] += delC.getSize()+1;
                        totalPatient[3] ++;
                        if (isBusyC) { // if server C is busy
                            servingPatient.type = "start waiting at";
                            delC.insert(servingPatient);
                            totalLength[3] += delC.getSize();
                            totalPatient[3] ++;
                        } else {    // if server C is free
                            if(clockTime - pretimeC > 0.0) {
                                idleTimeSum[3] += clockTime - pretimeC;
                                totalidelPatient[3] ++;
                            }
                            totalPatient[3] ++;
                            servingPatient.time = clockTime + exponential(muC);
                            servingPatient.type = "will end service at";
                            fel.insert(servingPatient);
                            isBusyC = true;
                        }
                    }

                } else { // if event is end of service
                    if (servingPatient.name.equals("A")) {
                        //update the type A service to next waiting patient
                        pretimeA = clockTime;
                        if (delA.isEmpty()) {
                            isBusyA = false;
                        } else {
                            Event nextPatient = delA.remove();    // move the first one of del to fel as end of service
                            nextPatient.time = clockTime + exponential(muA);
                            nextPatient.type = "will end service at";
                            fel.insert(nextPatient);
                            isBusyA = true;
                        }
                    } else if (servingPatient.name.equals("B")) {
                        //update the type B service to next waiting patient
                        pretimeB = clockTime;
                        if (delB.isEmpty()) {
                            isBusyB = false;
                        } else {
                            Event nextPatient = delB.remove(); // move the first one of del to fel as end of service
                            nextPatient.time = clockTime + exponential(muB);
                            nextPatient.type = "will end service at";
                            fel.insert(nextPatient);
                            isBusyB = true;
                        }
                    } else {
                        //update the type C service to next waiting patient
                        pretimeC = clockTime;
                        if (delC.isEmpty()) {
                            isBusyC = false;
                        } else {
                            Event nextPatient = delC.remove();    // move the first one of del to fel as end of service
                            nextPatient.time = clockTime + exponential(muC);
                            nextPatient.type = "will end service at";
                            fel.insert(nextPatient);
                            isBusyC = true;
                        }
                    }
                }
            }
            if (count < 20) {
                System.out.println("Clock Time: "+ clockTime);
                System.out.println();
                System.out.println("***************** Future_Event_List *****************");
                System.out.println(fel.toString());
                System.out.println("****************** Reception_Desk *******************");
                System.out.println(delReception.toString());
                System.out.println("****************** Delayed_List_A *******************");
                System.out.println(delA.toString());
                System.out.println("****************** Delayed_List_B *******************");
                System.out.println(delB.toString());
                System.out.println("****************** Delayed_List_C *******************");
                System.out.println(delC.toString());
                System.out.println("---------------------------------------------------------");
                count++;
            }
        }
        System.out.println("Average Queue Length:\nReception: " + 1.0 * totalLength[0] / totalPatient[0] + "\n"
                + "delA: " + 1.0 * totalLength[1] / totalPatient[1] + "\n"
                + "delB: " + 1.0 * totalLength[2] / totalPatient[2] + "\n"
                + "delC: " + 1.0 * totalLength[3] / totalPatient[3] + "\n");
        System.out.println("Average Idle Period: \nReception: " + idleTimeSum[0] / totalidelPatient[0] + "\n"
                + "Patient<A>: " + idleTimeSum[1] / totalidelPatient[1] + "\n"
                + "Patient<B>: " + idleTimeSum[2] / totalidelPatient[2] + "\n"
                + "Patient<C>: " + idleTimeSum[3] / totalidelPatient[3] + "\n");
    }
}
