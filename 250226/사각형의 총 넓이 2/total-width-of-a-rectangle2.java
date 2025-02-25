import java.util.Scanner;
public class Main {
    static int[][] board;
    static int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE, minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] x1 = new int[n];
        int[] y1 = new int[n];
        int[] x2 = new int[n];
        int[] y2 = new int[n];
        board = new int[201][201];

        for (int i = 0; i < n; i++) {
            // offset 처리
            x1[i] = sc.nextInt() + 100;
            y1[i] = sc.nextInt() + 100;
            x2[i] = sc.nextInt() + 100;
            y2[i] = sc.nextInt() + 100;

            minX = Math.min(Math.min(x1[i], x2[i]), minX);
            minY = Math.min(Math.min(y1[i], y2[i]), minY);
            maxX = Math.max(Math.max(x1[i], x2[i]), maxX);
            maxY = Math.max(Math.max(y1[i], y2[i]), maxY);

            for (int x = x1[i]; x < x2[i]; x++) {
                for (int y = y1[i]; y < y2[i]; y++) {
                    board[x][y] = 1;
                }
            }
        }
        // System.out.println(minX + " " + maxX + " " + minY + " " + maxY);

        int answer = 0;

        for (int i = minX; i < maxX; i++) {
            for (int j = minY; j < maxY; j++) {
                // System.out.print(board[i][j] + " ");
                if (board[i][j] == 1) answer++;
            }
            // System.out.println();
        }

        System.out.println(answer);
        

    }
}