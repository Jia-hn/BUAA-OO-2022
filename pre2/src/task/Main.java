package task;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        HashMap<Integer, Adventurer> adventurers = new HashMap<>();
        int equId;
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        for (int i = 0; i < n; i++) {
            int op = in.nextInt();
            int advId = in.nextInt();
            switch (op) {
                case 1:
                    String advName = in.next();
                    adventurers.put(advId, new Adventurer(advId, advName));
                    break;
                case 2:
                    int type = in.nextInt();
                    equId = in.nextInt();
                    String equName = in.next();
                    BigInteger price = new BigInteger(String.valueOf(in.nextLong()));
                    double var1 = in.nextDouble();
                    if (type == 1 || type == 4) {
                        adventurers.get(advId).
                                addEquipment1(type, equId, equName, price, var1);
                    } else {
                        double var2 = in.nextDouble();
                        adventurers.get(advId).
                                addEquipment2(type, equId, equName, price, var1, var2);
                    }
                    break;
                case 3:
                    equId = in.nextInt();
                    adventurers.get(advId).getSubstanceOfValues().remove(equId);
                    break;
                case 4:
                    System.out.println(adventurers.get(advId).sumOfPrice());
                    break;
                case 5:
                    System.out.println(adventurers.get(advId).maxOfPrice());
                    break;
                case 6:
                    System.out.println(adventurers.get(advId).numOfSubstanceOfValue());
                    break;
                case 7:
                    equId = in.nextInt();
                    System.out.println(adventurers.get(advId).getSubstanceOfValues().get(equId));
                    break;
                case 8:
                    adventurers.get(advId).use(adventurers.get(advId));
                    break;
                case 9:
                    System.out.println(adventurers.get(advId));
                    break;
                case 10:
                    int advId2 = in.nextInt();
                    adventurers.get(advId).getSubstanceOfValues().
                            put(advId2, adventurers.get(advId2));
                    break;
                default:
            }
        }
    }
}