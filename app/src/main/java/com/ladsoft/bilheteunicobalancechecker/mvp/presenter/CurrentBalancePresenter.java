package com.ladsoft.bilheteunicobalancechecker.mvp.presenter;

import android.os.Handler;

import com.ladsoft.bilheteunicobalancechecker.model.BilheteUnicoInfo;
import com.ladsoft.bilheteunicobalancechecker.model.TransurcBilheteUnicoQueryParameter;
import com.ladsoft.bilheteunicobalancechecker.mvp.BilheteUnicoCheckerMvp;
import com.ladsoft.bilheteunicobalancechecker.mvp.model.CurrentBalanceModel;
import com.ladsoft.bilheteunicobalancechecker.task.TaskCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static com.ladsoft.bilheteunicobalancechecker.mvp.BilheteUnicoCheckerMvp.CARD_ID_REGEX;
import static com.ladsoft.bilheteunicobalancechecker.mvp.BilheteUnicoCheckerMvp.DATE_FORMAT;

public class CurrentBalancePresenter extends MvpPresenter<BilheteUnicoCheckerMvp.View> implements
        BilheteUnicoCheckerMvp.Presenter, TaskCallback {
    private final BilheteUnicoCheckerMvp.Model model;

    public CurrentBalancePresenter(Handler handler) {
        this.model = new CurrentBalanceModel(handler, this);
    }

    @Override
    public void getCurrentBalance(String cardId, String date) {
        // Field validations
        if(validateFields(cardId, date)) {
            model.getCurrentBalance(new TransurcBilheteUnicoQueryParameter(cardId, date));
        }
    }

    public boolean validateFields(String cardId, String date) {
        boolean isValid = true;

        if(!cardId.matches(CARD_ID_REGEX)) {
            isValid = false;
            getView().onInvalidCardId();
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            sdf.setLenient(false);
            sdf.parse(date);
        } catch (ParseException e) {
            isValid = false;
            getView().onInvalidBirthdate();
        }

        return isValid;
    }

    @Override
    public void onBalanceResponse(BilheteUnicoInfo info) {
        if(isViewAttached()) {
            getView().refreshBalance(info);
        }
    }
}
