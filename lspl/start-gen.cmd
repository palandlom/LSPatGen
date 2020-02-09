@echo on


set rez=".\examlple-gen\out.txt"
set text=".\examlple-gen\text.txt"
set templ=".\cdp\naryparticipation.lsp"
set TARGET=Space

.\bin\lspl-find.exe -i %text% -p %templ% -o %rez% %TARGET%

:============================================
set CP="D:\Lomov\_Laboratory\_GRANTS\myRFFI_2015\exper\lspl\ontExt\bin;D:\Lomov\_Laboratory\_GRANTS\myRFFI_2015\exper\lspl\ontExt\lib\logback-classic-1.0.13.jar;D:\Lomov\_Laboratory\_GRANTS\myRFFI_2015\exper\lspl\ontExt\lib\logback-core-1.0.13.jar;D:\Lomov\_Laboratory\_GRANTS\myRFFI_2015\exper\lspl\ontExt\lib\slf4j-api-1.7.5.jar"

:java -classpath %CP% ontExt.run  %rez% %text%
:java -classpath %CP% ontExt.start  %rez% %text%


