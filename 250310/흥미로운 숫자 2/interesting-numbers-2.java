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
                // System.out.println(number);
                answer++;
            }
        }


        System.out.print(answer);
    }

    private static boolean interestNum(int number) {
        String curr = Integer.toString(number);
        int[] numbers = new int[10];

        for (int i = 0; i < curr.length(); i++) {
            int tmp = Integer.parseInt(String.valueOf(curr.charAt(i)));
            numbers[tmp]++; 
        }

        int number1 = 0;
        int number2 = 0;
        
        for (int i = 0; i < 10; i++) {
            if (numbers[i] == 0) continue;
            else if (numbers[i] == 1) number2++;
            else if (numbers[i] > 1) number1++;
        }

        if (number1 == 1 &&  number2 == 1) return true;
        else return false;
    }
}