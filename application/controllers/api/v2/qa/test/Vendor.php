<?php

defined('BASEPATH') or exit ('No direct script access allowed');

require APPPATH . 'libraries/REST_Controller.php';
require APPPATH . 'libraries/FirebaseLib.php';

class Vendor extends REST_Controller
{
    public function __construct()
    {
        parent::__construct();
        header('Access-Control-Allow-Origin: *');
        header('Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS');
        header('Access-Control-Allow-Headers: *');
        $this->load->database();
        $this->load->model('api/v2/qa/WebhookModel', 'WebhookModel');
        $this->load->helper('commonqa');
    }

    function generateUniqueID($length = 4)
    {
        $uniqueID = uniqid();
        $randomString = substr($uniqueID, -$length);

        return $randomString;
    }

    public function index_options()
    {
        return $this->response(NULL, REST_Controller::HTTP_OK);
    }

    public function index_get()
    {
        $response = array();
        $response['error'] = true;
        $response['message'] = 'Unauthorized Access';
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
                $authorization_key = 'Bearer EAAau0u0nhZBsBAKAAGr5N3ntnZAjCBxcwMl6i9TeSiMZCCw8AH3E0mf9vujSNGflcrFHxNHqSMC2N29TyYuop6uqWZA5jtbZAZAMmGiZA9VRVGmxm9IHSi20ENZBuDb6sOufsKJU7BvN01ofh0yupNcRA72q6K3Tg97HFY1qgckSKLC8IR2EVhHc';

                $data = "{ 'messaging_product': 'whatsapp', 'to': $number, 'type': 'template', 'template': { 'name': 'verify_login', 'language': { 'code': 'en' } } }";
                // Set POST variables
                $url = 'https://graph.facebook.com/v15.0/101399809564251/messages';

                $transaction_id = $this->generateUniqueID(4);

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
                    $response['message'] = 'Message sent to WhatsApp';
                    $httpStatus = REST_Controller::HTTP_OK;
                } else {
                    $response['error'] = true;
                    $response['message'] = 'WhatsApp Token Expired';
                    $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
                }

