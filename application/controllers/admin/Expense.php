<?php
defined('BASEPATH') or exit('No direct script access allowed');

class Expense extends CI_Controller
{

  public function __construct()
  {
    parent::__construct();
    $this->load->view('admin/includes/header');
    $this->load->view('admin/includes/footer');
    if (!$this->session->userdata('user_id'))
      return redirect('admin');

    $this->load->model('admin/ExpenseModel', 'ExpenseModel');
  }

  public function index()
  {
    $query = $this->ExpenseModel->getExpense();
    $data['EXPENSES'] = null;
    if ($query) {
      $data['EXPENSES'] =  $query;
    }
    $this->load->view('admin/expense/expense', $data);
  }

  public function add()
  {
    $data['type'] = array(
      'Credit' => 'Credit', 'Debit' => 'Debit'
    );
    $data['category'] = array(
      'Bills' => 'Bills & Utilities', 'Education' => 'Education', 'Entertainment' => 'Entertainment', 'Fees' => 'Fees & Charges', 'Food' => 'Food & Beverages', 'Gifts' => 'Gifts & Donation', 'Stationery' => 'Stationery',
      'Health' => 'Health & Fitness', 'Transportation' => 'Transportation', 'Function' => 'School Function', 'Others' => 'Others'
    );
    $this->load->view('admin/expense/add_expense', $data);
  }

  public function insert()
  {
    $this->form_validation->set_error_delimiters('<p class="text-danger">', '</p>');
    if ($this->form_validation->run('add_expense_rules') == FALSE) {
      $data['type'] = array(
        'Credit' => 'Credit', 'Debit' => 'Debit'
      );
      $data['category'] = array(
        'Bills' => 'Bills & Utilities', 'Education' => 'Education', 'Entertainment' => 'Entertainment', 'Fees' => 'Fees & Charges', 'Food' => 'Food & Beverages', 'Gifts' => 'Gifts & Donation', 'Stationery' => 'Stationery',
        'Health' => 'Health & Fitness', 'Transportation' => 'Transportation', 'Function' => 'School Function', 'Others' => 'Others'
      );
      $this->load->view('admin/expense/add_expense', $data);
    } else {
      $post = $this->input->post();
      unset($post['submit']);
      if ($this->ExpenseModel->addExpense($post)) {
        $this->session->set_flashdata('feedback', 'Added Succefully');
        $this->session->set_flashdata('feedback_class', 'alert-success');
      } else {
        $this->session->set_flashdata('feedback', 'Not Added');
        $this->session->set_flashdata('feedback_class', 'alert-danger');
      }
      return redirect('expense');
    }
  }

}
