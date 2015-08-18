package by.toggi.rxbsuir.mvp.view;

import by.toggi.rxbsuir.mvp.View;

import static by.toggi.rxbsuir.mvp.presenter.SchedulePresenter.Error;

public interface ScheduleView extends View {

    void showError(Error error);

    void showLoading();

    void showContent();

}
