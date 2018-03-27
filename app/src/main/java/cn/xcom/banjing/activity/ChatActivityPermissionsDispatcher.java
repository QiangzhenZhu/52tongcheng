package cn.xcom.banjing.activity;

import android.support.v4.app.ActivityCompat;

import permissions.dispatcher.PermissionUtils;

/**
 * Created by mac on 2017/10/3.
 */

final class ChatActivityPermissionsDispatcher {
    private static final int REQUEST_SHOWVOICE = 0;

    private static final String[] PERMISSION_SHOWVOICE = new String[] {"android.permission.RECORD_AUDIO","android.permission.READ_EXTERNAL_STORAGE","android.permission.WRITE_EXTERNAL_STORAGE"};

    private ChatActivityPermissionsDispatcher() {
    }

    static void showVoiceWithCheck(ChatActivity target) {
        if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWVOICE)) {
            target.showVoice();
        } else {
            ActivityCompat.requestPermissions(target, PERMISSION_SHOWVOICE, REQUEST_SHOWVOICE);
        }
    }

    static void onRequestPermissionsResult(ChatActivity target, int requestCode, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_SHOWVOICE:
                if (PermissionUtils.getTargetSdkVersion(target) < 23 && !PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWVOICE)) {
                    return;
                }
                if (PermissionUtils.verifyPermissions(grantResults)) {
                    target.showVoice();
                }
                break;
            default:
                break;
        }
    }
}

