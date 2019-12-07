package com.pb.apszone.service.repo;

import com.pb.apszone.service.model.AttendanceResponseModel;
import com.pb.apszone.service.model.ClassDetailResponseModel;
import com.pb.apszone.service.model.ClassSubjectResponseModel;
import com.pb.apszone.service.model.CommonResponseModel;
import com.pb.apszone.service.model.DashboardUIResponseModel;
import com.pb.apszone.service.model.DownloadResponseModel;
import com.pb.apszone.service.model.ErrorModel;
import com.pb.apszone.service.model.FeesResponseModel;
import com.pb.apszone.service.model.HomeworkResponseModel;
import com.pb.apszone.service.model.InboxResponseModel;
import com.pb.apszone.service.model.LearnResponseModel;
import com.pb.apszone.service.model.LoginResponseModel;
import com.pb.apszone.service.model.ProfileResponseModel;
import com.pb.apszone.service.model.SubmitAttendanceResponseModel;
import com.pb.apszone.service.model.SyllabusResponseModel;
import com.pb.apszone.service.model.TimetableResponseModel;

public class Events {

    private Events() {
    }

    public static class BaseEvent {
        private ErrorModel errorModel;
        private boolean success;

        BaseEvent(ErrorModel errorModel, boolean success) {
            this.errorModel = errorModel;
            this.success = success;
        }

        public ErrorModel getErrorModel() {
            return errorModel;
        }

        public boolean isSuccess() {
            return success;
        }
    }

    public static class LoginResponseEvent extends BaseEvent {
        private LoginResponseModel loginResponseModel;

        LoginResponseEvent(ErrorModel errorModel, boolean success, LoginResponseModel loginResponseModel) {
            super(errorModel, success);
            this.loginResponseModel = loginResponseModel;
        }

        public LoginResponseModel getLoginResponseModel() {
            return loginResponseModel;
        }
    }

    public static class ProfileResponseEvent extends BaseEvent {
        private ProfileResponseModel profileResponseModel;

        ProfileResponseEvent(ErrorModel errorModel, boolean success, ProfileResponseModel profileResponseModel) {
            super(errorModel, success);
            this.profileResponseModel = profileResponseModel;
        }

        public ProfileResponseModel getProfileResponseModel() {
            return profileResponseModel;
        }
    }

    public static class DashboardUIResponseEvent extends BaseEvent {
        private DashboardUIResponseModel dashboardUIResponseModel;

        DashboardUIResponseEvent(ErrorModel errorModel, boolean success, DashboardUIResponseModel dashboardUIResponseModel) {
            super(errorModel, success);
            this.dashboardUIResponseModel = dashboardUIResponseModel;
        }

        public DashboardUIResponseModel getDashboardUIResponseModel() {
            return dashboardUIResponseModel;
        }
    }

    public static class TimetableResponseEvent extends BaseEvent {
        private TimetableResponseModel timetableResponseModel;

        TimetableResponseEvent(ErrorModel errorModel, boolean success, TimetableResponseModel timetableResponseModel) {
            super(errorModel, success);
            this.timetableResponseModel = timetableResponseModel;
        }

        public TimetableResponseModel getTimetableResponseModel() {
            return timetableResponseModel;
        }
    }

    public static class AttendanceResponseEvent extends BaseEvent {
        private AttendanceResponseModel attendanceResponseModel;

        AttendanceResponseEvent(ErrorModel errorModel, boolean success, AttendanceResponseModel attendanceResponseModel) {
            super(errorModel, success);
            this.attendanceResponseModel = attendanceResponseModel;
        }

        public AttendanceResponseModel getAttendanceResponseModel() {
            return attendanceResponseModel;
        }
    }

    public static class SyllabusResponseEvent extends BaseEvent {
        private SyllabusResponseModel syllabusResponseModel;

        SyllabusResponseEvent(ErrorModel errorModel, boolean success, SyllabusResponseModel syllabusResponseModel) {
            super(errorModel, success);
            this.syllabusResponseModel = syllabusResponseModel;
        }

        public SyllabusResponseModel getSyllabusResponseModel() {
            return syllabusResponseModel;
        }
    }

