#!/bin/sh
sudo sysctl kernel.nmi_watchdog=0
sudo perf stat -r 128 -d ./two
sudo sysctl kernel.nmi_watchdog=1
