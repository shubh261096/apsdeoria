<?php
class ExpenseModel extends CI_Model
{
	public function __construct()
	{
		parent::__construct();
		$this->load->database();
	}

	/* Getting expenditure from school_expense table */
	public function getExpense()
	{
		$this->db->select("*");
		$this->db->from('school_expense');
		$query = $this->db->get();
		if ($query->num_rows() > 0) {
			return $query->result();
		}
	}

	/** function to insert expenditure */
	public function addExpense($array)
	{
		// set the timezone first
		if (function_exists('date_default_timezone_set')) {
			date_default_timezone_set("Asia/Kolkata");
		}
		$array['date'] = date("Y-m-d H:i:s");
		return $this->db->insert('school_expense', $array);
	}

	/** function to delete expenditure */
	public function deleteExpense($expense_id)
	{
		return $this->db
			->where('id', $expense_id)
			->delete('school_expense');
	}

	/** function to get total credit amount */
	public function getTotalCredit()
	{
		$sql = 'SELECT SUM(amount) as credit FROM `school_expense` WHERE type="Credit" AND MONTH(date) = MONTH(CURRENT_DATE())';
		$query = $this->db->query($sql);
		return $query->row();
	}

	/** function to get total debit amount */
	public function getTotalDebit()
	{
		$sql = 'SELECT SUM(amount) as debit FROM `school_expense` WHERE type="Debit" AND MONTH(date) = MONTH(CURRENT_DATE())';
		$query = $this->db->query($sql);
		return $query->row();
	}

	/** function to get total debit amount */
	public function getTotalSettlement()
	{
		$sql = 'SELECT SUM(amount) as settle FROM `school_expense` WHERE type="Settlement" AND MONTH(date) = MONTH(CURRENT_DATE())';
		$query = $this->db->query($sql);
		return $query->row();
	}

	public function getCollectAmount()
	{
		$sql = 'SELECT SUM(a.total_compensation) AS collect FROM (
					SELECT t.class_id, (f.total_amount * t.student) AS total_compensation FROM (
						SELECT COUNT(s.id) student, s.class_id FROM student s GROUP BY s.class_id) AS t
							JOIN fees f ON t.class_id = f.class_id) AS a';
		$query = $this->db->query($sql);
		return $query->row();
	}
}
