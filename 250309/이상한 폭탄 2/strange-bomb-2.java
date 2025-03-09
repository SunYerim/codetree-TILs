import java.util.*;

public class Main {
    static int answer = Integer.MIN_VALUE;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();
        int[] bombs = new int[n];
        for (int i = 0; i < n; i++) {
            bombs[i] = sc.nextInt();
        }

        // 탐색
        for (int i = 0; i < n; i++) {
            int number = bombs[i];
            
            if (i + k < n) {
                for (int j = i + 1; j <= i + k; j++) {
                    if (number == bombs[j]) answer = Math.max(number, answer);
                }
            } else {
                for (int j = i + 1; j < n; j++) {
                    if (number == bombs[j]) answer = Math.max(number, answer);
                }
            }
            
        }
        
        if (answer == Integer.MIN_VALUE) System.out.print(-1);
        else System.out.print(answer);
    }
}