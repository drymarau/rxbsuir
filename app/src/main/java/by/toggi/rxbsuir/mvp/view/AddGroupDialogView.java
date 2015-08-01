package by.toggi.rxbsuir.mvp.view;

import java.util.List;

import by.toggi.rxbsuir.mvp.View;
import by.toggi.rxbsuir.mvp.presenter.SchedulePresenter;

public interface AddGroupDialogView extends View {

    void updateStudentGroupList(List<String> studentGroupList);

    void showError(SchedulePresenter.Error error);

}
