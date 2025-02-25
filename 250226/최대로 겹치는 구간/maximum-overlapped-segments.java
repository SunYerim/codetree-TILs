import java.util.Scanner;

public class Main {
    static int[] checked;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] x1 = new int[n];
        int[] x2 = new int[n];
        checked = new int[201]; 

        for (int i = 0; i < n; i++) {
            x1[i] = sc.nextInt() + 100; // 오프셋 적용
            x2[i] = sc.nextInt() + 100;

            // check
            for (int j = x1[i]; j < x2[i]; j++) {
                checked[j]++;
            }
        }

        // maxNum
        int maxNum = Integer.MIN_VALUE;
        for (int i = 0; i < 201; i++) {
            maxNum = Math.max(maxNum, checked[i]);
        }

        System.out.println(maxNum);
    }
}
