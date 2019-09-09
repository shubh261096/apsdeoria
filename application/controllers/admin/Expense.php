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
    $data['credit_amount'] = $this->ExpenseModel->getTotalCredit();
    $data['debit_amount'] = $this->ExpenseModel->getTotalDebit();
    $data['settle_amount'] = $this->ExpenseModel->getTotalSettlement();
    $data['collect_amount'] = $this->ExpenseModel->getCollectAmount();
    $data['EXPENSES'] = null;
    if ($query) {
      $data['EXPENSES'] =  $query;
    }
    $this->load->view('admin/expense/expense', $data);
  }

  public function add()
  {
    $this->load->view('admin/expense/add_expense');
  }

  public function insert()
  {
    $this->form_validation->set_error_delimiters('<p class="text-danger">', '</p>');
    if ($this->form_validation->run('add_expense_rules') == FALSE) {
      $this->load->view('admin/expense/add_expense');
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

  public function delete($expense_id)
  {
    if ($this->ExpenseModel->deleteExpense($expense_id)) {
      $this->session->set_flashdata('feedback', 'Deleted Succefully');
      $this->session->set_flashdata('feedback_class', 'alert-success');
    } else {
      $this->session->set_flashdata('feedback', 'Not deleted');
      $this->session->set_flashdata('feedback_class', 'alert-danger');
    }
    return redirect('expense');
  }

}
