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
            <h3 class="box-title">Edit Teacher</h3>
          </div>
          <!-- /.box-header -->
          <!-- form start -->

          <?php echo form_open("teacher/update/$teacher->id", ['role' => 'form']); ?>

          <div class="box-body">
            <div class="form-group ">
              <label for="InputName">Teacher ID</label>
              <?php echo form_input(['id' => 'id', 'name' => 'id', 'readonly' => 'true', 'class' => 'form-control', 'placeholder' => 'Enter Teacher Id', 'type' => 'text', 'value' => set_value('id', $teacher->id)]); ?>
              <?php echo form_error('id');   ?>
            </div>
            <div class="form-group ">
              <label for="InputName">Full Name</label>
              <?php echo form_input(['id' => 'fullname', 'name' => 'fullname', 'class' => 'form-control', 'placeholder' => 'Enter Fullname', 'type' => 'text', 'value' => set_value('fullname',  $teacher->fullname)]); ?>
              <?php echo form_error('fullname');   ?>
            </div>
            <div class="form-group">
              <label for="InputPhone">Phone Number</label>
              <?php echo form_input(['id' => 'phone', 'name' => 'phone', 'class' => 'form-control', 'placeholder' => 'Enter Phone Number', 'type' => 'phone', 'value' => set_value('phone',  $teacher->phone)]); ?>
              <?php echo form_error('phone');   ?>
            </div>
            <div class="form-group">
              <label for="exampleInputEmail1">Email address</label>
              <?php echo form_input(['id' => 'email', 'name' => 'email', 'class' => 'form-control', 'placeholder' => 'Enter email', 'value' => set_value('email',  $teacher->email)]); ?>
              <?php echo form_error('email');   ?>
            </div>
            <div class="form-group ">
              <label for="InputName">Father's Name</label>
              <?php echo form_input(['id' => 'father_name', 'name' => 'father_name', 'class' => 'form-control', 'placeholder' => 'Enter Father Name', 'type' => 'text', 'value' => set_value('father_name',  $teacher->father_name)]); ?>
              <?php echo form_error('father_name');   ?>
            </div>
            <div class="form-group ">
              <label for="InputName">Mother's Name</label>
              <?php echo form_input(['id' => 'mother_name', 'name' => 'mother_name', 'class' => 'form-control', 'placeholder' => 'Enter Mother Name', 'type' => 'text', 'value' => set_value('mother_name',  $teacher->mother_name)]); ?>
              <?php echo form_error('mother_name');   ?>
            </div>
            <div class="form-group ">
              <label for="InputName">Husband's Name</label>
              <?php echo form_input(['id' => 'husband_name', 'name' => 'husband_name', 'class' => 'form-control', 'placeholder' => 'Enter Husband Name', 'type' => 'text', 'value' => set_value('husband_name',  $teacher->husband_name)]); ?>
              <?php echo form_error('husband_name');   ?>
            </div>
            <div class="form-group">
              <label for="gender">Select Gender</label>
              <?php echo form_dropdown('gender', $gender, $teacher->gender, 'class="form-control"'); ?>
            </div>
            <div class="form-group ">
              <label for="InputName">Qualification</label>
              <?php echo form_input(['id' => 'qualification', 'name' => 'qualification', 'class' => 'form-control', 'placeholder' => 'Enter Qualification', 'type' => 'text', 'value' => set_value('qualification',  $teacher->qualification)]); ?>
              <?php echo form_error('qualification');   ?>
            </div>
            <div class="form-group">
              <label>Date of birth</label>
              <div class="input-group date">
                <?php echo form_input(['id' => 'dob', 'name' => 'dob', 'class' => 'form-control pull-left', 'placeholder' => 'Enter DOB (YYYY-MM-DD) ', 'type' => 'text', 'value' => set_value('dob',  $teacher->dob)]); ?>
                <div class="input-group-addon">
                  <i class="fa fa-calendar"></i>
                </div>
              </div>
              <?php echo form_error('dob'); ?>
            </div>
            <div class="form-group ">
              <label for="InputName">Address</label>
              <?php echo form_input(['id' => 'address', 'name' => 'address', 'class' => 'form-control', 'placeholder' => 'Enter Address', 'type' => 'text', 'value' => set_value('address',  $teacher->address)]); ?>
              <?php echo form_error('address');   ?>
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
  //Date picker
  $('#dob').datepicker({
    autoclose: true,
    format: 'yyyy-mm-dd'
  })
</script>
</body>

</html>