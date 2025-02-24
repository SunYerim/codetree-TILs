import java.util.*;
public class Main {
    static int minNum = Integer.MAX_VALUE, answer;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] nums = new int[n];
        int[] dp = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = sc.nextInt();
        }
        
        // 초기화
        Arrays.fill(dp, minNum);
        dp[0] = nums[0];

        for (int i = 1; i < n; i++) {
            int maxNum = Math.max(dp[i-1] + nums[i], nums[i]);

            dp[i] = maxNum;
        }

        answer = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            answer = Math.max(answer, dp[i]);
        }

        System.out.println(answer);


    }
}