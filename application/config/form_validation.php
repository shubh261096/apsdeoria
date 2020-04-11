<?php

$config = [

	'add_contact_rules' => [
		[
			'field' => 'full_name',
			'label' => 'Full Name',
			'rules' => 'trim|required'
		],
		[
			'field' => 'phone_no',
			'label' => 'Phone Number',
			'rules' => 'trim|required|numeric|max_length[10]|regex_match[/^[0-9]{10}$/]'
		],
		[
			'field' => 'email',
			'label' => 'Email Address',
			'rules' => 'trim|required|valid_email'
		],
		[
			'field' => 'subject',
			'label' => 'Subject',
			'rules' => 'trim|required'
		],
		[
			'field' => 'message',
			'label' => 'Message',
			'rules' => 'trim|required'
		]
	],
	'add_admission_rules' => [
		[
			'field' => 'full_name',
			'label' => 'Student Fullname',
			'rules' => 'trim|required'
		],
		[
			'field' => 'phone_no',
			'label' => 'Phone Number',
			'rules' => 'trim|required|numeric|max_length[10]|regex_match[/^[0-9]{10}$/]'
		],
		[
			'field' => 'email',
			'label' => 'Email Address',
			'rules' => 'trim|valid_email'
		]
	]
];
