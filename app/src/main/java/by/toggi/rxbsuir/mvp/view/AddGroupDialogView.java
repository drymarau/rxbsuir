package by.toggi.rxbsuir.mvp.view;

import java.util.List;

import by.toggi.rxbsuir.mvp.View;
import by.toggi.rxbsuir.mvp.presenter.SchedulePresenter;
import by.toggi.rxbsuir.rest.model.StudentGroup;

public interface AddGroupDialogView extends View {

    void updateStudentGroupList(List<StudentGroup> studentGroupList);

    void showError(SchedulePresenter.Error error);

}
