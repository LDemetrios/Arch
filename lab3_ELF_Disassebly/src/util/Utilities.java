package util;

import java.io.File;
import java.io.FileInputStream;

public class Utilities {
    public static int assertIsInt(long it) {
        if ((long) ((int) it) == it) {
            return (int) it;
        } else {
            throw new AssertionError("Value " + it + " of Long is out of Int range");
        }
    }

}
