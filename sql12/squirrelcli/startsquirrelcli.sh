#! /bin/sh

export CLASSPATH=/home/gerd/programme/java/jdk1.8.0_152/jre/lib/charsets.jar:/home/gerd/programme/java/jdk1.8.0_152/jre/lib/deploy.jar:/home/gerd/programme/java/jdk1.8.0_152/jre/lib/ext/cldrdata.jar:/home/gerd/programme/java/jdk1.8.0_152/jre/lib/ext/dnsns.jar:/home/gerd/programme/java/jdk1.8.0_152/jre/lib/ext/jaccess.jar:/home/gerd/programme/java/jdk1.8.0_152/jre/lib/ext/jfxrt.jar:/home/gerd/programme/java/jdk1.8.0_152/jre/lib/ext/localedata.jar:/home/gerd/programme/java/jdk1.8.0_152/jre/lib/ext/nashorn.jar:/home/gerd/programme/java/jdk1.8.0_152/jre/lib/ext/sunec.jar:/home/gerd/programme/java/jdk1.8.0_152/jre/lib/ext/sunjce_provider.jar:/home/gerd/programme/java/jdk1.8.0_152/jre/lib/ext/sunpkcs11.jar:/home/gerd/programme/java/jdk1.8.0_152/jre/lib/ext/zipfs.jar:/home/gerd/programme/java/jdk1.8.0_152/jre/lib/javaws.jar:/home/gerd/programme/java/jdk1.8.0_152/jre/lib/jce.jar:/home/gerd/programme/java/jdk1.8.0_152/jre/lib/jfr.jar:/home/gerd/programme/java/jdk1.8.0_152/jre/lib/jfxswt.jar:/home/gerd/programme/java/jdk1.8.0_152/jre/lib/jsse.jar:/home/gerd/programme/java/jdk1.8.0_152/jre/lib/management-agent.jar:/home/gerd/programme/java/jdk1.8.0_152/jre/lib/plugin.jar:/home/gerd/programme/java/jdk1.8.0_152/jre/lib/resources.jar:/home/gerd/programme/java/jdk1.8.0_152/jre/lib/rt.jar:/home/gerd/work/java/squirrel/squirrelpj/bin:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/asm.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/poi.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/x86.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/axis.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/core.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/osgi.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/RText.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/antlr.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/cglib.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/dom4j.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/forms.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/icu4j.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/jmeld.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/log4j.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/common.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/rstaui.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/jcommon.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/nanoxml.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/javahelp.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/jide-oss.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/xml-apis.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/xmlbeans.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/asm-attrs.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/axis-saaj.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/hibernate.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/poi-ooxml.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/treetable.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/jfreechart.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/axis-jaxrpc.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/axis-wsdl4j.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/commons-cli.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/spring-core.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/autocomplete.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/commons-lang.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/spring-beans.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/swing-worker.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/versioncheck.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/commons-codec.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/spring-context.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/stringtemplate.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/commons-logging.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/rsyntaxtextarea.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/javax.activation.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/commons-discovery.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/poi-ooxml-schemas.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/commons-httpclient.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/jackson-core-2.6.3.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/hibernate-annotations.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/jackson-databind-2.6.3.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/spring-context-support.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/hibernate-entitymanager.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/markdowngeneratorJava1_6.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/commons-collections-3.2.1.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/jackson-annotations-2.6.3.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/org.eclipse.equinox.common.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/core/lib/hibernate-commons-annotations.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/plugins/laf/lafs/looks.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/plugins/laf/lafs/skinlf.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/plugins/laf/lafs/ilf-gpl.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/plugins/laf/lafs/tinylaf.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/plugins/laf/lafs/toniclf.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/plugins/laf/lafs/napkinlaf.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/plugins/laf/lafs/substance.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/plugins/laf/lafs/nimrod-laf.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/plugins/laf/lafs/kunstoff-laf.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/plugins/laf/lafs/JTattoo-1.6.10.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/plugins/laf/lafs/swingsetthemes.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/plugins/postgres/lib/postgis-jdbc-1.3.3.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/plugins/postgres/lib/postgresql-8.3-603.jdbc3.jar:/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/translations/squirrel-sql_fr.jar

export _JAVA_OPTIONS="-Dsquirrel.home=/home/gerd/work/java/squirrel/squirrel-sql-git/sql12/output/dist/ -Dsquirrel.userdir=/home/gerd/work/java/squirrel/userdir -Dsquirrel.param1='$1' -Dsquirrel.param2='$2'"


# Batch example:
# ./startsquirrelcli.sh "connect(PostgreSQL p_c)"  "select * from barcodes" | less

if [ $# == 0 ]; then
   /home/gerd/programme/java/jdk-9.0.1/bin/jshell --class-path $CLASSPATH  /home/gerd/work/java/squirrel/squirrel-sql-git/sql12/squirrelcli/squirrelcli.jsh
else
   /home/gerd/programme/java/jdk-9.0.1/bin/jshell --class-path $CLASSPATH  /home/gerd/work/java/squirrel/squirrel-sql-git/sql12/squirrelcli/squirrelbatch.jsh
fi