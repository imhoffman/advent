#!/bin/sh
sudo sysctl kernel.nmi_watchdog=0
sudo perf stat -r 4 -d $@
sudo sysctl kernel.nmi_watchdog=1
