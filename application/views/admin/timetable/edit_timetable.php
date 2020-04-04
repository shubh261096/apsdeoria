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
          <?php echo form_open("timetable/update/$timetable->id", ['role' => 'form']); ?>

          <div class="box-body">
            <div class="form-group">
              <label>Class</label>
              <select class="form-control" name="class_id" id="class_id">
                <?php foreach ($classes as $row) :
                  if (strcmp($row->id, $timetable->class_id) == 0) { ?>
                    <option value="<?php echo $row->id; ?>" selected="selected"><?php echo $row->name; ?></option>
                  <?php } else { ?>
                    <option value="<?php echo $row->id; ?>"><?php echo $row->name; ?></option>
                  <?php } ?>
                <?php endforeach; ?>
              </select>
            </div>
            <?php if ($timetable->subject_id == NULL && $timetable->teacher_id == NULL) { ?>
              <div class="checkbox">
                <label> <?php echo form_input(['id' => 'recess', 'checked' => 'checked', 'value' => 'true', 'name' => 'recess', 'type' => 'checkbox']); ?> Is it a recess? </label>
              </div>
              <div style="display:none" id="subject" class="form-group">
                <label for="subject_id">Select Subject</label>
                <?php echo form_dropdown('subject_id', $subjects, $timetable->subject_id, 'class="form-control"'); ?>
              </div>
              <div style="display:none" id="teacher" class="form-group">
                <label for="teacher_id">Select Teacher</label>
                <?php echo form_dropdown('teacher_id', $teachers, $timetable->teacher_id, 'class="form-control"'); ?>
              </div>
            <?php } else { ?>
              <div class="checkbox">
                <label> <?php echo form_input(['id' => 'recess', 'name' => 'recess', 'type' => 'checkbox']); ?> Is it a recess? </label>
              </div>
              <div class="form-group">
                <label>Subject</label>
                <select class="form-control" id="subject_id" name="subject_id">
                  <option value="<?php $subject_id->id ?>" selected="selected"> <?php echo $subject_id->name ?> </option>
                </select>
              </div>
              <div id="teacher" class="form-group">
                <label for="teacher_id">Select Teacher</label>
                <?php echo form_dropdown('teacher_id', $teachers, $timetable->teacher_id, 'class="form-control"'); ?>
              </div>
            <?php } ?>
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
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>

<script type="text/javascript">
  $(document).ready(function() {

    $('#class_id').change(function() {
      var id = $(this).val();
      $.ajax({
        url: "<?php echo base_url('timetable/getSubjectFromClass'); ?>",
        method: "POST",
        data: {
          id: id
        },
        async: true,
        dataType: 'json',
        success: function(data) {
          var html = '';
          var i;
          for (i = 0; i < data.length; i++) {
            html += '<option value=' + data[i].id + '>' + data[i].name + '</option>';
          }
          $('#subject_id').html(html);
        },
        error: function(xhr, status, error) {
          console.log(error);
        },
      });
      return false;
    });

  });
</script>

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
<script>
  $("#recess").on("click", function() {
    if ($(this).is(':checked')) {
      $('#teacher').hide();
      $('#subject').hide();
      $('#recess').val('true');
    } else {
      $('#teacher').show();
      $('#subject').show();
      $('#recess').val('false');
    }
  });
</script>
</body>

</html>