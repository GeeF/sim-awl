package org.pr2.simawl.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SimAWL implements EntryPoint {
	private static final int MAX_IN = 10;
	private static final int MAX_OUT = 1;
	private final DialogBox dialogBox = new DialogBox();
	private AWLParser awlParser;
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		RootPanel rootPanel = RootPanel.get("root_pnl");
		
		HorizontalPanel hPnl_right = new HorizontalPanel();
		rootPanel.add(hPnl_right, 388, 0);
		hPnl_right.setSize("382px", "690px");
		
		final VerticalPanel vPnl_input = new VerticalPanel();
		hPnl_right.add(vPnl_input);
		vPnl_input.setSize("384px", "378px");
		
		// Input Checkboxes
		for(int i = 0; i < 10; i++) {
			HorizontalPanel hp = new HorizontalPanel();
			vPnl_input.add(hp);
			CheckBox chkBx = new CheckBox("E 1."+i);
			hp.add(chkBx);
			hp.setCellVerticalAlignment(chkBx, HasVerticalAlignment.ALIGN_MIDDLE);
			final HTML html = new HTML("<div id=\"e1" + i + "\"></div>", true);
			html.setStyleName("status-red");
			hp.add(html);
			hp.setCellVerticalAlignment(html, HasVerticalAlignment.ALIGN_MIDDLE);
			
			chkBx.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					boolean checked = ((CheckBox) event.getSource()).getValue();
					onChk(html, checked);
				}
			});
		}

		HTMLPanel panel = new HTMLPanel("<br>\r\n<hline>");
		vPnl_input.add(panel);
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		vPnl_input.add(horizontalPanel);
		
		InlineLabel nlnlblA = new InlineLabel("A 1.0");
		nlnlblA.setStyleName("result_A");
		horizontalPanel.add(nlnlblA);
		horizontalPanel.setCellVerticalAlignment(nlnlblA, HasVerticalAlignment.ALIGN_MIDDLE);
		final HTML htmla10 = new HTML("<div id=\"A10\"></div>", true);
		htmla10.setStyleName("status-red");
		horizontalPanel.add(htmla10);
		horizontalPanel.setCellVerticalAlignment(htmla10, HasVerticalAlignment.ALIGN_MIDDLE);
		
		HorizontalPanel horizontalPanel_11 = new HorizontalPanel();
		vPnl_input.add(horizontalPanel_11);
		
		InlineLabel inlineLabel = new InlineLabel("VKE");
		inlineLabel.setStyleName("result_VKE");
		horizontalPanel_11.add(inlineLabel);
		final HTML htmlvke = new HTML("<div id=\"vke\"></div>", true);
		htmlvke.setStyleName("status-red");
		horizontalPanel_11.add(htmlvke);
		horizontalPanel_11.setCellVerticalAlignment(htmlvke, HasVerticalAlignment.ALIGN_MIDDLE);
		
		VerticalPanel vPnl_left = new VerticalPanel();
		rootPanel.add(vPnl_left, 0, 0);
		vPnl_left.setSize("388px", "688px");
		
		final TextArea textArea = new TextArea();
		vPnl_left.add(textArea);
		textArea.setSize("365px", "438px");
		textArea.setText("U E 1.0\nU E 1.1\n= A 1.0"); // default code
		
		Button btnParse = new Button("Parse");
		vPnl_left.add(btnParse);
		vPnl_left.setCellHorizontalAlignment(btnParse, HasHorizontalAlignment.ALIGN_CENTER);
		btnParse.setWidth("116px");
		btnParse.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onParse(vPnl_input, textArea);
			}
		});
		
		rootPanel.getElement().getStyle();
		// prepare dialogbox
		//dialogBox.setText("Could not parse Code.\nPlease check your AWL.");
		VerticalPanel vPnl_dialog = new VerticalPanel();
		Button btnDialogOk = new Button("OK");
		btnDialogOk.setWidth("50px");
		btnDialogOk.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
		HTML html_dialog = new HTML("Could not parse Code.<br>Please check your AWL.<br>");
		vPnl_dialog.add(html_dialog);
		vPnl_dialog.add(btnDialogOk);
		vPnl_dialog.setCellHorizontalAlignment(btnDialogOk, HasHorizontalAlignment.ALIGN_CENTER);
		vPnl_dialog.setCellHorizontalAlignment(html_dialog, HasHorizontalAlignment.ALIGN_CENTER);
		dialogBox.setText("An error occurred!");
		dialogBox.add(vPnl_dialog);
		dialogBox.setAnimationEnabled(true);
		dialogBox.center();
		dialogBox.hide();
		
		// Initialize AWL Parser
		awlParser = new AWLParser(MAX_IN, MAX_OUT);
		}
	
	private void onParse(VerticalPanel input, TextArea text) {
		boolean chkValues[] = new boolean[10];
		for(int i = 0; i < 10; i++) {
			HorizontalPanel p = (HorizontalPanel)input.getWidget(i);
			CheckBox cb = (CheckBox)p.getWidget(0);
			chkValues[i] = cb.getValue();
			
			HTML status = (HTML)p.getWidget(1);
			if(chkValues[i]) {
				status.setStyleName("status-green");
			}
			else {
				status.setStyleName("status-red");
			}
		}
		HorizontalPanel hp = (HorizontalPanel)input.getWidget(11);
		HTML a10 = (HTML)hp.getWidget(1);
		hp = (HorizontalPanel)input.getWidget(12);
		HTML vke = (HTML)hp.getWidget(1);
		
		// set html after parsing output
		// TODO: dynamic number of results
		boolean[] result = awlParser.parse(text.getText(), chkValues);
		GWT.log("Parser result:  A 1.0 " + String.valueOf(result[0]) + "    VKE: " + String.valueOf(result[MAX_OUT+1] + "    ERROR: " + String.valueOf(result[MAX_OUT])));
		// Error in parser occured
		if(result[MAX_OUT]) {
			dialogBox.show();
		}
		else {
			if(result[0]) a10.setStyleName("status-green");
			else a10.setStyleName("status-red");
			if(result[2]) vke.setStyleName("status-green");
			else vke.setStyleName("status-red");	
		}
	}
	
	private void onChk(HTML html, boolean checked) {
		if(checked) html.setStyleName("status-green");
		else html.setStyleName("status-red");
		GWT.log("chk click");
	}
}
