<?php
defined('BASEPATH') or exit('No direct script access allowed');

require APPPATH . 'libraries/NotificationPOJO.php';

class Notification extends CI_Controller
{
    public function __construct()
    {
        parent::__construct();
        $this->load->view('admin/includes/header');
        $this->load->view('admin/includes/footer');
        if (!$this->session->userdata('user_id'))
            return redirect('admin');

        $this->load->model('admin/NotificationModel');
    }

    public function index()
    {
        $data['result'] = NULL;
        $data['fields'] = NULL;
        $this->load->view('admin/notification', $data);
    }

    public function notify()
    {
        $config = [
            'upload_path' => 'asset/images/notification/',
            'allowed_types' => 'jpg|png|jpeg|PNG',
        ];

        $this->load->library('upload', $config);
        $this->upload->initialize($config);
        if (empty($_FILES['image']['name'])) {
            $imageUrl = null;
        } else {
            if ($this->upload->do_upload('image')) {
                $data = $this->upload->data();
                $imageUrl = base_url($config['upload_path'] . $data['raw_name'] . $data['file_ext']);
            } else {
                $data['upload_error'] = $this->upload->display_errors();
                $data['result'] = NULL;
                $this->load->view('admin/notification', $data);
                return;
            }
        }

        $notification = new NotificationPOJO();
        $title = trim($this->input->post('title'));
        $message = trim($this->input->post('message'));
        $action = $this->input->post('action');

        $actionDestination = trim($this->input->post('action_destination'));
        if ($actionDestination == '') {
            $action = '';
        }

        $notification->setTitle($title);
        $notification->setMessage($message);
        $notification->setImage($imageUrl);
        $notification->setAction($action);
        $notification->setActionDestination($actionDestination);

        $firebase_token = $this->input->post('firebase_token');
        $firebase_api = 'AAAALviGhbM:APA91bEg3inOYZLskeCtRZZfitG11bU92RAOUkR_i6Fm_FJmIW1xw9ZJvNtsQ24XQYgGnqevvo2S4q7zTA7SZxPWlw8-LM5efpgTajDnKCDUzkk7Qz6OsI_Jm8XfR3XOhFPgc_VbTl2L';

        $topic = $this->input->post('topic');

        $requestData = $notification->getNotificatin();

        if ($this->input->post('send_to') == 'topic') {
            $data['fields'] = array(
                'to' => '/topics/' . $topic,
                'data' => $requestData,
            );
        } else {
            $data['fields'] = array(
                'to' => $firebase_token,
                'data' => $requestData,
            );
        }

        // Set POST variables
        $url = 'https://fcm.googleapis.com/fcm/send';

        $headers = array(
            'Authorization: key=' . $firebase_api,
            'Content-Type: application/json'
        );

        // Open connection
        $ch = curl_init();

        // Set the url, number of POST vars, POST data
        curl_setopt($ch, CURLOPT_URL, $url);

        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

        // Disabling SSL Certificate support temporarily
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);

        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($data['fields']));

        // Execute post
        $data['result'] = curl_exec($ch);
        if ($data['result'] === FALSE) {
            die('Curl failed: ' . curl_error($ch));
        } else {
            if (!empty($title)) {
                $post['title'] = $title;
                $post['message'] = $message;
                $post['image_url'] = $imageUrl;
                $post['action'] = $action;
                $post['action_destination'] = $actionDestination;
                $post['send_to'] = $this->input->post('send_to');
                $post['topic'] = $topic;
                $post['firebase_token'] = $firebase_token;
                $this->NotificationModel->add_notification($post);
            }
        }

        // Close connection
        curl_close($ch);
        $this->load->view('admin/notification', $data);
    }
}
