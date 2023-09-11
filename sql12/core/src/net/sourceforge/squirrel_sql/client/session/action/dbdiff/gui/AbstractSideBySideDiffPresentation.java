/*
 * Copyright (C) 2011 Rob Manning
 * manningr@users.sourceforge.net
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package net.sourceforge.squirrel_sql.client.session.action.dbdiff.gui;

import net.sourceforge.squirrel_sql.client.Main;
import net.sourceforge.squirrel_sql.client.session.ISession;
import net.sourceforge.squirrel_sql.client.session.action.dbdiff.DBDiffScriptFileManager;
import net.sourceforge.squirrel_sql.client.session.action.dbdiff.prefs.DBDiffPreferenceBean;
import net.sourceforge.squirrel_sql.fw.dialects.CreateScriptPreferences;
import net.sourceforge.squirrel_sql.fw.dialects.DialectFactoryImpl;
import net.sourceforge.squirrel_sql.fw.dialects.HibernateDialect;
import net.sourceforge.squirrel_sql.fw.sql.IDatabaseObjectInfo;
import net.sourceforge.squirrel_sql.fw.sql.ITableInfo;
import net.sourceforge.squirrel_sql.fw.util.FileWrapper;
import net.sourceforge.squirrel_sql.fw.util.FileWrapperFactory;
import net.sourceforge.squirrel_sql.fw.util.FileWrapperFactoryImpl;
import net.sourceforge.squirrel_sql.fw.util.IOUtilitiesImpl;
import net.sourceforge.squirrel_sql.fw.util.log.ILogger;
import net.sourceforge.squirrel_sql.fw.util.log.LoggerController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all DiffPresentation implementations that display a comparison of the contents of two files
 * side-by-side in some internal or external window.
 */
public abstract class AbstractSideBySideDiffPresentation implements IDiffPresentation
{

	/** Logger for this class. */
	private final static ILogger s_log = LoggerController.createLogger(AbstractSideBySideDiffPresentation.class);

	/** fileWrapperFactory that allows this class to avoid constructing File objects directly */
	protected FileWrapperFactory fileWrapperFactory = new FileWrapperFactoryImpl();

	/**
	 * Sub-class implementations should override this method to provide the implementation for comparing the
	 * contents of the specified script filenames.
	 * 
	 * @param script1Filename
	 *           filename of the first script to compare.
	 * @param script2Filename
	 *           filename of the second script to compare.
	 * @throws Exception
	 */
	public abstract void executeDiff(String script1Filename, String script2Filename) throws Exception;

	/**
	 * @see IDiffPresentation#execute()
	 */
	@Override
	public void execute()
	{

		final ISession sourceSession = Main.getApplication().getDBDiffState().getSourceSession();
		final DBDiffScriptFileManager scriptFileManager = Main.getApplication().getDBDiffState().getScriptFileManager();

		final IDatabaseObjectInfo[] selectedDestObjects = Main.getApplication().getDBDiffState().getDestSelectedDatabaseObjects();
		final ISession destSession = Main.getApplication().getDBDiffState().getDestSession();
		final IDatabaseObjectInfo[] selectedSourceObjects = Main.getApplication().getDBDiffState().getSourceSelectedDatabaseObjects();

		// Here we use the same dialect for both the source and destination object.
		final HibernateDialect dialect = new DialectFactoryImpl().getDialect(sourceSession.getMetaData());

		final CreateScriptPreferences csprefs = new CreateScriptPreferences();

		final List<ITableInfo> sourcetables = getContainedTableInfos(selectedSourceObjects);

		final List<ITableInfo> desttables = getContainedTableInfos(selectedDestObjects);

		try
		{

			DBDiffPreferenceBean prefs = Main.getApplication().getDBDiffState().getDBDiffPreferenceBean();

			final String script1 =
					constructScriptFromList(dialect.getCreateTableSQL(sourcetables, sourceSession.getMetaData(), csprefs,false, prefs.isSortColumnsForSideBySideComparison()));

			final String script2 =
				constructScriptFromList(dialect.getCreateTableSQL(desttables, destSession.getMetaData(), csprefs,false, prefs.isSortColumnsForSideBySideComparison()));

			final String sourceFilename = scriptFileManager.getOutputFilenameForSession(sourceSession, 1);
			final String destFilename = scriptFileManager.getOutputFilenameForSession(destSession, 2);

			writeScriptToFile(script1, sourceFilename);
			writeScriptToFile(script2, destFilename);

			executeDiff(sourceFilename, destFilename);
		}
		catch (final Exception e)
		{
			s_log.error("Unexpected exception while generating sql scripts : " + e.getMessage(), e);
		}

	}

	private void writeScriptToFile(String sqlscript, String file) throws IOException
	{
		final FileWrapper sourcefileWrapper = fileWrapperFactory.create(file);
		new IOUtilitiesImpl().copyBytesToFile(new ByteArrayInputStream(sqlscript.getBytes()), sourcefileWrapper);
	}

	private List<ITableInfo> getContainedTableInfos(IDatabaseObjectInfo[] dbObjs)
	{
		final List<ITableInfo> result = new ArrayList<>();
		for (final IDatabaseObjectInfo dbObj : dbObjs)
		{
			if (dbObj instanceof ITableInfo)
			{
				final ITableInfo ti = (ITableInfo) dbObj;
				result.add(ti);
			}
		}
		return result;
	}

	private String constructScriptFromList(List<String> sqlscript)
	{
		final StringBuilder script = new StringBuilder();
		for (final String sql : sqlscript)
		{
			script.append(sql);
			script.append(";\n\n");
		}
		return script.toString();
	}
}
