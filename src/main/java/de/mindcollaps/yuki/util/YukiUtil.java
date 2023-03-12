package de.mindcollaps.yuki.util;

import java.lang.reflect.Array;

public class YukiUtil {

    public static String[] removeFromArray(String[] oldArray, String id) {
        if (oldArray.length == 0)
            return oldArray;

        if(oldArray.length == 1)
            return new String[0];

        String[] newArr = new String[oldArray.length - 1];

        int skipIndex = -1;
        for (int i = 0; i < oldArray.length; i++)
            if (oldArray[i].equalsIgnoreCase(id))
                skipIndex = i;

        if(skipIndex == -1)
            return oldArray;

        int j = 0;
        for (int i = 0; i < newArr.length; i++) {
            if (j != skipIndex)
                newArr[i] = oldArray[j];
            else
                i--;
            j++;
        }

        newArr[oldArray.length] = id;

        return newArr;
    }

    public static String[] addToArray(String[] oldArray, String id) {
        String[] newArr = new String[oldArray.length + 1];
        System.arraycopy(oldArray, 0, newArr, 0, oldArray.length);

        newArr[oldArray.length] = id;

        return newArr;
    }

    public static <T> T[] addToArray(T[] oldArray, T add, Class<T> clazz) {
        T[] newArr = (T[]) Array.newInstance(clazz, oldArray.length + 1);
        System.arraycopy(oldArray, 0, newArr, 0, oldArray.length);

        newArr[oldArray.length] = add;

        return newArr;
    }

    public static boolean isEmpty(String s) {
        if (s == null)
            return true;
        return s.length() == 0;
    }
}
