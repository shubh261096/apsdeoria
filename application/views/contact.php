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

                <li><a href="<?php echo base_url(); ?>">Home</a></li>
                <li><a href="<?php echo base_url() . 'events'; ?>">Events</a></li>
                <li><a href="<?php echo base_url() . 'about'; ?>">About Us</a></li>
                <li class="active"><a>Contact Us</a></li>
                <li><a href="<?php echo base_url() . 'gallery'; ?>">Gallery</a></li>
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
            <?php echo form_open('contact/add', ['role' => 'form', 'class' => 'probootstrap-form']); ?>

            <?php if ($feedback = $this->session->flashdata('feedback')) :
              $feedback_class = $this->session->flashdata('feedback_class');
            ?>
              <div class="alert <?= $feedback_class ?> alert-dismissible" role="alert">
                <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <strong><?= $feedback ?></strong>
              </div>
            <?php endif; ?>
            <div class="form-group">
              <label for="full_name">Full Name</label>
              <?php echo form_input(['id' => 'full_name', 'name' => 'full_name', 'class' => 'form-control', 'type' => 'text', 'value' => set_value('full_name')]); ?>
              <?php echo form_error('full_name');   ?>
            </div>
            <div class="form-group">
              <label for="phone_no">Phone No.</label>
              <?php echo form_input(['id' => 'phone_no', 'name' => 'phone_no', 'class' => 'form-control', 'type' => 'Tel', 'value' => set_value('phone_no')]); ?>
              <?php echo form_error('phone_no');   ?>
            </div>
            <div class="form-group">
              <label for="email">Email</label>
              <?php echo form_input(['id' => 'email', 'name' => 'email', 'class' => 'form-control', 'type' => 'emailAddress', 'value' => set_value('email')]); ?>
              <?php echo form_error('email');   ?>
            </div>
            <div class="form-group">
              <label for="subject">Subject</label>
              <?php echo form_input(['id' => 'subject', 'name' => 'subject', 'class' => 'form-control', 'type' => 'text', 'value' => set_value('subject')]); ?>
              <?php echo form_error('subject');   ?>
            </div>
            <div class="form-group">
              <label for="message">Message</label>
              <?php echo form_textarea(['id' => 'message', 'name' => 'message', 'class' => 'form-control', 'cols' => '30', 'rows' => '10', 'value' => set_value('message')]); ?>
              <?php echo form_error('message');   ?>
            </div>
            <div class="form-group">
              <?php echo form_submit(['id' => 'submit', 'value' => 'Send Message',  'class' => 'btn btn-primary btn-lg']); ?>
            </div>
            <?php echo form_close(); ?>
          </div>
        </div>
      </div>
    </div>
  </div>
</section>