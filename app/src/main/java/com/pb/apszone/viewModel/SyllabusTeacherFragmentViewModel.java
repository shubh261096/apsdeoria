package com.pb.apszone.viewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pb.apszone.service.repo.Events;
import com.pb.apszone.service.repo.Repository;
import com.pb.apszone.service.rest.model.HomeworkRequestModel;
import com.pb.apszone.service.rest.model.SyllabusRequestModel;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SyllabusTeacherFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<Events.CommonResponseEvent> commonResponseModelMutableLiveData;
    private MutableLiveData<Events.ClassSubjectResponseEvent> classSubjectResponseModelMutableLiveData;
    private Repository repository;
    private SyllabusRequestModel syllabusRequestModel;
    private HomeworkRequestModel homeworkRequestModel;

    public SyllabusTeacherFragmentViewModel(@NonNull Application application) {
        super(application);
        classSubjectResponseModelMutableLiveData = new MutableLiveData<>();
        commonResponseModelMutableLiveData = new MutableLiveData<>();
        repository = Repository.getInstance();
        syllabusRequestModel = new SyllabusRequestModel();
        homeworkRequestModel = new HomeworkRequestModel();
    }

    public void sendClassDetailRequest(String teacher_id) {
        homeworkRequestModel.setTeacherId(teacher_id);
        repository.getClassSubjectDetail(homeworkRequestModel, classSubjectResponseModelMutableLiveData);
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

    public LiveData<Events.ClassSubjectResponseEvent> getClassSubjectDetail() {
        return classSubjectResponseModelMutableLiveData;
    }
}
