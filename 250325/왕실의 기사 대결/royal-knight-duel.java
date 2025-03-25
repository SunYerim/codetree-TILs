import java.util.*;
import java.io.*;

public class Main {
    static List<Knight> list;
    static int L, N, Q, answer = 0;
    static int[] dy = {-1, 0, 1, 0}; // 위 - 오 - 아 - 왼
    static int[] dx = {0, 1, 0, -1};
    static int[][] map;
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        L = Integer.parseInt(st.nextToken()); // map크기
        N = Integer.parseInt(st.nextToken()); // 기사 수
        Q = Integer.parseInt(st.nextToken()); // 명령 수

        map = new int[L][L];
        list = new ArrayList<>();

        // 빈칸, 함정, 벽에 대한 map
        for (int i = 0; i < L; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < L; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        // 0번째 기사 추가
        list.add(new Knight(0, 0, 0, 0, 0, 0));

        // 기사
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            int h = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            int k = Integer.parseInt(st.nextToken());

            list.add(new Knight(i + 1, c, r, h, w, k));
        }

        // 초기 knightMap
        int[][] knightMap = new int[L][L];

        // 명령 시작
        for (int i = 0; i < Q; i++) {
            st = new StringTokenizer(br.readLine());
            int knightNum = Integer.parseInt(st.nextToken());
            int d = Integer.parseInt(st.nextToken());
            List<Integer> movedKnights = canMove(knightNum, d);
            if (movedKnights != null) {
                int[][] newMap = go(movedKnights, d);

                damage(movedKnights, knightNum);
            }
        }

        System.out.println(answer);
    }

    // 기사의 이동 -> 밀린 기사들까지 전체 map을 새로 만듬.
    public static int[][] go(List<Integer> movedKnights, int d) {
        int[][] newMap = new int[L][L];
        // System.out.println("~~~~");

        for (int idx : movedKnights) {
            Knight k = list.get(idx);
            if (k.k <= 0) continue;

            k.x += dy[d];
            k.y += dx[d];

            for (int i = 0; i < k.h; i++) {
                for (int j = 0; j < k.w; j++) {
                    newMap[k.x + i][k.y + j] = k.num;
                }
            }
        }

        return newMap;
    }


    // 이동이 가능한지 판단
    public static List<Integer> canMove(int knightNum, int d) {
        // 1. 큐에 i를 넣고 연쇄적으로 밀릴 애들을 다 탐색
        // 2. 밀린 위치가 벽, 범위 밖, 또는 trap이 아니면서 이동 가능한지 확인
        // 3. 이동 가능하면 true, 불가능하면 false
        Queue<Integer> queue = new LinkedList<>();
        List<Integer> movedKnights = new ArrayList<>();
        boolean[] visited = new boolean[N + 1];
        // System.out.println("debugging");

        queue.add(knightNum);
        visited[knightNum] = true;

        while (!queue.isEmpty()) {
            int curr = queue.poll();
            movedKnights.add(curr);

            Knight k = list.get(curr);
            // 다음 위치로 이동했을 때 충돌하는 기사들 찾아서 queue에 추가
            for (int other = 1; other <= N; other++) {
                if (visited[other]) continue;
                // 밀린 위치 벽, 범위 밖 아닌지 확인
                if (isOverlappingAfterMove(k, list.get(other), d)) {
                    queue.add(other);
                    visited[other] = true;
                }
            }
        }

        // 이동 후 범위 판단 여부
        for (int idx : movedKnights) {
            Knight k = list.get(idx);
            int nx = k.x + dy[d];
            int ny = k.y + dx[d];
            for (int i = 0; i < k.h; i++) {
                for (int j = 0; j < k.w; j++) {
                    int ni = nx + i;
                    int nj = ny + j;
                    if (ni < 0 || nj < 0 || ni >= L || nj >= L) return null;
                    if (map[ni][nj] == 2) return null; // 벽
                }
            }
        }

        return movedKnights;
        
    }

    public static boolean isOverlappingAfterMove(Knight mover, Knight other, int d) {
        // mover
        // mover의 이동 후 위치 계산
        int newX = mover.x + dy[d];
        int newY = mover.y + dx[d];

        int ax1 = newX;
        int ay1 = newY;
        int ax2 = newX + mover.h - 1;
        int ay2 = newY + mover.w - 1;

        int bx1 = other.x;
        int by1 = other.y;
        int bx2 = other.x + other.h - 1;
        int by2 = other.y + other.w - 1;

        // 두 사각형이 겹치는지 판별
        boolean overlap = !(ax2 < bx1 || bx2 < ax1 || ay2 < by1 || by2 < ay1);

        return overlap;
        
    }

    // 대결 대미지 반영
    public static void damage(List<Integer> movedKnights, int commandKnightNum) {
        for (int id : movedKnights) {
            if (id == commandKnightNum) continue;

            Knight k = list.get(id);
            if (k.k <= 0) continue;

            int trapCount = 0;

            for (int i = 0; i < k.h; i++) {
                for (int j = 0; j < k.w; j++) {
                    int ny = k.y + i;
                    int nx = k.x + j;
                    if (map[ny][nx] == 1) trapCount++;
                }
            }

            k.k -= trapCount;
            if (trapCount > 0) {
                answer += trapCount;
                // System.out.println("----");
            }
        }
    }

    
    static class Knight {
        int num, x, y, h, w, k;

        public Knight(int num, int x, int y, int h, int w, int k) {
            this.num = num;
            this.x = x;
            this.y = y;
            this.h = h;
            this.w = w;
            this.k = k;
        }
    }
}