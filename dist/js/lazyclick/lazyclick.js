// --This code is for Free version where no phone number is required--- //
// -------------------------------------------------------------------- //


function free_db(app_id, transaction_id, onFreeMessageReceived) {
    loadScriptsFree(function () {
        const firebaseConfig = {
            apiKey: "AIzaSyAq57wQRWvV_xWLMpNRVxVuT4cNY8VLJM8",
            authDomain: "lazyclick-in.firebaseapp.com",
            databaseURL: "https://lazyclick-in-default-rtdb.firebaseio.com",
            projectId: "lazyclick-in",
            storageBucket: "lazyclick-in.appspot.com",
            messagingSenderId: "385520452696",
            appId: "1:385520452696:web:34d44caff42b981e34f62d"
          };
        // Initialize Firebase
        firebase.initializeApp(firebaseConfig);
        // Initalize Database
        var database = firebase.database();
        var addRef = database.ref('/' + app_id + '/' + transaction_id);
        addRef.on('value', function (snapshot) {
            var data = snapshot.val();
            if (data != null) {
                onFreeMessageReceived(data);
                addRef.off("value");
                addRef.remove();
            }
        })
    });

}


// Define a function to load two JavaScript files and call a callback when both are loaded
function loadScriptsFree(callback) {

    // Create the first script element
    var scriptElem1 = document.createElement('script');
    scriptElem1.src = "https://www.gstatic.com/firebasejs/7.18.0/firebase-app.js";

    // Create the second script element
    var scriptElem2 = document.createElement('script');
    scriptElem2.src = "https://www.gstatic.com/firebasejs/7.18.0/firebase-database.js";

    var scriptElem3 = document.createElement('script');
    scriptElem3.src = "https://code.jquery.com/jquery-3.6.0.min.js";

    // Set up a counter to track the number of scripts that have loaded
    var loadedCount = 0;

    // Define a function to check if both scripts have loaded and call the callback
    function checkLoaded() {
        loadedCount++;
        if (loadedCount === 3) {
            callback();
        }
    }

    // Attach an event listener to the first script element to check when it has loaded
    scriptElem1.addEventListener('load', checkLoaded);

    // Attach an event listener to the second script element to check when it has loaded
    scriptElem2.addEventListener('load', checkLoaded);

    // Attach an event listener to the second script element to check when it has loaded
    scriptElem3.addEventListener('load', checkLoaded);

    // Get a reference to the head or body element of the HTML document
    var body = document.getElementsByTagName('body')[0];

    // Get a reference to the head or body element of the HTML document
    var head = document.getElementsByTagName('head')[0];

    // Append the first script element to the head or body element
    body.appendChild(scriptElem1);

    // Append the second script element to the head or body element
    body.appendChild(scriptElem2);

    // Append the second script element to the head or body element
    head.appendChild(scriptElem3);
}

function generateRandomVisibleUnicodes() {
    const unicodeList = ['\u200b', '\u200c', '\u200d', '\u200e']; let result = '\u200e\u200c\u200d\u200b\u200c\u200d'; for (let i = 0; i < 7; i++) { const randomIndex = Math.floor(Math.random() * unicodeList.length); result += unicodeList[randomIndex]; } return encodeURIComponent(result);
}

// TODO Not opening in ios devices safari or brave browser
function openWhatsAppFree(encodedResult, app_id) {
    const phone = '917880760128';
    const url = `https://api.whatsapp.com/send?phone=${phone}&text=${encodedResult}Send this message to continue to ${app_id}`;
    window.open(url, '_blank') || window.location.assign(url);
}

function initFree(onFreeMessageSent, onFreeMessageReceived) {
    // Generate app id 
    const app_id = window.location.hostname.split('.').slice(-2)[0];
    const encodedResult = generateRandomVisibleUnicodes();

    const decodedResult = decodeURIComponent(encodedResult);

    let unicodeList = '';
    for (let i = 0; i < decodedResult.length; i++) {
        const codePoint = decodedResult.codePointAt(i).toString(16);
        unicodeList += '\\u' + '0'.repeat(4 - codePoint.length) + codePoint;
    }

    let result = unicodeList.replace(/\\/g, '');

    var form = new FormData();
    form.append("app_id", app_id);
    form.append("redirect_url", window.location.href);
    form.append("platform", "web");
    form.append("unicode_char", result);

    var settings = {
        // "url": "http://localhost/apsdeoria/api/v2/qa/test/vendor/free",
        "url": "https://www.apsdeoria.com/apszone/api/v2/qa/test/vendor/free",
        "method": "POST",
        "timeout": 0,
        "processData": false,
        "mimeType": "multipart/form-data",
        "contentType": false,
        "data": form
    };

    $.ajax(settings).
        done(function (response) {
            var dataToSend = JSON.parse(response);
            onFreeMessageSent(dataToSend);
            openWhatsAppFree(encodedResult, app_id);
            var data = JSON.parse(response);
            var transaction_id = data['transaction_id'];
            free_db(app_id, transaction_id, onFreeMessageReceived);
        })
        .fail(function (response) {
            onFreeMessageSent(response.responseText);
        });
}