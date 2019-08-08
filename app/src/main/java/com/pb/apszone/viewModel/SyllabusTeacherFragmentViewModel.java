package com.pb.apszone.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.pb.apszone.service.repo.Events;
import com.pb.apszone.service.repo.Repository;
import com.pb.apszone.service.rest.SyllabusRequestModel;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SyllabusTeacherFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<Events.CommonResponseEvent> commonResponseModelMutableLiveData;
    private Repository repository;
    private SyllabusRequestModel syllabusRequestModel;

    public SyllabusTeacherFragmentViewModel(@NonNull Application application) {
        super(application);
        commonResponseModelMutableLiveData = new MutableLiveData<>();
        repository = Repository.getInstance();
        syllabusRequestModel = new SyllabusRequestModel();
    }

    public void sendRequest(String subject_id) {
        syllabusRequestModel.setSubjectId(subject_id);
        repository.checkSyllabus(syllabusRequestModel, commonResponseModelMutableLiveData);
    }

    public void updateRequest(File file, String subject_id, String subject_description) {
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("pdf/*"), file);
        RequestBody subjectId = RequestBody.create(MediaType.parse("text/plain"), subject_id);
        RequestBody subjectDescription = RequestBody.create(MediaType.parse("text/plain"), subject_description);
        MultipartBody.Part part = MultipartBody.Part.createFormData("pdfData", file.getName(), fileReqBody);
        repository.updateSyllabus(part, subjectId, subjectDescription, commonResponseModelMutableLiveData);
    }

    public LiveData<Events.CommonResponseEvent> checkResponse() {
        return commonResponseModelMutableLiveData;
    }
}
