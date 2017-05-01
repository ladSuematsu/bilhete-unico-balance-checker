package com.ladsoft.bilheteunicobalancechecker.mvp;


import com.ladsoft.bilheteunicobalancechecker.model.BilheteUnicoInfo;
import com.ladsoft.bilheteunicobalancechecker.model.TransurcBilheteUnicoQueryParameter;
import com.ladsoft.bilheteunicobalancechecker.mvp.presenter.MvpPresenter;

public interface BilheteUnicoCheckerMvp {
    String CARD_ID_MASK = "##.##.########-##";
    String DATE_MASK = "##/##/####";
    String CARD_ID_REGEX = "[0-9]{2}\\.[0-9]{2}\\.[0-9]{8}\\-[0-9]{1,2}";
    String DATE_FORMAT = "dd/MM/yyyy";

    interface View {
        void onInvalidCardId();
        void onInvalidBirthdate();
        void refreshBalance(BilheteUnicoInfo info);
    }

    interface Presenter extends Mvp.Presenter<BilheteUnicoCheckerMvp.View> {
        void getCurrentBalance(String cardId, String date);
    }

    interface Model {
        void getCurrentBalance(TransurcBilheteUnicoQueryParameter parameters);
    }
}
