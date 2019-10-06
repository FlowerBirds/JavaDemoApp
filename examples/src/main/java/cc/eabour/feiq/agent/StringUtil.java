package cc.eabour.feiq.agent;

/**
 * Created by pigercc on 2016/6/19.
 */
public class StringUtil {

    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equalsIgnoreCase(str2);
    }
    
    public static byte[] hexTobytes(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i = i + 2) {
            String subStr = hex.substring(i, i + 2);
            boolean flag = false;
            int intH = Integer.parseInt(subStr, 16);
            if (intH > 127) flag = true;
            if (intH == 128) {
                intH = -128;
            } else if (flag) {
                intH = 0 - (intH & 0x7F);
            }
            byte b = (byte) intH;
            bytes[i / 2] = b;
        }
        return bytes;
    }
    
    public static String bytesTohex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            boolean flag = false;
            if (b < 0) flag = true;
            int absB = Math.abs(b);
            if (flag) absB = absB | 0x80;
            String tmp = Integer.toHexString(absB & 0xFF);
            if (tmp.length() == 1) { //转化的十六进制不足两位，需要补0
                hex.append("0");
            }
            hex.append(tmp.toLowerCase() + " ");
            if(i % 15 == 0) {
            	hex.append("\n");
            }
        }
        return hex.toString();
    }
}
