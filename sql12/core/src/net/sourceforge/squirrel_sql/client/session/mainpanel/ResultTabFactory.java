package net.sourceforge.squirrel_sql.client.session.mainpanel;

import net.sourceforge.squirrel_sql.client.session.ISession;
import net.sourceforge.squirrel_sql.client.session.SQLExecutionInfo;
import net.sourceforge.squirrel_sql.fw.datasetviewer.DataSetException;
import net.sourceforge.squirrel_sql.fw.datasetviewer.IDataSetUpdateableTableModel;
import net.sourceforge.squirrel_sql.fw.datasetviewer.ResultSetDataSet;
import net.sourceforge.squirrel_sql.fw.datasetviewer.ResultSetMetaDataDataSet;
import net.sourceforge.squirrel_sql.fw.id.IntegerIdentifierFactory;

import java.util.ArrayList;

public class ResultTabFactory
{
   private IntegerIdentifierFactory _idFactory = new IntegerIdentifierFactory();
   private ISession _session;
   private SQLResultExecuterPanelFacade _sqlResultExecuterPanelFacade;

   public ResultTabFactory(ISession session, SQLResultExecuterPanelFacade sqlResultExecuterPanelFacade)
   {
      _session = session;
      _sqlResultExecuterPanelFacade = sqlResultExecuterPanelFacade;
   }

   public ResultTab createResultTab(SQLExecutionInfo exInfo, IDataSetUpdateableTableModel dataSetUpdateableTableModel, ResultSetDataSet rsds, ResultSetMetaDataDataSet mdds) throws DataSetException
   {
      final ResultTabListener resultTabListener = (sql, resultTab) -> _sqlResultExecuterPanelFacade.rerunSQL(sql, resultTab);


      ResultTab tab = new ResultTab(_session, _sqlResultExecuterPanelFacade, _idFactory.createIdentifier(), exInfo, dataSetUpdateableTableModel, resultTabListener);
      tab.showResults(rsds, mdds);
      return tab;
   }

   public ErrorPanel createErrorPanel(ArrayList<String> sqlExecErrorMsgs, String lastExecutedStatement)
   {
      ErrorPanelListener errorPanelListener = new ErrorPanelListener()
      {
         @Override
         public void removeErrorPanel(ErrorPanel errorPanel)
         {
            _sqlResultExecuterPanelFacade.removeErrorPanel(errorPanel);
         }
      };

      return new ErrorPanel(_session, errorPanelListener, sqlExecErrorMsgs, lastExecutedStatement);
   }


}
