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
import by.toggi.rxbsuir.rest.model.Schedule;
import by.toggi.rxbsuir.rest.model.ScheduleModel;
import by.toggi.rxbsuir.rest.model.StudentGroup;
import retrofit.RestAdapter;
import retrofit.converter.SimpleXMLConverter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class BsuirServiceTest {

    private static final String RESPONSE_EMPLOYEE_LIST = "<employeeXmlModels><employee><academicDepartment>МНЭ</academicDepartment><firstName>Игорь</firstName><id>500434</id><lastName>Абрамов</lastName><middleName>Иванович</middleName></employee><employee><academicDepartment>ВМ</academicDepartment><firstName>Татьяна</firstName><id>504576</id><lastName>Автушко</lastName><middleName>Сергеевна</middleName></employee><employee><academicDepartment>Электрон.</academicDepartment><firstName>Вадим</firstName><id>504367</id><lastName>Адамович</lastName><middleName>Евгеньевич</middleName></employee></employeeXmlModels>";
    private static final String RESPONSE_EMPLOYEE_SCHEDULE_LIST = "<scheduleXmlModels><scheduleModel><schedule><auditory>122-1</auditory><auditory>130-1</auditory><employee><academicDepartment>ЭТТ</academicDepartment><firstName>Анна</firstName><id>500093</id><lastName>Воробей</lastName><middleName>Михайловна</middleName></employee><lessonTime>11:40-13:15</lessonTime><lessonType>ЛР</lessonType><note/><numSubgroup>1</numSubgroup><studentGroup>211801</studentGroup><subject>БтСУ</subject><weekNumber>1</weekNumber><zaoch>false</zaoch></schedule><schedule><auditory>122-1</auditory><auditory>130-1</auditory><employee><academicDepartment>ЭТТ</academicDepartment><firstName>Анна</firstName><id>500093</id><lastName>Воробей</lastName><middleName>Михайловна</middleName></employee><lessonTime>11:40-13:15</lessonTime><lessonType>ЛР</lessonType><note/><numSubgroup>2</numSubgroup><studentGroup>211801</studentGroup><subject>БтСУ</subject><weekNumber>3</weekNumber><zaoch>false</zaoch></schedule><schedule><auditory>122-1</auditory><auditory>130-1</auditory><employee><academicDepartment>ЭТТ</academicDepartment><firstName>Анна</firstName><id>500093</id><lastName>Воробей</lastName><middleName>Михайловна</middleName></employee><lessonTime>11:40-13:15</lessonTime><lessonType>ПЗ</lessonType><note/><numSubgroup>0</numSubgroup><studentGroup>211801</studentGroup><subject>БтСУ</subject><weekNumber>2</weekNumber><weekNumber>4</weekNumber><zaoch>false</zaoch></schedule><schedule><auditory>122-1</auditory><employee><academicDepartment>ЭТТ</academicDepartment><firstName>Анна</firstName><id>500093</id><lastName>Воробей</lastName><middleName>Михайловна</middleName></employee><lessonTime>13:25-15:00</lessonTime><lessonType>ЛР</lessonType><note/><numSubgroup>0</numSubgroup><studentGroup>211801</studentGroup><subject>БтСУ</subject><weekNumber>4</weekNumber><zaoch>false</zaoch></schedule><weekDay>Вторник</weekDay></scheduleModel></scheduleXmlModels>";
    private static final String RESPONSE_STUDENT_GROUP_LIST = "<studentGroupXmlModels><studentGroup><id>21411</id><name>560801</name><course>0</course><facultyId>20040</facultyId><specialityDepartmentEducationFormId>20103</specialityDepartmentEducationFormId></studentGroup><studentGroup><id>20169</id><name>212602</name><course>2</course><facultyId>20017</facultyId><specialityDepartmentEducationFormId>20013</specialityDepartmentEducationFormId></studentGroup><studentGroup><id>20079</id><name>110201</name><course>5</course><facultyId>20017</facultyId><specialityDepartmentEducationFormId>20014</specialityDepartmentEducationFormId></studentGroup></studentGroupXmlModels>";
    private static final String RESPONSE_STUDENT_SCHEDULE_LIST = "<scheduleXmlModels><scheduleModel><schedule><auditory>135-1</auditory><employee><academicDepartment>ЭТТ</academicDepartment><firstName>Николай</firstName><id>500116</id><lastName>Собчук</lastName><middleName>Сергеевич</middleName></employee><lessonTime>11:40-13:15</lessonTime><lessonType>ЛК</lessonType><note/><numSubgroup>0</numSubgroup><studentGroup>111801</studentGroup><subject>ПИСЭД</subject><weekNumber>0</weekNumber><weekNumber>1</weekNumber><weekNumber>2</weekNumber><weekNumber>3</weekNumber><weekNumber>4</weekNumber><zaoch>false</zaoch></schedule><schedule><auditory>135-1</auditory><employee><academicDepartment>ЭТТ</academicDepartment><firstName>Николай</firstName><id>500116</id><lastName>Собчук</lastName><middleName>Сергеевич</middleName></employee><lessonTime>13:25-15:00</lessonTime><lessonType>ЛК</lessonType><note/><numSubgroup>0</numSubgroup><studentGroup>111801</studentGroup><subject>ОДиРСМЭ</subject><weekNumber>0</weekNumber><weekNumber>1</weekNumber><weekNumber>2</weekNumber><weekNumber>3</weekNumber><weekNumber>4</weekNumber><zaoch>false</zaoch></schedule><weekDay>Понедельник</weekDay></scheduleModel><scheduleModel><schedule><auditory>202-3</auditory><employee><academicDepartment>ИПиЭ</academicDepartment><firstName>Дарья</firstName><id>500178</id><lastName>Пархоменко</lastName><middleName>Александровна</middleName></employee><lessonTime>17:05-18:40</lessonTime><lessonType>ЛК</lessonType><note/><numSubgroup>0</numSubgroup><studentGroup>111801</studentGroup><subject>ОПиП</subject><weekNumber>0</weekNumber><weekNumber>1</weekNumber><weekNumber>2</weekNumber><weekNumber>3</weekNumber><weekNumber>4</weekNumber><zaoch>false</zaoch></schedule><schedule><auditory>607-2</auditory><employee><academicDepartment>ИПиЭ</academicDepartment><firstName>Дарья</firstName><id>500178</id><lastName>Пархоменко</lastName><middleName>Александровна</middleName></employee><lessonTime>18:45-20:20</lessonTime><lessonType>ПЗ</lessonType><note/><numSubgroup>0</numSubgroup><studentGroup>111801</studentGroup><subject>ОПиП</subject><weekNumber>0</weekNumber><weekNumber>1</weekNumber><weekNumber>2</weekNumber><weekNumber>3</weekNumber><weekNumber>4</weekNumber><zaoch>false</zaoch></schedule><schedule><auditory>603-2</auditory><employee><academicDepartment>Экологии</academicDepartment><firstName>Андрей</firstName><id>500200</id><lastName>Клюев</lastName><middleName>Петрович</middleName></employee><lessonTime>20:25-22:00</lessonTime><lessonType>ПЗ</lessonType><note/><numSubgroup>0</numSubgroup><studentGroup>111801</studentGroup><subject>ЗНиОотЧС.РБ</subject><weekNumber>1</weekNumber><weekNumber>3</weekNumber><zaoch>false</zaoch></schedule><weekDay>Вторник</weekDay></scheduleModel></scheduleXmlModels>";
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
    public void employeeScheduleParseTest() {
        mServer.enqueue(new MockResponse().setBody(RESPONSE_EMPLOYEE_SCHEDULE_LIST));
        List<ScheduleModel> scheduleModelList = mService.getEmployeeSchedule("")
                .toBlocking()
                .first()
                .scheduleModelList;
        assertThat(scheduleModelList).isNotNull();
        assertThat(scheduleModelList).hasSize(1);
        ScheduleModel scheduleModel = scheduleModelList.get(0);
        assertThat(scheduleModel.weekDay).isEqualTo("Вторник");
        List<Schedule> scheduleList = scheduleModel.scheduleList;
        assertThat(scheduleList).hasSize(4);
        assertThat(scheduleList).extracting("note", "subject", "lessonType", "numSubgroup").contains(
                tuple(null, "БтСУ", "ЛР", 1),
                tuple(null, "БтСУ", "ЛР", 2),
                tuple(null, "БтСУ", "ПЗ", 0),
                tuple(null, "БтСУ", "ЛР", 0)
        );
    }

    @Test
    public void studentScheduleParseTest() {
        mServer.enqueue(new MockResponse().setBody(RESPONSE_STUDENT_SCHEDULE_LIST));
        List<ScheduleModel> scheduleModelList = mService.getGroupSchedule("111801")
                .toBlocking()
                .first()
                .scheduleModelList;
        assertThat(scheduleModelList).isNotNull();
        assertThat(scheduleModelList).hasSize(2);
        ScheduleModel scheduleModel = scheduleModelList.get(0);
        assertThat(scheduleModel.weekDay).isEqualTo("Понедельник");
        List<Schedule> scheduleList = scheduleModel.scheduleList;
        assertThat(scheduleList).hasSize(2);
        assertThat(scheduleList).extracting("note", "subject").contains(
                tuple(null, "ПИСЭД"),
                tuple(null, "ОДиРСМЭ")
        );
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
