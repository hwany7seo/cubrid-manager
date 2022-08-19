/*
 * Copyright (C) 2009 Search Solution Corporation. All rights reserved by Search
 * Solution.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: -
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. - Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. - Neither the name of the <ORGANIZATION> nor the names
 * of its contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */
package com.cubrid.cubridmanager.ui.monitoring.editor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.experimental.chart.swt.ChartComposite;
import org.slf4j.Logger;

import com.cubrid.common.core.util.LogUtil;
import com.cubrid.common.ui.spi.event.CubridNodeChangedEvent;
import com.cubrid.common.ui.spi.model.ICubridNode;
import com.cubrid.common.ui.spi.part.CubridViewPart;
import com.cubrid.cubridmanager.core.common.model.ServerInfo;
import com.cubrid.cubridmanager.core.monitoring.model.DiagStatusResult;
import com.cubrid.cubridmanager.core.monitoring.model.StatusTemplateInfo;
import com.cubrid.cubridmanager.core.monitoring.model.TargetConfigInfo;
import com.cubrid.cubridmanager.core.monitoring.task.GetDiagdataTask;
import com.cubrid.cubridmanager.ui.CubridManagerUIPlugin;

/**
 * 
 * A editor part is used to view status monitor.
 * 
 * @author lizhiqiang
 * @version 1.0 - 2009-5-5 created by lizhiqiang
 */
