package projectParser;
import edu.princeton.cs.algs4.StdIn;

public class DcgCalc {
    public static void main(String[] args) {
        
        while (!StdIn.isEmpty()) {
            
            double dcg = 0;
            int[] vector = new int[13];
            vector[0] = -1;
            
            for (int i = 1; i < 13; i++) {
                vector[i] = StdIn.readInt();
            }
            
            for (int rank = 1; rank <= 12; rank++){
                
                dcg += vector[rank]/(Math.log(rank+1)/Math.log(2));
                
            }
            
            System.out.println("DCG: " + dcg);
        } 
    }
}