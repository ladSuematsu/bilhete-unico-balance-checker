package com.ladsoft.bilheteunicobalancechecker.task;

import com.ladsoft.bilheteunicobalancechecker.model.BilheteUnicoInfo;

/**
 * Created by suematsu on 9/24/16.
 */

public interface TaskCallback {
    void onBalanceResponse(BilheteUnicoInfo info);
}
