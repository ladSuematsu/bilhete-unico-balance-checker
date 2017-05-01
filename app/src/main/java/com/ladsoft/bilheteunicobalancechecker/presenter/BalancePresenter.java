package com.ladsoft.bilheteunicobalancechecker.presenter;


public interface BalancePresenter {
    String CARD_ID_MASK = "##.##.########-##";
    String DATE_MASK = "##/##/####";
    String CARD_ID_REGEX = "[0-9]{2}\\.[0-9]{2}\\.[0-9]{8}\\-[0-9]{1,2}";
    String DATE_FORMAT = "dd/MM/yyyy";

    void getCurrentBalance(String cardId, String date);

}
