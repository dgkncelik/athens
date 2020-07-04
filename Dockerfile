FROM python:3.7-stretch
RUN apt-get update
RUN apt-get install ant ivy
## PATCH JCC  cat jcc/setup.py | grep jvm ----> linux: /usr/lib/jvm/	java-1.8.0-openjdk-amd64
RUN apt-get install gcc build-essential
RUN python setup.py build
python setup.py install

# PATCH MAKEFILE
PREFIX_PYTHON=/usr
ANT=JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64 /usr/bin/ant
PYTHON=$(PREFIX_PYTHON)/local/bin/python
JCC=$(PYTHON) -m jcc --shared
NUM_FILES=10

export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
MAKE 
make test
make install
# pycharmda nasil kullanacagim bu memory indexeri linting calismiyor
# google -> pycharm compiled extensions