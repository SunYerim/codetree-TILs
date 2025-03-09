import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int B = Integer.parseInt(st.nextToken());
        int[] prices = new int[N];

        for (int i = 0; i < N; i++) {
            prices[i] = Integer.parseInt(br.readLine());
        }

        // 1. 가격을 오름차순 정렬
        Arrays.sort(prices);

        // 2. 할인 없이 최대 몇 명까지 살 수 있는지 계산
        int maxStudents = 0;
        int totalCost = 0;
        
        for (int i = 0; i < N; i++) {
            if (totalCost + prices[i] > B) break;
            totalCost += prices[i];
            maxStudents++;
        }

        // 3. 각 선물을 할인했을 때 최대 학생 수 갱신
        for (int i = 0; i < N; i++) {
            int discountedPrice = prices[i] / 2;
            int tempTotal = discountedPrice; // 할인된 가격으로 구매
            int tempStudents = 1; 

            for (int j = 0; j < N; j++) {
                if (i == j) continue; // 할인한 제품 제외
                
                if (tempTotal + prices[j] > B) break;
                tempTotal += prices[j];
                tempStudents++;
            }

            maxStudents = Math.max(maxStudents, tempStudents);
        }

        System.out.println(maxStudents);
    }
}
