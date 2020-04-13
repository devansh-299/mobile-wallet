package org.mifos.mobilewallet.mifospay.utils;


import android.content.Context;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import java.util.List;

public class SimCardUtil {

    public static int getSimCardNumber(Context context) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(
                    context.TELEPHONY_SERVICE);
            int sim1State = telephonyManager.getSimState(0);

            int sim2State = telephonyManager.getSimState(1);

            if (sim1State == TelephonyManager.SIM_STATE_READY &&
                    sim2State == TelephonyManager.SIM_STATE_READY) {
                return 2;
            } else if ((sim1State == TelephonyManager.SIM_STATE_ABSENT &&
                    sim2State == TelephonyManager.SIM_STATE_READY) ||
                    (sim1State == TelephonyManager.SIM_STATE_READY
                            && sim2State == TelephonyManager.SIM_STATE_READY)) {
                return 1;
            }
        }
        return 0;
    }

    public static String getNetworkOperatorName(Context context, int slotNumber) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            SubscriptionManager subscriptionManager = (SubscriptionManager)context.getSystemService(
                        Context.TELEPHONY_SUBSCRIPTION_SERVICE);
            List<SubscriptionInfo> subscriptionInfoList =
                        subscriptionManager.getActiveSubscriptionInfoList();
            if (subscriptionInfoList != null && subscriptionInfoList.size() > 0) {
                return subscriptionInfoList.get(slotNumber).getCarrierName().toString();
            }
            return null;
        }
        return null;
    }
}
