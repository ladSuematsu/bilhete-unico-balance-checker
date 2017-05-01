package com.ladsoft.bilheteunicobalancechecker.mvp;

public interface Mvp {

    interface Presenter<V> {
        void attachView(V view);

        void detachView();

        boolean isViewAttached();

        V getView();
    }

}