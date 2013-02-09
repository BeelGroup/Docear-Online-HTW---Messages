set -x

export xsbt="java -Xms100M -Xmx200M -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=128M  -jar sbtwrapper/sbt-launch.jar -Dsbt.log.noformat=true"
$xsbt clean test