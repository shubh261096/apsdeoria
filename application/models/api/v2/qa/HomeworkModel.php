<?php

class HomeworkModel extends CI_model
{
    var $db;

    public function __construct()
    {
        parent::__construct();
        $this->db = $this->load->database('qa_db', TRUE);
    }

    /* Query for getting homework by date and class*/
    public function get_homeworkByDate($class_id, $date)
    {
        $query = $this->db->where(['class_id' => $class_id, 'date' => $date])
            ->from('homework')
            ->get();
        if ($query->num_rows()) {
            return $query->result();
        } else {
            return FALSE;
        }
    }

    /* Query for getting class by teacher_id*/
    public function get_Class($teacher_id)
    {
        $sql = 'SELECT class_id FROM `timetable` WHERE teacher_id="' . $teacher_id . '" GROUP BY class_id';
        $query = $this->db->query($sql);
        if ($query->num_rows()) {
            return $query->result();
        } else {
            return NULL;
        }
    }

    /* Query for getting subject by class_id and teacher_id*/
    public function get_Subject($class_id, $teacher_id)
    {
        $sql = 'SELECT subject_id FROM `timetable` WHERE class_id = "' . $class_id . '" AND teacher_id = "' . $teacher_id . '" GROUP BY subject_id';
        $query = $this->db->query($sql);
        if ($query->num_rows()) {
            return $query->result();
        } else {
            return NULL;
        }
    }

    /* Query for adding homwork */
    public function add_Homework($array)
    {
        return $this->db->insert('homework', $array);
    }

    /* Query for checking if homework is already added */
    public function is_HomeworkAvailable($array)
    {
        $date = $array['date'];
        $teacher_id = $array['teacher_id'];
        $subject_id = $array['subject_id'];
        $class_id = $array['class_id'];

        $sql = 'SELECT * FROM homework WHERE class_id= "' . $class_id . '" AND subject_id= "' . $subject_id . '" AND teacher_id= "' . $teacher_id . '" AND date= "' . $date . '" ';
        $query = $this->db->query($sql);
        if ($query->num_rows() > 0) {
            return TRUE;
        } else {
            return FALSE;
        }
    }
}
