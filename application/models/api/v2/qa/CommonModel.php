<?php

class CommonModel extends CI_model
{
    var $db;
    
    public function __construct()
    {
        parent::__construct();
        $this->db = $this->load->database('qa_db', TRUE);
    }

    /* Query for getting class etails by class_id */
    public function get_classDetails($class_id)
    {
        $query = $this->db->select('id, name')
            ->where(['id' => $class_id, 'status' => 1])
            ->from('class')
            ->get();
        if ($query->num_rows()) {
            return $query->row();
        } else {
            return FALSE;
        }
    }

    /* Query for getting teacher details by teacher_id */
    public function get_teacherDetails($teacher_id)
    {
        $query = $this->db->select('id, fullname')
            ->where(['id' => $teacher_id, 'status' => 1])
            ->from('teacher')
            ->get();
        if ($query->num_rows()) {
            return $query->row();
        } else {
            return FALSE;
        }
    }

    /* Query for getting subject details by subject_id */
    public function get_subjectDetails($subject_id)
    {
        $query = $this->db->select('id, name, syllabus')
            ->where(['id' => $subject_id])
            ->from('subject')
            ->get();
        if ($query->num_rows()) {
            return $query->row();
        } else {
            return FALSE;
        }
    }

    /* Query for getting fees details by fees_id */
    public function get_feesDetails($fees_id)
    {
        $query = $this->db->select('*')
            ->where(['id' => $fees_id])
            ->from('fees')
            ->get();
        if ($query->num_rows()) {
            return $query->row();
        } else {
            return FALSE;
        }
    }

    /* Query for getting student details by student_id */
    public function get_studentDetails($student_id)
    {
        $query = $this->db->select('*')
            ->where(['id' => $student_id])
            ->from('student')
            ->get();
        if ($query->num_rows()) {
            return $query->row();
        } else {
            return FALSE;
        }
    }

    /* Query for getting teacher status by teacher_id */
    public function get_teacherStatus($teacher_id)
    {
        $query = $this->db->select('status')
            ->where(['id' => $teacher_id])
            ->from('teacher')
            ->get();
        if ($query->num_rows()) {
            if ($query->row()->status == '1') {
                return TRUE;
            } else {
                return FALSE;
            }
        } else {
            return FALSE;
        }
    }

    /* Query for getting all subjects by class_id */
    public function get_AllSubjectsByClassId($class_id)
    {
        $query = $this->db->select('id, name')
            ->where(['class_id' => $class_id])
            ->from('subject')
            ->get();
        if ($query->num_rows()) {
            return $query->result();
        } else {
            return NULL;
        }
    }

    /* Query to get user_type by id */
    public function getUserType($id)
    {
        $query = $this->db->select('type')
            ->where('id', $id)
            ->from('login')
            ->get();
        if ($query->num_rows()) {
            return $query->row()->type;
        } else {
            return NULL;
        }
    }

    /** Query to get class_id from student_id */
    public function getClassIdFromStudentId($student_id)
    {
        $query = $this->db->select('class_id')
            ->where('id', $student_id)
            ->from('student')
            ->get();
        if ($query->num_rows()) {
            return $query->row()->class_id;
        } else {
            return NULL;
        }
    }
}
