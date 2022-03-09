import java.util.*;

public class MM1Queue {

    public static void main(String[] args) {
        double lambda = 8.0;  // arrival rate
        double mu = 10.0;     // service rate

        Queue<Double> queue  = new LinkedList<Double>();       // arrival times of customers
        double nextArrival   = randExp(lambda);   // time of next arrival
        double nextDeparture = Double.POSITIVE_INFINITY;  // time of next departure

        double totalServiceTime = 0.0;
        int totalCustomersServiced = 0;

        double totalIdleT = nextArrival; // total idle time

        boolean departed = false;
        Queue<Double> tempQ  = new LinkedList<Double>(); // temp queue

        // M/M/1 queue simulator
        double clockTime = 0.0; // clock time

        int depCustNum = 0; // departing customer #
        int ariCustNum = 0; // arriving customer #

        // p = lambda / mu, avgQueueLength = p/(1-p)
        int count = 0;
        int cusCount = 1;
        int queueLengthSum = 0;


        Double currentCustTime = nextArrival;
        double currCustArivTime = 0;

        int printCount = 0;

        while (totalCustomersServiced < 10000) { // 10,000 customers

            // Arrival
            if (nextArrival <= nextDeparture) {

                if (queue.isEmpty()) {
                    nextDeparture = nextArrival + randExp(mu); // generates a departure time for an arrival when queue is empty
                }

                queue.add(nextArrival); // adds arrival to the end of the queue
                tempQ.add(nextArrival); // temp queue

                currentCustTime = nextArrival;

                clockTime = currentCustTime;

                nextArrival += randExp(lambda); // next arrival time
                cusCount++;

                count++;
                queueLengthSum += queue.size() - 1;

                departed = false;

                ariCustNum++;
            }

            // Departure
            else {
                depCustNum++;
                if(!queue.isEmpty() && !tempQ.isEmpty() && tempQ.peek().equals(queue.peek())) {
                    tempQ.remove(); // temp queue, remove queue
                }
                currentCustTime = queue.remove();
                currCustArivTime = currentCustTime;

                departed = true;

                if(nextArrival > nextDeparture){
                    totalIdleT += nextArrival - nextDeparture;
                }

                double wait = nextDeparture - currentCustTime; // how long customer servicing took

                totalServiceTime += wait; // adding customer service time
                totalCustomersServiced++; // done with customer so moving on to the next customer

                currentCustTime = nextDeparture;

                if (queue.isEmpty()) {
                    nextDeparture = Double.POSITIVE_INFINITY; //when queue is empty after removing it's changed to positive infinity (nextArrival <= positive infinity)
                }
                else {
                    nextDeparture += randExp(mu); // if queue is not empty after removing, departure time is created for next arrival
                }
            }

            if(printCount == 0) { // to only print first 20
                tempQ.remove();

                System.out.println("Clock Time: " + 0.0);
                System.out.println("fel: [Customer #" + ariCustNum + " Arrival Time: " + currentCustTime + "]");
                System.out.println("del: " + tempQ);
                //System.out.println("TEST del: " + queue);
                System.out.println("--------------------------------------------------------------------");
                System.out.println("");

                printCount++;
            }

            if(!departed && printCount < 20 && printCount != 0) { // to only print first 20
                System.out.println("Clock Time: " + clockTime);
                System.out.println("fel: [Customer #" + ariCustNum + " Arrival Time: " + currentCustTime + ",   Customer #" + (depCustNum + 1) + " Departure Time: " + nextDeparture + ",   Next Arrival: " + nextArrival + "(Customer #" + cusCount + ")" + "]");

                if(!tempQ.isEmpty() && (currentCustTime >= nextDeparture || ariCustNum == (depCustNum + 1))){
                    tempQ.remove();
                }

                if(queue.size() >= 2) {
                    for(int i = 1; i < tempQ.size(); i++){
                        double temp = tempQ.remove();
                        tempQ.add(temp);
                    }

                    double temp2 = tempQ.remove();
                    System.out.println("del: " + tempQ);
                    tempQ.add(temp2);
                }
                else{
                    System.out.println("del: " + tempQ);
                }
                //System.out.println("TEST del: " + queue);
                System.out.println("--------------------------------------------------------------------");
                System.out.println("");

                printCount++;
            }

            if(departed && printCount < 20 && printCount != 0) { // to only print first 20
                if(queue.peek() == null || currentCustTime > queue.peek()){
                    System.out.println("Clock Time: " + currentCustTime);

                    if(!queue.isEmpty()) {
                        System.out.println("fel: [Customer #" + depCustNum + " Left (Arrival Time: " + currCustArivTime + "),   Customer #" + (depCustNum + 1) + " is being served (Arrival Time: " + queue.peek() + "),   Next Arrival: " + nextArrival + "(Customer #" + cusCount + ")" + "]");
                    }
                    else{
                        System.out.println("fel: [Customer #" + depCustNum + " Left (Arrival Time: " + currCustArivTime + "),   Next Arrival: " + nextArrival + "(Customer #" + cusCount + ")" + "]");
                    }
                }
                else {
                    System.out.println("Clock Time: " + clockTime);
                    System.out.println("fel: [Customer #" + depCustNum + " Departure Time: " + currentCustTime + ",   Customer #" + ariCustNum + " Arival Time: " + queue.peek() + ",   Next Arrival: " + nextArrival + "(Customer #" + cusCount + ")" + "]");
                }

                if(!tempQ.isEmpty()) {
                    tempQ.remove();
                }
                System.out.println("del: " + tempQ);
                //System.out.println("TEST del: " + queue);
                System.out.println("--------------------------------------------------------------------");
                System.out.println("");

                printCount++;
            }

        }

        System.out.println("********************************************************************");
        System.out.println("Total Clock Time: " + clockTime);
        System.out.println("Total Customer Served: " + totalCustomersServiced);

        Double avgQueueLength = queueLengthSum*1.0 / count;
        System.out.println("Average queue length: " + avgQueueLength);

        System.out.println("Average idle period: " + (totalIdleT / totalCustomersServiced));
        System.out.println("********************************************************************");
    }

    // Exponentially distributed random number
    public static double randExp(double rate) {
        Random random = new Random();
        if (rate < 0.0) {
            throw new IllegalArgumentException("Mu/Lambda must be positive: " + rate);
        }

        double value = -Math.log(1 - random.nextDouble()) / rate;

        return value;
    }

}