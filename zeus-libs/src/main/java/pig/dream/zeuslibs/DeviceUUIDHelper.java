package pig.dream.zeuslibs;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * @author zhukun on 2017/5/2.
 */

public class DeviceUUIDHelper {

    public static UUID uuid;

    public static String UUID(Context context) {
        if (uuid == null) {
            synchronized (DeviceUUIDHelper.class) {
                if (uuid == null) {
                    String id = (String) SPHelper.get(context, DeviceUUIDSP.FILE_NAME, DeviceUUIDSP.DEVICE_ID_KEY, "");
                    if (TextUtils.isEmpty(id)) {
                        final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                        try {
                            if (!"9774d56d682e549c".equals(androidId)) {
                                uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                            } else {
                                Object object = context.getSystemService(Context.TELEPHONY_SERVICE);
                                if (object instanceof TelephonyManager) {
                                    final String deviceId = ((TelephonyManager) object).getDeviceId();
                                    uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();
                                } else {
                                    uuid = UUID.randomUUID();
                                }
                            }
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException();
                        }
                        SPHelper.put(context, DeviceUUIDSP.FILE_NAME, DeviceUUIDSP.DEVICE_ID_KEY, uuid.toString());
                    } else {
                        uuid = UUID.fromString(id);
                    }

                }
            }
        }

        return uuid.toString();
    }


    private static class DeviceUUIDSP {
        private static final String FILE_NAME = "device_id.xml";
        private static final String DEVICE_ID_KEY = "device_id";

    }
}
