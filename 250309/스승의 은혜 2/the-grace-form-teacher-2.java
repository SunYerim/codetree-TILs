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

        // 계산
        int idx = 0;
        int curr = 0;
        while (curr <= B) {
            int tmp = curr + prices[idx];
            int tmp2 = curr + prices[idx] / 2;

            if (tmp > B && tmp2 < B) {
                curr += tmp2;
            }
            else {
                curr += tmp;
            }

            idx++;

        }

        System.out.println(idx+1);

    }
}