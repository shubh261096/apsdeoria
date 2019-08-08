package com.pb.apszone.service.repo;

import com.pb.apszone.service.model.ErrorModel;
import com.pb.apszone.service.model.ProfileResponseModel;

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

    public static class ProfileFragmentResponseEvent extends BaseEvent {
        private ProfileResponseModel profileResponseModel;

        ProfileFragmentResponseEvent(ErrorModel errorModel, boolean success, ProfileResponseModel profileResponseModel) {
            super(errorModel, success);
            this.profileResponseModel = profileResponseModel;
        }

        public ProfileResponseModel getProfileResponseModel() {
            return profileResponseModel;
        }

        public void setProfileResponseModel(ProfileResponseModel profileResponseModel) {
            this.profileResponseModel = profileResponseModel;
        }
    }

}
