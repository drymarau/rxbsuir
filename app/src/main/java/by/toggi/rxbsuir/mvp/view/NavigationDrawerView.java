package by.toggi.rxbsuir.mvp.view;

import java.util.Map;

import by.toggi.rxbsuir.mvp.View;

public interface NavigationDrawerView extends View {

    void updateGroupList(Map<Integer, String> groupMap);

    void updateEmployeeList(Map<Integer, String> employeeMap);

}
