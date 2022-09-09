package de.mindcollaps.yuki.util;

public class YukiUtil {

    public static String[] removeFromArray(String[] oldArray, String id) {
        if(oldArray.length == 0)
            return oldArray;

        String[] newArr = new String[oldArray.length - 1];
        if(newArr.length == 0)
            return newArr;

        int skipIndex = -1;
        for (int i = 0; i < oldArray.length; i++)
            if (oldArray[i].equalsIgnoreCase(id))
                skipIndex = i;

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
        for (int i = 0; i < oldArray.length; i++)
            newArr[i] = oldArray[i];

        newArr[oldArray.length] = id;

        return newArr;
    }

    public static boolean isEmpty(String s){
        if(s == null)
            return true;
        if(s.length() == 0)
            return true;
        return false;
    }
}
