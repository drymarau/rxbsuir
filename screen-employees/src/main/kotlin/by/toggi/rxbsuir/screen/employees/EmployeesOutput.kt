package by.toggi.rxbsuir.screen.employees

import by.toggi.rxbsuir.api.Employee

public sealed interface EmployeesOutput {

    @JvmInline
    public value class OnEmployee(public val employee: Employee) : EmployeesOutput

    public object OnBack : EmployeesOutput
}
