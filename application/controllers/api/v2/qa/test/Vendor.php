<?php

use LDAP\Result;

defined('BASEPATH') or exit('No direct script access allowed');

require APPPATH . 'libraries/REST_Controller.php';
require APPPATH . 'libraries/FirebaseLib.php';

class Vendor extends REST_Controller
{

    public function __construct()
    {
        parent::__construct();
        header("Access-Control-Allow-Origin: *");
        header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS");
        header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
        $this->load->database();
        $this->load->model('api/v2/qa/WebhookModel', 'WebhookModel');
        $this->load->helper('commonqa');
    }


    public function index_get()
    {
        $response = array();
        $response['error'] = true;
        $response['message'] = "Unauthorized Access";
        $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
        $this->response($response, $httpStatus);
    }

    // -------------------------------------------------------------------- //
    // ---------------This code is for Pro Yes/No Reply-------------------- //
    // -------------------------------------------------------------------- //
    public function proV2_post()
    {
        if (isTheseParametersAvailable(array('app_id', 'number', 'platform'))) {
            $response = array();
            $app_id = $this->input->post('app_id');
            $number = $this->input->post('number');
            $platform = $this->input->post('platform');

            $isVendorAvailable = $this->WebhookModel->isVendorAvailable($app_id);

            if ($isVendorAvailable) {
                $authorization_key = "Bearer EAAau0u0nhZBsBAKAAGr5N3ntnZAjCBxcwMl6i9TeSiMZCCw8AH3E0mf9vujSNGflcrFHxNHqSMC2N29TyYuop6uqWZA5jtbZAZAMmGiZA9VRVGmxm9IHSi20ENZBuDb6sOufsKJU7BvN01ofh0yupNcRA72q6K3Tg97HFY1qgckSKLC8IR2EVhHc";

                $data = "{ 'messaging_product': 'whatsapp', 'to': $number, 'type': 'template', 'template': { 'name': 'verify_login', 'language': { 'code': 'en' } } }";
                // Set POST variables
                $url = 'https://graph.facebook.com/v15.0/101399809564251/messages';

                $transaction_id = 'txnId' . uniqid() . '_' . preg_replace('/(0)\.(\d+) (\d+)/', '$3$1$2', microtime());

                $headers = array(
                    'Authorization: ' . $authorization_key,
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

                curl_setopt($ch, CURLOPT_POSTFIELDS, $data);

                // Execute post
                $curl_response = json_decode(curl_exec($ch));


                /**$
                 * response = '{
                 *              "messaging_product": "whatsapp",
                 *              "contacts": [
                 *                              {
                 *                                  "input": "918447050052",
                 *                                  "wa_id": "918447050052"
                 *                              }
                 *                          ],
                 *              "messages": [
                 *                              {
                 *                                  "id": "wamid.HBgMOTE4NDQ3MDUwMDUyFQIAERgSMzBFMDY4QkY3RjFDRDkyM0RBAA=="
                 *                              }
                 *                          ]
                 *             }';
                 * */

                if (
                    (property_exists($curl_response, 'contacts') && isset($curl_response->contacts)) &&
                    (property_exists($curl_response, 'messages') && isset($curl_response->messages))
                ) {
                    $message_number = $curl_response->contacts[0]->input;
                    $message_id = $curl_response->messages[0]->id;
                    $response = array(
                        'message_number' => $message_number,
                        'transaction_id' => $transaction_id,
                        'firebaseConfig' => $this->getFirebaseConfig()
                    );

                    // Adding data to DB, so later we can check with message_id and send the payload data response coming from whatsapp to vendor
                    $add_array = array('message_id' => $message_id, 'message_number' => $message_number, 'transaction_id' => $transaction_id, 'status' => 0, 'app_id' => $app_id, 'platform' => $platform);
                    $this->WebhookModel->addMessageSentOnWhatsappProVendor($add_array);

                    $response['error'] = false;
                    $response['message'] = "Message sent to WhatsApp";
                    $httpStatus = REST_Controller::HTTP_OK;
                } else {
                    $response['error'] = true;
                    $response['message'] = "WhatsApp Token Expired";
                    $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
                }

                // Close connection
                curl_close($ch);
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


    // -------------------------------------------------------------------- //
    // ---------------This code is for Pro Yes Button Deeplink------------- //
    // -------------------------------------------------------------------- //

    public function pro_post()
    {
        if (isTheseParametersAvailable(array('app_id', 'number', 'platform', 'redirect_url'))) {
            $response = array();
            $app_id = $this->input->post('app_id');
            $number = $this->input->post('number');
            $platform = $this->input->post('platform');
            $redirect_url = $this->input->post('redirect_url');

            $isVendorAvailable = $this->WebhookModel->isVendorAvailable($app_id);

            if ($isVendorAvailable) {
                $authorization_key = "Bearer EAAau0u0nhZBsBAKAAGr5N3ntnZAjCBxcwMl6i9TeSiMZCCw8AH3E0mf9vujSNGflcrFHxNHqSMC2N29TyYuop6uqWZA5jtbZAZAMmGiZA9VRVGmxm9IHSi20ENZBuDb6sOufsKJU7BvN01ofh0yupNcRA72q6K3Tg97HFY1qgckSKLC8IR2EVhHc";

                // Set POST variables
                $url = 'https://graph.facebook.com/v15.0/101399809564251/messages';

                $transaction_id = 'txnId' . uniqid() . '_' . preg_replace('/(0)\.(\d+) (\d+)/', '$3$1$2', microtime());

                $dataForDeeplink = "{
                    'messaging_product': 'whatsapp',
                    'recipient_type': 'individual',
                    'to': $number,
                    'type': 'template',
                    'template': {
                        'name': 'button_deeplink',
                        'language': {
                            'code': 'en'
                        },
                        'components': [
                            {
                                'type': 'button',
                                'sub_type' : 'url',
                                'index': '0', 
                                'parameters': [
                                    {
                                        'type': 'text',
                                        'text': 'verifypro/$transaction_id'
                                    }
                                ]
                            }
                        ]
                    }
                }";
                $headers = array(
                    'Authorization: ' . $authorization_key,
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

                curl_setopt($ch, CURLOPT_POSTFIELDS, $dataForDeeplink);

                // Execute post
                $curl_response = json_decode(curl_exec($ch));

                // Close connection
                curl_close($ch);


                /** Success response that comes from Whatsapp
                 * 
                 * $response = '{
                 *              "messaging_product": "whatsapp",
                 *              "contacts": [
                 *                              {
                 *                                  "input": "918447050052",
                 *                                  "wa_id": "918447050052"
                 *                              }
                 *                          ],
                 *              "messages": [
                 *                              {
                 *                                  "id": "wamid.HBgMOTE4NDQ3MDUwMDUyFQIAERgSMzBFMDY4QkY3RjFDRDkyM0RBAA=="
                 *                              }
                 *                          ]
                 *              }';
                 * 
                 * */

                if (
                    (property_exists($curl_response, 'contacts') && isset($curl_response->contacts)) &&
                    (property_exists($curl_response, 'messages') && isset($curl_response->messages))
                ) {
                    $message_number = $curl_response->contacts[0]->wa_id;
                    $message_id = $curl_response->messages[0]->id;
                    $response = array(
                        'message_number' => $message_number,
                        'transaction_id' => $transaction_id,
                        'firebaseConfig' => $this->getFirebaseConfig()
                    );

                    // Adding data to DB, so later we can check with message_id and send the payload data response coming from whatsapp to vendor
                    $add_array = array(
                        'message_id' => $message_id,
                        'message_number' => $message_number,
                        'transaction_id' => $transaction_id,
                        'status' => 0,
                        'app_id' => $app_id,
                        'platform' => $platform,
                        'redirect_url' => $redirect_url
                    );
                    $this->WebhookModel->addMessageSentOnWhatsappProVendor($add_array);

                    $response['error'] = false;
                    $response['message'] = "Message sent to WhatsApp";
                    $httpStatus = REST_Controller::HTTP_OK;
                } else {
                    $response['error'] = true;
                    $response['message'] = "WhatsApp Token Expired";
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

    public function verifypro_get($transaction_id)
    {
        $response = array();
        $result = $this->WebhookModel->verifyTranIdWhatsappProVendor($transaction_id);
        if (!empty($result)) {
            $this->WebhookModel->updateTranIdWhatsappProVendor($transaction_id);
            $response = array(
                'transaction_id' => $result->transaction_id,
                'webhook_url' => $result->vendor_webhook_url,
                'message_number' => $result->message_number,
                'redirect_url' => $result->redirect_url,
                'app_id' => $result->vendor_app_id,
                'is_verifed' => true
            );
            $response['error'] = false;
            $response['message'] = "Thank you. WhatsApp Verified";
            $httpStatus = REST_Controller::HTTP_OK;

            // Sendind data to firebase so that response gets on the console
            $this->sendDataToFirebase($response);

            // $this->sendDataToWebhookUrl($response , $result->vendor_webhook_url, "");
            $this->redirect_to_url($result->redirect_url);
        } else {
            $response['error'] = true;
            $response['message'] = "Link Expired!";
            $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
        }


        $this->response($response, $httpStatus);
    }


    // -------------------------------------------------------------------- //
    // ------------This code is for Free version Yes Button Deeplink------- //
    // -------------------------------------------------------------------- //

    public function free_post()
    {
        if (isTheseParametersAvailable(array('app_id', 'redirect_url', 'platform', 'unicode_char'))) {
            $response = array();
            $app_id = $this->input->post('app_id');
            $redirect_url = $this->input->post('redirect_url');
            $platform = $this->input->post('platform');
            $unicode_char = $this->input->post('unicode_char');
            $timestamp = time();
            $transaction_id = 'txnId' . uniqid() . '_' . preg_replace('/(0)\.(\d+) (\d+)/', '$3$1$2', microtime());

            $isVendorAvailable = $this->WebhookModel->isVendorAvailable($app_id);

            if (!$isVendorAvailable) {
                $add_array = array('vendor_name' => $app_id, 'vendor_app_id' => $app_id);
                $this->WebhookModel->add_vendor($add_array);
            }

            // Adding data to DB, so later we can check with message_id and send the payload data response coming from whatsapp to vendor
            $add_array = array('unicode_char' => $unicode_char, 'timestamp' => $timestamp, 'app_id' => $app_id, 'redirect_url' => $redirect_url, 'status' => 0, 'platform' => $platform, 'transaction_id' => $transaction_id);
            $this->WebhookModel->add_click_button($add_array);
            $response = array(
                'transaction_id' => $transaction_id,
                'firebaseConfig' => $this->getFirebaseConfig()
            );
            $response['error'] = false;
            $response['message'] = "Response added to DB";
            $httpStatus = REST_Controller::HTTP_OK;
        } else {
            $response['error'] = true;
            $response['message'] = "Parameters not found";
            $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
        }
        $this->response($response, $httpStatus);
    }

    public function verifyfree_get($transaction_id)
    {
        $response = array();

        $result = $this->WebhookModel->verifyTranIdWhatsappFreeVendor($transaction_id);
        if (!empty($result)) {
            $this->WebhookModel->updateTranIdWhatsappFreeVendor($transaction_id);
            $response = array(
                'app_id' => $result->app_id,
                'transaction_id' => $transaction_id,
                'wa_name' => $result->wa_name,
                'wa_number' => $result->wa_number,
                'isVerifed' => true
            );
            $response['redirect_url'] = $result->redirect_url;
            $response['error'] = false;
            $response['message'] = "Thank you. WhatsApp Verified";
            $httpStatus = REST_Controller::HTTP_OK;

            // Sending data to firebase so that response gets on the console
            $this->sendDataToFirebase($response);

            // $this->sendDataToWebhookUrl($response , $result->vendor_webhook_url, "");
            // $this->redirect_to_url($result->redirect_url);
        } else {
            $response['redirect_url'] = null;
            $response['error'] = true;
            $response['message'] = "Link expired!";
            $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
        }

        
        // $this->response($response, $httpStatus);
        $data['value'] = $response['redirect_url'];
        $data['message'] = $response['message'];
        $data['error'] = $response['error'];
        $this->load->view('lazyclick/thankyou', $data);
    }


    // -------------------------------------------------------------------- //
    // --------------------------Common Function--------------------------- //
    // -------------------------------------------------------------------- //
    private function sendDataToFirebase($response)
    {
        $url = "https://zerootp-100-default-rtdb.firebaseio.com";
        $token = "GdQu9hJkcx9dSnr8TbtbGMFbtRqNZJvfMke8FVQf";
        $path = $response['app_id'];

        $firebase = new FirebaseLib($url, $token);
        $firebase->set($path . '/' . $response['transaction_id'], $response);
    }

    private function redirect_to_url($url)
    {
        redirect($url);
    }

    private function sendDataToWebhookUrl($response, $url, $headerAuthKey)
    {
        if ($headerAuthKey != null) {
            $headers = array(
                'Authorization: key=' . $headerAuthKey,
                'Content-Type: application/json'
            );
        }


        // Open connection
        $ch = curl_init();

        // Set the url, number of POST vars, POST data
        curl_setopt($ch, CURLOPT_URL, $url);

        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

        // Disabling SSL Certificate support temporarily
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);

        curl_setopt($ch, CURLOPT_POSTFIELDS, $response);

        // Execute post
        $data['result'] = curl_exec($ch);
        if ($data['result'] === FALSE) {
            die('Curl failed: ' . curl_error($ch));
        }

        // Close connection
        curl_close($ch);
    }


    private function getFirebaseConfig()
    {
        $firebaseConfig = array(
            'apiKey' => 'AIzaSyD8rl2PACsA5FHpa1NCdigR7BlfrPSY8GI',
            'authDomain' => 'zerootp-100.firebaseapp.com',
            'databaseURL' => 'https://zerootp-100-default-rtdb.firebaseio.com',
            'projectId' => 'zerootp-100',
            'storageBucket' => 'zerootp-100.appspot.com',
            'messagingSenderId' => '615329825807',
            'appId' => '1:615329825807:web:98cf7ee2b6a8f691198f7a'
        );

        return $firebaseConfig;
    }
}