<?php

class AdminCommonModel extends CI_model
{

	public function __construct()
	{
		parent::__construct();
		$this->load->database();
	}

	/** Getting Class Detail */
	public function getClassList()
	{
		$result = $this->db->select('id, name')->get('class')->result_array();
		$classes = array();
		foreach ($result as $r) {
			$classes[$r['id']] = $r['name'];
		}
		return $classes;
	}

	/** Getting Subject Detail */
	public function getSubjectList()
	{
		$result = $this->db->select('id, name')->get('subject')->result_array();
		$subjects = array();
		foreach ($result as $r) {
			$subjects[$r['id']] = $r['name'] . " - " . $r['id'];
		}
		return $subjects;
	}

	/** Getting Teacher Detail */
	public function getTeacherList()
	{
		$result = $this->db->select('id, fullname')->get('teacher')->result_array();
		$teachers = array();
		foreach ($result as $r) {
			$teachers[$r['id']] = $r['fullname'];
		}
		return $teachers;
	}

	/** Getting Student Detail */
	public function getStudentList()
	{
		$result = $this->db->select('id, fullname')->get('student')->result_array();
		$student = array();
		foreach ($result as $r) {
			$student[$r['id']] = $r['fullname'] . " - " . $r['id'];
		}
		return $student;
	}

	/* Query for getting teacher details by teacher_id */
    public function getTeacherDetails($teacher_id)
    {
        $query = $this->db->select('id, fullname')
            ->where(['id' => $teacher_id])
            ->from('teacher')
            ->get();
        if ($query->num_rows()) {
            return $query->row();
        } else {
            return FALSE;
        }
    }
}
