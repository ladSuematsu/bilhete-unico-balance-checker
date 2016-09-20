package com.ladsoft.bilheteunicobalancechecker.presenter;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.ladsoft.bilheteunicobalancechecker.model.BilheteUnicoInfo;

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
            void onBalanceResponse(BilheteUnicoInfo info);
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

        private static final String CARD_NUMBER_FIELD = "#ContentPlaceHolder1_lblNumCartao";
        private static final String USER_NAME_FIELD = "#ContentPlaceHolder1_lblNomeUsuario";
        private static final String STATUS_VALUE_FIELD = "#ContentPlaceHolder1_lblStatusCartao";
        private static final String COMMON_PASS_BALANCE_VALUE_FIELD = "#ContentPlaceHolder1_lblSaldoAtual_CO";
        private static final String COMMON_PASS_BALANCE_DATE = "#ContentPlaceHolder1_lblDtSaldo_CO";
        private static final String VT_BALANCE_VALUE_FIELD = "#ContentPlaceHolder1_lblSaldoAtual_VT";
        private static final String VT_BALANCE_DATE = "#ContentPlaceHolder1_lblDtSaldo_VT";
        private void getCurrentBalance() {
            try {
                Document document = Jsoup.connect("http://www.transurc.com.br/SiteApp/Movel/Saldo/ResultadoSaldoMovel.aspx?d=21|04|01079579|1|15/02/1988").get();

                Elements fields = new Elements();
                fields.addAll(document.select(CARD_NUMBER_FIELD));
                fields.addAll(document.select(USER_NAME_FIELD));
                fields.addAll(document.select(STATUS_VALUE_FIELD));
                fields.addAll(document.select(COMMON_PASS_BALANCE_VALUE_FIELD));
                fields.addAll(document.select(COMMON_PASS_BALANCE_DATE));
                fields.addAll(document.select(VT_BALANCE_VALUE_FIELD));
                fields.addAll(document.select(VT_BALANCE_DATE));

                String cardNumber = fields.select(CARD_NUMBER_FIELD).isEmpty() ? "" : fields.select(CARD_NUMBER_FIELD).get(0).text();
                String userName = fields.select(USER_NAME_FIELD).isEmpty() ? "" : fields.select(USER_NAME_FIELD).get(0).text();
                String status = fields.select(STATUS_VALUE_FIELD).isEmpty() ? "" : fields.select(STATUS_VALUE_FIELD).get(0).text();
                float commonPassBalanceValue = fields.select(COMMON_PASS_BALANCE_VALUE_FIELD).isEmpty() ? 0.0F : Float.valueOf(fields.select(COMMON_PASS_BALANCE_VALUE_FIELD).get(0).text().substring(3).replace(',', '.'));
                float vtBalanceValue = fields.select(VT_BALANCE_VALUE_FIELD).isEmpty() ? 0.0F : Float.valueOf(fields.select(VT_BALANCE_VALUE_FIELD).get(0).text().substring(3).replace(',', '.'));

                final BilheteUnicoInfo info = new BilheteUnicoInfo(cardNumber, userName, status, vtBalanceValue, commonPassBalanceValue);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onBalanceResponse(info);
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
