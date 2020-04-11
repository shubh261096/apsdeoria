<section class="probootstrap-section probootstrap-section-colored">
    <div class="container">
        <div class="row">
            <div class="col-md-12 text-left section-heading probootstrap-animate mb0">
                <h1 class="mb0">Admission Form</h1>
            </div>
        </div>
    </div>
</section>

<section class="probootstrap-section probootstrap-bg-white">
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <h3>Fill this form & pay</h3>
                <?php if ($feedback = $this->session->flashdata('feedback')) :
                    $feedback_class = $this->session->flashdata('feedback_class');
                ?>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="alert <?= $feedback_class ?> alert-dismissible" role="alert">
                                <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span>
                                </button>
                                <strong><?= $feedback ?></strong>
                            </div>
                        </div>
                    </div>
                <?php endif; ?>
                <?php echo form_open('admission/add', ['role' => 'form', 'class' => 'probootstrap-form']); ?>
                <div class="form-group">
                    <label for="full_name">Student Full Name</label>
                    <?php echo form_input(['id' => 'full_name', 'name' => 'full_name', 'class' => 'form-control', 'type' => 'text', 'value' => set_value('full_name')]); ?>
                    <?php echo form_error('full_name');   ?>
                </div>
                <div class="form-group">
                    <label for="class">Select Class</label>
                    <?php echo form_dropdown('class', $class, '', 'class="form-control"'); ?>
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
                    <?php echo form_submit(['id' => 'pay_online', 'name' => 'pay_online', 'value' => 'Pay Online',  'class' => 'btn btn-primary btn-lg']); ?> OR
                    <?php echo form_submit(['id' => 'pay_upi', 'name' => 'pay_upi', 'value' => 'Pay using UPI',  'class' => 'btn btn-primary btn-lg']); ?>
                </div>
                <?php echo form_close(); ?>
            </div>
        </div>
    </div>
</section>