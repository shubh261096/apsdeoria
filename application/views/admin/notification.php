<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            Send Notification on Phone
        </h1>

    </section>
    <section class="content">
        <!-- Small boxes (Stat box) -->
        <div class="row">
            <div class="col-lg-6">
                <!-- general form elements -->
                <div class="box box-primary">
                    <div class="box-body">
                        <?php echo form_open('admin/Notification/notify', ['role' => 'form']); ?>
                        <div class="form-group">
                            <label for="send_to">Send To:</label>
                            <select name="send_to" id="send_to" class="form-control">
                                <option value="sngle">Single Device</option>
                                <option value="topic">Topic</option>
                            </select>
                        </div>
                        <div class="form-group" id="firebase_token_group">
                            <label for="firebase_token">Firebase Token:</label>
                            <input type="text" required="" class="form-control" id="firebase_token" placeholder="Enter Firebase Token" name="firebase_token">
                        </div>
                        <div class="form-group" style="display: none" id="topic_group">
                            <label for="topic">Choose Topic:</label>
                            <select name="topic" id="topic" class="form-control">
                                <option value="global">Global</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="title">Title:</label>
                            <input type="text" required="" class="form-control" id="title" placeholder="Enter Notification Title" name="title">
                        </div>
                        <div class="form-group">
                            <label for="message">Message:</label>
                            <textarea required="" class="form-control" rows="5" id="message" placeholder="Enter Notification Message" name="message"></textarea>
                        </div>
                        <div class="checkbox">
                            <label><input type="checkbox" id="include_image" name="include_image">Include Image</label>
                        </div>
                        <div class="form-group" style="display: none" id="image_url_group">
                            <label for="image_url">Image URL:</label>
                            <input type="url" class="form-control" id="image_url" placeholder="Enter Image URL" name="image_url">
                        </div>
                        <div class="checkbox">
                            <label><input type="checkbox" id="include_action" name="include_action">Include Action</label>
                        </div>
                        <div class="form-group" style="display: none" id="action_group">
                            <label for="action">Action:</label>
                            <select name="action" id="action" class="form-control">
                                <option value="url">Open URL</option>
                                <option value="activity">Open Activity</option>
                            </select>
                        </div>
                        <div class="form-group" style="display: none" id="action_destination_group">
                            <label for="action_destination">Destination:</label>
                            <input type="text" class="form-control" id="action_destination" placeholder="Enter Destination URL or Activity name" name="action_destination">
                        </div>

                        <button type="submit" class="btn btn-info">Submit</button>
                        <?php form_close(); ?>
                    </div>
                </div>
            </div>
            <div class="col-lg-6">
                <?php
                if ($result != NULL && $fields != NULL) { ?>
                    <div class="box box-primary">
                        <div class="box-body">
                            <?php echo '<h1>Result</h1><hr/><h3>Request</h3><p><pre>';
                            echo json_encode($fields, JSON_PRETTY_PRINT);
                            echo '</pre></p><h3>Response </h3><p><pre>';
                            echo $result;
                            echo '</pre></p>'; ?>
                        </div>
                    </div>
                <?php } else {
                    echo '';
                }
                ?>
            </div>
        </div>
    </section>
</div>


<footer class="main-footer">
    <div class="pull-right hidden-xs">
        <b>Version</b> 2.3.7
    </div>
    <strong>Copyright &copy; 2019-2020 <a href="#">Apsdeoria</a>.</strong> All rights reserved.
</footer>

<div class="control-sidebar-bg"></div>

<!-- jQuery 2.2.3 -->
<script src="<?php echo base_url(); ?>plugins/jQuery/jquery-2.2.3.min.js"></script>
<!-- jQuery UI 1.11.4 -->
<script src="https://code.jquery.com/ui/1.11.4/jquery-ui.min.js"></script>
<!-- Resolve conflict in jQuery UI tooltip with Bootstrap tooltip -->
<script>
    $.widget.bridge('uibutton', $.ui.button);
</script>
<!-- Bootstrap 3.3.6 -->
<script src="<?php echo base_url(); ?>bootstrap/js/bootstrap.min.js"></script>
<!-- Morris.js charts -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/raphael/2.1.0/raphael-min.js"></script>
<script src="<?php echo base_url(); ?>plugins/morris/morris.min.js"></script>
<!-- Sparkline -->
<script src="<?php echo base_url(); ?>plugins/sparkline/jquery.sparkline.min.js"></script>
<!-- jvectormap -->
<script src="<?php echo base_url(); ?>plugins/jvectormap/jquery-jvectormap-1.2.2.min.js"></script>
<script src="<?php echo base_url(); ?>plugins/jvectormap/jquery-jvectormap-world-mill-en.js"></script>
<!-- jQuery Knob Chart -->
<script src="<?php echo base_url(); ?>plugins/knob/jquery.knob.js"></script>
<!-- daterangepicker -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.11.2/moment.min.js"></script>
<script src="<?php echo base_url(); ?>plugins/daterangepicker/daterangepicker.js"></script>
<!-- datepicker -->
<script src="<?php echo base_url(); ?>plugins/datepicker/bootstrap-datepicker.js"></script>
<!-- Bootstrap WYSIHTML5 -->
<script src="<?php echo base_url(); ?>plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js"></script>
<!-- Slimscroll -->
<script src="<?php echo base_url(); ?>plugins/slimScroll/jquery.slimscroll.min.js"></script>
<!-- FastClick -->
<script src="<?php echo base_url(); ?>plugins/fastclick/fastclick.js"></script>
<!-- AdminLTE App -->
<script src="<?php echo base_url(); ?>dist/js/app.min.js"></script>
<!-- AdminLTE dashboard demo (This is only for demo purposes) -->
<script src="<?php echo base_url(); ?>dist/js/pages/dashboard.js"></script>
<!-- AdminLTE for demo purposes -->
<script src="<?php echo base_url(); ?>dist/js/demo.js"></script>


<script src="<?php echo base_url(); ?>plugins/jQuery/jquery-2.2.3.min.js"></script>
<!-- Bootstrap 3.3.6 -->
<script src="<?php echo base_url(); ?>bootstrap/js/bootstrap.min.js"></script>
<!-- AdminLTE App -->
<script src="<?php echo base_url(); ?>dist/js/app.min.js"></script>
<!-- AdminLTE for demo purposes -->
<script src="<?php echo base_url(); ?>dist/js/demo.js"></script>

<script>
    $('#include_image').change(function(e) {
        if ($(this).prop("checked") == true) {
            $('#image_url_group').show();
            $("#image_url").prop('required', true);
        } else {
            $('#image_url_group').hide();
            $("#image_url").prop('required', false);
        }
    });
    $('#include_action').change(function(e) {
        if ($(this).prop("checked") == true) {
            $('#action_group').show();
            $('#action_destination_group').show();
            $("#action_destination").prop('required', true);
        } else {
            $('#action_group').hide();
            $('#action_destination_group').hide();
            $("#action_destination").prop('required', false);
        }
    });

    $('#send_to').change(function(e) {
        var selectedVal = $("#send_to option:selected").val();
        if (selectedVal == 'topic') {
            $('#topic_group').show();
            $("#topic").prop('required', true);
            $('#firebase_token_group').hide();
            $("#firebase_token").prop('required', false);
        } else {
            $('#topic_group').hide();
            $("#topic").prop('required', false);
            $('#firebase_token_group').show();
            $("#firebase_token").prop('required', true);
        }
    });
</script>

</body>

</html>