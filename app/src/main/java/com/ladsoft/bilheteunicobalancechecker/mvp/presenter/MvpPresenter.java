package com.ladsoft.bilheteunicobalancechecker.mvp.presenter;

import com.ladsoft.bilheteunicobalancechecker.mvp.Mvp;

import java.lang.ref.WeakReference;

public abstract class MvpPresenter<V> implements Mvp.Presenter<V> {

    private WeakReference<V> view;

    @Override
    public void attachView(V view) {
        this.view = new WeakReference<>(view);
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public boolean isViewAttached() {
        return this.view != null && this.view.get()!= null;
    }

    @Override
    public V getView() {
        return view.get();
    }

}
