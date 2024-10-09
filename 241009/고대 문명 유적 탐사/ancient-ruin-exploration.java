import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    static int[] dx = {-1, 1, 0, 0};
    static int[] dy = {0, 0, -1, 1};
    static int k, m;
    static int[][] map;
    static int[][] newMap;
    static ArrayList<Node> candidates;
    static Queue<Integer> wallNumber;
    static PriorityQueue<Point> remove;
    static int[] answer;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        st = new StringTokenizer(br.readLine());
        k = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        map = new int[5][5];
        answer = new int[k];

        for (int i = 0; i < 5; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < 5; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        wallNumber = new LinkedList<>();
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < m; i++) {
            wallNumber.add(Integer.parseInt(st.nextToken()));
        }

        // k번 수행
        for (int t = 0; t < k; t++) {
            ArrayList<Node> candidate = new ArrayList<>(); // 우선순위 맞게 회전 중심좌표를 정하기 위함

            for (int dir = 1; dir < 4; dir++) {
                for (int i = 1; i <= 3; i++) {
                    for (int j = 1; j <= 3; j++) {
                        rotateMap(i, j, dir);
                        int score = bfs(newMap);

                        // 유물을 획득한다면
                        if (score > 0) {
                            candidate.add(new Node(i, j, dir, score));
                        }
                    }
                }
            }
            // 유물 획득 못 하면
            if (candidate.isEmpty()) {
                break;
            }

            // 정렬
            Collections.sort(candidate);

            // 중심좌표는
            Node center = candidate.get(0);
            int centerX = center.x;
            int centerY = center.y;

            rotateMap(centerX, centerY, center.roundType);
            map = newMap;

            int score = bfs(map);
            int sum = 0;

            // 유물 재 획득
            while (score > 0) {
                fillMap();
                sum += score;

                score = bfs(map);
            }
            answer[t] = sum;
        }

        for (int n : answer) {
            if (n == 0)
                break;
            System.out.print(n + " ");
        }

    }
    // 배열 회전
    private static void rotateMap(int x, int y, int dir) {
        newMap = new int[5][5];

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                newMap[i][j] = map[i][j];
            }
        }

        int startX = x - 1;
        int startY = y - 1;
        int[][] temp = new int[3][3];

        // 원본 3x3 부분 복사
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                temp[i][j] = map[startX + i][startY + j];
            }
        }

        // 회전 처리
        for (int r = 0; r < dir; r++) {
            int[][] newTemp = new int[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    newTemp[j][2 - i] = temp[i][j]; // 90도 회전
                }
            }
            temp = newTemp;
        }

        // 회전된 데이터를 결과 배열에 적용
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                newMap[startX + i][startY + j] = temp[i][j];
            }
        }
    }


    // bfs
    private static int bfs(int[][] arr) {
        boolean[][] visited = new boolean[5][5];
        Queue<Point> queue = new LinkedList<>();

        // 사라지는 유물을 저장합니다.
        remove = new PriorityQueue<>();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (!visited[i][j]) {
                    queue.offer(new Point(i, j));
                    visited[i][j] = true;

                    ArrayList<Point> list = new ArrayList<>();

                    int cnt = 1; // 같은숫자
                    list.add(new Point(i, j));

                    while (!queue.isEmpty()) {
                        Point curr = queue.poll();

                        for (int d = 0; d < 4; d++) {
                            int nx = curr.x + dx[d];
                            int ny = curr.y + dy[d];

                            if (!isRange(nx, ny) || visited[nx][ny])
                                continue;

                            // 동일한 유물조각이라면
                            if (arr[nx][ny] == arr[i][j]) {
                                queue.add(new Point(nx, ny));
                                visited[nx][ny] = true;

                                cnt++;
                                list.add(new Point(nx, ny));
                            }
                        }
                    }
                    // 3조각 이상 모였다면? 삭제
                    if (cnt >= 3) {
                        remove.addAll(list);
                    }
                }
            }
        }
        return remove.size();
    }

    // 채워넣음
    private static void fillMap() {
        while (!remove.isEmpty()) {
            Point curr = remove.poll();
            map[curr.x][curr.y] = wallNumber.poll();
        }
    }

    // 범위 내인가
    private static boolean isRange(int x, int y) {
        return x >= 0 && x < 5 && y >= 0 && y < 5;
    }

    // 객체 1
    static class Point implements Comparable<Point> {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
        @Override
        public int compareTo(Point o) {
            if (this.y == o.y)
                return o.x - this.x; // 행을 기준으로 내림차순
            return this.y - o.y; // 열을 기준으로 오름차순
        }
    }

    static class Node implements Comparable<Node> {
        int x;
        int y;
        int roundType;
        int score;
        public Node (int x, int y, int roundType, int score) {
            this.x = x;
            this.y = y;
            this.roundType = roundType;
            this.score = score;
        }
        @Override
        public int compareTo(Node o) {
            if (this.score == o.score) {
                if (this.roundType == o.roundType) {
                    if (this.y == o.y) {
                        return this.x - o.x; // 오름차순
                    }
                    return this.y - o.y; // 열 기준 오름차순
                }
                return this.roundType - o.roundType; // 각도 기준 오름차순
            }
            return o.score - this.score; // 점수 기준 내림차순
        }
    }
}