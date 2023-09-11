package net.sourceforge.squirrel_sql.client.session.mainpanel;

import net.sourceforge.squirrel_sql.fw.gui.ClipboardUtil;
import net.sourceforge.squirrel_sql.fw.util.StringManager;
import net.sourceforge.squirrel_sql.fw.util.StringManagerFactory;
import net.sourceforge.squirrel_sql.fw.util.Utilities;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CurrentSqlLabelController
{
   private static final Color TRANSPARENT = new Color(0x00FFFFFF, true);

   private static final StringManager s_stringMgr = StringManagerFactory.getStringManager(CurrentSqlLabelController.class);

   private String _normalizedSQL;
   private JTextPane _textPaneLabel;
   private int _rowCount = -1;
   private String _rawSQL;

   public CurrentSqlLabelController()
   {
      _textPaneLabel = new JTextPane();
      _textPaneLabel.setContentType("text/html");
      _textPaneLabel.setEditable(false);
      _textPaneLabel.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
      _textPaneLabel.setBackground(TRANSPARENT);
      _textPaneLabel.setOpaque(false);

      _textPaneLabel.addMouseListener(new MouseAdapter() {

         @Override
         public void mousePressed(MouseEvent e)
         {
            onMousePressedOnTextPane(e);
         }

         @Override
         public void mouseReleased(MouseEvent e)
         {
            onMousePressedOnTextPane(e);
         }
      });

   }

   private void onMousePressedOnTextPane(MouseEvent e)
   {
      if(false == e.isPopupTrigger())
      {
         return;
      }

      JPopupMenu popupMenu = new JPopupMenu();
      JMenuItem menuItem;

      menuItem = new JMenuItem(s_stringMgr.getString("CurrentSqlLabelController.copyRowNumber"));
      menuItem.addActionListener(ae -> onCopyRowNumber());
      popupMenu.add(menuItem);

      menuItem = new JMenuItem(s_stringMgr.getString("CurrentSqlLabelController.copySelection"));
      menuItem.addActionListener(ae -> onCopySelection());
      popupMenu.add(menuItem);

      menuItem = new JMenuItem(s_stringMgr.getString("CurrentSqlLabelController.copyAll"));
      menuItem.addActionListener(ae -> onCopyAll());
      popupMenu.add(menuItem);

      menuItem = new JMenuItem(s_stringMgr.getString("CurrentSqlLabelController.copySQL"));
      menuItem.addActionListener(ae -> onCopySQL());
      popupMenu.add(menuItem);

      popupMenu.show(_textPaneLabel, e.getX(), e.getY());
   }

   private void onCopySQL()
   {
      ClipboardUtil.copyToClip(_rawSQL, true);
   }

   private void onCopyAll()
   {
      try
      {
         final String htmlFreePlainText =
               _textPaneLabel.getDocument().getText(0, _textPaneLabel.getDocument().getLength());

         ClipboardUtil.copyToClip(htmlFreePlainText, true);
      }
      catch (BadLocationException e)
      {
         throw Utilities.wrapRuntime(e);
      }
   }

   private void onCopySelection()
   {
      ClipboardUtil.copyToClip(_textPaneLabel.getSelectedText(), true);
   }

   private void onCopyRowNumber()
   {
      if(-1 < _rowCount)
      {
         ClipboardUtil.copyToClip("" + _rowCount, true);
      }
   }

   public void reInit(int rowCount, boolean isResultLimitedByMaxRowsCount)
   {
      reInit(rowCount, isResultLimitedByMaxRowsCount, null, null);
   }

   public void reInit(int rowCount, boolean isResultLimitedByMaxRowsCount, String normalizedSQL, String rawSQL)
   {
      if(null != normalizedSQL)
      {
         _normalizedSQL = normalizedSQL;
      }

      if(null != rawSQL)
      {
         _rawSQL = rawSQL;
      }

      _rowCount = rowCount;

      String escapedSql = Utilities.escapeHtmlChars(_normalizedSQL);

      if (isResultLimitedByMaxRowsCount)
      {
         String limitMsg = s_stringMgr.getString("ResultTab.limitMessage", Integer.valueOf(rowCount));
         _textPaneLabel.setText("<html><pre>" + limitMsg + ";&nbsp;&nbsp;" + escapedSql + "</pre></html>");
      }
      else
      {
         String rowsMsg = s_stringMgr.getString("ResultTab.rowsMessage", Integer.valueOf(rowCount));
         _textPaneLabel.setText("<html><pre>" + rowsMsg + ";&nbsp;&nbsp;" + escapedSql + "</pre></html>");
      }
   }


   public void clear()
   {
      _normalizedSQL = "";
      _textPaneLabel.setText("");
   }

   public JTextPane getLabel()
   {
      return _textPaneLabel;
   }
}
