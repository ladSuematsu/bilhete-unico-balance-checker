package com.ladsoft.bilheteunicobalancechecker.task;

import com.ladsoft.bilheteunicobalancechecker.model.BilheteUnicoInfo;

public interface TaskCallback {
    void onBalanceResponse(BilheteUnicoInfo info);
}
