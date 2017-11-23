package MUAMessageUtil;

public class MUAErrorMessage {
    private static String splitter = "%%";
    public static void error(ErrorStringResource where, ErrorStringResource what, String why) {
        String[] splitted = what.getResourceVal().split(splitter);
        String msg = new String();
        if(splitted.length >= 1) {
            msg = splitted[0] + why;
        }
        if(splitted.length >= 2) {
            msg = msg + splitted[1];
        }
        System.err.println("[" + where.getResourceVal() + " ERROR] " + msg);
    }
    public static void warn(ErrorStringResource where, ErrorStringResource what, String why) {
        String[] splitted = what.getResourceVal().split(splitter);
        String msg = new String();
        if(splitted.length >= 1) {
            msg = splitted[0] + why;
        }
        if(splitted.length >= 2) {
            msg = msg + splitted[1];
        }
        System.err.println("[" + where.getResourceVal() + " WARNING] " + msg);
    }
}
