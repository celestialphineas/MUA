import java.util.Scanner;

public class TestMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String str = scanner.nextLine();
//            String result = str.replace("make\\s\"(?=\\[)\\[\\s\\[(?=\\[)", "declare \"$1 [$2] make \"$1 [[$2");
            String result = str.replaceAll("make\\s*\"(.*?)\\s*\\[\\s*\\[(.*?)\\]",
                    "declare \"$1 [$2] make \"$1 [[$2]");
            System.out.println(result);
        }

    }
}
