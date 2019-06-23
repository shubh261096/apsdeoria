<?php
class NotificationModel extends CI_Model
{
    public function __construct()
    {
        parent::__construct();
        $this->load->database();
    }

    public function add_notification($array)
    {
        return $this->db->insert('inbox', $array);
    }
}
