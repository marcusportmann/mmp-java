// Show feedback for a form control
function show_form_component_feedback(form_component_id, feedback_class, feedback_message_classes, feedback_message)
{
  var formField = $("#" + form_component_id)

  var formFieldLabel = formField.parents("label");

  formFieldLabel.removeClass("has-error has-warning has-info has-success")

  // Remove the feedback div for the form field if it exists
  $("#" + form_component_id + "-feedback").remove()

  // Add the feedback class to the parent form group for the form field
  formFieldLabel.addClass(feedback_class)

  // Create the new feedback div for the form field
  if (feedback_message.length > 0)
  {
    formFieldLabel.parent().append($("<div id=\"" + form_component_id + "-feedback\" class=\"" + feedback_class + " " + feedback_message_classes + " help-block help-block-left animated fadeInDown\">" + feedback_message + "</div>"));
  }
}

// Clear feedback for a form control
function clear_form_component_feedback(form_component_id)
{
  var formField = $("#" + form_component_id)

  var formFieldLabel = formField.parents("label");

  formFieldLabel.removeClass("has-error has-warning has-info has-success")

  // Remove the feedback div for the form field if it exists
  $("#" + form_component_id + "-feedback").remove()
}



$(document).ready(function() {

  // Initialise Select2 elements
  $(".select2").select2();

  // Initialise the datepicker elements
  $(".datepicker").datepicker({ autoclose: true });

  // Initialise the bootstrap-timepicker elements
  $(".timepicker").timepicker({ showInputs: false });

  // TODO: Initialise other "standard" elements here
});

