<?php
if (!empty($_POST["contact_us"])) {
  /* Form Required Field Validation */
  foreach ($_POST as $key => $value) {
    if (empty($_POST['full_name'])) {
      $error_message = "Full name is required";
      break;
    }
    if (empty($_POST['phone_no'])) {
      $error_message = "Phone  number is required";
      break;
    }
    if (empty($_POST['subject'])) {
      $error_message = "Subject is required";
      break;
    }
    if (empty($_POST['message'])) {
      $error_message = "Message is required";
      break;
    }
    /* Email Validation */
    if (!empty($_POST['email'])) {
      if (!filter_var($_POST["email"], FILTER_VALIDATE_EMAIL)) {
        $error_message = "Invalid Email Address";
        break;
      }
    }
  }

  if (!isset($error_message)) {
    require_once("dbcontroller.php");
    $db_handle = new DBController();
    $query = "INSERT INTO contact_us (full_name, phone_no, email, subject, message) VALUES
		('" . $_POST["full_name"] . "', '" . $_POST["phone_no"] . "', '" . $_POST["email"] . "', '" . $_POST["subject"] . "', '" . $_POST["message"] . "')";
    $result = $db_handle->insertQuery($query);
    if (!empty($result)) {
      $error_message = "";
      $success_message = "Thank-you for contacting us!";
      unset($_POST);
    } else {
      $error_message = "Oops! There is a problem. Please Try Again!";
    }
  }
}
?>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Agrawal Public School | Welcome To The New School Of Thoughts</title>
  <meta name="description" content="Agrawal Public School | Welcome To The New School Of Thoughts. Admission Open For 2019-2020 | Play Group - Class VIII">
  <meta name="keywords" content="School, Education, Admission Open, Play-Group, LKG, UKG, APS, Deoria, Public School">
  <meta name="author" content="Shubham Agrawal">
  <link rel="shortcut icon" type="image/x-icon" href="img/aps_logo.png">
  <link href="https://fonts.googleapis.com/css?family=Raleway:300,400,500,700|Open+Sans" rel="stylesheet">
  <link rel="stylesheet" href="css/styles-merged.css">
  <link rel="stylesheet" href="css/style.min.css">

  <!--[if lt IE 9]>
      <script src="js/vendor/html5shiv.min.js"></script>
      <script src="js/vendor/respond.min.js"></script>
    <![endif]-->
  <script async src="https://www.googletagmanager.com/gtag/js?id=UA-135429012-1"></script>
  <script>
    window.dataLayer = window.dataLayer || [];

    function gtag() {
      dataLayer.push(arguments);
    }
    gtag('js', new Date());

    gtag('config', 'UA-135429012-1');
  </script>
</head>

