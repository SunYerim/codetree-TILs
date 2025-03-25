import java.util.*;
import java.io.*;

public class Main {
    static final int MAX_N = 31;
    static final int MAX_L = 31;

    static int l, n, q;
    static int[][] info = new int[MAX_L][MAX_N];
    static int[] bef_k = new int[MAX_N];
    static int[] r = new int[MAX_N], c = new int[MAX_N], h = new int[MAX_N], w = new int[MAX_N], k = new int[MAX_N];
    static int[] nr = new int[MAX_N], nc = new int[MAX_N];
    static int[] dmg = new int[MAX_N];
    static boolean[] is_moved = new boolean[MAX_N];
    static int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};

    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        l = Integer.parseInt(st.nextToken());
        n = Integer.parseInt(st.nextToken());
        q = Integer.parseInt(st.nextToken());

        for (int i = 1; i <= l; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 1; j <= l; j++) {
                info[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        for (int i = 1; i <= n; i++) {
            st = new StringTokenizer(br.readLine());
            r[i] = Integer.parseInt(st.nextToken());
            c[i] = Integer.parseInt(st.nextToken());
            h[i] = Integer.parseInt(st.nextToken());
            w[i] = Integer.parseInt(st.nextToken());
            k[i] = Integer.parseInt(st.nextToken());
            bef_k[i] = k[i];
        }

        for (int i = 1; i <= q; i++) {
            st = new StringTokenizer(br.readLine());
            int idx = Integer.parseInt(st.nextToken());
            int dir = Integer.parseInt(st.nextToken());
            movePiece(idx, dir);
        }

        long answer = 0;
        for (int i = 1; i <= n; i++) {
            if (k[i] > 0) {
                answer += (bef_k[i] - k[i]);
            }
        }

        System.out.println(answer);
    }

    // 움직임 시도
    public static boolean tryMovement(int idx, int d) {
        Queue<Integer> queue = new LinkedList<>();
        boolean is_pos = true;

        for (int i = 1; i <= n; i++) {
            dmg[i] = 0;
            is_moved[i] = false;
            nr[i] = r[i];
            nc[i] = c[i];
        }

        queue.add(idx);
        is_moved[idx] = true;

        while (!queue.isEmpty()) {
            int x = queue.poll();

            nr[x] += dx[d];
            nc[x] += dy[d];

            // 경계
            if (nr[x] < 1 || nc[x] < 1 || nr[x] + h[x] - 1 > l || nc[x] + w[x] - 1 > l) return false;

            // 충돌
            for (int i = nr[x]; i <= nr[x] + h[x] - 1; i++) {
                for (int j = nc[x]; j <= nc[x] + w[x] - 1; j++) {
                    if (info[i][j] == 1) dmg[x]++;
                    if (info[i][j] == 2) return false;
                }
            }

            // 충돌하는 경우 같이 이동
            for (int i = 1; i <= n; i++) {
                if (is_moved[i] || k[i] <= 0) continue;
                if (r[i] > nr[x] + h[x] - 1 || nr[x] > r[i] + h[i] - 1) continue;
                if (c[i] > nc[x] + w[x] - 1 || nc[x] > c[i] + w[i] - 1) continue;

                is_moved[i] = true;
                queue.add(i);
            }
        }

        dmg[idx] = 0;
        return true;
    }

    // 이동
    public static void movePiece(int idx, int d) {
        if (k[idx] <= 0) return;

        if (tryMovement(idx, d)) {
            for (int i = 1; i <= n; i++) {
                r[i] = nr[i];
                c[i] = nc[i];
                k[i] -= dmg[i];
            }
        }
    }
}