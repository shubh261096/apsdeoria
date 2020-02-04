<?php

class LearnModel extends CI_model
{
    var $db;

    public function __construct()
    {
        parent::__construct();
        $this->db = $this->load->database('qa_db', TRUE);
    }

    /* Query for getting videoUrl by subject_id*/
    public function getVideoUrl($subject_id)
    {
        $query = $this->db->where(['subject_id' => $subject_id])
            ->from('learn')
            ->get();
        if ($query->num_rows()) {
            return $query->result();
        } else {
            return NULL;
        }
    }
}
