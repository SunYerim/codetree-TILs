import java.util.*;
import java.io.*;

public class Main {
    static int N, B;
    static int[] prices;
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        B = Integer.parseInt(st.nextToken());
        int answer = 0;
        prices = new int[N];

        for (int i = 0; i < N; i++) {
            int price = Integer.parseInt(br.readLine());
            prices[i] = price;
        }

        // sort
        Arrays.sort(prices);

        int maxStudents = 1;
        int sum = 0;
        int maxDiscountIndex = -1;

        // 계산
        for (int i = 0; i < N; i++) {
            if (sum + prices[i] > B) {
                maxDiscountIndex = i;
                break;
            }
            sum += prices[i];
            maxStudents++;
        }

        

        System.out.println(maxDiscountIndex+1);

    }
}