public class StatusMonitorViewPart extends
		CubridViewPart {

	private static final Logger LOGGER = LogUtil.getLogger(StatusMonitorViewPart.class);
	public static final String ID = "com.cubrid.cubridmanager.ui.monitoring.editor.StatusMonitorViewPart";
	private int numChart;
	private TimeSeries maximum[];
	private TimeSeries minimum[];
	private TimeSeries average[];
	private TimeSeries current[];
	private Composite composite;
	private boolean runflag = true;
	private ServerInfo site;
	private static DiagStatusResult diagOldOneStatusResult = new DiagStatusResult();
	private static DiagStatusResult diagOldTwoStatusResult = new DiagStatusResult();
	private DiagStatusResult diagStatusResult = new DiagStatusResult();
	private List<TargetConfig> monitorList;
	private int[] min;
	private int[] max;
	private double[] avg;
	private long monitorTimes;
	private Text[] currentTxt;
	private Text[] minTxt;
	private Text[] maxTxt;
	private Text[] avgTxt;
	private int term = 1;
	private String dbName;
	private int startRun = 0;
	private Calendar lastSec;
	private Calendar nowSec;

	/**
	 * @param site IViewSite the view site
	 * @throws PartInitException if this view was not initialized successfully
	 * @see com.cubrid.common.ui.spi.part.CubridViewPart#init(org.eclipse.ui.IViewSite)
	 */
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		initSection();
	}

	/**
	 *Initializes the page
	 * 
	 */
	private void initSection() {
		ICubridNode selection = getCubridNode();
		site = selection.getServer().getServerInfo();
		StatusTemplateInfo statusTemplateInfo = (StatusTemplateInfo) selection.getAdapter(StatusTemplateInfo.class);
		String samplingTerm = statusTemplateInfo.getSampling_term();
		dbName = statusTemplateInfo.getDb_name();
		if (dbName == null) {
			dbName = "";
		}
		term = Integer.parseInt(samplingTerm);
		List<TargetConfigInfo> list = statusTemplateInfo.getTargetConfigInfoList();

		TargetConfigInfo targetConfigInfo = (TargetConfigInfo) list.get(0);
		numChart = targetConfigInfo.getList().size();
		List<String[]> targetInfos = targetConfigInfo.getList();
		monitorList = new ArrayList<TargetConfig>();
		EnumMap<EnumTargetConfig, TargetConfig> map = TargetConfigMap.getInstance().getMap();
		for (String[] targetInfo : targetInfos) {
			for (TargetConfig tc : map.values()) {
				if (targetInfo[0].equalsIgnoreCase(tc.getName())) {
					tc.setColor(Integer.parseInt(targetInfo[1]));
					tc.setMagnification(Float.parseFloat(targetInfo[2]));
					monitorList.add(tc);
				}
			}

		}
		maximum = new TimeSeries[numChart];
		minimum = new TimeSeries[numChart];
		average = new TimeSeries[numChart];
		current = new TimeSeries[numChart];
		min = new int[numChart];
		max = new int[numChart];
		avg = new double[numChart];
		currentTxt = new Text[numChart];
		minTxt = new Text[numChart];
		maxTxt = new Text[numChart];
		avgTxt = new Text[numChart];

	}

	/**
	 * Creates the SWT controls for this workbench part.
	 * 
	 * @param parent the parent control
	 */
	@Override
	public void createPartControl(Composite parent) {
		final Composite composite = new Composite(parent, SWT.RESIZE);
		this.composite = composite;
		load();
	}

	/**
	 * Defines the layout of chart and starts the thread
	 * 
	 */
	public void load() {
		GridLayout layout = new GridLayout();
		if (numChart == 4 || numChart == 3) {
			layout.numColumns = 2;
		} else if (numChart % 4 == 0) {
			layout.numColumns = 4;
		} else {
			layout.numColumns = 3;
		}
		composite.setLayout(layout);

		for (int i = 0; i < numChart; i++) {
			final Composite comp = new Composite(composite, SWT.RESIZE);
			comp.setLayout(new GridLayout());
			final GridData gdUnit = new GridData(SWT.FILL, SWT.FILL, true, true);
			comp.setLayoutData(gdUnit);
			createPlotTableUnit(comp, i);
		}

		new DataGenerator().start();
	}

	/**
	 * Creates the plot table unit
	 * 
	 * @param composite The composite to contains plot table unit
	 * @param unitNumber the number of unit
	 */
	private void createPlotTableUnit(Composite composite, int unitNumber) {
		final JFreeChart chart = createChart(unitNumber);
		ChartComposite frame = new ChartComposite(composite, SWT.NONE, chart,
				false, true, false, true, true);
		GridData gdFrame = new GridData(SWT.FILL, SWT.FILL, true, true);
		frame.setLayoutData(gdFrame);
		frame.setLayout(new FillLayout());
		createValueDisplay(composite, unitNumber);
	}

	/**
	 * Creates chart unit
	 * 
	 * @param unitNumber the number of unit
	 * @return JFreeChart
	 */
	private JFreeChart createChart(int unitNumber) {
		String currentLbl = "Current";
		current[unitNumber] = new TimeSeries(currentLbl);
		current[unitNumber].setMaximumItemAge(18000000);

		String minLbl = "Min";
		minimum[unitNumber] = new TimeSeries(minLbl);
		minimum[unitNumber].setMaximumItemAge(18000000);

		String maxLbl = "Max";
		maximum[unitNumber] = new TimeSeries(maxLbl);
		maximum[unitNumber].setMaximumItemAge(18000000);

		String avgLbl = "Avg";
		average[unitNumber] = new TimeSeries(avgLbl);
		average[unitNumber].setMaximumItemAge(18000000);

		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
		timeseriescollection.addSeries(current[unitNumber]);
		timeseriescollection.addSeries(minimum[unitNumber]);
		timeseriescollection.addSeries(maximum[unitNumber]);
		timeseriescollection.addSeries(average[unitNumber]);

		DateAxis dateaxis = new DateAxis("");
		NumberAxis numberaxis = new NumberAxis("");
		dateaxis.setTickLabelFont(new Font("SansSerif", 0, 10));
		dateaxis.setLabelFont(new Font("SansSerif", 0, 7));
		XYLineAndShapeRenderer xylineandshaperenderer = new XYLineAndShapeRenderer(
				true, false);
		xylineandshaperenderer.setSeriesPaint(0, new Color(146, 208, 80));
		xylineandshaperenderer.setSeriesPaint(1, new Color(166, 166, 166));
		xylineandshaperenderer.setSeriesPaint(2, new Color(74, 126, 187));
		xylineandshaperenderer.setSeriesPaint(3, new Color(255, 51, 0));
		xylineandshaperenderer.setSeriesStroke(0, new BasicStroke(2F, 0, 2));
		xylineandshaperenderer.setSeriesStroke(3, new BasicStroke(2F, 0, 2));
		XYPlot xyplot = new XYPlot(timeseriescollection, dateaxis, numberaxis,
				xylineandshaperenderer);
		xyplot.setBackgroundPaint(Color.BLACK);
		xyplot.setDomainGridlinePaint(new Color(130, 130, 130));
		xyplot.setRangeGridlinePaint(new Color(130, 130, 130));

		dateaxis.setFixedAutoRange(300000d);
		dateaxis.setLowerMargin(0.0D);
		dateaxis.setUpperMargin(0.0D);
		dateaxis.setTickLabelsVisible(true);
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		JFreeChart chart = new JFreeChart(
				monitorList.get(unitNumber).getChartTitle(), new Font(
						"SansSerif", 1, 15), xyplot, false);

		return chart;
	}

	/**
	 * Creates the value showing composite
	 * 
	 * @param comp Composite
	 * @param unitNumber the number of unit
	 * 
	 */
	private void createValueDisplay(Composite comp, int unitNumber) {
		GridLayout txtLayout = new GridLayout(18, true);
		txtLayout.verticalSpacing = 0;
		txtLayout.horizontalSpacing = 0;
		final GridData gdTxtComp = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Composite txtComp = new Composite(comp, SWT.RESIZE);
		txtComp.setLayout(txtLayout);
		txtComp.setLayoutData(gdTxtComp);

		new Label(txtComp, SWT.NONE);

		final Label currentLbl = new Label(txtComp, SWT.NONE | SWT.RIGHT);
		final GridData gdCurrentLbl = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gdCurrentLbl.horizontalSpan = 2;
		currentLbl.setLayoutData(gdCurrentLbl);
		currentLbl.setText("Current");

		final Label currentImg = new Label(txtComp, SWT.NONE | SWT.RIGHT);
		currentImg.setImage(CubridManagerUIPlugin.getImage("icons/st_line_cur.png"));

		currentTxt[unitNumber] = new Text(txtComp, SWT.NONE | SWT.LEFT);
		final GridData gdCurrentText = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gdCurrentText.horizontalSpan = 1;
		currentTxt[unitNumber].setBackground(comp.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		currentTxt[unitNumber].setLayoutData(gdCurrentText);

		final Label minLbl = new Label(txtComp, SWT.NONE | SWT.RIGHT);
		final GridData gdMinLbl = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gdMinLbl.horizontalSpan = 2;
		minLbl.setLayoutData(gdMinLbl);
		minLbl.setText("Min");

		final Label minImg = new Label(txtComp, SWT.NONE | SWT.RIGHT);
		minImg.setImage(CubridManagerUIPlugin.getImage("icons/st_line_min.png"));

		minTxt[unitNumber] = new Text(txtComp, SWT.NONE | SWT.LEFT);
		final GridData gdMinText = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gdMinText.horizontalSpan = 1;
		minTxt[unitNumber].setBackground(comp.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		minTxt[unitNumber].setLayoutData(gdMinText);

		final Label maxLbl = new Label(txtComp, SWT.NONE | SWT.RIGHT);
		final GridData gdMaxLbl = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gdMaxLbl.horizontalSpan = 2;
		maxLbl.setLayoutData(gdMaxLbl);
		maxLbl.setText("Max");

		final Label maxImg = new Label(txtComp, SWT.NONE | SWT.RIGHT);
		maxImg.setImage(CubridManagerUIPlugin.getImage("icons/st_line_max.png"));

		maxTxt[unitNumber] = new Text(txtComp, SWT.NONE | SWT.LEFT);
		final GridData gdMaxTxt = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gdMaxTxt.horizontalSpan = 1;
		maxTxt[unitNumber].setBackground(comp.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		maxTxt[unitNumber].setLayoutData(gdMaxTxt);

		final Label avgLbl = new Label(txtComp, SWT.RIGHT);
		final GridData gdAvgLbl = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gdAvgLbl.horizontalSpan = 2;
		avgLbl.setLayoutData(gdAvgLbl);
		avgLbl.setText("Avg");

		final Label avgImg = new Label(txtComp, SWT.NONE | SWT.RIGHT);
		avgImg.setImage(CubridManagerUIPlugin.getImage("icons/st_line_avg.png"));

		avgTxt[unitNumber] = new Text(txtComp, SWT.NONE | SWT.LEFT);
		final GridData gdAvgTxt = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gdAvgTxt.horizontalSpan = 1;
		avgTxt[unitNumber].setBackground(comp.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		avgTxt[unitNumber].setLayoutData(gdAvgTxt);

		new Label(txtComp, SWT.NONE);
	}

	/**
	 * Adds the current value observation
	 * 
	 * @param num int[] all the unit number to be observed
	 */
	private void addCurrentObservation(int[] num) {
		for (int i = 0; i < numChart; i++) {
			current[i].add(new Millisecond(), num[i]);
			currentTxt[i].setText(Integer.toString(num[i]));
		}
	}

	/**
	 * Adds the average value observation
	 * 
	 * @param avg double[] all the all average value
	 */
	private void addAvgObservation(double[] avg) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		nf.setGroupingUsed(false);
		for (int i = 0; i < numChart; i++) {
			String value = nf.format(avg[i]);
			average[i].add(new Millisecond(), Double.valueOf(value));
			avgTxt[i].setText(value);
		}
	}

	/**
	 * Adds the minimum value observation
	 * 
	 * @param dval int[]
	 */
	private void addMinObservation(int dval[]) {
		for (int i = 0; i < numChart; i++) {
			minimum[i].add(new Millisecond(), dval[i]);
			minTxt[i].setText(Integer.toString(dval[i]));
		}
	}

	/**
	 * Adds the maximum value observation
	 * 
	 * @param dval int[]
	 */
	private void addMaxObservation(int[] dval) {
		for (int i = 0; i < numChart; i++) {
			maximum[i].add(new Millisecond(), dval[i]);
			maxTxt[i].setText(Integer.toString(dval[i]));
		}
	}

	/**
	 * A inner class that update the data of chart in a single thread
	 * 
	 * @author lizhiqiang
	 * @version 1.0 - 2009-6-4 created by lizhiqiang
	 */
	class DataGenerator extends
			Thread {
		private Map<String, String> updateMap;
		private int count;

		/**
		 * Thread run method
		 */
		@Override
		public void run() {
			while (getRunflag()) {
				try {
					updateMap = getUpdateValue(startRun);
					Thread.sleep(1000);
				} catch (Exception e) {
					LOGGER.error(e.getMessage());
				}
				if (startRun <= 1) {
					startRun++;
				} else {
					if (count % term == 0) {
						Display.getDefault().asyncExec(new Runnable() {
							public void run() {
								if (composite != null
										&& !composite.isDisposed()) {
									update(updateMap);
								}
							}

						});
						count = 0;
					}
					count++;
				}
			}
		}
	}

	/**
	 * Get update value
	 * 
	 * @param startRun int
	 * @return Map<String, String>
	 */
	private Map<String, String> getUpdateValue(int startRun) {
		GetDiagdataTask task = new GetDiagdataTask(site);
		if (dbName.trim().length() > 0) {
			task.setDbname(dbName);
		}

		List<String> monitorNameList = new ArrayList<String>();

		for (TargetConfig tc : monitorList) {
			monitorNameList.add(tc.getMonitorName());
		}
		task.buildMsg(monitorNameList);
		task.setUsingSpecialDelimiter(true);
		task.execute();
		Map<String, String> resultMap = null;
		if (!task.isSuccess()) {
			return null;
		}
		float inter = 0.0f;

		if (startRun == 0) {
			diagStatusResult = task.getResult();
			return diagStatusResult.getDiagStatusResultMap();
		} else if (startRun == 1) {
			lastSec = Calendar.getInstance();

			diagOldOneStatusResult.copy_from(diagStatusResult);
			diagStatusResult = task.getResult();
			DiagStatusResult diagStatusResultDelta = new DiagStatusResult();
			diagStatusResultDelta.getDelta(diagStatusResult,
					diagOldOneStatusResult);
			return diagStatusResultDelta.getDiagStatusResultMap();
		} else {
			nowSec = Calendar.getInstance();
			double interval = (double) (nowSec.getTimeInMillis() - lastSec.getTimeInMillis()) / 1000;
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMaximumFractionDigits(3);
			inter = Float.parseFloat(nf.format(interval));
			LOGGER.debug("monitorTimes =" + monitorTimes);
			LOGGER.debug("lastSec time =" + lastSec.getTimeInMillis());
			LOGGER.debug("nowSec time =" + nowSec.getTimeInMillis());
			LOGGER.debug("inter =" + inter);
			lastSec = nowSec;

			diagOldTwoStatusResult.copy_from(diagOldOneStatusResult);
			diagOldOneStatusResult.copy_from(diagStatusResult);
			diagStatusResult = task.getResult();
			DiagStatusResult diagStatusResultDelta = new DiagStatusResult();
			diagStatusResultDelta.getDelta(diagStatusResult,
					diagOldOneStatusResult, diagOldTwoStatusResult, inter);
			resultMap = diagStatusResultDelta.getDiagStatusResultMap();
		}
		return resultMap;

	}

	/**
	 * Update the data of chart
	 * 
	 * @param resultMap Map<String, String>
	 */
	private void update(Map<String, String> resultMap) {

		int[] values = new int[numChart];
		for (int k = 0; k < monitorList.size(); k++) {
			for (Map.Entry<String, String> entry : resultMap.entrySet()) {
				if (monitorList.get(k).getMonitorName().equals(entry.getKey())) {
					values[k] = Integer.valueOf(entry.getValue());
					if (values[k] < min[k]) {
						min[k] = values[k];
					}
					if (values[k] > max[k]) {
						max[k] = values[k];
					}
					avg[k] = avg[k]
							* ((double) (monitorTimes) / (monitorTimes + 1))
							+ (double) (values[k]) / (monitorTimes + 1);
				}
			}
		}
		addCurrentObservation(values);
		addMinObservation(min);
		addMaxObservation(max);
		addAvgObservation(avg);
		//logger.debug("current:"+values[0]+" min:"+min[0]+" max:"+max[0]+" Avg:"+avg[0]);
		monitorTimes += 1;
	}

	/**
	 * Gets the value of runflag
	 * 
	 * @return boolean
	 */
	public boolean getRunflag() {
		synchronized (this) {
			return runflag;
		}
	}

	/**
	 * Disposes this view when it closed
	 */
	public void dispose() {
		synchronized (this) {
			runflag = false;
			super.dispose();
		}
	}

	/**
	 * Responses when the node changes
	 * 
	 * @param ex CubridNodeChangedEvent
	 */
	public void nodeChanged(CubridNodeChangedEvent ex) {
		//
	}

}
