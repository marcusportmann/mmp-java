// Show feedback for a form control
function show_form_component_feedback(form_component_id, feedback_class, feedback_message)
{
  var formField = $("#" + form_component_id)

  var formGroup = formField.closest(".form-group")

  // Remove any existing feedback classes from the parent form group for the form field
  formGroup.removeClass("has-error has-warning has-info has-success")

  // Remove the feedback div for the form field if it exists
  $("#" + form_component_id + "-feedback").remove()

  // Add the feedback class to the parent form group for the form field
  formGroup.addClass(feedback_class)

  // Create the new feedback div for the form field
  if (feedback_message.length > 0)
  {
    formField.parents(".form-group > div").append($("<div id=\"" + form_component_id + "-feedback\"class=\"help-block help-block-left animated fadeInDown\">" + feedback_message + "</div>"));
  }
}

// Clear feedback for a form control
function clear_form_component_feedback(form_component_id)
{
  var formField = $("#" + form_component_id)

  var formGroup = formField.closest(".form-group")

  // Remove any existing feedback classes from the parent form group for the form field
  formGroup.removeClass("has-error has-warning has-info has-success")

  // Remove the feedback div for the form field if it exists
  $("#" + form_component_id + "-feedback").remove()
}



