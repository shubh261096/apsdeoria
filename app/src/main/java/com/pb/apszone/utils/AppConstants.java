package com.pb.apszone.utils;

public class AppConstants {
    /* SHARED PREFERENCE */
    public final static String KEY_USER_ID = "key_user_id";
    public final static String KEY_USER_TYPE = "key_user_type";
    public final static String KEY_USER_LOGIN_STATUS = "key_user_login_status";
    public final static String KEY_TEACHER_ID = "key_teacher_id";

    /* USER TYPE */
    public final static String USER_TYPE_STUDENT = "student";
    public final static String USER_TYPE_TEACHER = "teacher";

    /* USER GENDER */
    public final static String USER_GENDER_MALE = "Male";
    public final static String USER_GENDER_FEMALE = "Female";

    public final static String KEY_ENABLED = "enabled";

    /* KEY PROFILE */
    public final static String PROFILE_ID = "ID";
    public final static String PROFILE_EMAIL = "Email";
    public final static String PROFILE_PHONE = "Phone";
    public final static String PROFILE_DOB = "Date of birth";
    public final static String PROFILE_ADDRESS = "Address";
    public final static String PROFILE_FATHER_NAME = "Father's Name";
    public final static String PROFILE_MOTHER_NAME = "Mother's Name";
    public final static String PROFILE_GUARDIAN_NAME = "Guardian Name";
    public final static String PROFILE_HUSBAND_NAME = "Husband's Name";
    public final static String PROFILE_QUALIFICATION = "Qualification";

    /* KEY TIMETABLE FILTER */
    public final static String KEY_FILTER_BY_DAY = "day";
    public final static String KEY_FILTER_BY_WEEK = "week";

    /* DASHBOARD UI ELEMENT NAME*/
    public final static String UI_ELEMENT_TIMETABLE = "Timetable";
    public final static String UI_ELEMENT_ATTENDANCE = "Attendance";
    public final static String UI_ELEMENT_SYLLABUS = "Syllabus";
    public final static String UI_ELEMENT_HOMEWORK = "Homework";
    public final static String UI_ELEMENT_FEES = "Fees";
    public final static String UI_ELEMENT_INBOX = "Inbox";
    public final static String UI_ELEMENT_DOWNLOAD = "Downloads";
    public final static String UI_ELEMENT_TEACHER_FEEDBACK = "Teacher's Feedback";
    public final static String UI_ELEMENT_LEARN = "Learn";

    public static long KEY_DOWNLOAD_ID;

    /* BUNDLE KEY FOR SHARING DATA*/
    public final static String KEY_CLASS_ID = "key_class_id";
    public final static String KEY_SUBJECT_ID = "key_subject_id";
    public final static String KEY_SUBJECT_NAME = "key_subject_name";
    public final static String KEY_DASHBOARD_ELEMENT_NAME = "key_dashboard_element_name";
    public final static String KEY_PDF_URL = "key_pdf_url";
    public final static String KEY_PDF_SUBJECT_NAME = "key_pdf_subject_name";

    /* KEY CONSTANT VALUES */
    public final static int READ_EXTERNAL_STORAGE_CODE = 101;
    public final static int WRITE_EXTERNAL_STORAGE_CODE = 102;
    public final static int PDF_REQ_CODE = 901;

    /* CONSTANT APP URL AND INFO */
    public final static String PRIVACY_POLICY_URL = "https://www.apsdeoria.com/privacy";
    public final static String WEBSITE_URL = "https://www.apsdeoria.com/";

    /* ERROR CONSTANTS USED IN REPOSITORY */
    public interface ErrorConstants {
        String SERVER_ERROR = "Unable to process your request. \nPlease try again later.";
        String BAD_REQUEST_ERROR = "Unable to process your request. \nPlease try again later.";
        String UNKNOWN_ERROR = "Unable to process your request. \nPlease try again later.";
        String SOCKET_ERROR = "Unable to connect with the server.\nPlease try again later.";
    }

    /* APP CONSTANT NUMBERS */
    public interface Numbers {
        String ZERO = "0";
        String ONE = "1";
    }

    /* SHARED PREFERENCE FOR FCM SUBSCRIPTION */
    public final static String KEY_SUBSCRIBE_CLASS_ID = "key_subscribe_class_id";
    public final static String KEY_SUBSCRIBE_TEACHER = "key_subscribe_teacher";
    public final static String KEY_SUBSCRIBE_STUDENT = "key_subscribe_student";

    /*API KEYS */
    public final static String YOUTUBE_API_KEY = "AIzaSyBAEhRwaUAxTn42CRe14nO2riK3yUZt8WE";

    /* BUNDLE CONSTANTS */
    public final static String VIDEO_LIST = "video_list";
    public final static String VIDEO_URL = "video_url";
    public final static String VIDEO_TITLE = "video_title";
    public final static String VIDEO_POSITION = "video_position";

    /* IN-APP UPDATE MANAGER*/
    public static final String KEY_APP_UPDATE_LAST_SHOWN_DAY = "key_app_update_day";

    /**
     * Enum for Update Mode that can either be #FLEXIBLE or #IMMEDIATE
     */
    public enum UpdateMode {
        FLEXIBLE,
        IMMEDIATE
    }

    /**
     * Enum for InstallStatus that can either #DOWNLOADED or #INSTALLED or #FAILED
     */
    public enum InstallStat {
        DOWNLOADED, INSTALLED, FAILED
    }

    static final int UPDATE_ERROR_START_APP_UPDATE_FLEXIBLE = 100;
    static final int UPDATE_ERROR_START_APP_UPDATE_IMMEDIATE = 101;

    public static final int REQ_CODE_VERSION_UPDATE_FLEXIBLE = 530;
    public static final int REQ_CODE_VERSION_UPDATE_IMMEDIATE = 531;
}
