package task;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] arg) {
        ArrayList<Message> messages = new ArrayList<>();
        Pattern pattern = Pattern.compile(
                "(\\d+)/(\\d+)/(\\d+)-([a-zA-Z0-9]+)(@[a-zA-Z0-9]+ )?" +
                        ":\"([a-zA-Z0-9 ?!,.]*(@[a-zA-Z0-9]+ )?[a-zA-Z0-9 ?!,.]*)\";");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String string = scanner.nextLine();
            if (string.equals("END_OF_MESSAGE")) {
                break;
            }
            Matcher matcher = pattern.matcher(string);
            while (matcher.find()) {
                messages.add(new Message(matcher.group(0), matcher.group(1),
                        matcher.group(2), matcher.group(3), matcher.group(4),
                        (matcher.group(5) != null) ? matcher.group(5) : matcher.group(7),
                        matcher.group(6)));
            }
        }
        while (scanner.hasNext()) {
            String string = scanner.next();
            switch (string) {
                case "qdate":
                    String date = scanner.next();
                    for (Message message : messages) {
                        if (message.equals1(date)) {
                            System.out.println(message);
                        }
                    }
                    break;
                case "qsend":
                    String sender = scanner.next();
                    for (Message message : messages) {
                        if (message.equals2(sender)) {
                            System.out.println(message);
                        }
                    }
                    break;
                case "qrecv":
                    String receiver = scanner.next();
                    for (Message message : messages) {
                        if (message.equals3(receiver)) {
                            System.out.println(message);
                        }
                    }
                    break;
                case "qmess":
                    char parameter1 = scanner.next().charAt(0);
                    int parameter2 = scanner.nextInt();
                    for (Message message : messages) {
                        if (message.equals4(parameter1,parameter2)) {
                            System.out.println(message);
                        }
                    }
                    break;
                default:
            }
        }
    }
}