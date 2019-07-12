package priv.hypo.chess.util;

/**
 * Created by Administrator on 2019/7/11.
 */
public class ConvertUtil {

    /**
     * 链接byte数组
     * @param arrs
     * @return
     */
    public static byte[] joinBytes(byte[]... arrs) {
        if (arrs == null) {
            return null;
        }
        int length = 0;
        for (byte[] arr : arrs) {
            if (arr == null) {
                continue;
            }
            length += arr.length;
        }
        byte[] bytes = new byte[length];
        int offset = 0;
        for (byte[] arr : arrs) {
            if (arr == null) {
                continue;
            }
            System.arraycopy(arr, 0, bytes, offset, arr.length);
            offset += arr.length;
        }
        return bytes;
    }

    /**
     * 将int转换为byte数组
     * @param num
     * @return
     */
    public static byte[] toBytes(int num) {
        byte[] data = new byte[4];
        data[0] = (byte) (num >> 24);
        data[1] = (byte) (num >> 16);
        data[2] = (byte) (num >> 8);
        data[3] = (byte) num;
        return data;
    }

    /**
     * 将byte数组转换为int
     * @param data
     * @return
     */
    public static int toInt(byte[] data) {
        if (data == null || data.length < 4) {
            return 0;
        }
        return data[3] | data[2] << 8 | data[1] << 16 | data[0] << 24;
    }

}
