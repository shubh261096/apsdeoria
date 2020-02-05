<?php
class DownloadModel extends CI_Model
{
    var $db;
    
    public function __construct()
    {
        parent::__construct();
        $this->db = $this->load->database('default', TRUE);
    }

    /* Query for getting download by user_type*/
    public function get_downloadByUserType($user_type)
    {
        $query = $this->db->where('type', $user_type)
            ->where('status', '1')
            ->or_where('type', 'global')
            ->get('download');

        if ($query->num_rows()) {
            return $query->result();
        } else {
            return FALSE;
        }
    }
}
