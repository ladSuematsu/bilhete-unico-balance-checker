package com.ladsoft.bilheteunicobalancechecker.presenter;

import android.os.Handler;

import com.ladsoft.bilheteunicobalancechecker.model.TransurcBilheteUnicoQueryParameter;
import com.ladsoft.bilheteunicobalancechecker.task.TaskCallback;
import com.ladsoft.bilheteunicobalancechecker.task.TransurcTask;

public class CurrentBalancePresenter implements BalancePresenter {

    private final CurrentBalancePresenterCallback callback;
    private TransurcTask workerThread;

    public CurrentBalancePresenter(Handler uiHandler, CurrentBalancePresenterCallback  callback) {
        this.callback = callback;
        workerThread = new TransurcTask(uiHandler, callback);
        workerThread.start();
        workerThread.prepareHandler();
    }

    @Override
    public void getCurrentBalance(String cardId, String date) {
        // Field validations
        if(validateFields(cardId, date)) {
            workerThread.post(new TransurcBilheteUnicoQueryParameter(cardId, date));

        }
    }

    public boolean validateFields(String cardId, String date) {
        boolean isValid = true;

        if(cardId.isEmpty()) {
            isValid = false;
            callback.onInvalidCardId();
        }

        if(date.isEmpty()) {
            isValid = false;
            callback.onInvalidBirthdate();
        }

        return isValid;
    }

    public interface CurrentBalancePresenterCallback extends TaskCallback {
        void onInvalidCardId();
        void onInvalidBirthdate();
    }
}
