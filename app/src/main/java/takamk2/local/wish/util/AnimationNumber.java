package takamk2.local.wish.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by takamk2 on 16/12/16.
 * <p>
 * The Edit Fragment of Base Class.
 */

public class AnimationNumber extends Thread {

    private final long mOldPrice;
    private final long mNewPrice;
    private final int mCount;
    private final int mTime;
    private final CallBack mCallBack;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Long num = (Long) msg.obj;
            mCallBack.onCall(num);
        }
    };

    public AnimationNumber(long oldPrice, long newPrice, int count, int time, CallBack callback) {
        mOldPrice = oldPrice;
        mNewPrice = newPrice;
        mCount = count;
        mTime = time;
        mCallBack = callback;
        start();
    }

    public AnimationNumber(long oldPrice, long newPrice, CallBack callback) {
        this(oldPrice, newPrice, 10, 20, callback);
    }

    @Override
    public void run() {
        Long num = 0L;
        for (int i = 0; i < mCount; i++) {
            try {
                Thread.sleep(mTime);
                Message msg = Message.obtain();
                num = mOldPrice + (((mNewPrice - mOldPrice) / mCount) * (i + 1));
                if (i == mCount - 1) num = mNewPrice;
                msg.obj = num;
                mHandler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public interface CallBack {
        void onCall(Long num);
    }
}