                // Close connection
                curl_close($ch);
            } else {
                $response['error'] = true;
                $response['message'] = 'Vendor Not available';
                $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
            }
        } else {
            $response['error'] = true;
            $response['message'] = 'Parameters not found';
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
                $authorization_key = 'Bearer EAAIvjH6tjOwBAICW8fUY14ns1TaJPx26j0imcffcVkxW7VXXn44XSMZC90emVdZAnWFMLsO44XBSlObr6HHtZAAm7Anhr7GWMxrlXFgsu0PKiZAKjYaT60lH2wkkC6TP3njqYQm3zveLKGyBsDSL7mNvyFgbZBZBndflc9TKaGFTyFer2hz0xi';

                // Set POST variables
                $url = 'https://graph.facebook.com/v16.0/100254659725118/messages';

                $transaction_id = $this->generateUniqueID(9);

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
                                        'text': 'vpro/$transaction_id'
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

                /**
                 * Success response that comes from Whatsapp
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
                 *
                 */
                if (
                    (property_exists($curl_response, 'contacts') && isset($curl_response->contacts)) &&
                    (property_exists($curl_response, 'messages') && isset($curl_response->messages))
                ) {
                    $message_number = $curl_response->contacts[0]->wa_id;
                    $message_id = $curl_response->messages[0]->id;

                    if ($platform == 'web') {
                        $response = array(
                            'message_number' => $message_number,
                            'transaction_id' => $transaction_id,
                            'firebaseConfig' => $this->getFirebaseConfig()
                        );
                    } else {
                        $response = array(
                            'message_number' => $message_number,
                            'transaction_id' => $transaction_id
                        );
                    }

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
                    $response['message'] = 'Message sent to WhatsApp';
                    $httpStatus = REST_Controller::HTTP_OK;
                } else {
                    $response['error'] = true;
                    $response['message'] = 'WhatsApp Token Expired';
                    $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
                }
            } else {
                $response['error'] = true;
                $response['message'] = 'Vendor Not available';
                $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
            }
        } else {
            $response['error'] = true;
            $response['message'] = 'Parameters not found';
            $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
        }
        $this->response($response, $httpStatus);
    }

    public function verifypro_get($transaction_id)
    {
        $response = array();
        $result = $this->WebhookModel->verifyTranIdWhatsappProVendor($transaction_id);
        if (!empty($result)) {
            if ($result->platform == 'android') {
                $finalUrl = $result->redirect_url . '.lazyclick.in:://pro/' . $transaction_id;
                if (!empty($finalUrl)) {
                    // echo $finalUrl;
                    $val = $this->scrape_post($finalUrl);
                    // echo $val;
                    redirect($val);
                }
            } else {
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
                $response['message'] = 'Thank you. WhatsApp Verified';
                $httpStatus = REST_Controller::HTTP_OK;

                // Sendind data to firebase so that response gets on the console
                $this->sendDataToFirebase($response);

                // $this->sendDataToWebhookUrl($response , $result->vendor_webhook_url, "");
                // $this->redirect_to_url($result->redirect_url);
                $data['value'] = $response['redirect_url'];
                $data['message'] = $response['message'];
                $data['error'] = $response['error'];
                $this->load->view('lazyclick/thankyou', $data);
            }
        } else {
            $response['redirect_url'] = null;
            $response['error'] = true;
            $response['message'] = 'Link expired!';
            $httpStatus = REST_Controller::HTTP_BAD_REQUEST;

            $data['value'] = $response['redirect_url'];
            $data['message'] = $response['message'];
            $data['error'] = $response['error'];
            $this->load->view('lazyclick/thankyou', $data);
        }
    }

    // This function is added to extend the limit of FB URL text char to 9
    public function vpro_get($transaction_id)
    {
        $response = array();
        $result = $this->WebhookModel->verifyTranIdWhatsappProVendor($transaction_id);
        if (!empty($result)) {
            if ($result->platform == 'android') {
                $finalUrl = $result->redirect_url . '.lazyclick.in:://pro/' . $transaction_id;
                if (!empty($finalUrl)) {
                    // echo $finalUrl;
                    $val = $this->scrape_post($finalUrl);
                    // echo $val;
                    redirect($val);
                }
            } else {
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
                $response['message'] = 'Thank you. WhatsApp Verified';
                $httpStatus = REST_Controller::HTTP_OK;

                // Sendind data to firebase so that response gets on the console
                $this->sendDataToFirebase($response);

                // $this->sendDataToWebhookUrl($response , $result->vendor_webhook_url, "");
                // $this->redirect_to_url($result->redirect_url);
                $data['value'] = $response['redirect_url'];
                $data['message'] = $response['message'];
                $data['error'] = $response['error'];
                $this->load->view('lazyclick/thankyou', $data);
            }
        } else {
            $response['redirect_url'] = null;
            $response['error'] = true;
            $response['message'] = 'Link expired!';
            $httpStatus = REST_Controller::HTTP_BAD_REQUEST;

            $data['value'] = $response['redirect_url'];
            $data['message'] = $response['message'];
            $data['error'] = $response['error'];
            $this->load->view('lazyclick/thankyou', $data);
        }
    }

    // -------------------------------------------------------------------- //
    // ------------This code is for Free version Yes Button Deeplink------- //
    // -------------------------------------------------------------------- //

    // This function is responsible for decoding values coming from js file
    function customDecode($encodedString)
    {
        $decodedString = '';
        for ($i = 0; $i < strlen($encodedString); $i++) {
            $charCode = ord($encodedString[$i]) - 5;  // Subtract 5 from the character code
            $decodedString .= chr($charCode);
        }
        return $decodedString;
    }

    public function free_post()
    {
        // This service is stopped. For clients deliverable we have put Auth Key Missing
        $response = array();
        $response['error'] = true;
        $response['message'] = 'Missing authentication key';
        $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
        $this->response($response, $httpStatus);
        return;
        exit ();

        if (isTheseParametersAvailable(array('app_id', 'redirect_url', 'platform', 'unicode_char'))) {
            $response = array();
            $app_id = $this->input->post('app_id');
            $redirect_url = $this->input->post('redirect_url');
            $platform = $this->input->post('platform');
            $unicode_char = $this->input->post('unicode_char');

            // Some other parameters coming from android
            $app_name = $this->input->post('app_name');
            $sdk_version = $this->input->post('sdk_version');
            $device_details = $this->input->post('device_details');

            // This is a random protection deocde/encode scheme
            $decodeString = base64_decode($this->customDecode(urldecode($unicode_char)));

            $prefix = 'u200';
            if (substr($unicode_char, 0, strlen($prefix)) === $prefix) {
                $unicode_char = $unicode_char;
            } else {
                $unicode_char = $decodeString;
            }
            $timestamp = time();
            $transaction_id = $this->generateUniqueID(9);

            $isVendorAvailable = $this->WebhookModel->isVendorAvailable($app_id);

            if (!$isVendorAvailable) {
                $add_array = array('vendor_name' => $app_id, 'vendor_app_id' => $app_id);
                $this->WebhookModel->add_vendor($add_array);
            }

            // Adding data to DB, so later we can check with message_id and send the payload data response coming from whatsapp to vendor
            $add_array = array('unicode_char' => $unicode_char, 'timestamp' => $timestamp, 'app_id' => $app_id, 'redirect_url' => $redirect_url, 'status' => 0, 'platform' => $platform, 'transaction_id' => $transaction_id);

            // Checking if platform is android - adding some extra details to db
            $add_android_data = array('app_name' => $app_name, 'sdk_version' => $sdk_version, 'device_details' => $device_details, 'transaction_id' => $transaction_id, 'platform' => $platform);

            $this->WebhookModel->add_click_button($add_array);
            if ($platform == 'android') {
                $this->WebhookModel->add_android_details($add_android_data);
                $response = array(
                    'transaction_id' => $transaction_id
                );
            } else {
                $response = array(
                    'transaction_id' => $transaction_id
                );
            }
            $response['error'] = false;
            // $response['message'] = "";
            $httpStatus = REST_Controller::HTTP_OK;
        } else {
            $response['error'] = true;
            $response['message'] = 'Parameters not found';
            $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
        }
        $this->response($response, $httpStatus);
    }

    public function verifyfree_get($transaction_id)
    {
        $response = array();

        $result = $this->WebhookModel->verifyTranIdWhatsappFreeVendor($transaction_id);
        if (!empty($result)) {
            if ($result->platform == 'android') {
                $finalUrl = $result->redirect_url . '.lazyclick.in:://free/' . $transaction_id;
                if (!empty($finalUrl)) {
                    // echo 'final url -> ' .$finalUrl;
                    $shortUrl = $this->WebhookModel->get_url_details($transaction_id);
                    if (!empty($shortUrl) && !empty($shortUrl->android_url)) {
                        // echo 'android 1 url -> ' .$shortUrl->android_url;
                        redirect($shortUrl->android_url);
                    } else {
                        // $longUrl = base_url('api/v2/qa/test/vendor/verifyfree/' .$transaction_id);
                        // echo 'long url -> ' .$longUrl;
                        $val = $this->scrape_post($finalUrl);
                        if (empty($val)) {
                            $val = $this->generateTinyUrl($finalUrl);
                        }
                        $this->WebhookModel->update_url_details($transaction_id, NULL, NULL, $val, NULL);
                        // echo 'android 2 url ->' .$val;
                        redirect($val);
                    }
                }
            } else {
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
                $response['message'] = 'Thank you. WhatsApp Verified';
                $httpStatus = REST_Controller::HTTP_OK;

                // Sending data to firebase so that response gets on the console
                $this->sendDataToFirebase($response);

                // $this->sendDataToWebhookUrl($response , $result->vendor_webhook_url, "");
                // $this->redirect_to_url($result->redirect_url);
                $data['value'] = $response['redirect_url'];
                $data['message'] = $response['message'];
                $data['error'] = $response['error'];
                $this->load->view('lazyclick/thankyou', $data);
            }
        } else {
            $response['redirect_url'] = null;
            $response['error'] = true;
            $response['message'] = 'Link expired!';
            $httpStatus = REST_Controller::HTTP_BAD_REQUEST;

            $data['value'] = $response['redirect_url'];
            $data['message'] = $response['message'];
            $data['error'] = $response['error'];
            $this->load->view('lazyclick/thankyou', $data);
        }
    }

    // This function is added to extend the limit of FB URL text char to 9
    public function vfree_get($transaction_id)
    {
        $response = array();

        $result = $this->WebhookModel->verifyTranIdWhatsappFreeVendor($transaction_id);
        if (!empty($result)) {
            if ($result->platform == 'android') {
                $finalUrl = $result->redirect_url . '.lazyclick.in:://free/' . $transaction_id;
                if (!empty($finalUrl)) {
                    // echo 'final url -> ' .$finalUrl;
                    $shortUrl = $this->WebhookModel->get_url_details($transaction_id);
                    if (!empty($shortUrl) && !empty($shortUrl->android_url)) {
                        // echo 'android 1 url -> ' .$shortUrl->android_url;
                        redirect($shortUrl->android_url);
                    } else {
                        // $longUrl = base_url('api/v2/qa/test/vendor/vfree/' .$transaction_id);
                        // echo 'long url -> ' .$longUrl;
                        $val = $this->scrape_post($finalUrl);
                        if (empty($val)) {
                            $val = $this->generateTinyUrl($finalUrl);
                        }
                        $this->WebhookModel->update_url_details($transaction_id, NULL, NULL, $val, NULL);
                        // echo 'android 2 url ->' .$val;
                        redirect($val);
                    }
                }
            } else {
                $this->sendEmail($result->wa_name, $result->wa_number);
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
                $response['message'] = 'Thank you ' . $result->wa_name;
                $httpStatus = REST_Controller::HTTP_OK;

                // Sending data to firebase so that response gets on the console
                $this->sendDataToFirebase($response);

                // $this->sendDataToWebhookUrl($response , $result->vendor_webhook_url, "");
                // $this->redirect_to_url($result->redirect_url);
                $data['value'] = $response['redirect_url'];
                $data['message'] = $response['message'];
                $data['error'] = $response['error'];
                $this->load->view('lazyclick/thankyou', $data);
            }
        } else {
            $response['redirect_url'] = null;
            $response['error'] = true;
            $response['message'] = 'Link expired!';
            $httpStatus = REST_Controller::HTTP_BAD_REQUEST;

            $data['value'] = $response['redirect_url'];
            $data['message'] = $response['message'];
            $data['error'] = $response['error'];
            $this->load->view('lazyclick/thankyou', $data);
        }
    }

    // Assuming you have loaded the cURL library in CodeIgniter

    public function scrape_post($finalUrl)
    {
        $postData = array(
            'u' => $finalUrl,
        );

        $apiUrl = 'https://www.shorturl.at/shortener.php';  // Replace with your API endpoint URL

        $ch = curl_init();

        // Set the cURL options
        curl_setopt($ch, CURLOPT_URL, $apiUrl);
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $postData);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

        // Execute the cURL request
        $response = curl_exec($ch);
        // echo $response;

        // Check for cURL errors
        if (curl_errno($ch)) {
            $error = curl_error($ch);
            // Handle the error
        }

        // Close the cURL session
        curl_close($ch);

        $shortUrl = $this->scrape_value_from_html($response);

        return $shortUrl;
    }

    public function scrape_value_from_html($html)
    {
        require_once (APPPATH . 'helpers/simple_html_dom.php');

        $dom = new simple_html_dom();
        $dom->load($html);

        $input = $dom->find('#shortenurl', 0);
        $dom->clear();

        if ($input) {
            // Get the value attribute
            $value = $input->value;
            return $value;
            // return $value;
            // echo "Shorten URL Value: " . $value;
        } else {
            echo NULL;
        }
    }

    public function verifyfree_post()
    {
        $response = array();
        $transaction_id = $this->input->post('transaction_id');
        $result = $this->WebhookModel->verifyTranIdWhatsappFreeVendor($transaction_id);
        if (!empty($result)) {
            $this->sendEmail($result->wa_name, $result->wa_number);
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
            $response['message'] = 'Thank you. WhatsApp Verified';
            $httpStatus = REST_Controller::HTTP_OK;
        } else {
            $response['redirect_url'] = null;
            $response['error'] = true;
            $response['message'] = 'Link expired!';
            $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
        }

        $this->response($response, $httpStatus);
    }

    public function verifypro_post()
    {
        $response = array();
        $transaction_id = $this->input->post('transaction_id');
        $result = $this->WebhookModel->verifyTranIdWhatsappProVendor($transaction_id);
        if (!empty($result)) {
            $this->WebhookModel->updateTranIdWhatsappProVendor($transaction_id);
            $response = array(
                'transaction_id' => $result->transaction_id,
                'message_number' => $result->message_number,
                'redirect_url' => $result->redirect_url,
                'app_id' => $result->vendor_app_id,
                'is_verifed' => true
            );
            $response['redirect_url'] = $result->redirect_url;
            $response['error'] = false;
            $response['message'] = 'Thank you. WhatsApp Verified';
            $httpStatus = REST_Controller::HTTP_OK;
        } else {
            $response['redirect_url'] = null;
            $response['error'] = true;
            $response['message'] = 'Link expired!';
            $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
        }

        $this->response($response, $httpStatus);
    }

    // -------------------------------------------------------------------- //
    // --------------------------Common Function--------------------------- //
    // -------------------------------------------------------------------- //
    private function sendDataToFirebase($response)
    {
        $url = 'https://lazyclick-in-default-rtdb.firebaseio.com';
        $token = 'H2N5G32BriM4LjyRZ0Hsh0kORNfjn2sQcc0Rr7vU';
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
            die ('Curl failed: ' . curl_error($ch));
        }

        // Close connection
        curl_close($ch);
    }

    private function getFirebaseConfig()
    {
        $firebaseConfig = array(
            'apiKey' => 'AIzaSyAq57wQRWvV_xWLMpNRVxVuT4cNY8VLJM8',
            'authDomain' => 'lazyclick-in.firebaseapp.com',
            'databaseURL' => 'https://lazyclick-in-default-rtdb.firebaseio.com',
            'projectId' => 'lazyclick-in',
            'storageBucket' => 'lazyclick-in.appspot.com',
            'messagingSenderId' => '385520452696',
            'appId' => '1:385520452696:web:34d44caff42b981e34f62d'
        );

        return $firebaseConfig;
    }

    function generateRandomVisibleUnicodes()
    {
        $unicodeList = [
            '\u200b',
            // ZERO WIDTH SPACE
            '\u200c',
            // ZERO WIDTH NON-JOINER
            '\u200d',
            // ZERO WIDTH JOINER
            '\u200e',  // LEFT-TO-RIGHT MARK
        ];
        $result = '\u200e\u200c\u200d\u200b\u200c\u200d';
        for ($i = 0; $i < 7; $i++) {
            $randomIndex = rand(0, count($unicodeList) - 1);
            $result .= $unicodeList[$randomIndex];
        }
        return urlencode($result);
    }

    function sendEmail($wa_name, $wa_number)
    {
        // echo 'I am here';
        $config = array(
            'protocol' => 'smtp',
            'smtp_host' => 'ssl://smtp.googlemail.com',
            'smtp_port' => 465,
            'smtp_user' => 'lazyclick1@gmail.com',
            'smtp_pass' => 'ykzejeyrwnqrdrfe',
            'mailtype' => 'html',
            'charset' => 'utf-8'
        );
        $this->load->library('email');
        $this->email->initialize($config);
        $this->email->set_newline("\r\n");

        $this->email->set_mailtype('html');
        $this->email->set_newline("\r\n");

        //Email content
        $htmlContent = '<h1>' . $wa_name . ' just signed up</h1>';
        $htmlContent .= '<h3>Here is the number ' . $wa_number . '</h3>';

        $this->email->to('sa159871@gmail.com');
        $this->email->from('lazyclick1@gmail.com', 'Lazyclick');
        $this->email->subject('User Success Verification');
        $this->email->message($htmlContent);
        $this->email->send();
        // if ($this->email->send()) {
        //     echo 'Your Email has successfully been sent.';
        // } else {
        //     show_error($this->email->print_debugger());
        // }
    }

    public function loginhistory_post()
    {
        $response = array();
        $number = $this->input->post('number');
        $result = $this->WebhookModel->getLoginHistory($number);
        if (!empty($result)) {
            $response['history'] = $result;
            $response['error'] = false;
            $response['message'] = 'Login History Found';
            $httpStatus = REST_Controller::HTTP_OK;
        } else {
            $response['error'] = true;
            $response['message'] = 'History Not Found';
            $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
        }
        $this->response($response, $httpStatus);
    }

    private function generateTinyUrl($url)
    {
        $curl = curl_init();
        $data = array(
            'url' => $url,
            'domain' => 'tinyurl.com'
        );
        curl_setopt_array($curl, array(
            CURLOPT_URL => 'https://api.tinyurl.com/create?api_token=9XJSa49cJinW9iAFCOIdPYckW7emCMNNF4basRNLSLKizkiYSQQAb9zFIZPe',
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_ENCODING => '',
            CURLOPT_MAXREDIRS => 10,
            CURLOPT_TIMEOUT => 0,
            CURLOPT_FOLLOWLOCATION => true,
            CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
            CURLOPT_CUSTOMREQUEST => 'POST',
            CURLOPT_POSTFIELDS => json_encode($data),
            CURLOPT_HTTPHEADER => array(
                'accept: application/json',
                'Content-Type: application/json'
            ),
        ));

        $response = curl_exec($curl);

        curl_close($curl);
        // Decode the JSON response
        $responseData = json_decode($response, true);

        if (isset($responseData['data'])) {
            // "data" key exists and is not null
            $tinyUrl = $responseData['data']['tiny_url'];
            return $tinyUrl;
        }
    }

    // PIVOT CODE
    public function paid_post()
    {
        $requestHeaders = $this->input->request_headers();
        $response = array();

        if ($requestHeaders != null && key_exists('x_lazyclick_key', $requestHeaders) && !empty($requestHeaders['x_lazyclick_key'])) {
            $lazyclickKey = $requestHeaders['x_lazyclick_key'];
            if (isTheseParametersAvailable(array('app_id', 'redirect_url', 'platform', 'unicode_char'))) {
                $response = array();
                $app_id = $this->input->post('app_id');
                $redirect_url = $this->input->post('redirect_url');
                $platform = $this->input->post('platform');
                $unicode_char = $this->input->post('unicode_char');

                // Some other parameters coming from android
                $app_name = $this->input->post('app_name');
                $sdk_version = $this->input->post('sdk_version');
                $device_details = $this->input->post('device_details');

                // This is a random protection deocde/encode scheme
                $decodeString = base64_decode($this->customDecode(urldecode($unicode_char)));
                $prefix = 'u200';
                if (substr($unicode_char, 0, strlen($prefix)) === $prefix) {
                    $unicode_char = $unicode_char;
                } else {
                    $unicode_char = $decodeString;
                }

                $timestamp = time();
                $transaction_id = $this->generateUniqueID(9);

                $vendor = $this->WebhookModel->get_vendor($lazyclickKey, $app_id);
                if ($vendor != NULL) {
                    if ($vendor->is_active == 1) {
                        // Convert end date to timestamp
                        $end_timestamp = strtotime($vendor->end_date);
                        // Get current timestamp
                        $current_timestamp = time();
                        // Compare timestamps
                        if ($current_timestamp > $end_timestamp) {
                            $response['error'] = true;
                            $response['message'] = 'Validity over';
                            $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
                        } else {
                            if ($vendor->credits > 0) {
                                // Adding data to DB, so later we can check with message_id and send the payload data response coming from whatsapp to vendor
                                $add_array = array('unicode_char' => $unicode_char, 'timestamp' => $timestamp, 'app_id' => $app_id, 'redirect_url' => $redirect_url, 'status' => 0, 'platform' => $platform, 'transaction_id' => $transaction_id, 'is_paid' => 1);
                                $this->WebhookModel->add_click_button($add_array);

                                if ($platform == 'android') {
                                    // Checking if platform is android - adding some extra details to db
                                    $add_android_data = array('app_name' => $app_name, 'sdk_version' => $sdk_version, 'device_details' => $device_details, 'transaction_id' => $transaction_id, 'platform' => $platform);
                                    $this->WebhookModel->add_android_details($add_android_data);
                                    $response = array(
                                        'transaction_id' => $transaction_id
                                    );
                                } else {
                                    $response = array(
                                        'transaction_id' => $transaction_id
                                    );
                                }
                                $response['error'] = false;
                                // $response['message'] = "";
                                $httpStatus = REST_Controller::HTTP_OK;
                            } else {
                                $response['error'] = true;
                                $response['message'] = 'Credits Exhausted';
                                $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
                            }
                        }
                    } else {
                        $response['error'] = true;
                        $response['message'] = 'Vendor not active';
                        $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
                    }
                } else {
                    $response['error'] = true;
                    $response['message'] = 'Vendor not found';
                    $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
                }
            } else {
                $response['error'] = true;
                $response['message'] = 'Parameters not found';
                $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
            }
        } else {
            $response['error'] = true;
            $response['message'] = 'Missing Authentication Key';
            $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
        }

        $this->response($response, $httpStatus);
    }


    function sendEmail_post()
    {
        $jsonDecodeData = json_decode(file_get_contents('php://input'), true);
        $rawData = file_get_contents('php://input');
        // Convert JSON string to a PHP object 
        $phpObject = json_decode($rawData);

        $name = $phpObject->name;
        $phone = $phpObject->phoneNumber;
        $astrologerName = $phpObject->astrologerName;
        $mesage = $phpObject->message;
        $dob = $phpObject->dob;
        $gender = $phpObject->gender;
        $placeOfBirth = $phpObject->placeOfBirth;
        $time = $phpObject->time;
        $timeToCall = $phpObject->timeToCall;


        // echo 'I am here';
        $config = array(
            'protocol' => 'smtp',
            'smtp_host' => 'ssl://smtp.googlemail.com',
            'smtp_port' => 465,
            'smtp_user' => 'lazyclick1@gmail.com',
            'smtp_pass' => 'ykzejeyrwnqrdrfe',
            'mailtype' => 'html',
            'charset' => 'utf-8'
        );
        $this->load->library('email');
        $this->email->initialize($config);
        $this->email->set_newline("\r\n");

        $this->email->set_mailtype('html');
        $this->email->set_newline("\r\n");

        //Email content
        $htmlContent = '<h1>' . $name . ' just requested a callback</h1>';
        $htmlContent .= '<h3>Here is the number ' . $phone . '</h3>';
        $htmlContent .= '<h4>Here are other the details 
        <br> Fullname - ' . $name . '
        <br> Phone Number - ' .$phone . '
        <br> Time To Call - ' .$timeToCall . '
        <br> Astrologer name - ' .$astrologerName . '
        <br> Message - ' .$mesage . '
        <br> DOB - ' .$dob . '
        <br> Gender - ' .$gender . '
        <br> Place of Birth - ' .$placeOfBirth . '
        <br> Time of Birth - ' .$time . '
        </h4>';

        $this->email->to('hello@lazyclick.in');
        // $this->email->cc('sa159871@gmail.com');
        $this->email->from('lazyclick1@gmail.com', 'Lazyclick');
        $this->email->subject('iastro Request Callback Form');
        $this->email->message($htmlContent);
        $this->email->send();
        // if ($this->email->send()) {
        //     echo 'Your Email has successfully been sent.';
        // } else {
        //     show_error($this->email->print_debugger());
        // }
    }

}
