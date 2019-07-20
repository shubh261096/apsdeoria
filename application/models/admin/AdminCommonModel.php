<?php

class AdminCommonModel extends CI_model
{

	public function __construct()
	{
		parent::__construct();
		$this->load->database();
	}

	public function getClassList()
	{
		$result = $this->db->select('id, name')->get('class')->result_array();
		$classes = array();
		foreach ($result as $r) {
			$classes[$r['id']] = $r['name'];
		}
		return $classes;
	}

	public function getSubjectList()
	{
		$result = $this->db->select('id, name')->get('subject')->result_array();
		$subjects = array();
		foreach ($result as $r) {
			$subjects[$r['id']] = $r['name'] . " - " . $r['id'];
		}
		return $subjects;
	}

	public function getTeacherList()
	{
		$result = $this->db->select('id, fullname')->get('teacher')->result_array();
		$teachers = array();
		foreach ($result as $r) {
			$teachers[$r['id']] = $r['fullname'];
		}
		return $teachers;
	}
}
