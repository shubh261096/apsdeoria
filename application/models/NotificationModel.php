<?php
class NotificationModel extends CI_Model
{
    public function __construct()
    {
        parent::__construct();
        $this->load->database();
    }

    /* Query to add payload data*/
    public function add_notification($array)
    {
        date_default_timezone_set("Asia/Calcutta");
        $array['date'] = date("Y-m-d H:i:sa");
        return $this->db->insert('inbox', $array);
    }

    /* Query for getting all notification data*/
    public function get_notification()
    {
        $query = $this->db->where('topic', 'global')
            ->order_by('date', 'DESC')
            ->get('inbox');

        if ($query->num_rows()) {
            return $query->result();
        } else {
            return FALSE;
        }
    }
}
