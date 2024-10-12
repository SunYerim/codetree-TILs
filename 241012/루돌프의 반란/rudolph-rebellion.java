import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class Main {
    static int[][] map;
    static int n, m, P, C, D, rouDolR, rouDolC;
    static List<Santa> santas;
    static int[] dr = {-1, 0, 1, 0, -1, 1, 1, -1}; // 상우하좌 + 대각선
    static int[] dc = {0, 1, 0, -1, 1, 1, -1, -1};
    static boolean globalFlag = false;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        P = Integer.parseInt(st.nextToken()); // 산타 수
        C = Integer.parseInt(st.nextToken()); // 루돌프 힘
        D = Integer.parseInt(st.nextToken()); // 산타 힘

        map = new int[n + 1][n + 1];
        santas = new ArrayList<>();

        st = new StringTokenizer(br.readLine());
        rouDolR = Integer.parseInt(st.nextToken());
        rouDolC = Integer.parseInt(st.nextToken());
        map[rouDolR][rouDolC] = -1; // 루돌프

        santas.add(new Santa(0, 0, 0, 0, true, 0));
        for (int i = 1; i <= P; i++) {
            st = new StringTokenizer(br.readLine());
            int santaNum = Integer.parseInt(st.nextToken());
            int santaR = Integer.parseInt(st.nextToken());
            int santaC = Integer.parseInt(st.nextToken());
            map[santaR][santaC] = santaNum;
            santas.add(new Santa(santaNum, santaR, santaC, 0, true, 0));
        }

        Collections.sort(santas);

        while (m-- > 0) {
            moveRudolph();
            if (globalFlag) break;
            moveSanta();
            if (globalFlag) break;

            for (int i = 1; i <= P; i++) {
                Santa santa = santas.get(i);
                if (santa.isAlive) {
                    if (santa.coma > 0)
                        santas.set(i, new Santa(i, santa.r, santa.c, santa.grade + 1, santa.isAlive, --santa.coma));
                    else
                        santas.set(i, new Santa(i, santa.r, santa.c, santa.grade + 1, santa.isAlive, santa.coma));
                }
            }
        }

        for (int i = 1; i <= P; i++) {
            Santa santa = santas.get(i);
            System.out.print(santa.grade + " ");
        }
    }

    private static void moveRudolph() {
        int who = 0;
        double minDistance = Double.MAX_VALUE;
        int targetR = -1, targetC = -1;

        for (int i = 1; i <= P; i++) {
            Santa santa = santas.get(i);
            if (santa.isAlive) {
                double distance = Math.pow((rouDolR - santa.r), 2) + Math.pow((rouDolC - santa.c), 2);
                if (distance < minDistance || (distance == minDistance && (santa.r > targetR || (santa.r == targetR && santa.c > targetC)))) {
                    who = i;
                    minDistance = distance;
                    targetR = santa.r;
                    targetC = santa.c;
                }
            }
        }

        if (who != 0) {
            Santa targetSanta = santas.get(who);
            int idx = -1;
            minDistance = Double.MAX_VALUE;

            for (int i = 0; i < 8; i++) {
                int nr = rouDolR + dr[i];
                int nc = rouDolC + dc[i];
                if (nr < 1 || nc < 1 || nr > n || nc > n) continue;
                double distance = Math.pow((nr - targetSanta.r), 2) + Math.pow((nc - targetSanta.c), 2);
                if (distance < minDistance) {
                    minDistance = distance;
                    idx = i;
                }
            }

            map[rouDolR][rouDolC] = 0;
            rouDolR += dr[idx];
            rouDolC += dc[idx];

            if (map[rouDolR][rouDolC] > 0) {
                int newR = targetSanta.r + C * dr[idx];
                int newC = targetSanta.c + C * dc[idx];
                int newGrade = targetSanta.grade + C;

                if (newR < 1 || newC < 1 || newR > n || newC > n) {
                    santas.set(targetSanta.idx, new Santa(targetSanta.idx, newR, newC, newGrade, false, 0));
                    if (!canContinue()) {
                        globalFlag = true;
                        return;
                    }
                } else {
                    if (map[newR][newC] > 0) {
                        interaction(map[newR][newC], newR, newC, idx);
                    }
                    map[newR][newC] = targetSanta.idx;
                    santas.set(targetSanta.idx, new Santa(targetSanta.idx, newR, newC, newGrade, true, 2));
                }
            }
            map[rouDolR][rouDolC] = -1;
        }
    }

    private static void moveSanta() {
        for (int i = 1; i <= P; i++) {
            Santa santa = santas.get(i);

            if (!santa.isAlive || santa.coma > 0) continue;
            double currDistance = Math.pow((santa.r - rouDolR), 2) + Math.pow((santa.c - rouDolC), 2);
            int idx = -1;

            for (int j = 0; j < 4; j++) {
                int nr = santa.r + dr[j];
                int nc = santa.c + dc[j];
                if (nr < 1 || nc < 1 || nr > n || nc > n) continue;
                if (map[nr][nc] > 0) continue;
                double newDistance = Math.pow((nr - rouDolR), 2) + Math.pow((nc - rouDolC), 2);
                if (newDistance < currDistance) {
                    currDistance = newDistance;
                    idx = j;
                }
            }

            if (idx != -1) {
                map[santa.r][santa.c] = 0;
                int nr = santa.r + dr[idx];
                int nc = santa.c + dc[idx];
                if (map[nr][nc] == -1) {
                    int newR = nr + D * dr[(idx + 2) % 4];
                    int newC = nc + D * dc[(idx + 2) % 4];
                    int newGrade = santa.grade + D;
                    if (newR < 1 || newC < 1 || newR > n || newC > n) {
                        santas.set(i, new Santa(santa.idx, newR, newC, newGrade, false, 0));
                        if (!canContinue()) {
                            globalFlag = true;
                            return;
                        }
                    } else {
                        if (map[newR][newC] > 0) {
                            interaction(map[newR][newC], newR, newC, (idx + 2) % 4);
                        }
                        map[newR][newC] = santa.idx;
                        santas.set(i, new Santa(santa.idx, newR, newC, newGrade, true, 2));
                    }
                } else {
                    map[nr][nc] = santa.idx;
                    santas.set(i, new Santa(santa.idx, nr, nc, santa.grade, true, 0));
                }
            }
        }
    }

    private static void interaction(int idx, int r, int c, int dir) {
        Santa santa = santas.get(idx);
        int nr = santa.r + dr[dir];
        int nc = santa.c + dc[dir];
        if (nr < 1 || nc < 1 || nr > n || nc > n) {
            santas.set(idx, new Santa(idx, nr, nc, santa.grade, false, 0));
            if (!canContinue()) {
                globalFlag = true;
            }
            return;
        }
        if (map[nr][nc] > 0) {
            interaction(map[nr][nc], nr, nc, dir);
        }
        map[nr][nc] = idx;
        santas.set(idx, new Santa(idx, nr, nc, santa.grade, true, santa.coma));
    }

    private static boolean canContinue() {
        for (int i = 1; i <= P; i++) {
            if (santas.get(i).isAlive) {
                return true;
            }
        }
        return false;
    }

    static class Santa implements Comparable<Santa> {
        int idx, r, c, grade, coma;
        boolean isAlive;

        public Santa(int idx, int r, int c, int grade, boolean isAlive, int coma) {
            this.idx = idx;
            this.r = r;
            this.c = c;
            this.grade = grade;
            this.isAlive = isAlive;
            this.coma = coma;
        }

        @Override
        public int compareTo(Santa o) {
            return Integer.compare(this.idx, o.idx);
        }
    }
}