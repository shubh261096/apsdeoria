<?php
class DownloadModel extends CI_Model
{
    public function __construct()
    {
        parent::__construct();
        $this->load->database();
    }

    /* Query for getting download by user_type*/
    public function get_downloadByUserType($user_type)
    {
        $query = $this->db->where('type', $user_type)
            ->or_where('type', 'global')
            ->get('download');

        if ($query->num_rows()) {
            return $query->result();
        } else {
            return FALSE;
        }
    }
}
