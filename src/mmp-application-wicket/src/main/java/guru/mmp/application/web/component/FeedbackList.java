/*
 * Copyright 2014 Marcus Portmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package guru.mmp.application.web.component;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.wicket.Component;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.FeedbackMessagesModel;
import org.apache.wicket.feedback.IFeedback;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.Response;

import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>FeedbackList</code> class renders the <code>FeedBackMessage</code>s for the page
 * that are not linked to a particular input control as a set of alerts.
 *
 * @author Marcus Portmann
 */
public class FeedbackList extends Component
  implements IFeedback
{
  private static final long serialVersionUID = 1000000;

  /**
   * @see org.apache.wicket.Component#Component(String)
   *
   * @param id the non-null id of this component
   */
  public FeedbackList(final String id)
  {
    super(id);

    setOutputMarkupId(true);

    setDefaultModel(new FeedbackMessagesModel(this));
  }

  /**
   * Returns the filtered feedback messages that will be displayed as alerts.
   *
   * @return the filtered feedback messages that will be displayed as alerts, possibly empty
   */
  protected final List<FeedbackMessage> getFilteredMessages()
  {
    @SuppressWarnings("unchecked") final List<FeedbackMessage> messages =
      (List<FeedbackMessage>) getDefaultModelObject();

    List<FeedbackMessage> filteredMessages = new ArrayList<>();

    for (FeedbackMessage message : messages)
    {
      Component reporter = message.getReporter();

      if ((reporter instanceof Button) || (reporter instanceof Form<?>))
      {
        filteredMessages.add(message);
      }
    }

    return filteredMessages;
  }

  /**
   * Render the alerts.
   */
  @Override
  protected void onRender()
  {
    MarkupStream markupStream = findMarkupStream();
    MarkupElement element = markupStream.get();
    Response response = getResponse();

    if (element instanceof ComponentTag)
    {
      List<FeedbackMessage> messages = getFilteredMessages();

      if (messages.size() == 0)
      {
        response.write("<div id=\"");
        response.write(getMarkupId());
        response.write("\" class=\"alerts\"></div>");

        return;
      }

      StringBuilder errorBuffer = null;
      StringBuilder infoBuffer = null;
      StringBuilder warningBuffer = null;
      StringBuilder debugBuffer = null;

      int numberOfErrorMessages = 0;
      int numberOfInfoMessages = 0;
      int numberOfWarningMessages = 0;
      int numberOfDebugMessages = 0;

      for (FeedbackMessage message : messages)
      {
        if (message.isError())
        {
          if (errorBuffer == null)
          {
            errorBuffer = new StringBuilder();
          }

          numberOfErrorMessages++;
        }
        else if (message.isWarning())
        {
          if (warningBuffer == null)
          {
            warningBuffer = new StringBuilder();
          }

          numberOfWarningMessages++;
        }
        else if (message.isInfo())
        {
          if (infoBuffer == null)
          {
            infoBuffer = new StringBuilder();
          }

          numberOfInfoMessages++;
        }
        else if (message.isFatal())
        {
          if (errorBuffer == null)
          {
            errorBuffer = new StringBuilder();
          }

          numberOfErrorMessages++;
        }
        else if (message.isDebug())
        {
          if (debugBuffer == null)
          {
            debugBuffer = new StringBuilder();
          }

          numberOfDebugMessages++;
        }
      }

      for (FeedbackMessage message : messages)
      {
        if (message.isError())
        {
          if (numberOfErrorMessages == 1)
          {
            errorBuffer.append(message.getMessage().toString());
          }
          else
          {
            errorBuffer.append("<li>");
            errorBuffer.append(message.getMessage().toString());
            errorBuffer.append("</li>");
          }
        }
        else if (message.isWarning())
        {
          if (numberOfWarningMessages == 1)
          {
            warningBuffer.append(message.getMessage().toString());
          }
          else
          {
            warningBuffer.append("<li>");
            warningBuffer.append(message.getMessage().toString());
            warningBuffer.append("</li>");
          }
        }
        else if (message.isInfo())
        {
          if (numberOfInfoMessages == 1)
          {
            infoBuffer.append(message.getMessage().toString());
          }
          else
          {
            infoBuffer.append("<li>");
            infoBuffer.append(message.getMessage().toString());
            infoBuffer.append("</li>");
          }
        }
        else if (message.isFatal())
        {
          if (numberOfErrorMessages == 1)
          {
            errorBuffer.append(message.getMessage().toString());
          }
          else
          {
            errorBuffer.append("<li>");
            errorBuffer.append(message.getMessage().toString());
            errorBuffer.append("</li>");
          }
        }
        else if (message.isDebug())
        {
          if (numberOfDebugMessages == 1)
          {
            debugBuffer.append(message.getMessage().toString());
          }
          else
          {
            debugBuffer.append("<li>");
            debugBuffer.append(message.getMessage().toString());
            debugBuffer.append("</li>");
          }
        }

        message.markRendered();
      }

      response.write("<div id=\"");
      response.write(getMarkupId());
      response.write("\" class=\"alerts\">");

      // Display error messages first
      if (numberOfErrorMessages > 0)
      {
        response.write("<div class=\"errorHandler alert alert-danger\">");
        response.write("<button data-dismiss=\"alert\" class=\"close\">&times;</button>");
        response.write("<i class=\"fa fa-times-circle\"></i>&nbsp;");

        if (numberOfErrorMessages == 1)
        {
          response.write(errorBuffer.toString());
          response.write("</div>");
        }
        else
        {
          response.write("The following errors occurred while processing your request:");
          response.write("<ul style=\"padding: 5px 0px 0px 19px;\">");
          response.write(errorBuffer.toString());
          response.write("</ul></div>");
        }
      }

      // Display warning messages second
      if (numberOfWarningMessages > 0)
      {
        response.write("<div class=\"alert alert-warning\">");
        response.write("<button data-dismiss=\"alert\" class=\"close\">&times;</button>");
        response.write("<i class=\"fa fa-exclamation-triangle\"></i>&nbsp;");

        if (numberOfWarningMessages == 1)
        {
          response.write(warningBuffer.toString());
          response.write("</div>");
        }
        else
        {
          response.write("The following warnings were raised while processing your request:");
          response.write("<ul style=\"padding: 5px 0px 0px 19px;\">");
          response.write(warningBuffer.toString());
          response.write("</ul></div>");
        }
      }

      // Display informational messages third
      if (numberOfInfoMessages > 0)
      {
        response.write("<div class=\"alert alert-success\">");
        response.write("<button data-dismiss=\"alert\" class=\"close\">&times;</button>");
        response.write("<i class=\"fa fa-check-circle\"></i>&nbsp;");

        if (numberOfInfoMessages == 1)
        {
          response.write(infoBuffer.toString());
          response.write("</div>");
        }
        else
        {
          response.write("Please take note of the following:");
          response.write("<ul style=\"padding: 5px 0px 0px 19px;\">");
          response.write(infoBuffer.toString());
          response.write("</ul></div>");
        }
      }

      // Display debug messages fourth
      if (numberOfDebugMessages > 0)
      {
        response.write("<div class=\"alert alert-info\">");
        response.write("<button data-dismiss=\"alert\" class=\"close\">&times;</button>");
        response.write("<i class=\"fa fa-info-circle\"></i>&nbsp;");

        if (numberOfDebugMessages == 1)
        {
          response.write(debugBuffer.toString());
          response.write("</div>");
        }
        else
        {
          response.write("Please take note of the following:");
          response.write("<ul style=\"padding: 5px 0px 0px 19px;\">");
          response.write(debugBuffer.toString());
          response.write("</ul></div>");
        }
      }

      response.write("</div>");
    }
  }
}
