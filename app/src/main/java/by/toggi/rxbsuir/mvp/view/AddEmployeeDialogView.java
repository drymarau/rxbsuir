package by.toggi.rxbsuir.mvp.view;

import java.util.List;

import by.toggi.rxbsuir.mvp.View;
import by.toggi.rxbsuir.rest.model.Employee;

public interface AddEmployeeDialogView extends View {

    void updateEmployeeList(List<Employee> employeeList);

}
