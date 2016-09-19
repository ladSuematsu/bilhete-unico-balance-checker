package com.ladsoft.bilheteunicobalancechecker.presenter;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class CurrentBalancePresenter implements BalancePresenter {

    private WorkerThread workerThread;

    public CurrentBalancePresenter(Handler uiHandler, WorkerThread.Callback callback) {
        workerThread = new WorkerThread(uiHandler, callback);
        workerThread.start();
        workerThread.prepareHandler();
    }

    @Override
    public void getCurrentBalance() {
        workerThread.post();
    }


    public static class WorkerThread extends HandlerThread{
        public interface Callback {
            void onBalanceResponse(String value);
        }


        private static final String TAG = WorkerThread.class.getSimpleName();
        private final Handler handler;
        private final Callback callback;
        private Handler workerHandler;

        public WorkerThread(Handler handler, Callback callback) {
            super(TAG);
            this.handler = handler;
            this.callback = callback;
        }

        protected void prepareHandler() {
            workerHandler = new Handler(getLooper(), new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    getCurrentBalance();
                    return true;
                }
            });
        }

        protected void post() {
            workerHandler.obtainMessage().sendToTarget();
        }

        private static final String BALANCE_VALUE_FIELD = "#ContentPlaceHolder1_lblSaldoAtual_CO";
        private static final String STATUS_VALUE_FIELD = "#ContentPlaceHolder1_lblStatusCartao";
        private void getCurrentBalance() {
            try {
                Document document = Jsoup.connect("http://www.transurc.com.br/SiteApp/Movel/Saldo/ResultadoSaldoMovel.aspx?d=21|04|01079579|1|15/02/1988").get();

                Elements balanceValueField = document.select(BALANCE_VALUE_FIELD);
                Elements statusValueField = document.select(STATUS_VALUE_FIELD);

                final StringBuilder value = new StringBuilder();
                value.append(balanceValueField.isEmpty() ? "0" : balanceValueField.get(0).text())
                .append(", ").append(statusValueField.isEmpty() ? "N/A" : statusValueField.get(0).text());

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onBalanceResponse(value.toString());
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
