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
}
