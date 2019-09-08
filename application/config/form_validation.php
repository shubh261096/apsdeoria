<?php

$config = [

	'add_teacher_rules' => [
		[
			'field' => 'fullname',
			'label' => 'Full Name',
			'rules' => 'trim|required|max_length[20]'
		],
		[
			'field' => 'phone',
			'label' => 'Phone Number',
			'rules' => 'trim|required|numeric|max_length[10]|regex_match[/^[0-9]{10}$/]'
		],
		[
			'field' => 'email',
			'label' => 'Email Address',
			'rules' => 'trim|required|valid_email'
		],
		[
			'field' => 'dob',
			'label' => 'Date of birth',
			'rules' => 'trim|required'
		],
		[
			'field' => 'gender',
			'label' => 'Gender',
			'rules' => 'trim|required'
		]
	],

	'add_class_rules' => [
		[
			'field' => 'id',
			'label' => 'Class Id',
			'rules' => 'trim|required'

		],
		[
			'field' => 'name',
			'label' => 'Class Name',
			'rules' => 'trim|required'
		],
		[
			'field' => 'section',
			'label' => 'Class Section',
			'rules' => 'trim|required'
		],
		[
			'field' => 'year',
			'label' => 'Year',
			'rules' => 'trim|required|numeric|max_length[4]'
		]
	],

	'add_student_rules' => [
		[
			'field' => 'fullname',
			'label' => 'Full Name',
			'rules' => 'trim|required'
		],
		[
			'field' => 'dob',
			'label' => 'Date of birth',
			'rules' => 'trim|required'
		],
		[
			'field' => 'gender',
			'label' => 'Gender',
			'rules' => 'trim|required'
		]
	],

	'add_subject_rules' => [
		[
			'field' => 'id',
			'label' => 'Subject ID',
			'rules' => 'trim|required'

		],
		[
			'field' => 'name',
			'label' => 'Subject Name',
			'rules' => 'trim|required'
		],
		[
			'field' => 'description',
			'label' => 'Subject Description',
			'rules' => 'trim|required'
		]
	],

	'add_fees_rules' => [
		[
			'field' => 'fees_paid',
			'label' => 'Paying amount',
			'rules' => 'trim|required'

		]
	],

	'add_parent_rules' => [
		[
			'field' => 'fullname',
			'label' => 'Full Name',
			'rules' => 'trim|required'
		],
		[
			'field' => 'phone',
			'label' => 'Phone Number',
			'rules' => 'trim|required|numeric|max_length[10]|regex_match[/^[0-9]{10}$/]'
		],
		[
			'field' => 'address',
			'label' => 'Address',
			'rules' => 'trim|required'
		]
	],
	
	'add_expense_rules' => [
		[
			'field' => 'description',
			'label' => 'Description',
			'rules' => 'trim|required'
		],
		[
			'field' => 'amount',
			'label' => 'Amount',
			'rules' => 'trim|required'
		],
		[
			'field' => 'type',
			'label' => 'Expense type',
			'rules' => 'trim|required'
		]
	],
];
