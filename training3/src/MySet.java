import java.util.ArrayList;
import java.util.HashSet;

public class MySet implements IntSet {
    private ArrayList<Integer> arr;
    private int count;

    public MySet() {
        arr = new ArrayList<>();
        count = 0;
    }

    @Override
    public Boolean contains(int x) {
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i) == x) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getNum(int x) throws IndexOutOfBoundsException {
        if (x >= 0 && x < count) {
            return arr.get(x);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public void insert(int x) {
        if (count == 0) {
            arr.add(x);
            count++;
        }
        int left = 0;
        int right = count - 1;
        while (left < right) {
            int mid = (left + right) / 2;
            if (arr.get(mid) >= x) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        if (arr.get(left) < x) {
            count++;
            arr.add(x);
        } else if (arr.get(left) > x) {
            arr.add(left, x);
            count++;
        }
    }

    @Override
    public void delete(int x) {
        int left = 0;
        int right = count - 1;
        while (left < right) {
            int mid = (left + right) / 2;
            if (arr.get(mid) >= x) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        if (arr.get(left) == x) {
            arr.remove(left);
            count--;
        }
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public void elementSwap(IntSet a) {
        ArrayList<Integer> temp;
        temp = ((MySet) a).arr;
        ((MySet) a).arr = arr;
        arr = temp;
    }

    @Override
    public IntSet symmetricDifference(IntSet a) throws NullPointerException {
        if (a == null) {
            throw new NullPointerException();
        }
        HashSet<Integer> hashSet = new HashSet<>(arr);
        for (Integer integer : ((MySet) a).arr) {
            if (hashSet.contains(integer)) {
                hashSet.remove(integer);
            } else {
                hashSet.add(integer);
            }
        }
        IntSet intSet = new MySet();
        for (Integer integer : hashSet) {
            intSet.insert(integer);
        }
        return intSet;
    }

    @Override
    public boolean repOK() {
        for (int i = 0; i < arr.size(); i++) {
            for (int j = i + 1; j < arr.size(); j++) {
                if (arr.get(i).compareTo(arr.get(j)) >= 0) {
                    return false;
                }
            }
        }
        return true;
    }
}
