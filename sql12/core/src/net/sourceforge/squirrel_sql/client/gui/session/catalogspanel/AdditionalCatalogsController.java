package net.sourceforge.squirrel_sql.client.gui.session.catalogspanel;

import net.sourceforge.squirrel_sql.client.Main;
import net.sourceforge.squirrel_sql.client.session.ISession;
import net.sourceforge.squirrel_sql.fw.gui.GUIUtils;
import net.sourceforge.squirrel_sql.fw.util.Utilities;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Collections;

public class AdditionalCatalogsController
{

   private final AdditionalCatalogsDlg _dlg;
   private boolean _ok;

   public AdditionalCatalogsController(ISession session)
   {
      try
      {
         _dlg = new AdditionalCatalogsDlg(GUIUtils.getOwningWindow(session.getSessionPanel()));

         String[] catalogs = session.getSQLConnection().getSQLMetaData().getCatalogs();

         AliasCatalogLoadModelJsonBean bean = Main.getApplication().getCatalogLoadModelManager().getAliasCatalogLoadModelJsonBean(session.getAlias());

         DefaultListModel<CatalogChecked> listModel = new DefaultListModel<>();
         for (String catalog : catalogs)
         {
            listModel.addElement(createCatalogChecked(catalog, bean));
         }

         _dlg.chkLstCatalogs.setModel(listModel);

         _dlg.chkLstCatalogs.setCellRenderer(new CheckListRenderer());

         // Add a mouse listener to toggle the checkbox when an item is clicked
         _dlg.chkLstCatalogs.addMouseListener(new MouseAdapter()
         {
            @Override
            public void mouseClicked(MouseEvent event)
            {
               onMouseClicked(event);
            }
         });


         _dlg.btnSelectAll.addActionListener(e -> onSelectAll());
         _dlg.btnInvertSelection.addActionListener(e -> onInvertSelection());
         _dlg.btnOk.addActionListener(e -> onOk(session));

         GUIUtils.initLocation(_dlg, 400, 600);
         GUIUtils.enableCloseByEscape(_dlg);

         _dlg.setVisible(true);


      }
      catch (SQLException e)
      {
         throw Utilities.wrapRuntime(e);
      }
   }

   private void onInvertSelection()
   {
      for (int i = 0; i < _dlg.chkLstCatalogs.getModel().getSize(); i++)
      {
         CatalogChecked catalogChecked = _dlg.chkLstCatalogs.getModel().getElementAt(i);
         catalogChecked.setChecked(!catalogChecked.isChecked());
      }
      _dlg.chkLstCatalogs.repaint();
   }

   private void onSelectAll()
   {
      for (int i = 0; i < _dlg.chkLstCatalogs.getModel().getSize(); i++)
      {
          _dlg.chkLstCatalogs.getModel().getElementAt(i).setChecked(true);
      }
      _dlg.chkLstCatalogs.repaint();
   }

   private void onMouseClicked(MouseEvent event)
   {
      int index = _dlg.chkLstCatalogs.locationToIndex(event.getPoint());
      if (    index >= 0
           && index < _dlg.chkLstCatalogs.getModel().getSize()
           && _dlg.chkLstCatalogs.getCellBounds(index, index).contains(event.getPoint()))
      {
         CatalogChecked catalogChecked = _dlg.chkLstCatalogs.getModel().getElementAt(index);
         catalogChecked.setChecked(!catalogChecked.isChecked());
         _dlg.chkLstCatalogs.repaint(_dlg.chkLstCatalogs.getCellBounds(index, index));
      }
   }

   public boolean isOk()
   {
      return _ok;
   }

   private void onOk(ISession session)
   {
      AliasCatalogLoadModelJsonBean bean = Main.getApplication().getCatalogLoadModelManager().getAliasCatalogLoadModelJsonBean(session.getAlias());

      bean.getAdditionalUserChosenCatalogs().clear();

      for (CatalogChecked catalogChecked : Collections.list(((DefaultListModel<CatalogChecked>) _dlg.chkLstCatalogs.getModel()).elements()))
      {
         if (catalogChecked.isChecked())
         {
            bean.getAdditionalUserChosenCatalogs().add(catalogChecked.getCatalog());
         }
      }

      _ok = true;

      _dlg.setVisible(false);
      _dlg.dispose();
   }

   private static CatalogChecked createCatalogChecked(String catalog, AliasCatalogLoadModelJsonBean bean)
   {
      boolean checked =
            bean.getAdditionalUserChosenCatalogs().stream().anyMatch(c -> StringUtils.equalsIgnoreCase(c, catalog));

      return new CatalogChecked(catalog, checked);
   }
}
