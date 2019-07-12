<?php

class SyllabusModel extends CI_model
{

    public function __construct()
    {
        parent::__construct();
        $this->load->database();
    }

    /* Query for getting syllabus by class*/
    public function get_syllabusByClass($class_id)
    {
        $query = $this->db->where(['class_id' => $class_id])
            ->from('subject')
            ->get();
        if ($query->num_rows()) {
            return $query->result();
        } else {
            return FALSE;
        }
    }

    /* Teacher - Query to check if syllabus is already available by subject_id*/
    public function isSyllabusAvailable($subject_id)
    {
        $sql = 'SELECT syllabus FROM subject WHERE id = "' . $subject_id . '"';
        $query = $this->db->query($sql);
        if ($query->num_rows()) {
            return $query->row();
        } else {
            return NULL;
        }
    }

    /* Teacher - Query to update syllabus data */
    public function update_Syllabus($subject_id, $path)
    {
        $sql = 'UPDATE subject SET syllabus = "' . $path . '" WHERE id = "' . $subject_id . '"';
        $query = $this->db->query($sql);
        if ($query) {
            return TRUE;
        } else {
            return FALSE;
        }
    }
}
