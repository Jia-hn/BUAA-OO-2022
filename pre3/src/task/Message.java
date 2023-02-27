package task;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message {
    private String total;
    private String year;
    private String month;
    private String day;
    private String sender;
    private String receiver;
    private String text;

    public Message(String total, String year, String month,
                   String day, String sender, String receiver, String text) {
        this.total = total;
        this.year = year;
        this.month = month;
        this.day = day;
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
    }

    public boolean equals1(String date) {
        Pattern pattern = Pattern.compile("(\\d*)/(\\d*)/(\\d*)");
        Matcher matcher = pattern.matcher(date);
        matcher.matches();
        return (matcher.group(1).equals("") ||
                Integer.parseInt(matcher.group(1)) == Integer.parseInt(year))
                && (matcher.group(2).equals("") ||
                Integer.parseInt(matcher.group(2)) == Integer.parseInt(month))
                && (matcher.group(3).equals("") ||
                Integer.parseInt(matcher.group(3)) == Integer.parseInt(day));
    }

    public boolean equals2(String sender) {
        return this.sender.equals(sender);
    }

    public boolean equals3(String receiver) {
        return this.receiver != null && this.receiver.trim().substring(1).equals(receiver);
    }

    public boolean equals4(char parameter1, int parameter2) {
        Pattern pattern;
        Matcher matcher;
        switch (parameter1) {
            case 'A':
                switch (parameter2) {
                    case 1:
                        pattern = Pattern.compile("^a{2,3}b{2,4}c{2,4}");
                        matcher = pattern.matcher(text);
                        return matcher.find();
                    case 2:
                        pattern = Pattern.compile("a{2,3}b{2,4}c{2,4}$");
                        matcher = pattern.matcher(text);
                        return matcher.find();
                    case 3:
                        pattern = Pattern.compile(".*?a{2,3}b{2,4}c{2,4}.*?");
                        matcher = pattern.matcher(text);
                        return matcher.find();
                    case 4:
                        pattern = Pattern.compile(".*?a.*?a.*?b.*?b.*?c.*?c.*?");
                        matcher = pattern.matcher(text);
                        return matcher.find();
                    default:
                }
                break;
            case 'B':
                switch (parameter2) {
                    case 1:
                        pattern = Pattern.compile("^a{2,3}b{2,1000000}c{2,4}");
                        matcher = pattern.matcher(text);
                        return matcher.find();
                    case 2:
                        pattern = Pattern.compile("a{2,3}b{2,1000000}c{2,4}$");
                        matcher = pattern.matcher(text);
                        return matcher.find();
                    case 3:
                        pattern = Pattern.compile(".*?a{2,3}b{2,1000000}c{2,4}.*?");
                        matcher = pattern.matcher(text);
                        return matcher.find();
                    case 4:
                        pattern = Pattern.compile(".*?a.*?a.*?b.*?b.*?c.*?c.*?");
                        matcher = pattern.matcher(text);
                        return matcher.find();
                    default:
                }
                break;
            default:
        }
        return true;
    }

    public String toString() {
        return total;
    }

}
