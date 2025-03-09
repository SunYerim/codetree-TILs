import java.util.*;

public class Main {
    static int minH = Integer.MAX_VALUE, maxH = Integer.MIN_VALUE;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] h = new int[n];
        for (int i = 0; i < n; i++) {
            h[i] = sc.nextInt();
            minH = Math.min(minH, h[i]);
            maxH = Math.max(maxH, h[i]);
        }

        int answer = Integer.MIN_VALUE;
        // 탐색
        for (int i = minH; i < maxH; i++) {
            int count = 0;
            int[] visited = new int[n];
            for (int j = 0; j < n; j++) {
                if (h[j] > i) visited[j] = 1;
            }

            // 빙산 갯수 계산
            // int currIdx = 0;
            for (int j = 0; j < n; j++) {
                if (j == 0) {
                    if (visited[j] == 1) count++;
                }
                else {
                    if (visited[j-1] == 1 && visited[j] == 1) continue;
                    else if (visited[j] == 1) count++;
                }
            }

            answer = Math.max(answer, count);

        }

        System.out.print(answer);
        
    }
}