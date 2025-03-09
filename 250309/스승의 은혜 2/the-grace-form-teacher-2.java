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
        int maxStudents = 1;
        int totalCost = 0;

        for (int i = 0; i < N; i++) {
            if (totalCost + prices[i] > B) break;
            totalCost += prices[i];
            maxStudents++;
        }

        int[] halfPrice = new int[N];
        Arrays.fill(halfPrice, -1);
        
        // 3. 각 선물을 할인했을 때 최대 학생 수 갱신
        for (int i = 0; i < N; i++) {
            int discountedPrice = prices[i] / 2;
            int total = discountedPrice;
            int students = 1;
            for (int j = i + 1; j < N; j++) {
                if (total > B) {
                    halfPrice[i] = students;
                    students++;
                    break;
                }
                total += prices[i];
            }
        }

        // 최대 학생수
        for (int i = 0; i < N; i++) {
            maxStudents = Math.max(maxStudents, halfPrice[i]);
        }

        System.out.println(maxStudents);
    }
}
