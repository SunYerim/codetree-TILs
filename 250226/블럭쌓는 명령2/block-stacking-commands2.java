import java.util.*;
public class Main {
    static int[] blocks;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int K = sc.nextInt();

        blocks = new int[N+1];

        for (int i = 0; i < K; i++) {
            int A = sc.nextInt();
            int B = sc.nextInt();

            // logic
            for (int j = A; j <= B; j++) {
                blocks[j]++;
            }
        }

        // maxNum
        int maxNum = Integer.MIN_VALUE;
        for (int i = 1; i <= N; i++) {
            maxNum = Math.max(maxNum, blocks[i]);
        }

        System.out.println(maxNum);
       
    }
}