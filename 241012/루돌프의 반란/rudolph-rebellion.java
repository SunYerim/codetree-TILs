import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class Main {
    static int[][] map;
    static int n, m, P, C, D, k, rouDolR, rouDolC;
    static int[] score;
    static List<Santa> santas;
    static int[] dr = {-1, 0, 1, 0, 1, 1, -1, -1};
    static int[] dc = {0, 1, 0, -1, 1, -1, 1, -1};
    static boolean globalFlag = false;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        P = Integer.parseInt(st.nextToken()); // 산타 수
        C = Integer.parseInt(st.nextToken()); // 루돌프 힘
        D = Integer.parseInt(st.nextToken()); // 산타 힘

        map = new int[n+1][n+1];
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
             moveRoudolp();
             if (globalFlag)
                 break;
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
            if (globalFlag) break;
         }

         for (int i = 1; i <= P; i++) {
             Santa santa = santas.get(i);
             System.out.print(santa.grade + " ");
         }
    }

    private static boolean canContinue() {
        boolean isContinue = false;
        for (int i = 1; i <= P; i++) {
            Santa santa = santas.get(i);
            if (santa.isAlive) {
                isContinue = true;
                break;
            }
        }
        return isContinue;
    }

    // 루돌프 움직임
    private static void moveRoudolp() {
        int who = 0;
        int distance = Integer.MAX_VALUE;
        int r = Integer.MIN_VALUE;
        int c = Integer.MIN_VALUE;
        for (int i = 1; i <= P; i++) {
            Santa santa = santas.get(i);
            if (santa.isAlive) {
                double dis = Math.pow((rouDolR - santa.r) , 2) + Math.pow((rouDolC - santa.c), 2);
                if (dis > distance) continue;
                else {
                    if (dis == distance) {
                        if (santa.r > r) {
                            who = i;
                            r = santa.r;
                            c = santa.c;
                        } else {
                            if (santa.r == r) {
                                if (santa.c > c) {
                                    who = i;
                                    c = santa.c;
                                }
                            }
                        }
                    } else {
                        who = i;
                        distance = (int) dis;
                        r = santa.r;
                        c = santa.c;
                    }
                }
            } else continue;
        }

        if (who != 0) {
            Santa santa = santas.get(who);
            int x = santa.r;
            int y = santa.c;
            int idx = -1;
            double curDir = Math.pow((rouDolR - r), 2) + Math.pow((rouDolC - c), 2);
            for (int i = 0; i < 8; i++) {
                int nr = rouDolR + dr[i];
                int nc = rouDolC + dc[i];
                double moveDir = Math.pow((nr - r), 2) + Math.pow((nc - c), 2);
                if (moveDir < curDir) {
                    curDir = moveDir;
                    idx = i;
                }
            }
            map[rouDolR][rouDolC] = 0;
            rouDolR += dr[idx];
            rouDolC += dc[idx];
            // 산타가 있으면
            if (map[rouDolR][rouDolC] > 0) {
                int newIdx = idx;
                int newX = santa.r + C * dr[newIdx];
                int newY = santa.r + C * dc[newIdx];
                int newGrade = santa.grade + C;

                if (newX < 1 || newY < 1 || newX > n || newY > n) {
                    santas.set(santa.idx, new Santa(santa.idx, newX, newY, newGrade, false, 0));
                    if (!isContinue()) {
                        globalFlag = true;
                        return;
                    }
                } else {
                    // 상호작용
                    if (map[newX][newY] > 0) {
                        interaction(map[newX][newY], newX, newY, newIdx);
                        map[newX][newY] = santa.idx;
                        santas.set(santa.idx, new Santa(santa.idx, newX, newY, newGrade, true, 2));
                    } else {
                        map[newX][newY] = santa.idx;
                        santas.set(santa.idx, new Santa(santa.idx, newX, newY, newGrade, true, 2));
                    }
                }
                map[rouDolR][rouDolC] = -1;
            } else map[rouDolR][rouDolC] = -1;
        }

    }

    // 산타 움직임
    private static void moveSanta() {
        // 1번부터 p번까지 움직임
        // 산타는 루돌프에게 거리가 가장 가까워지는 방향으로 1칸 이동
        // 다른 산타가 있는 칸 또는 게임판 밖이면 못 움직임
        // 움직일 수 있는 칸 없다면 안 움직임
        // 움직일 수 있는 칸 있더라도 루돌프로부터 가까워질 수 있는 방법 없다면 안 움직임
        // 상하좌우로 인접한 4방향중 움직임. 가장 가까워지는 방향이 여러개라면 상우하좌 우선순위에 맞춰 움직임
        for (int i = 1; i <= P; i++) {
            Santa santa = santas.get(i);

            // 이미 죽은 산타
            if (!santa.isAlive || santa.coma > 0) continue;
            double currDir = Math.pow((santa.r - rouDolR), 2) + Math.pow((santa.c - rouDolC), 2);
            int idx = -1;
            for (int j = 0; j < 4; j++) {
                int nr = santa.r + dr[j];
                int nc = santa.c + dc[j];
                if (nr < 1 || nc < 1 || nr > n || nc > n) continue;
                if (map[nr][nc] > 0) continue;
                double moveDir = Math.pow((nr - rouDolR), 2) + Math.pow((nc - rouDolC), 2);
                if (moveDir < currDir) {
                    currDir = moveDir;
                    idx = j;
                }
            }
            // 움직일 수 있으면
            if (idx != -1) {
                map[santa.r][santa.c] = 0; // 빈칸으로
                int nr = santa.r + dr[idx];
                int nc = santa.c + dc[idx];
                // 박았다면
                if (map[nr][nc] == -1) {
                    int newIdx = (idx + 2) % 4;
                    int newR = nr + D * dr[newIdx];
                    int newC = nc + D * dc[newIdx];
                    int newGrade = santa.grade + D;
                    if (newR < 1 || newC < 1 || newR > n || newC > n) {
                        santas.set(i, new Santa(santa.idx, newR, newC, newGrade, false, 0));
                        if (!isContinue()) {
                            globalFlag = true;
                            return;
                        }
                    } else {
                        // 상호작용
                        if (map[newR][newC] > 0) {
                            interaction(map[newR][newC], newR, newC, newIdx);
                            map[newR][newC] = santa.idx;
                            santas.set(i, new Santa(santa.idx, newR, newC, newGrade, true, 2));
                        } else {
                            map[newR][newC] = santa.idx;
                            santas.set(i, new Santa(santa.idx, newR, newC, newGrade, true, 2));
                        }
                    }
                } else {
                    if (santa.coma > 0) {
                        santas.set(i, new Santa(santa.idx, santa.r, santa.c, santa.grade, true, --santa.coma));
                    } else {
                        map[nr][nc] = santa.idx;
                       santas.set(i, new Santa(santa.idx, nr, nc, santa.grade, true, 0));
                    }
                }
            }
        }
    }

    // interaction
    private static void interaction(int idx, int r, int c, int dir) {
        Santa santa = santas.get(idx);
        int nr = santa.r + dr[dir];
        int nc = santa.c + dc[dir];
        if (nr < 1 || nc < 1 || nr > n || nc > n) {
            santas.set(idx, new Santa(idx, nr, nc, santa.grade, false, 0));
            if (!canContinue()) {
                globalFlag = true;
            }
        }
        if (map[nr][nc] > 0) {
            interaction(map[nr][nc], nr, nc, dir);
        }
        map[nr][nc] = idx;
        santas.set(idx, new Santa(idx, nr, nc, santa.grade, true, santa.coma));
    }

    private static boolean isContinue() {
        boolean flag = false;
        for (int i = 1; i <= P; i++) {
            Santa santa = santas.get(i);
            if (santa.isAlive) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    // 산타 객체
    static class Santa implements Comparable<Santa>{
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
            return this.idx - o.idx;
        }
    }



}