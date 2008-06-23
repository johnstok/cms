package ccc.view.contentcreator.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ContentCreator implements EntryPoint {

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    Image img = new Image("http://code.google.com/webtoolkit/logo-185x175.png");
    Button button = new Button("Clik me");

    VerticalPanel vPanel = new VerticalPanel();
    // We can add style names.
    vPanel.addStyleName("widePanel");
    vPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
    vPanel.add(img);
    vPanel.add(button);

    // Add image and button to the RootPanel
    RootPanel.get().add(vPanel);

    // Create the dialog box
    final DialogBox dialogBox = new DialogBox();
    dialogBox.setText("Welcome to GWT!");
    dialogBox.setAnimationEnabled(true);
    Button closeButton = new Button("close");
    VerticalPanel dialogVPanel = new VerticalPanel();
    dialogVPanel.setWidth("100%");
    dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
    dialogVPanel.add(closeButton);

    closeButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        dialogBox.hide();
      }
    });

    // Set the contents of the Widget
    dialogBox.setWidget(dialogVPanel);
    
    button.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
         
         ResourceServiceAsync resourceService = (ResourceServiceAsync) GWT.create(ResourceService.class);

         AsyncCallback<String> callback = new AsyncCallback<String>() {
           public void onSuccess(String result) {
             Window.alert(result);
           }

           public void onFailure(Throwable caught) {
              Window.alert(caught.getMessage());
           }
         };

         resourceService.save(callback);

//        dialogBox.center();
//        dialogBox.show();
      }
    });
  }
}
