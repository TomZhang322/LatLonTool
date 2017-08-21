package com.adcc.view;

import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;

import com.adcc.util.CalculationLogLatDistance;
import com.adcc.util.GISUtil;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class MainShell extends Shell {
	private Text textLon;
	private Text textLat;
	private Text textDistance;
	private Text textAngel;
	private Text textDestLon;
	private Text textDestLat;
	
    //经纬度含点的正则校验
	private String REGEX_LATLON = "^[E|W|N|S]{1}[0-9]*[.].*$";

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			MainShell shell = new MainShell(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public MainShell(Display display) {
		super(display, SWT.SHELL_TRIM);
		
		Group group = new Group(this, SWT.NONE);
		group.setBounds(10, 10, 503, 336);
		
		Label lblNewLabel = new Label(group, SWT.NONE);
		lblNewLabel.setBounds(20, 30, 61, 17);
		lblNewLabel.setText("原点经度：");
		
		textLon = new Text(group, SWT.BORDER);
		textLon.setText("E1131835");
		textLon.setBounds(96, 30, 101, 23);
		
		Label lblNewLabel_1 = new Label(group, SWT.NONE);
		lblNewLabel_1.setBounds(20, 89, 61, 17);
		lblNewLabel_1.setText("原点纬度：");
		
		textLat = new Text(group, SWT.BORDER);
		textLat.setText("N232332");
		textLat.setBounds(96, 86, 101, 23);
		
		Label lblNewLabel_2 = new Label(group, SWT.NONE);
		lblNewLabel_2.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		lblNewLabel_2.setBounds(216, 36, 101, 17);
		lblNewLabel_2.setText("（如：E1131835）");
		
		Label lblNewLabel_3 = new Label(group, SWT.NONE);
		lblNewLabel_3.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		lblNewLabel_3.setBounds(216, 89, 101, 17);
		lblNewLabel_3.setText("（如：N232332）");
		
		Label lblNewLabel_4 = new Label(group, SWT.NONE);
		lblNewLabel_4.setBounds(20, 145, 61, 17);
		lblNewLabel_4.setText("两点距离：");
		
		textDistance = new Text(group, SWT.BORDER);
		textDistance.setText("200");
		textDistance.setBounds(96, 142, 101, 23);
		
		Label lblkm = new Label(group, SWT.NONE);
		lblkm.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		lblkm.setText("（单位：km）");
		lblkm.setBounds(216, 142, 101, 17);
		
		Label lblNewLabel_5 = new Label(group, SWT.NONE);
		lblNewLabel_5.setBounds(20, 189, 61, 17);
		lblNewLabel_5.setText("方位角：");
		
		textAngel = new Text(group, SWT.BORDER);
		textAngel.setBounds(96, 189, 101, 23);
		
		Label label_1 = new Label(group, SWT.NONE);
		label_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		label_1.setText("（如：10.5）");
		label_1.setBounds(216, 189, 86, 17);
		
		Button btnCalculate = new Button(group, SWT.NONE);
		btnCalculate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String lon1 = textLon.getText().trim();
				String lat1 = textLat.getText().trim();
				String distance = textDistance.getText().trim();
				String angel = textAngel.getText().trim();
				
	            if (Pattern.matches(REGEX_LATLON, lon1)) {
	            	lon1 = lon1.substring(0, lon1.indexOf("."));
	            }
	            if (Pattern.matches(REGEX_LATLON, lat1)) {
	            	lat1 = lat1.substring(0, lat1.indexOf("."));
	            }
	            double curLon = GISUtil.parseEWNSValue2(lon1);
	            double curLat = GISUtil.parseEWNSValue2(lat1);
	            
	            String ss = CalculationLogLatDistance.ConvertDistanceToLogLat(curLon, curLat, Double.parseDouble(distance), 
	            		Double.parseDouble(angel));
	            String[] arr = ss.split(",");
	            textDestLon.setText(arr[0].toString());
	            textDestLat.setText(arr[1].toString());
			}
		});
		btnCalculate.setBounds(20, 235, 80, 27);
		btnCalculate.setText("计算");
		
		Label lblNewLabel_6 = new Label(group, SWT.NONE);
		lblNewLabel_6.setBounds(20, 287, 80, 17);
		lblNewLabel_6.setText("目标点经度：");
		
		textDestLon = new Text(group, SWT.BORDER);
		textDestLon.setEditable(false);
		textDestLon.setBounds(96, 287, 141, 23);
		
		Label lblNewLabel_7 = new Label(group, SWT.NONE);
		lblNewLabel_7.setBounds(259, 287, 75, 17);
		lblNewLabel_7.setText("目标点纬度：");
		
		textDestLat = new Text(group, SWT.BORDER);
		textDestLat.setEditable(false);
		textDestLat.setBounds(340, 284, 141, 23);
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("根据一点经纬度、距离、方位角，计算另一点经纬度");
		setSize(539, 394);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
