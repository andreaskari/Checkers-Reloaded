import java.util.HashMap; // Import Java's HashMap so we can use it

public class FibonacciMemo {

    /**
     * The classic recursive implementation with no memoization. Don't care
     * about graceful error catching, we're only interested in performance.
     * 
     * @param n
     * @return The nth fibonacci number
     */
    public static int fibNoMemo(int n) {
        if (n <= 1) {
            return n;
        }
        return fibNoMemo(n - 2) + fibNoMemo(n - 1);
    }

    public static HashMap<Integer, Integer> mapOfFibonacciValues 
        = new HashMap<Integer, Integer>();

    /**
     * Your optimized recursive implementation with memoization. 
     * You may assume that n is non-negative.
     * 
     * @param n
     * @return The nth fibonacci number
     */
    public static int fibMemo(int n) {
        if (n <= 1) {
            return n;
        } else if (mapOfFibonacciValues.containsKey(n)) {
            return mapOfFibonacciValues.get(n);
        }
        Integer value = fibMemo(n - 2) + fibMemo(n - 1);
        mapOfFibonacciValues.put(n, value);
        return value;
    }

    /**
     * Answer the following question as a returned String in this method:
     * Why does even a correctly implemented fibMemo not return 2,971,215,073
     * as the 47th Fibonacci number?
     */
    public static String why47() {
        String answer = "Because the maximum positive int that can be stored ";
        answer += "is less than 2,971,215,073. Therefore these is an overflow.";
        return answer;
    }

    public static void main(String[] args) {
        // Optional testing here        
        String m = "Fibonacci's real name was Leonardo Pisano Bigollo.";
        m += "\n" + "He was the son of a wealthy merchant.\n";
        System.out.println(m);
        System.out.println("0: " + FibonacciMemo.fibMemo(0));
        System.out.println("1: " + FibonacciMemo.fibNoMemo(1));
        System.out.println("2: " + FibonacciMemo.fibNoMemo(2));
        System.out.println("3: " + FibonacciMemo.fibNoMemo(3));
        System.out.println("4: " + FibonacciMemo.fibNoMemo(4));

        System.out.println("46 memo: " + FibonacciMemo.fibMemo(46));
        System.out.println("46 No memo: " + FibonacciMemo.fibNoMemo(46));
        System.out.println("8000 memo: " + FibonacciMemo.fibMemo(8000));

        // 46th Fibonacci = 1,836,311,903
        // 47th Fibonacci = 2,971,215,073
    }
}
