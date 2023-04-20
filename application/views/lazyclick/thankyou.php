<?php
defined('BASEPATH') or exit('No direct script access allowed');
?>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Lazyclick</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.6 -->
    <link rel="stylesheet" href="<?php echo base_url(); ?>other/styles.css">

    <link rel="stylesheet" href="<?php echo base_url(); ?>bootstrap/css/bootstrap.min.css">

    <script type="text/javascript">
        var count = 5;
        function countdown() {
            setTimeout(function () {
                count--;
                if (count == 0) {
                    <?php if ($value != null) { ?>
                        window.location.href = "<?php echo $value; ?>";
                    <?php } ?>
                } else {
                    document.getElementById("countdown").innerHTML = count;
                    countdown();
                }
            }, 1000);
        }
        window.onload = countdown;
    </script>

    <script>
        function redirect() {
            <?php if ($value != null) { ?>
                window.location.href = "<?php echo $value; ?>";
            <?php } ?>
        }
    </script>

</head>

<body>

    <div class='container'>
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-body p-0">
                    <div class="crop text-center">
                        <?php if ($error == false) { ?>
                            <img src="<?php echo base_url(); ?>dist/img/verified.png" width="50%">
                        <?php } else { ?>
                            <img src="<?php echo base_url(); ?>dist/img/error.png" width="50%">
                        <?php } ?>
                    </div>
                </div>
                <div class="d-flex justify-content-center">
                    <div class="text-center">
                        <h2>
                            <?php echo $message; ?>
                        </h2>
                        <?php if ($value != null) { ?>
                            <p>You will be redirected to
                                <?php echo $value; ?> in <span id="countdown">5</span> seconds...
                            </p>
                            <div class="d-flex justify-content-center">
                                <button onclick="redirect()" type="button" class="okay btn">Redirect</button>
                            </div>
                        <?php } ?>

                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="<?php echo base_url(); ?>plugins/jQuery/jquery-2.2.3.min.js"></script>
    <!-- Bootstrap 3.3.6 -->
    <script src="<?php echo base_url(); ?>bootstrap/js/bootstrap.min.js"></script>

</body>

</html>