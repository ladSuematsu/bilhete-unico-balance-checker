package com.ladsoft.bilheteunicobalancechecker.task;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.ladsoft.bilheteunicobalancechecker.BuildConfig;
import com.ladsoft.bilheteunicobalancechecker.model.BilheteUnicoInfo;
import com.ladsoft.bilheteunicobalancechecker.model.TransurcBilheteUnicoQueryParameter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class TransurcTask extends HandlerThread {

    private static final String TAG = TransurcTask.class.getSimpleName();
    private final Handler handler;
    private final TaskCallback callback;
    private Handler workerHandler;

    public TransurcTask(Handler handler, TaskCallback callback) {
        super(TAG);
        this.handler = handler;
        this.callback = callback;
    }

    public void prepareHandler() {
        workerHandler = new Handler(getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                getCurrentBalance(message.obj);
                return true;
            }
        });
    }

    public void post(Object queryParameters) {
        Message message = workerHandler.obtainMessage();
        message.obj = queryParameters;
        message.sendToTarget();
    }


    private final static String ENDPOINT_URL = BuildConfig.TRANSURC_ENDPOINT_URL;
    private static final int DEFAULT_TIMEOUT = BuildConfig.REQUEST_TIMEOUT_SECS * 1000;

    private static final String REQUEST_PARAMETER_SEPARATOR = "|";
    private static final String CARD_NUMBER_FIELD = "#ContentPlaceHolder1_lblNumCartao";
    private static final String USER_NAME_FIELD = "#ContentPlaceHolder1_lblNomeUsuario";
    private static final String STATUS_VALUE_FIELD = "#ContentPlaceHolder1_lblStatusCartao";
    private static final String COMMON_PASS_BALANCE_VALUE_FIELD = "#ContentPlaceHolder1_lblSaldoAtual_CO";
    private static final String COMMON_PASS_BALANCE_DATE = "#ContentPlaceHolder1_lblDtSaldo_CO";
    private static final String VT_BALANCE_VALUE_FIELD = "#ContentPlaceHolder1_lblSaldoAtual_VT";
    private static final String VT_BALANCE_DATE = "#ContentPlaceHolder1_lblDtSaldo_VT";
    private void getCurrentBalance(Object queueParameters) {
        try {

            TransurcBilheteUnicoQueryParameter queueValues = (TransurcBilheteUnicoQueryParameter) queueParameters;

            String id = queueValues.getId().replaceAll("(\\.|-)+", "|");


            StringBuilder builder = new StringBuilder().append(ENDPOINT_URL)
                    .append(id).append(REQUEST_PARAMETER_SEPARATOR)
                    .append(queueValues.getBirthDate());

            Document document = Jsoup.connect(builder.toString()).timeout(DEFAULT_TIMEOUT).get();

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
