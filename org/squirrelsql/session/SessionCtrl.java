package org.squirrelsql.session;

import javafx.scene.Node;
import javafx.scene.control.Label;
import org.squirrelsql.aliases.Alias;
import org.squirrelsql.aliases.dbconnector.DbConnectorResult;
import org.squirrelsql.services.I18n;

public class SessionCtrl
{
   private DbConnectorResult _dbConnectorResult;

   private I18n _i18n = new I18n(getClass());

   public SessionCtrl(DbConnectorResult dbConnectorResult)
   {
      _dbConnectorResult = dbConnectorResult;
   }

   public String getTabHeaderNode()
   {
      Alias alias = _dbConnectorResult.getAlias();
      return _i18n.t("session.tab.header", alias.getName(), alias.getUserName());
   }

   public Node getNode()
   {
      return new Label("Session goes here");
   }
}
