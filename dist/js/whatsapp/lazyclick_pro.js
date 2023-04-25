// -------------------------------------------------------------------- //
// ---------------This code is for Yes Button Deeplink----------------- //
// -------------------------------------------------------------------- //

function pro_db(app_id, transaction_id, onProMessageReceived, firebaseConfig) {
    loadScriptsPro(function () {
        // Initialize Firebase
        firebase.initializeApp(firebaseConfig);
        // Initalize Database
        var database = firebase.database();
        var addRef = database.ref('/' + app_id + '/' + transaction_id);
        addRef.on('value', function (snapshot) {
            // find all empty games
            var data = snapshot.val();
            if (data != null) {
                onProMessageReceived(data);
                addRef.off("value");
                addRef.remove();
            }
        })
    });

}


// Define a function to load two JavaScript files and call a callback when both are loaded
function loadScriptsPro(callback) {

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

function openWhatsAppPro() {
    const phone = '917880760128';
    const url = `whatsapp://send?phone=${phone}`;
    window.open(url);
}


function initPro(app_id, number, onProMessageSent, onProMessageReceived) {
    var form = new FormData();
    form.append("app_id", app_id);
    form.append("number", number);
    form.append("platform", "web");
    form.append("redirect_url", window.location.href);

    var settings = {
        // "url": "http://localhost/apsdeoria/api/v2/qa/test/vendor/pro",
        "url": "https://www.apsdeoria.com/apszone/api/v2/qa/test/vendor/pro",
        "method": "POST",
        "timeout": 0,
        "processData": false,
        "mimeType": "multipart/form-data",
        "contentType": false,
        "data": form
    };

    $.ajax(settings)
        .done(function (response) {
            var dataToSend = JSON.parse(response);
            delete dataToSend['firebaseConfig'];
            onProMessageSent(dataToSend);
            openWhatsAppPro();
            var data = JSON.parse(response);
            var transaction_id = data['transaction_id'];
            var firebaseConfig = data['firebaseConfig'];
            pro_db(app_id, transaction_id, onProMessageReceived, firebaseConfig);
        })
        .fail(function (response) {
            onProMessageSent(response.responseText);
        });
};