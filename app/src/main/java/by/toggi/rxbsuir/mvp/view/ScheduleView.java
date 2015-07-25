package by.toggi.rxbsuir.mvp.view;

import by.toggi.rxbsuir.mvp.View;

public interface ScheduleView extends View {

    void showError(Throwable throwable);

    void showLoading();

    void showContent(int position);

}
