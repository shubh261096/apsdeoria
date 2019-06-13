package com.pb.apszone.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.pb.apszone.service.model.AttendanceResponseModel;
import com.pb.apszone.service.repo.Repository;
import com.pb.apszone.service.rest.AttendanceRequestModel;

import static com.pb.apszone.utils.CommonUtils.getStringMonthNumber;

public class AttendanceFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<AttendanceResponseModel> attendanceResponseModelMutableLiveData;
    private Repository repository;
    private AttendanceRequestModel attendanceRequestModel;

    public AttendanceFragmentViewModel(@NonNull Application application) {
        super(application);
        attendanceResponseModelMutableLiveData = new MutableLiveData<>();
        repository = Repository.getInstance();
        attendanceRequestModel = new AttendanceRequestModel();
    }

    public void sendRequest(String student_id, String month, String year) {
        attendanceRequestModel.setStudentId("APS101"); // TODO student_id needs to be replaced.
        attendanceRequestModel.setMonth(getStringMonthNumber(month));
        attendanceRequestModel.setYear(year);
    }

    public LiveData<AttendanceResponseModel> getAttendance() {
        attendanceResponseModelMutableLiveData = repository.getAttendance(attendanceRequestModel);
        return attendanceResponseModelMutableLiveData;
    }

}
