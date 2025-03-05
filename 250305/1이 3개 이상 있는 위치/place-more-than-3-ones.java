import java.util.*;
import java.io.*;

public class Main {
    static int N, answer = 0;
    static int[][] board;
    static int[] dy = {0, 0, 1, -1};
    static int[] dx = {1, -1, 0, 0};
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        N = Integer.parseInt(br.readLine());

        board = new int[N][N];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                board[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        // logic
        for (int j = 0; j < N; j++) {
            for (int i = 0; i < N; i++) {
                boolean flag = count(j, i);
                if (flag) answer++;
            }
        }


        System.out.println(answer);
    }

    private static boolean count(int y, int x) {
        boolean tmp = false;
        int cnt = 0;
        for (int i = 0; i < 4; i++) {
            int ny = y + dy[i];
            int nx = x + dx[i];

            if (ny < 0 || nx < 0 || ny >= N || nx >= N) continue;
            if (board[ny][nx] == 1) cnt++;
        }

        if (cnt >= 3) tmp = true;

        return tmp;
    }
}