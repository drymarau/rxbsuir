package by.toggi.rxbsuir.mvp.view;

import java.util.Map;
import java.util.Set;

import by.toggi.rxbsuir.mvp.View;
import by.toggi.rxbsuir.rest.model.Employee;

public interface NavigationDrawerView extends View {

    void updateGroupList(Map<Integer, String> groupMap);

    void updateEmployeeList(Map<Integer, String> employeeMap);

}
