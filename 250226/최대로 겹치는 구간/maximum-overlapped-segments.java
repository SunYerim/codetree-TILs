import java.util.Scanner;
public class Main {
    static int[] checked;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] x1 = new int[n];
        int[] x2 = new int[n];
        checked = new int[101];
        for (int i = 0; i < n; i++) {
            x1[i] = sc.nextInt();
            x2[i] = sc.nextInt();

            // check
            for (int j = x1[i]; j <= x2[i]; j++) {
                checked[j]++;
            }
        }
        // maxNum
        int maxNum = Integer.MIN_VALUE;
        for (int i = 1; i < 101; i++) {
            maxNum = Math.max(maxNum, checked[i]);
        }

        System.out.println(maxNum);
    }
}