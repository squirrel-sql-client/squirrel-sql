package net.sourceforge.squirrel_sql.client.session.properties;
/*
 * Copyright (C) 2001 Colin Bell
 * colbell@users.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sourceforge.squirrel_sql.fw.gui.CharField;
import net.sourceforge.squirrel_sql.fw.gui.IntegerField;

import net.sourceforge.squirrel_sql.client.IApplication;
import net.sourceforge.squirrel_sql.client.preferences.IGlobalPreferencesPanel;
import net.sourceforge.squirrel_sql.client.preferences.SquirrelPreferences;
import net.sourceforge.squirrel_sql.client.resources.SquirrelResources;
import net.sourceforge.squirrel_sql.client.session.ISession;

public class SQLPropertiesPanel
		implements IGlobalPreferencesPanel, ISessionPropertiesPanel {
	private boolean _initialized = false;
	private String _title;
	private String _hint;
	private IApplication _app;
	private SessionProperties _props;

	private MyPanel _myPanel = new MyPanel();

	public SQLPropertiesPanel(String title, String hint) {
		super();

		_title = title != null ? title : MyPanel.i18n.SQL;
		_hint = hint != null ? hint : MyPanel.i18n.SQL;
	}

	public void initialize(IApplication app)
			throws IllegalArgumentException {
		if (app == null) {
			throw new IllegalArgumentException("Null IApplication passed");
		}

		_app = app;
		_props = app.getSquirrelPreferences().getSessionProperties();

		if (!_initialized) {
			_initialized = true;
			_myPanel.createUserInterface(app);
		}
		_myPanel.loadData(_props);
	}

	public void initialize(IApplication app, ISession session)
			throws IllegalArgumentException {
		if (app == null) {
			throw new IllegalArgumentException("Null IApplication passed");
		}
		if (session == null) {
			throw new IllegalArgumentException("Null ISession passed");
		}
		_app = app;
		_props = session.getProperties();

		if (!_initialized) {
			_initialized = true;
			_myPanel.createUserInterface(app);
		}
		_myPanel.loadData(_props);
	}

	public Component getPanelComponent() {
		return _myPanel;
	}

	public String getTitle() {
		return _title;
	}

	public String getHint() {
		return _hint;
	}

	public void applyChanges() {
		_myPanel.applyChanges(_props);
	}

	private static final class MyPanel extends JPanel {
		/**
		 * This interface defines locale specific strings. This should be
		 * replaced with a property file.
		 */
		interface i18n {
			String AUTO_COMMIT = "Auto Commit SQL:";
			String COMMIT_ON_CLOSE = "Commit On Closing Session:";
			String NBR_ROWS_CONTENTS = "Number of rows:";
			String NBR_ROWS_SQL = "Number of rows:";
			String LIMIT_ROWS_CONTENTS = "Contents - Limit rows:";
			String LIMIT_ROWS_SQL = "SQL - Limit rows:";
			String MULTIPLE_TABS_SQL = "SQL - Reuse Output Tabs:";
			String SHOW_ROW_COUNT = "Show Row Count for Tables:";
			String TABLE = "Table";
			String TEXT = "Text";
			String STATEMENT_SEPARATOR = "Statement Separator:";
			String PERF_WARNING = "<HTML><BR>Note: Settings marked with an hourglass will<BR>have performance implications.</HTML>";
			String SQL = "SQL";
		}

		private JCheckBox _autoCommitChk = new JCheckBox();
		private JCheckBox _commitOnClose = new JCheckBox();
		private IntegerField _contentsNbrRowsToShowField = new IntegerField();
		private JCheckBox _contentsLimitRowsChk = new JCheckBox();
		private JCheckBox _showRowCount = new JCheckBox();
		private IntegerField _sqlNbrRowsToShowField = new IntegerField();
		private JCheckBox _sqlLimitRows = new JCheckBox();
		private JCheckBox _sqlMultipleTabs = new JCheckBox();
		private CharField _stmtSepChar = new CharField();

		MyPanel() {
			super();
		}

		void loadData(SessionProperties props) {
			_autoCommitChk.setSelected(props.getAutoCommit());
			_commitOnClose.setSelected(props.getCommitOnClosingConnection());
			_contentsNbrRowsToShowField.setInt(props.getContentsNbrRowsToShow());
			_contentsLimitRowsChk.setSelected(props.getContentsLimitRows());
			_sqlNbrRowsToShowField.setInt(props.getSqlNbrRowsToShow());
			_sqlLimitRows.setSelected(props.getSqlLimitRows());
			_sqlMultipleTabs.setSelected(props.getSqlReuseOutputTabs());
			_showRowCount.setSelected(props.getShowRowCount());
			_stmtSepChar.setChar(props.getSqlStatementSeparatorChar());
		}

		void applyChanges(SessionProperties props) {
			props.setAutoCommit(_autoCommitChk.isSelected());
			props.setCommitOnClosingConnection(_commitOnClose.isSelected());
			props.setContentsNbrRowsToShow(_contentsNbrRowsToShowField.getInt());
			props.setContentsLimitRows(_contentsLimitRowsChk.isSelected());
			props.setSqlNbrRowsToShow(_sqlNbrRowsToShowField.getInt());
			props.setSqlLimitRows(_sqlLimitRows.isSelected());
			props.setSqlReuseOutputTabs(_sqlMultipleTabs.isSelected());
			props.setShowRowCount(_showRowCount.isSelected());
			props.setSqlStatementSeparatorChar(_stmtSepChar.getChar());
		}

		private void createUserInterface(IApplication app) {
			setLayout(new GridBagLayout());

			final Icon warnIcon = app.getResources().getIcon(SquirrelResources.ImageNames.PERFORMANCE_WARNING);
			_autoCommitChk.addChangeListener(new AutoCommitCheckBoxListener());
			_contentsLimitRowsChk.addChangeListener(new LimitRowsCheckBoxListener(_contentsNbrRowsToShowField));
			_sqlLimitRows.addChangeListener(new LimitRowsCheckBoxListener(_sqlNbrRowsToShowField));

			_contentsNbrRowsToShowField.setColumns(5);
			_sqlNbrRowsToShowField.setColumns(5);
			_stmtSepChar.setColumns(1);

			final GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill = gbc.HORIZONTAL;

			// First column is a set of labels.
			gbc.insets = new Insets(2, 0, 2, 4);
			gbc.gridx = 0;
			gbc.gridy = 0;
			add(new JLabel(i18n.AUTO_COMMIT), gbc);
			++gbc.gridy;
			add(new JLabel(i18n.SHOW_ROW_COUNT), gbc);
			++gbc.gridy;
			add(new JLabel(i18n.LIMIT_ROWS_CONTENTS), gbc);
			++gbc.gridy;
			add(new JLabel(i18n.LIMIT_ROWS_SQL), gbc);
			++gbc.gridy;
			add(new JLabel(i18n.MULTIPLE_TABS_SQL), gbc);

			// Second column is the data entry controls for the labels in first column.
			gbc.insets = new Insets(2, 0, 2, 1);
			gbc.gridx = 1;
			gbc.gridy = 0;
			add(_autoCommitChk, gbc);
			++gbc.gridy;
			add(_showRowCount, gbc);
			++gbc.gridy;
			add(_contentsLimitRowsChk, gbc);
			++gbc.gridy;
			add(_sqlLimitRows, gbc);
			++gbc.gridy;
			add(_sqlMultipleTabs, gbc);

			// Third column is the icon specifying "performance implications" for the data entry control
			// in the second column.
			gbc.insets = new Insets(2, 0, 2, 4);
			gbc.gridx = 2;
			gbc.gridy = 1;
			add(new JLabel(warnIcon), gbc);
			++gbc.gridy;
			add(new JLabel(warnIcon), gbc);
			++gbc.gridy;
			add(new JLabel(warnIcon), gbc);

			// Fourth column is a set of labels.
			gbc.insets = new Insets(2, 0, 2, 4);
			gbc.gridx = 3;
			gbc.gridy = 0;
			add(new JLabel(i18n.COMMIT_ON_CLOSE), gbc);
			++gbc.gridy;
			++gbc.gridy;
			add(new JLabel(i18n.NBR_ROWS_CONTENTS), gbc);
			++gbc.gridy;
			add(new JLabel(i18n.NBR_ROWS_SQL), gbc);
			++gbc.gridy;
			add(new JLabel(i18n.STATEMENT_SEPARATOR), gbc);

			// Fifth column is the data entry controls for the labels in fourth column.
			gbc.insets = new Insets(2, 0, 2, 1);
			gbc.gridx = 4;
			gbc.gridy = 0;
			add(_commitOnClose, gbc);
			++gbc.gridy;
			++gbc.gridy;
			add(_contentsNbrRowsToShowField, gbc);
			++gbc.gridy;
			add(_sqlNbrRowsToShowField, gbc);
			++gbc.gridy;
			add(_stmtSepChar, gbc);

			// Sixth column is the icon specifying "performance implications" for the data entry control
			// in the fifth column.
			gbc.gridx = 5;
			gbc.gridy = 3;
			add(new JLabel(warnIcon), gbc);
			++gbc.gridy;
			add(new JLabel(warnIcon), gbc);

			// Right at the bottom we put the performance warning.
			gbc.gridx = 0;
			gbc.gridy = gbc.RELATIVE;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			add(new JLabel(i18n.PERF_WARNING), gbc);
		}

		private class LimitRowsCheckBoxListener implements ChangeListener {
			private IntegerField _field;
			LimitRowsCheckBoxListener(IntegerField field) {
				super();
				_field = field;
			}
			public void stateChanged(ChangeEvent evt) {
				_field.setEnabled(((JCheckBox)evt.getSource()).isSelected());
			}
		}

		private class AutoCommitCheckBoxListener implements ChangeListener {
			public void stateChanged(ChangeEvent evt) {
				_commitOnClose.setEnabled(!((JCheckBox)evt.getSource()).isSelected());
			}
		}

		private final static class OutputType {
			private final String _name;
			private final String _className;

			OutputType(String name, String className) {
				super();
				_name = name;
				_className = className;
			}

			public String toString() {
				return _name;
			}

			String getPanelClassName() {
				return _className;
			}
		}

	}

}
