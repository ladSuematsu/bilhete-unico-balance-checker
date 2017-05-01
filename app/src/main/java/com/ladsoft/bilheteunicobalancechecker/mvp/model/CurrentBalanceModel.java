package com.ladsoft.bilheteunicobalancechecker.mvp.model;


import android.os.Handler;

import com.ladsoft.bilheteunicobalancechecker.model.TransurcBilheteUnicoQueryParameter;
import com.ladsoft.bilheteunicobalancechecker.mvp.BilheteUnicoCheckerMvp;
import com.ladsoft.bilheteunicobalancechecker.task.TaskCallback;
import com.ladsoft.bilheteunicobalancechecker.task.TransurcTask;

public class CurrentBalanceModel implements BilheteUnicoCheckerMvp.Model {

    private TransurcTask workerThread;
    public CurrentBalanceModel(Handler handler, TaskCallback callback) {
        workerThread = new TransurcTask(handler, callback);
        workerThread.start();
        workerThread.prepareHandler();
    }

    @Override
    protected void finalize() throws Throwable {
        workerThread.quit();
        super.finalize();
    }

    @Override
    public void getCurrentBalance(TransurcBilheteUnicoQueryParameter parameters) {
        workerThread.post(parameters);
    }
}
