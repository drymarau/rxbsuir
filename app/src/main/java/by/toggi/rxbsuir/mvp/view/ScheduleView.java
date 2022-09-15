package by.toggi.rxbsuir.mvp.view;

import static by.toggi.rxbsuir.mvp.presenter.SchedulePresenter.Error;

import by.toggi.rxbsuir.mvp.View;

public interface ScheduleView extends View {

    void showError(Error error);

    void showLoading();

    void showContent();

}
