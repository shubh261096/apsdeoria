<?php
class NotificationModel extends CI_Model
{
    var $db;

    public function __construct()
    {
        parent::__construct();
        $this->db = $this->load->database('default', TRUE);
    }

    /* Query to add payload data*/
    public function add_notification($array)
    {
        date_default_timezone_set("Asia/Calcutta");
        $array['date'] = date("Y-m-d H:i:s");
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

    /* Query for getting notification by user_type*/
    public function get_notificationByUserType($user_type)
    {
        $query = $this->db->where('topic', 'global')
            ->or_where('topic', $user_type)
            ->order_by('date', 'DESC')
            ->get('inbox');

        if ($query->num_rows()) {
            return $query->result();
        } else {
            return FALSE;
        }
    }
}
