#!/bin/sh

./build.pl web.stage \
	&& \
scp ./build/deploy/drools-web.tar.gz werken@drools.org:/home/groups/d/dr/drools \
	&& \
ssh werken@drools.org rm -Rf /home/groups/d/dr/drools/htdocs.old \
	&& \
ssh werken@drools.org mv /home/groups/d/dr/drools/htdocs /home/groups/d/dr/drools/htdocs.old \
	&& \
ssh werken@drools.org 'cd /home/groups/d/dr/drools && tar zxvf drools-web.tar.gz' \
	&& \
ssh werken@drools.org 'cd /home/groups/d/dr/drools && rm drools-web.tar.gz' 
