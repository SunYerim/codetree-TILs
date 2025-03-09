import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int x = sc.nextInt();
        int y = sc.nextInt();
        int answer = 0;
        
        for (int i = x; i <= y; i++) {
            int number = i;
            // 판별
            if (interestNum(number)) {
                answer++;
            }
        }


        System.out.print(answer);
    }

    private static boolean interestNum(int number) {
        String curr = Integer.toString(number);
        int[] numbers = new int[curr.length()];

        for (int i = 0; i < curr.length(); i++) {
            numbers[i] = Integer.parseInt(String.valueOf(curr.charAt(i)));
        }

        Arrays.sort(numbers);

        int diff = 0;
        int curNum = numbers[0];
        for (int i = 1; i < curr.length(); i++) {
            if (curNum != numbers[i]) {
                diff++;
                curNum = numbers[i];
            }
        }

        if (diff != 1) return false;
        else return true;
    }
}