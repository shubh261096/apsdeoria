<?php
defined('BASEPATH') or exit('No direct script access allowed');

require APPPATH . 'libraries/REST_Controller.php';
require APPPATH . 'libraries/FirebaseLib.php';


class Unused extends REST_Controller
{
    public function __construct()
    {
        parent::__construct();
        $this->load->database();
        $this->load->model('api/v2/qa/WebhookModel', 'WebhookModel');
        $this->load->helper('commonqa');
    }

    public function otpless_webhook_post($data)
    {

        // Dummy Success Response WhatsApp send us when user sends message

        $response = array();
        if (
            (array_key_exists('entry', $data) && isset($data['entry'])) &&
            (array_key_exists('changes', $data['entry'][0]) && isset($data['entry'][0])) &&
            (array_key_exists('value', $data['entry'][0]['changes'][0]) && isset($data['entry'][0]['changes'][0])) &&
            (array_key_exists('messages', $data['entry'][0]['changes'][0]['value']) && isset($data['entry'][0]['changes'][0]['value']))
        ) {
            if (
                (array_key_exists('type', $data['entry'][0]['changes'][0]['value']['messages'][0]) && isset($data['entry'][0]['changes'][0]['value']['messages'][0])) &&
                (array_key_exists('timestamp', $data['entry'][0]['changes'][0]['value']['messages'][0]) && isset($data['entry'][0]['changes'][0]['value']['messages'][0]))
            ) {
                $type = $data['entry'][0]['changes'][0]['value']['messages'][0]['type'];
                $timestamp = $data['entry'][0]['changes'][0]['value']['messages'][0]['timestamp'];
                // echo $type;
                // echo $timestamp;
            }

            if (
                (array_key_exists('text', $data['entry'][0]['changes'][0]['value']['messages'][0]) && isset($data['entry'][0]['changes'][0]['value']['messages'][0])) &&
                (array_key_exists('body', $data['entry'][0]['changes'][0]['value']['messages'][0]['text']) && isset($data['entry'][0]['changes'][0]['value']['messages'][0]['text']))
            ) {
                $message_text = $data['entry'][0]['changes'][0]['value']['messages'][0]['text']['body'];
                // echo $message_text;
            }

            if (
                (array_key_exists('profile', $data['entry'][0]['changes'][0]['value']['contacts'][0]) && isset($data['entry'][0]['changes'][0]['value']['contacts'][0])) &&
                (array_key_exists('wa_id', $data['entry'][0]['changes'][0]['value']['contacts'][0]) && isset($data['entry'][0]['changes'][0]['value']['contacts'][0])) &&
                (array_key_exists('name', $data['entry'][0]['changes'][0]['value']['contacts'][0]['profile']) && isset($data['entry'][0]['changes'][0]['value']['contacts'][0]['profile']))
            ) {
                $number = $data['entry'][0]['changes'][0]['value']['contacts'][0]['wa_id'];
                $name = $data['entry'][0]['changes'][0]['value']['contacts'][0]['profile']['name'];
                // echo $number;
                // echo $name;
            }
            $result = $this->WebhookModel->get_callBackUrl($message_id, $message_number);

            if (!empty($result)) {
                $this->WebhookModel->update_status($message_id);

                $response = array(
                    'return_url' => $result->vendor_return_url,
                    'name' => $result->vendor_name,
                    'app_id' => $result->vendor_app_id,
                    'transaction_id' => $result->transaction_id,
                    'message_text' => $message_text,
                    'message_number' => $message_number
                );
                $response['error'] = false;
                $httpStatus = REST_Controller::HTTP_OK;
                $this->sendPushNotification($response);
                $this->senDataToFirebase($response);
            } else {
                $response['error'] = true;
                $response['message'] = "User han't responded to the text yet or already responded";
                $httpStatus = REST_Controller::HTTP_OK;
            }
        } else {
            $response['error'] = true;
            $response['message'] = "Key doesn't exists";
            $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
        }

        $this->response($response, $httpStatus);
    }



