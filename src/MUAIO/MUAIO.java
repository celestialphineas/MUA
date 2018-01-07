package MUAIO;

import MUABackEnd.MUAObjects.MUAObject;
import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;

import java.io.*;
import java.util.Scanner;

public class MUAIO {
    public Scanner in;
    public PrintStream out;
    public PrintStream err;
    public String getFile(String filename) {
        StringBuilder stringBuilder = new StringBuilder();
        // TODO
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (FileNotFoundException e) {
            MUAErrorMessage.error(ErrorStringResource.mua_io,
                    ErrorStringResource.file_not_found,
                    filename);
            return null;
        } catch (IOException e) {
            MUAErrorMessage.error(ErrorStringResource.mua_io,
                    ErrorStringResource.file_reading_error,
                    filename);
            return null;
        }
    }

    public void saveFile(String data, String filename) {
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(filename, "UTF-8");
        } catch (FileNotFoundException e) {
            MUAErrorMessage.error(ErrorStringResource.mua_io,
                    ErrorStringResource.file_not_found,
                    filename);
        } catch (UnsupportedEncodingException e) {
            MUAErrorMessage.error(ErrorStringResource.mua_io,
                    ErrorStringResource.unsupported_encoding,
                    filename);
        }
        printWriter.print(data);
        printWriter.close();
    }
    private static MUAIO instance = new MUAIO();
    public static MUAIO getInstance() { return instance; }
    private MUAIO() {
        in  = new Scanner(System.in);
        out = System.out;
        err = System.err;
    }
}
