import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        for(;;) {
            String str = scanner.nextLine();
            Pattern pattern = Pattern.compile("(.*?)\\s*make\\s*\"(.*?)\\s*\\[\\s*\\[(.*?)\\](.*)");
            Matcher matcher = pattern.matcher(str);
            if(matcher.find()) {
                String[] makeArgs = matcher.group(3).split(" ");
                for(int i = 0; i < makeArgs.length; i++)
                    if(makeArgs[i].charAt(0) != '\"')
                        makeArgs[i] = "\"" + makeArgs[i];
                StringBuilder thirdPart = new StringBuilder();
                for(String arg : makeArgs) {
                    thirdPart.append(arg + " ");
                }
                str = matcher.group(1) + " make \"" + matcher.group(2) + " [[ " + thirdPart + " ] " + matcher.group(4);
            }
        }
    }
}