    // Otpless
    public function otpless_vendor_post()
    {
        if (isTheseParametersAvailable(array('app_id', 'redirect_url', 'platform'))) {
            $response = array();
            $app_id = $this->input->post('app_id');
            $redirect_url = $this->input->post('redirect_url');
            $platform = $this->input->post('platform');
            $timestamp = time();
            $transaction_id = 'txnId' . uniqid() . '_' . preg_replace('/(0)\.(\d+) (\d+)/', '$3$1$2', microtime());

            $isVendorAvailable = $this->WebhookModel->isVendorAvailable($app_id);

            if ($isVendorAvailable) {
                // Adding data to DB, so later we can check with message_id and send the payload data response coming from whatsapp to vendor
                $add_array = array('timestamp' => $timestamp, 'app_id' => $app_id, 'redirect_url' => $redirect_url, 'status' => 0, 'platform' => $platform, 'transaction_id' => $transaction_id);
                $this->WebhookModel->add_click_button($add_array);

                $response['error'] = false;
                $response['message'] = "Response added to DB";
                $httpStatus = REST_Controller::HTTP_OK;
            } else {
                $add_array = array('vendor_name' => $app_id, 'vendor_app_id' => $app_id, 'vendor_return_url' => $redirect_url);
                $this->WebhookModel->add_vendor($add_array);
                $response['error'] = true;
                $response['message'] = "Vendor Added to DB";
                $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
            }
        } else {
            $response['error'] = true;
            $response['message'] = "Parameters not found";
            $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
        }
        $this->response($response, $httpStatus);
    }



    public function verify_get($transaction_id)
    {
        $response = array();


        $result = $this->WebhookModel->get_Status($transaction_id);
        if (!empty($result)) {
            $this->WebhookModel->update_status_txnId($transaction_id);
            $response = array(
                'transaction_id' => $transaction_id
            );
            $response['error'] = false;
            $response['message'] = "Thank you. WhatsApp Verified";
            $httpStatus = REST_Controller::HTTP_OK;
        } else {
            $response['error'] = true;
            $response['message'] = "WhatsApp Already or User Not Responseded yet";
            $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
        }


        $this->response($response, $httpStatus);
    }

    public function verify_post()
    {
        if (isTheseParametersAvailable(array('txn_id'))) {
            $response = array();
            // $app_id = $this->input->post('app_id');
            $transaction_id = $this->input->post('txn_id');
            // $number = $this->input->post('number');

            // $isVendorAvailable = $this->WebhookModel->isVendorAvailable($app_id);

            if (true) {
                $result = $this->WebhookModel->get_Status($transaction_id);
                if (!empty($result)) {
                    $this->WebhookModel->update_status_txnId($transaction_id);
                    $response = array(
                        'transaction_id' => $transaction_id
                    );
                    $response['error'] = false;
                    $response['message'] = "WhatsApp Verified";
                    $httpStatus = REST_Controller::HTTP_OK;
                } else {
                    $response['error'] = true;
                    $response['message'] = "WhatsApp Already or User Not Responseded yet";
                    $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
                }
            } else {
                $response['error'] = true;
                $response['message'] = "Vendor Not available";
                $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
            }
        } else {
            $response['error'] = true;
            $response['message'] = "Parameters not found";
            $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
        }
        $this->response($response, $httpStatus);
    }


    function sendPushNotification($response)
    {
      // get main CodeIgniter object
      $ci = get_instance();
      $firebase_api = 'AAAAj0SEBA8:APA91bFOZyfx4lhGoe0jMZzeDpj_yzbuqgG6KWqE9JVh6GzGIPA3j0IlwvEnAR7CMAC9d3GNDOkLPobbFA3PaDZ3Y6eVZVtfmOJS6eFHXCvXG7S2mHXkvMiJ7hwEPMN1j0EhpwNvo6Z3';
  
  
      $data['fields'] = array(
        'to' => '/topics/' . $response['app_id'],
        'data' => $response,
      );
  
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
      }
  
