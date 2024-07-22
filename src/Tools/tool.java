package Tools;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Random;

public class tool {



    public static long bytesToLong(byte[] bytes) {
        long num = 0;
        for (int ix = 0; ix < 8; ++ix) {
            num <<= 8;
            num |= (bytes[ix] & 0xff);
        }
        return num;
    }

    public static byte[] longToBytes(long num) {
        byte[] byteNum = new byte[8];
        for (int ix = 0; ix < 8; ++ix) {
            int offset = 64 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((num >> offset) & 0xff);
        }
        return byteNum;
    }

    public static boolean Xor_Empty(byte[] xor) {
        for (int i = 0; i < xor.length; i++) {
            if (xor[i] == 0) {
                continue;
            } else
                return false;
        }
        return true;
    }


    public static byte[] Xor(byte[] x, byte[] y) {
        int min =0;
        if(x.length>y.length){
            min = y.length;
        }else{
            min = x.length;
        }
        byte[] temp = new byte[min];
        for (int i = 0; i < min; i++) {
            byte xx = x[i];
            byte yy = y[i];
            temp[i] = (byte) (x[i] ^ y[i]);
        }
        return temp;
    }




    public static int[] TtS(int inNum, int index, int level) {
        int[] result = new int[level];
        int i = 0;
        while (i < level) {
            result[i] = (inNum % index);
            inNum = inNum / index;
            i++;
        }
        return result;
    }



}