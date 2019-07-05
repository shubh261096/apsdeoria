package com.pb.apszone.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.pb.apszone.service.model.ClassDetailResponseModel;
import com.pb.apszone.service.model.SubmitAttendanceResponseModel;
import com.pb.apszone.service.repo.Repository;
import com.pb.apszone.service.rest.ClassDetailRequestModel;
import com.pb.apszone.service.rest.SubmitAttendanceRequestModel;

public class AttendanceTeacherFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<ClassDetailResponseModel> classDetailResponseModelMutableLiveData;
    private MutableLiveData<SubmitAttendanceResponseModel> submitAttendanceResponseModelMutableLiveData;
    private Repository repository;
    private ClassDetailRequestModel classDetailRequestModel;

    public AttendanceTeacherFragmentViewModel(@NonNull Application application) {
        super(application);
        classDetailResponseModelMutableLiveData = new MutableLiveData<>();
        submitAttendanceResponseModelMutableLiveData = new MutableLiveData<>();
        repository = Repository.getInstance();
        classDetailRequestModel = new ClassDetailRequestModel();
    }

    public void sendRequest(String teacher_id, String date) {
        classDetailRequestModel.setTeacherId(teacher_id);
        classDetailRequestModel.setDate(date);
        repository.getClassDetail(classDetailRequestModel, classDetailResponseModelMutableLiveData);
    }

    public void editAttendanceRequest(SubmitAttendanceRequestModel submitAttendanceRequestModel) {
        repository.editAttendance(submitAttendanceRequestModel, submitAttendanceResponseModelMutableLiveData);
    }

    public void addAttendanceRequest(SubmitAttendanceRequestModel submitAttendanceRequestModel) {
        repository.addAttendance(submitAttendanceRequestModel, submitAttendanceResponseModelMutableLiveData);
    }

    public LiveData<ClassDetailResponseModel> getClassDetail() {
        return classDetailResponseModelMutableLiveData;
    }

    public LiveData<SubmitAttendanceResponseModel> getSubmitResponse() {
        return submitAttendanceResponseModelMutableLiveData;
    }

}
