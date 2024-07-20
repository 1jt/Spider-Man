package Tools;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Hash {

    private static Random random = new Random();

    // 64-bit hash function
    public static long hash64(long x, long seed) {
        x += seed;
        x = (x ^ (x >>> 33)) * 0xff51afd7ed558ccdL;
        x = (x ^ (x >>> 33)) * 0xc4ceb9fe1a85ec53L;
        x = x ^ (x >>> 33);
        return x;
    }

    // reduce hash value to [0,n)
    public static int reduce(int hash, int n) {
        // https://lemire.me/blog/2016/06/27/a-fast-alternative-to-the-modulo-reduction/
        return (int) (((hash & 0xffffffffL) * n) >>> 32);
    }

    public static byte[]  Get_SHA_256(byte[] passwordToHash) {
        try {
            // Create MessageDigest instance for Sha-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            //Add password bytes to digest
            md.update(passwordToHash);
            //Get the hash's bytes
            return md.digest();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[]  Get_MD5(byte[] passwordToHash) {
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(passwordToHash);
            //Get the hash's bytes
            return md.digest();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static byte[]  Get_Sha_128(byte[] passwordToHash) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(passwordToHash);
            byte[] bytes = md.digest();
            byte[] hash_128 = new byte[16];
            System.arraycopy(hash_128,0,bytes,0,16);
            return hash_128;
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
