<div class="content-wrapper">
  <!-- Content Header (Page header) -->
  <section class="content-header">
  </section>

  <section class="content">
    <div class="row">
      <div class="col-md-10">
        <!-- general form elements -->
        <div class="box box-primary">
          <div class="box-header with-border">
            <h3 class="box-title">Edit Timetable</h3>
          </div>
          <!-- /.box-header -->
          <!-- form start -->
          <?php echo form_open("admin/timetable/update_timetable/$timetable->id", ['role' => 'form']); ?>

          <div class="box-body">
            <div class="form-group">
              <label for="class_id">Select Class</label>
              <?php echo form_dropdown('class_id', $classes, $timetable->class_id, 'class="form-control"'); ?>
            </div>
            <div class="form-group">
              <label for="subject_id">Select Subject</label>
              <?php echo form_dropdown('subject_id', $subjects, $timetable->subject_id, 'class="form-control"'); ?>
            </div>
            <div class="form-group">
              <label for="teacher_id">Select Teaher</label>
              <?php echo form_dropdown('teacher_id', $teachers, $timetable->teacher_id, 'class="form-control"'); ?>
            </div>
            <div class="form-group">
              <label for="day">Select Day</label>
              <?php echo form_dropdown('day', $days, $timetable->day, 'class="form-control"'); ?>
            </div>
            <div class="bootstrap-timepicker">
              <div class="form-group">
                <label>Start Time</label>
                <div class="input-group">
                  <?php echo form_input(['id' => 'start_time', 'name' => 'start_time', 'class' => 'form-control timepicker', 'value' => set_value('start_time', $timetable->start_time)]); ?>
                  <div class="input-group-addon"><i class="fa fa-clock-o"></i></div>
                </div>
              </div>
            </div>
            <div class="bootstrap-timepicker">
              <div class="form-group">
                <label>End Time</label>
                <div class="input-group">
                  <?php echo form_input(['id' => 'end_time', 'name' => 'end_time', 'class' => 'form-control timepicker', 'value' => set_value('end_time', $timetable->end_time)]); ?>
                  <div class="input-group-addon"><i class="fa fa-clock-o"></i></div>
                </div>
              </div>
            </div>

          </div>
          <!-- /.box-body -->
          <div class="box-footer">
            <?php
            echo form_reset(['id' => 'reset', 'value' => 'Reset', 'class' => 'btn btn-danger']),
              form_submit(['id' => 'submit', 'value' => 'Submit', 'class' => 'btn btn-primary']);
            ?>
          </div>
          <?php echo form_close(); ?>
        </div>
      </div>


      <!-- /.row -->
  </section>
</div>

<footer class="main-footer">
  <div class="pull-right hidden-xs">
    <b>Version</b> 2.3.7
  </div>
  <strong>Copyright &copy; 2019-2020 <a href="#">Apsdeoria</a>.</strong> All rights reserved.
</footer>

<div class="control-sidebar-bg"></div>
</div>


<script src="<?php echo base_url(); ?>plugins/jQuery/jquery-2.2.3.min.js"></script>
<!-- Bootstrap 3.3.6 -->
<script src="<?php echo base_url(); ?>bootstrap/js/bootstrap.min.js"></script>
<!-- DataTables -->
<script src="<?php echo base_url(); ?>plugins/datatables/jquery.dataTables.min.js"></script>
<script src="<?php echo base_url(); ?>plugins/datatables/dataTables.bootstrap.min.js"></script>
<!-- SlimScroll -->
<script src="<?php echo base_url(); ?>plugins/slimScroll/jquery.slimscroll.min.js"></script>
<!-- FastClick -->
<script src="<?php echo base_url(); ?>plugins/fastclick/fastclick.js"></script>
<!-- AdminLTE App -->
<script src="<?php echo base_url(); ?>dist/js/app.min.js"></script>
<!-- AdminLTE for demo purposes -->
<script src="<?php echo base_url(); ?>dist/js/demo.js"></script>
<!-- page script -->

<!-- ./wrapper -->

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
<script src="<?php echo base_url(); ?>plugins/timepicker/bootstrap-timepicker.min.js"></script>

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
<script type="text/javascript">
  $('.timepicker').timepicker({
    showInputs: false,
    showMeridian: false
  })
</script>
</body>

</html>