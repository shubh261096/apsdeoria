<?php
class FeedbackModel extends CI_Model
{

	public function __construct()
	{
		parent::__construct();
		$this->load->database();
	}

	public function getFeedback()
	{
		$this->db->select("*");
		$this->db->from('feedback');
		$query = $this->db->get();
		if ($query->num_rows() > 0) {
			return $query->result();
		}
	}
}