      // Close connection
      curl_close($ch);
    }
}




// --------------------------------- SOME IMPORTANT UNI CODES DECODED VALUES ----------------------------------- //


/* // OTPLESS
whatsapp://send?phone=911141169439&text=%E2%80%8E%E2%80%8C%E2%80%8D%E2%80%8B%E2%80%8C%E2%80%8D%E2%80%8D%E2%80%8C%E2%80%8D%E2%80%8D%E2%80%8D%E2%80%8D%E2%80%8CSend%20this%20message%20to%20continue%20to%20Apsdeoria



%E2%80%8E
%E2%80%8C
%E2%80%8D
%E2%80%8B
%E2%80%8C
%E2%80%8D
%E2%80%8D
%E2%80%8C
%E2%80%8D
%E2%80%8D
%E2%80%8D
%E2%80%8D
%E2%80%8C

// OTPLESS
whatsapp://send?phone=911141169439&text=%E2%80%8E%E2%80%8C%E2%80%8D%E2%80%8B%E2%80%8C%E2%80%8D%E2%80%8D%E2%80%8C%E2%80%8D%E2%80%8E%E2%80%8B%E2%80%8E%E2%80%8ESend%20this%20message%20to%20continue%20to%20Apsdeoria


%E2%80%8E
%E2%80%8C
%E2%80%8D
%E2%80%8B
%E2%80%8C
%E2%80%8D
%E2%80%8D
%E2%80%8C
%E2%80%8D
%E2%80%8E
%E2%80%8B
%E2%80%8E
%E2%80%8E


// BYPASS
whatsapp://send?
phone=15550020663&text=%E2%80%8D%E2%80%8B%E2%80%8C%E2%80%8E%E2%80%8C%E2%80%8E%E2%80%8C%E2%80%8E%E2%80%8C%E2%80%8B%E2%80%8D%E2%80%8E%E2%80%8D%E2%80%8DContinue%20to%20apsdeoria


%E2%80%8D
%E2%80%8B
%E2%80%8C
%E2%80%8E
%E2%80%8C
%E2%80%8E
%E2%80%8C
%E2%80%8E
%E2%80%8C
%E2%80%8B
%E2%80%8D
%E2%80%8E
%E2%80%8D
%E2%80%8D


// OTPLESS
whatsapp://send?phone=911141169439&text=%E2%80%8E%E2%80%8C%E2%80%8D%E2%80%8B%E2%80%8C%E2%80%8D%E2%80%8D%E2%80%8C%E2%80%8E%E2%80%8B%E2%80%8D%E2%80%8B%E2%80%8DSend%20this%20message%20to%20continue%20to%20Apsdeoria

%E2%80%8E
%E2%80%8C
%E2%80%8D
%E2%80%8B
%E2%80%8C
%E2%80%8D
%E2%80%8D
%E2%80%8C
%E2%80%8E
%E2%80%8B
%E2%80%8D
%E2%80%8B
%E2%80%8D



// BYPASS
whatsapp://send?
phone=15550020663&text=%E2%80%8E%E2%80%8C%E2%80%8D%E2%80%8B%E2%80%8C%E2%80%8D%E2%80%8E%E2%80%8D%E2%80%8D%E2%80%8D%E2%80%8D%E2%80%8E%E2%80%8DContinue%20to%20localhost

%E2%80%8E
%E2%80%8C
%E2%80%8D
%E2%80%8B
%E2%80%8C
%E2%80%8D
%E2%80%8E
%E2%80%8D
%E2%80%8D
%E2%80%8D
%E2%80%8D
%E2%80%8E
%E2%80%8D


// OTPLESS
whatsapp://send?phone=911141169439&text=%E2%80%8E%E2%80%8C%E2%80%8D%E2%80%8B%E2%80%8C%E2%80%8E%E2%80%8D%E2%80%8B%E2%80%8D%E2%80%8D%E2%80%8E%E2%80%8B%E2%80%8ESend%20this%20message%20to%20continue%20to%20Apsdeoria


%E2%80%8E
%E2%80%8C
%E2%80%8D
%E2%80%8B
%E2%80%8C
%E2%80%8E
%E2%80%8D
%E2%80%8B
%E2%80%8D
%E2%80%8D
%E2%80%8E
%E2%80%8B
%E2%80%8E


// OTPLESS
whatsapp://send?phone=911141169439&text=%E2%80%8E%E2%80%8C%E2%80%8D%E2%80%8B%E2%80%8C%E2%80%8E%E2%80%8D%E2%80%8B%E2%80%8E%E2%80%8B%E2%80%8C%E2%80%8D%E2%80%8DSend%20this%20message%20to%20continue%20to%20Apsdeori


%E2%80%8E
%E2%80%8C
%E2%80%8D
%E2%80%8B
%E2%80%8C
%E2%80%8E
%E2%80%8D
%E2%80%8B
%E2%80%8E
%E2%80%8B
%E2%80%8C
%E2%80%8D
%E2%80%8D


// BYPASS
whatsapp://send?phone=15550020663&text=%E2%80%8E%E2%80%8C%E2%80%8D%E2%80%8B%E2%80%8C%E2%80%8D%E2%80%8E%E2%80%8D%E2%80%8E%E2%80%8E%E2%80%8D%E2%80%8C%E2%80%8DSend%20this%20message%20to%20continue%20to%20apsdeoria


%E2%80%8E
%E2%80%8C
%E2%80%8D
%E2%80%8B
%E2%80%8C
%E2%80%8D
%E2%80%8E
%E2%80%8D
%E2%80%8E
%E2%80%8E
%E2%80%8D
%E2%80%8C
%E2%80%8D



// BYPASS
whatsapp://send?phone=15550020663&text=%E2%80%8E%E2%80%8C%E2%80%8D%E2%80%8B%E2%80%8C%E2%80%8D%E2%80%8B%E2%80%8D%E2%80%8B%E2%80%8D%E2%80%8C%E2%80%8D%E2%80%8BSend%20this%20message%20to%20continue%20to%20apsdeoria


%E2%80%8E
%E2%80%8C
%E2%80%8D
%E2%80%8B
%E2%80%8C
%E2%80%8D
%E2%80%8B
%E2%80%8D
%E2%80%8B
%E2%80%8D
%E2%80%8C
%E2%80%8D
%E2%80%8B
 */






 // ------------------------------------ SOME WHATSAPP RESPONSE CODES -------------------------------------- //

/*  {
  "object": "whatsapp_business_account",
  "entry": [
    {
      "id": "103797195987793",
      "changes": [
        {
          "value": {
            "messaging_product": "whatsapp",
            "metadata": {
              "display_phone_number": "15550020663",
              "phone_number_id": "101399809564251"
            },
            "contacts": [
              {
                "profile": {
                  "name": "Shubham Agrawal"
                },
                "wa_id": "918447050052"
              }
            ],
            "messages": [
              {
                "from": "918447050052",
                "id": "wamid.HBgMOTE4NDQ3MDUwMDUyFQIAEhgWM0VCMEIzNDMxM0E2MEZGODVGMzBFMwA=",
                "timestamp": "1680894686",
                "text": {
                  "body": "\u200e\u200c\u200c\u200e\u200e\u200d\u200e\u200e\u200c\u200d\u200e\u200b\u200cSend this message to continue to Apsdeoria"
                },
                "type": "text"
              }
            ]
          },
          "field": "messages"
        }
      ]
    }
  ]
} */