<div class="content-wrapper">
  <!-- Content Header (Page header) -->
  <section class="content-header">
    <a href="<?php echo base_url('fees/add'); ?>" class="btn btn-primary btn-s">Add Fee</a>
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
            <h3 class="box-title">Fee Details</h3>
          </div>
          <!-- /.box-header -->
          <div class="box-body">
            <table id="example2" class="table table-bordered table-striped">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Student Name - ID</th>
                  <th>Monthly Amount</th>
                  <th>Due Amount</th>
                  <th>Paid Amount</th>
                  <th>Month</th>
                  <th>Year</th>
                  <th>Date Paid</th>
                  <th>Status</th>
                  <th>Action</th>
                </tr>
              </thead>
              <tbody>
                <?php
                if (!empty($FEES)) {
                  foreach ($FEES as $fee) { ?>
                    <tr>
                      <td><?= $fee->id; ?></td>
                      <td><?= $fee->student_id->fullname . " - " . $fee->student_id->id; ?></td>
                      <td><?= $fee->fees_id->total_amount; ?></td>
                      <td><?= $fee->due_amount; ?></td>
                      <td><?= $fee->fees_paid; ?></td>
                      <td><?= $fee->period; ?></td>
                      <td><?= $fee->year; ?></td>
                      <td><?= $fee->date_paid; ?></td>
                      <td>
                        <?php if ($fee->status == 'paid') { ?>
                          <span class="label label-success">paid</span>
                        <?php } else { ?>
                          <span class="label label-danger">unpaid</span>
                        <?php } ?>
                      </td>
                      <td>
                        <a href="<?php echo base_url("fees/edit/$fee->id"); ?>" class="fa fa-pencil" aria-hidden="true"></a>&nbsp;&nbsp;&nbsp;
                        <!-- <a class="fa fa-trash" onclick="javascript:deleteConfirm('<?php echo base_url() . 'fees/delete/' . $fee->id; ?>');" deleteConfirm href="#"></a> -->
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