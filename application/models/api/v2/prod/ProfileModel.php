<?php

class ProfileModel extends CI_model
{
    var $db;

    public function __construct()
    {
        parent::__construct();
        $this->db = $this->load->database('default', TRUE);
    }

    /* Query for getting profile info */
    public function get_profile($id, $type)
    {
        if (strcasecmp($type, TEACHER) == 0) {
            $sql = 'SELECT * FROM teacher WHERE id="' . $id . '" AND status=1';
        } elseif (strcasecmp($type, STUDENT) == 0) {
            $sql = 'SELECT * FROM student WHERE id="' . $id . '" AND status=1';
        }
        $query = $this->db->query($sql);
        if ($query->num_rows()) {
            return $query->row();
        } else {
            return FALSE;
        }
    }


    /* Query for getting parent info*/
    public function get_parentInfo($student_id)
    {
        $query = $this->db->where('student_id', $student_id)
            ->from('parent')
            ->get();
        if ($query->num_rows()) {
            return $query->result();
        } else {
            return NULL;
        }
    }
}
