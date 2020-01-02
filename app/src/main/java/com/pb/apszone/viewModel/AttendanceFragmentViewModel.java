package com.pb.apszone.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pb.apszone.service.repo.Events;
import com.pb.apszone.service.repo.Repository;
import com.pb.apszone.service.rest.AttendanceRequestModel;

import java.util.ArrayList;
import java.util.List;

import static com.pb.apszone.utils.CommonUtils.getFirstDayOfMonth;
import static com.pb.apszone.utils.CommonUtils.getNumOfDaysInMonth;
import static com.pb.apszone.utils.CommonUtils.getStringMonthNumber;

public class AttendanceFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<Events.AttendanceResponseEvent> attendanceResponseModelMutableLiveData;
    private Repository repository;
    private AttendanceRequestModel attendanceRequestModel;
    private List<String> day;

    public AttendanceFragmentViewModel(@NonNull Application application) {
        super(application);
        attendanceResponseModelMutableLiveData = new MutableLiveData<>();
        repository = Repository.getInstance();
        attendanceRequestModel = new AttendanceRequestModel();
        day = new ArrayList<>();
    }

    public void sendRequest(String student_id, String month, String year) {
        attendanceRequestModel.setStudentId(student_id);
        attendanceRequestModel.setMonth(getStringMonthNumber(month));
        attendanceRequestModel.setYear(year);
        repository.getAttendance(attendanceRequestModel, attendanceResponseModelMutableLiveData);
    }

    public LiveData<Events.AttendanceResponseEvent> getAttendance() {
        return attendanceResponseModelMutableLiveData;
    }

    public List<String> setUpList(String currentMonth, String currentYear) {
        if (day.size() > 0) {
            day.clear();
        }
        int numDay = getNumOfDaysInMonth(currentYear, currentMonth);
        int skipPosition = getFirstDayOfMonth(currentYear, currentMonth);
        for (int i = 0; i < skipPosition - 1; i++) {
            day.add(i, "");
        }
        for (int i = 1; i <= 9; i++) {
            day.add("0" + i);
        }
        for (int i = 10; i <= numDay; i++) {
            day.add(String.valueOf(i));
        }
        return day;
    }

}