    public static class HomeworkResponseEvent extends BaseEvent {
        private HomeworkResponseModel homeworkResponseModel;

        HomeworkResponseEvent(ErrorModel errorModel, boolean success, HomeworkResponseModel homeworkResponseModel) {
            super(errorModel, success);
            this.homeworkResponseModel = homeworkResponseModel;
        }

        public HomeworkResponseModel getHomeworkResponseModel() {
            return homeworkResponseModel;
        }
    }

    public static class FeesResponseEvent extends BaseEvent {
        private FeesResponseModel feesResponseModel;

        FeesResponseEvent(ErrorModel errorModel, boolean success, FeesResponseModel feesResponseModel) {
            super(errorModel, success);
            this.feesResponseModel = feesResponseModel;
        }

        public FeesResponseModel getFeesResponseModel() {
            return feesResponseModel;
        }
    }

    public static class InboxResponseEvent extends BaseEvent {
        private InboxResponseModel inboxResponseModel;

        InboxResponseEvent(ErrorModel errorModel, boolean success, InboxResponseModel inboxResponseModel) {
            super(errorModel, success);
            this.inboxResponseModel = inboxResponseModel;
        }

        public InboxResponseModel getInboxResponseModel() {
            return inboxResponseModel;
        }
    }

    public static class ClassDetailResponseEvent extends BaseEvent {
        private ClassDetailResponseModel classDetailResponseModel;

        ClassDetailResponseEvent(ErrorModel errorModel, boolean success, ClassDetailResponseModel classDetailResponseModel) {
            super(errorModel, success);
            this.classDetailResponseModel = classDetailResponseModel;
        }

        public ClassDetailResponseModel getClassDetailResponseModel() {
            return classDetailResponseModel;
        }
    }

    public static class SubmitAttendanceResponseEvent extends BaseEvent {
        private SubmitAttendanceResponseModel submitAttendanceResponseModel;

        SubmitAttendanceResponseEvent(ErrorModel errorModel, boolean success, SubmitAttendanceResponseModel submitAttendanceResponseModel) {
            super(errorModel, success);
            this.submitAttendanceResponseModel = submitAttendanceResponseModel;
        }

        public SubmitAttendanceResponseModel getSubmitAttendanceResponseModel() {
            return submitAttendanceResponseModel;
        }
    }

    public static class ClassSubjectResponseEvent extends BaseEvent {
        private ClassSubjectResponseModel classSubjectResponseModel;

        ClassSubjectResponseEvent(ErrorModel errorModel, boolean success, ClassSubjectResponseModel classSubjectResponseModel) {
            super(errorModel, success);
            this.classSubjectResponseModel = classSubjectResponseModel;
        }

        public ClassSubjectResponseModel getClassSubjectResponseModel() {
            return classSubjectResponseModel;
        }
    }

    public static class CommonResponseEvent extends BaseEvent {
        private CommonResponseModel commonResponseModel;

        CommonResponseEvent(ErrorModel errorModel, boolean success, CommonResponseModel commonResponseModel) {
            super(errorModel, success);
            this.commonResponseModel = commonResponseModel;
        }

        public CommonResponseModel getCommonResponseModel() {
            return commonResponseModel;
        }
    }

    public static class DownloadResponseEvent extends BaseEvent {
        private DownloadResponseModel downloadResponseModel;

        DownloadResponseEvent(ErrorModel errorModel, boolean success, DownloadResponseModel downloadResponseModel) {
            super(errorModel, success);
            this.downloadResponseModel = downloadResponseModel;
        }

        public DownloadResponseModel getDownloadResponseModel() {
            return downloadResponseModel;
        }
    }

    public static class LearnResponseEvent extends BaseEvent {
        private LearnResponseModel learnResponseModel;

        LearnResponseEvent(ErrorModel errorModel, boolean success, LearnResponseModel learnResponseModel) {
            super(errorModel, success);
            this.learnResponseModel = learnResponseModel;
        }

        public LearnResponseModel getLearnResponseModel() {
            return learnResponseModel;
        }
    }

}
