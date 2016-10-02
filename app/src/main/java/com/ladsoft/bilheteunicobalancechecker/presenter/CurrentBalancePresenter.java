package com.ladsoft.bilheteunicobalancechecker.presenter;

import android.os.Handler;

import com.ladsoft.bilheteunicobalancechecker.model.TransurcBilheteUnicoQueryParameter;
import com.ladsoft.bilheteunicobalancechecker.task.TaskCallback;
import com.ladsoft.bilheteunicobalancechecker.task.TransurcTask;

public class CurrentBalancePresenter implements BalancePresenter {

    private TransurcTask workerThread;

    public CurrentBalancePresenter(Handler uiHandler, TaskCallback callback) {
        workerThread = new TransurcTask(uiHandler, callback);
        workerThread.start();
        workerThread.prepareHandler();
    }

    @Override
    public void getCurrentBalance(String cardId, String date) {
        workerThread.post(new TransurcBilheteUnicoQueryParameter(cardId, date));

    }
}
