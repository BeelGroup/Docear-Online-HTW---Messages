set -x

export xsbt="java -Xms600M -Xmx600M -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=600M  -jar sbtwrapper/sbt-launch.jar -Dsbt.log.noformat=true"
$xsbt clean test
