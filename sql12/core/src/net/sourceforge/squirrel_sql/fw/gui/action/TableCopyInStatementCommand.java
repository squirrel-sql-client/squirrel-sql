package net.sourceforge.squirrel_sql.fw.gui.action;

/*
 * Copyright (C) 2005 Gerd Wagner
 * gerdwagner@users.sourceforge.net
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

import net.sourceforge.squirrel_sql.client.session.ISession;
import net.sourceforge.squirrel_sql.fw.datasetviewer.ColumnDisplayDefinition;
import net.sourceforge.squirrel_sql.fw.datasetviewer.ExtTableColumn;
import net.sourceforge.squirrel_sql.fw.gui.ClipboardUtil;
import net.sourceforge.squirrel_sql.fw.util.ICommand;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.util.ArrayList;

/**
 * This command gets the current selected text from a <TT>JTable</TT>
 * and formats it as HTML table and places it on the system clipboard.
 *
 * @author <A HREF="mailto:colbell@users.sourceforge.net">Colin Bell</A>
 */
public class TableCopyInStatementCommand extends TableCopySqlPartCommandBase implements ICommand
{
   /**
    * The table we are copying data from.
    */
   private JTable _table;

   /**
    * Ctor specifying the <TT>JTable</TT> to get the data from.
    *
    * @param	table	The <TT>JTable</TT> to get data from.
    * @throws	IllegalArgumentException Thrown if <tt>null</tt> <tt>JTable</tt> passed.
    */
   public TableCopyInStatementCommand(JTable table, ISession session)
   {
      super(session);
      if (table == null)
      {
         throw new IllegalArgumentException("JTable == null");
      }
      _table = table;
   }

   /**
    * Execute this command.
    */
   public void execute()
   {
      ArrayList<InStatColumnInfo> inStatColumnInfos = getInStatColumnInfos();

      if(0 == inStatColumnInfos.size())
      {
         return;
      }

      StringBuffer buf = new StringBuffer();

      for (InStatColumnInfo inStatColumnInfo : inStatColumnInfos)
      {
         buf.append(inStatColumnInfo.getInstat()).append("\n");
      }

      ClipboardUtil.copyToClip(buf);

   }


   public ArrayList<InStatColumnInfo> getInStatColumnInfos()
   {
      return getInStatColumnInfos(false);
   }

   public ArrayList<InStatColumnInfo> getInStatColumnInfos(boolean firstLineAllColumns)
   {
      ArrayList<InStatColumnInfo> ret = new ArrayList<>();

      int nbrSelRows = _table.getSelectedRowCount();
      int nbrSelCols = _table.getSelectedColumnCount();
      int[] selRows = _table.getSelectedRows();
      int[] selCols = _table.getSelectedColumns();

      if(firstLineAllColumns && 0 < _table.getRowCount())
      {
         nbrSelRows = 1;
         nbrSelCols = 1;
         selRows = new int[]{0};
         selCols = new int[_table.getColumnModel().getColumnCount()];

         for (int i = 0; i < selCols.length; i++)
         {
            selCols[i] = i;
         }
      }

      if (selRows.length != 0 && selCols.length != 0)
      {
         for (int colIdx = 0; colIdx < nbrSelCols; ++colIdx)
         {
            TableColumn col = _table.getColumnModel().getColumn(selCols[colIdx]);

            InStatColumnInfo infoBuf = new InStatColumnInfo();
            ret.add(infoBuf);

            ColumnDisplayDefinition colDef = null;
            if(col instanceof ExtTableColumn)
            {
               colDef = ((ExtTableColumn) col).getColumnDisplayDefinition();
               infoBuf.setColDef(colDef);
            }

            StringBuffer buf = new StringBuffer();
            int lastLength = buf.length();
            buf.append("(");
            for (int rowIdx = 0; rowIdx < nbrSelRows; ++rowIdx)
            {
               if(0 < rowIdx)
               {
                  buf.append(",");
                  if(100 < buf.length() - lastLength)
                  {
                     lastLength = buf.length();
                     buf.append("\n");
                  }
               }

               final Object cellObj = _table.getValueAt(selRows[rowIdx], selCols[colIdx]);
               buf.append(getData(colDef, cellObj, StatType.IN));
            }
            buf.append(")");
            infoBuf.setInstat(buf);
         }
      }

      return ret;
   }

}
