# MAVcomm

## Java based proxy/hub for PX4 companions

[![Build Status](https://travis-ci.org/ecmnet/MAVComm.svg?branch=master)](https://travis-ci.org/ecmnet/MAVComm) 

This Java based tool enables companions to be connected to PixHawk and serves as MAVLink proxy for QGC / MAVGC. Additionally MAVcomm can serve as central hub for distributed high-level flight control. 

It also provides a MAVLink parser and a flat data model to https://github.com/ecmnet/MAVGCL and to the visual odometry https://github.com/ecmnet/MAVSlam.

**Supported companion platforms:**

MAVComm supports any platform which is capable to run Java8 and can be connected to PixHawk or PixRacer via a high speed serial link. 

It is currently tested with the following systems:

- Raspberry PI 2/3  (requires  native high speed serial connection)

- [UP-Board](http://www.up-board.org) (no native lib for serial connection required)



