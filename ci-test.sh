set -x

export xsbt="java -Xms128M -Xmx256M -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=256M  -jar sbtwrapper/sbt-launch.jar -Dsbt.log.noformat=true"
$xsbt clean test