<body>

  <div class="probootstrap-search" id="probootstrap-search">
    <a href="#" class="probootstrap-close js-probootstrap-close"><i class="icon-cross"></i></a>
    <form action="#">
      <input type="search" name="s" id="search" placeholder="Search a keyword and hit enter...">
    </form>
  </div>

  <div class="probootstrap-page-wrapper">
    <!-- Fixed navbar -->

    <div class="probootstrap-header-top">
      <div class="container">
        <div class="row">
          <div class="col-lg-9 col-md-9 col-sm-9 probootstrap-top-quick-contact-info">
            <span><i class="icon-location2"></i><a href="https://maps.app.goo.gl/mv1dX" target="_blank">New Colony Near Shiv Mandir, Deoria U.P</a></span>
            <span><i class="icon-phone2"></i><a href="tel:9454549454">+91-9454549454</a></span>
            <span><i class="icon-mail"></i><a href="mailto: contact@apsdeoria.com">conatct@apsdeoria.com</a></span>
          </div>
          <div class="col-lg-3 col-md-3 col-sm-3 probootstrap-top-social">
            <ul>
              <li><a href="https://twitter.com/apsdeoria" target="_blank"><i class="icon-twitter"></i></a></li>
              <li><a href="https://www.facebook.com/apsdeoria" target="_blank"><i class="icon-facebook2"></i></a></li>
              <li><a href="https://www.instagram.com/apsdeoria" target="_blank"><i class="icon-instagram2"></i></a></li>
              <li><a href="https://www.youtube.com/channel/UCqWRbnwa38SUG3fdBZjh7oQ" target="_blank"><i class="icon-youtube"></i></a></li>
              <li><a href="#" class="probootstrap-search-icon js-probootstrap-search"><i class="icon-search"></i></a></li>
            </ul>
          </div>
        </div>
      </div>
    </div>
    <nav class="navbar navbar-default probootstrap-navbar">
      <div class="container">
        <div class="navbar-header">
          <div class="btn-more js-btn-more visible-xs">
            <a href="#"><i class="icon-dots-three-vertical "></i></a>
          </div>
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-collapse" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="index.html" title="uiCookies:Enlight">Enlight</a>
        </div>

        <div id="navbar-collapse" class="navbar-collapse collapse">
          <ul class="nav navbar-nav navbar-right">
            <li><a href="index.html">Home</a></li>
            <li><a href="about.html">About Us</a></li>
            <!-- <li><a href="teachers.html">Teachers</a></li> -->
            <li><a href="events.html">Events</a></li>
            <!-- <li class="dropdown">
                <a href="#" data-toggle="dropdown" class="dropdown-toggle">Pages</a>
                <ul class="dropdown-menu">
                  
                  <li><a href="courses.html">Courses</a></li>
                  <li class="active"><a href="course-single.html">Course Single</a></li>
                  <li><a href="gallery.html">Gallery</a></li>
                  <li class="dropdown-submenu dropdown">
                    <a href="#" data-toggle="dropdown" class="dropdown-toggle"><span>Sub Menu</span></a>
                    <ul class="dropdown-menu">
                      <li><a href="#">Second Level Menu</a></li>
                      <li><a href="#">Second Level Menu</a></li>
                      <li><a href="#">Second Level Menu</a></li>
                      <li><a href="#">Second Level Menu</a></li>
                    </ul>
                  </li>
                  <li><a href="news.html">News</a></li>
                </ul>
              </li> -->
            <li><a href="contact.php">Contact</a></li>
            <li><a href="gallery.html">Gallery</a></li>
          </ul>
        </div>
      </div>
    </nav>

    <section class="probootstrap-section probootstrap-section-colored">
      <div class="container">
        <div class="row">
          <div class="col-md-12 text-left section-heading probootstrap-animate">
            <h1 class="mb0">Contact Us</h1>
          </div>
        </div>
      </div>
    </section>



    <section class="probootstrap-section probootstrap-section-sm">
      <div class="container">
        <div class="row">
          <div class="col-md-12">
            <div class="row probootstrap-gutter0">
              <div class="col-md-4" id="probootstrap-sidebar">
                <div class="probootstrap-sidebar-inner probootstrap-overlap probootstrap-animate">
                  <h3>Pages</h3>
                  <ul class="probootstrap-side-menu">

                    <li><a href="index.html">Home</a></li>
                    <!-- <li><a href="courses.html">Courses</a></li> -->
                    <!-- <li><a href="teachers.html">Teachers</a></li> -->
                    <li><a href="events.html">Events</a></li>
                    <li><a href="about.html">About Us</a></li>
                    <li class="active"><a>Contact Us</a></li>
                    <li><a href="gallery.html">Gallery</a></li>
                  </ul>
                </div>

                <div class="probootstrap-sidebar-inner">
                  <div class="mapouter">
                    <div class="gmap_canvas">
                      <iframe id="gmap_canvas" width="100%" height="300" src="https://maps.google.com/maps?q=Agrawal%20Public%20School%20Deoria&t=&z=13&ie=UTF8&iwloc=&output=embed" frameborder="0" scrolling="no" marginheight="0" marginwidth="0"></iframe>
                    </div>
                  </div>
                </div>
              </div>
              <div class="col-md-7 col-md-push-1  probootstrap-animate" id="probootstrap-content">
                <h2>Get In Touch</h2>
                <p>Contact us using the form below.</p>
                <form name="frmRegistration" action="" method="post" class="probootstrap-form">
                  <?php if (!empty($success_message)) { ?>
                    <div class="alert alert-success alert-dismissible" role="alert">
                      <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span>
                      </button>
                      <strong><?= $success_message ?></strong>
                    </div>
                  <?php } ?>
                  <?php if (!empty($error_message)) { ?>
                    <div class="alert alert-danger alert-dismissible" role="alert">
                      <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span>
                      </button>
                      <strong><?= $error_message ?></strong>
                    </div>
                  <?php } ?>
                  <div class="form-group">
                    <label for="full_name">Full Name</label>
                    <input type="text" class="form-control" id="full_name" name="full_name" value="<?php if (isset($_POST['full_name'])) echo $_POST['full_name']; ?>">
                  </div>
                  <div class="form-group">
                    <label for="Phone">Phone No.</label>
                    <input type="Tel" class="form-control" id="phone_no" name="phone_no" value="<?php if (isset($_POST['phone_no'])) echo $_POST['phone_no']; ?>">
                  </div>
                  <div class="form-group">
                    <label for="emailAddress">Email</label>
                    <input type="emailAddress" class="form-control" id="emailAddress" name="email" value="<?php if (isset($_POST['email'])) echo $_POST['email']; ?>">
                  </div>
                  <div class="form-group">
                    <label for="subject">Subject</label>
                    <input type="text" class="form-control" id="subject" name="subject" value="<?php if (isset($_POST['subject'])) echo $_POST['subject']; ?>">
                  </div>
                  <div class="form-group">
                    <label for="message">Message</label>
                    <textarea cols="30" rows="10" class="form-control" id="message" name="message" value="<?php if (isset($_POST['message'])) echo $_POST['message']; ?>"></textarea>
                  </div>
                  <div class="form-group">
                    <input type="submit" class="btn btn-primary btn-lg" id="submit" name="contact_us" value="Send Message">
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="probootstrap-cta">
      <div class="container">
        <div class="row">
          <div class="col-md-12">
            <h2 class="probootstrap-animate" data-animate-effect="fadeInRight">Get your admission now!</h2>
            <a href="https://forms.gle/aiLxn8fkkwCsLAJh6" target="_blank" role="button" class="btn btn-primary btn-lg btn-ghost probootstrap-animate" data-animate-effect="fadeInLeft">Enroll</a>
          </div>
        </div>
      </div>
    </section>
    <footer class="probootstrap-footer probootstrap-bg">
      <div class="container">
        <div class="row">
          <div class="col-md-4">
            <div class="probootstrap-footer-widget">
              <h3>About The School</h3>
              <p>With the mission to cultivate and nurture young minds into leaders of the future, we as the Agrawal Group have widespread interest in the sphere of quality education with a commitment to excellence.</p>
              <h3>Social</h3>
              <ul class="probootstrap-footer-social">
                <li><a href="https://www.facebook.com/apsdeoria" target="_blank"><i class="icon-facebook"></i></a></li>
                <li><a href="https://www.instagram.com/apsdeoria" target="_blank"><i class="icon-instagram"></i></a></li>
                <li><a href="https://www.youtube.com/channel/UCqWRbnwa38SUG3fdBZjh7oQ" target="_blank"><i class="icon-youtube"></i></a></li>
                <li><a href="https://twitter.com/apsdeoria" target="_blank"><i class="icon-twitter"></i></a></li>
              </ul>
            </div>
          </div>
          <div class="col-md-3 col-md-push-1">
            <div class="probootstrap-footer-widget">
              <h3>Links</h3>
              <ul>
                <li><a href="index.html">Home</a></li>
                <li><a href="about.html">About Us</a></li>
                <li><a href="events.html">Events</a></li>
                <li><a href="contact.php">Contact</a></li>
                <li><a href="gallery.html">Gallery</a></li>
                <li><a href="privacy-policy.html">Privacy Policy</a></li>
              </ul>
            </div>
          </div>
          <div class="col-md-4">
            <div class="probootstrap-footer-widget">
              <h3>Contact Info</h3>
              <ul class="probootstrap-contact-info">
                <li><i class="icon-location2"></i> <span><a href="https://maps.app.goo.gl/mv1dX" target="_blank">New Colony Near Shiv Mandir, Deoria U.P</span></li>
                <li><i class="icon-mail"></i><span><a href="mailto: contact@apsdeoria.com">conatct@apsdeoria.com</a></span></li>
                <li><i class="icon-phone2"></i><span><a href="tel:9454549454">+91-9454549454</a></span></li>
              </ul>
            </div>
          </div>

        </div>
        <!-- END row -->

      </div>

      <div class="probootstrap-copyright">
        <div class="container">
          <div class="row">
            <div class="col-md-8 text-left">
              <p>&copy; 2020 <a href="https://www.apsdeoria.com/">CD Agrawal Public School</a>. All Rights Reserved. Designed &amp; Developed with <i class="icon icon-heart"></i> by <a href="https://www.apsdeoria.com/">apsdeoria.com</a></p>
            </div>
            <div class="col-md-4 probootstrap-back-to-top">
              <p><a href="#" class="js-backtotop">Back to top <i class="icon-arrow-long-up"></i></a></p>
            </div>
          </div>
        </div>
      </div>
    </footer>

  </div>
  <!-- END wrapper -->


  <script src="js/scripts.min.js"></script>
  <script src="js/main.min.js"></script>
  <script src="js/custom.js"></script>


</body>

</html>