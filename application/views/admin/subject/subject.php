<div class="content-wrapper">
  <!-- Content Header (Page header) -->
  <section class="content-header">
    <a href="<?php echo base_url('subject/add'); ?>" class="btn btn-primary btn-s">Add Subject</a>
  </section>

  <?php if ($feedback = $this->session->flashdata('feedback')) :
    $feedback_class = $this->session->flashdata('feedback_class');
    ?>
    <div class="row">
      <div class="col-lg-6 col-lg-offset-3">
        <div class="alert <?= $feedback_class ?> alert-dismissible" role="alert">
          <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span>
          </button>
          <strong><?= $feedback ?></strong>
        </div>
      </div>
    </div>
  <?php endif; ?>

  <section class="content">
    <div class="row">
      <div class="col-xs-12">

        <div class="box">
          <div class="box-header">
            <h3 class="box-title">Subject Details</h3>
          </div>
          <!-- /.box-header -->
          <div class="box-body">
            <table id="example2" class="table table-bordered table-striped">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Name</th>
                  <th>Description</th>
                  <th>Class</th>
                  <th>Syllabus</th>
                  <th>Action</th>
                </tr>
              </thead>
              <tbody>
                <?php
                if (!empty($SUBJECTS)) {
                  foreach ($SUBJECTS as $subject) { ?>
                    <tr>
                      <td><?= $subject->id; ?></td>
                      <td><?= $subject->name; ?></td>
                      <td><?= $subject->description; ?></td>
                      <td><?= $subject->class_id->name; ?></td>
                      <td>
                        <?php if(!empty($subject->syllabus)) { ?>
                          <a href="<?php echo base_url("subject/download/$subject->id"); ?>" class="fa fa-download" aria-hidden="true"></a>
                        <?php } ?>
                      </td>
                      <td>
                        <a href="<?php echo base_url("subject/edit/$subject->id"); ?>" class="fa fa-pencil" aria-hidden="true"></a>&nbsp;&nbsp;&nbsp;
                        <a class="fa fa-trash" onclick="javascript:deleteConfirm('<?php echo base_url() . 'subject/delete/' . $subject->id; ?>');" deleteConfirm href="#"></a>
                      </td>
                    </tr>
                  <?php }
                } ?>
            </table>
          </div>
          <!-- /.box-body -->
        </div>
        <!-- /.box -->
      </div>
      <!-- /.col -->
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


<script type="text/javascript">
  function deleteConfirm(url) {
    if (confirm('Do you want to Delete this record ?')) {
      window.location.href = url;
    }
  }
</script>


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
<script>
  $(function() {
    $("#example1").DataTable();
    $('#example2').DataTable({
      "sScrollX": "100%",
      "bScrollCollapse": true,
      "paging": true,
      "lengthChange": true,
      "searching": true,
      "ordering": true,
      "info": true,
      "autoWidth": false
    });
  });
</script>
</body>

</html>