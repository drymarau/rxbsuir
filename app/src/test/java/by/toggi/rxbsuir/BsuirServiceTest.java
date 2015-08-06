package by.toggi.rxbsuir;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import by.toggi.rxbsuir.rest.BsuirService;
import by.toggi.rxbsuir.rest.model.Employee;
import by.toggi.rxbsuir.rest.model.StudentGroup;
import retrofit.RestAdapter;
import retrofit.converter.SimpleXMLConverter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class BsuirServiceTest {

    private static final String RESPONSE_EMPLOYEE_LIST = "<employeeXmlModels><employee><academicDepartment>МНЭ</academicDepartment><firstName>Игорь</firstName><id>500434</id><lastName>Абрамов</lastName><middleName>Иванович</middleName></employee><employee><academicDepartment>ВМ</academicDepartment><firstName>Татьяна</firstName><id>504576</id><lastName>Автушко</lastName><middleName>Сергеевна</middleName></employee><employee><academicDepartment>Электрон.</academicDepartment><firstName>Вадим</firstName><id>504367</id><lastName>Адамович</lastName><middleName>Евгеньевич</middleName></employee></employeeXmlModels>";
    private static final String RESPONSE_STUDENT_GROUP_LIST = "<studentGroupXmlModels><studentGroup><id>21411</id><name>560801</name><course>0</course><facultyId>20040</facultyId><specialityDepartmentEducationFormId>20103</specialityDepartmentEducationFormId></studentGroup><studentGroup><id>20169</id><name>212602</name><course>2</course><facultyId>20017</facultyId><specialityDepartmentEducationFormId>20013</specialityDepartmentEducationFormId></studentGroup><studentGroup><id>20079</id><name>110201</name><course>5</course><facultyId>20017</facultyId><specialityDepartmentEducationFormId>20014</specialityDepartmentEducationFormId></studentGroup></studentGroupXmlModels>";
    private MockWebServer mServer;
    private BsuirService mService;

    @Before
    public void setup() throws IOException {
        mServer = new MockWebServer();
        mServer.start();
        mService = new RestAdapter.Builder()
                .setConverter(new SimpleXMLConverter())
                .setEndpoint(mServer.getUrl("/").toString())
                .build()
                .create(BsuirService.class);
    }

    @Test
    public void employeeListParseTest() {
        mServer.enqueue(new MockResponse().setBody(RESPONSE_EMPLOYEE_LIST));
        List<Employee> employeeList = mService.getEmployees()
                .toBlocking()
                .first()
                .employeeList;
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).hasSize(3);
        assertThat(employeeList).extracting("id", "firstName", "lastName").contains(
                tuple(500434, "Игорь", "Абрамов"),
                tuple(504576, "Татьяна", "Автушко"),
                tuple(504367, "Вадим", "Адамович")
        );
    }

    @Test
    public void groupListParseTest() {
        mServer.enqueue(new MockResponse().setBody(RESPONSE_STUDENT_GROUP_LIST));
        List<StudentGroup> studentGroupList = mService.getStudentGroups()
                .toBlocking()
                .first()
                .studentGroupList;
        assertThat(studentGroupList).isNotNull();
        assertThat(studentGroupList).hasSize(3);
        assertThat(studentGroupList).extracting("id", "name", "course").contains(
                tuple(21411, "560801", 0),
                tuple(20169, "212602", 2),
                tuple(20079, "110201", 5)
        );
    }

    @After
    public void tearDown() throws IOException {
        mServer.shutdown();
    }

}
