import java.util.*;

public class monteCarlo {
    public static void main(String [] args){
        Random rand = new Random();
        int count = 0;   // number of points/dots
        int s = 0; // points/dots under curve
        double rectangle1_area = 1; // rectangle/box area of integral 1
        double rectangle2_area = 2; // rectangle/box area of integral 2

        double integral1Estimate = 0.0; // Integral 1 estimate
        double integral2Estimate = 0.0; // Integral 2 estimate

        // Integral 1
        while(count < 10000){
            double randX_coord = Math.random();
            double randY_coord = Math.random();

            if(randY_coord < func(randX_coord)){
                s++;
            }

            count++;
        }

        integral1Estimate = (rectangle1_area * s * 1.0) / count;
        System.out.println("Integral 1 estimate: " + integral1Estimate);

        // Integral 2
        count = 0;
        s = 0;
        while(count < 10000){
            double randX_coord = rand.nextInt(2) + Math.random();
            double randY_coord = Math.random();

            if(randY_coord < func(randX_coord)){
                s++;
            }

            count++;
        }

        integral2Estimate = (rectangle2_area * s * 1.0) / count;
        System.out.println("Integral 2 estimate: " + integral2Estimate);

    }

    public static double func(double x){
        return Math.exp(-x);
    }

}
