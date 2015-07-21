package by.toggi.rxbsuir.mvp.view;

import java.util.List;

import by.toggi.rxbsuir.mvp.View;

public interface ScheduleView extends View {

    void showError(Throwable throwable);

    void showLoading();

    void finishRefresh();

    void updateStudentGroupList(List<String> studentGroupList);

}