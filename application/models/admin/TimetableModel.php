<?php
class TimetableModel extends CI_Model
{

	public function __construct()
	{
		parent::__construct();
		$this->load->database();
	}

	public function getTimetable()
	{
		$this->db->select("*");
		$this->db->from('timetable');
		$query = $this->db->get();
		if ($query->num_rows() > 0) {
			return $query->result();
		}
	}

	/** function to get class name & id for dropdown class */
	public function getClass()
	{
		$result = $this->db->select('id, name')->get('class')->result_array();
		$classes = array();
		foreach ($result as $r) {
			$classes[$r['id']] = $r['name'];
		}
		return $classes;
	}

	/** function to get subject name & id for dropdown subject */
	public function getSubject()
	{
		$result = $this->db->select('id, name')->get('subject')->result_array();
		$subject = array();
		foreach ($result as $r) {
			$subject[$r['id']] = $r['name'] . " - " . $r['id'];
		}
		return $subject;
	}

	/** function to get teacher name & id for dropdown teacher */
	public function getTeacher()
	{
		$result = $this->db->select('id, fullname')->get('teacher')->result_array();
		$teacher = array();
		foreach ($result as $r) {
			$teacher[$r['id']] = $r['fullname'];
		}
		return $teacher;
	}

	public function addTimetable($array)
	{
		$array['status'] = 1;
		return $this->db->insert('timetable', $array);
	}

	public function editTimetable($timetable_id)
	{
		$q = $this->db->select("*")
			->where('id', $timetable_id)
			->get('timetable');
		return $q->row();
	}

	public function updateTimetable($timetable_id, array $timetable)
	{
		return $this->db
			->where('id', $timetable_id)
			->update('timetable', $timetable);
	}


	public function deleteTimetable($timetable_id)
	{
		return $this->db
			->where('id', $timetable_id)
			->delete('timetable');
	}
}
