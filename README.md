# Yuneec SDK Android example

[![Build Status](https://travis-ci.com/YUNEEC/Yuneec-SDK-Android-Example.svg?token=5772mkLLvKwYKBhk4s9n&branch=master)](https://travis-ci.com/YUNEEC/Yuneec-SDK-Android-Example)

## Overview

This is a proof-of-concept Android app built on the of the Yuneec SDK.

## Building

The project has been created using Android Studio 2.2.x.

### Checkout this repo

```
git clone git@github.com:YUNEEC/Yuneec-SDK-Android-Example-prerelease.git
git submodule update --init --recursive
```

## Description

The app uses the submodule [Yuneec-SDK-Android](https://github.com/YUNEEC/Yuneec-SDK-Android) which is an Android library module containing the C++ wrapper. The actual precompiled C++ library binary and header files are in yet another submodule called [Yuneec-SDK](https://github.com/YUNEEC/Yuneec-SDK).

The C++ wrapper uses JNI to communicate with the C++ library.

## Use the Yuneec software in the loop (SITL) simulation for testing

The Android app can be tested against the software simulation.

The SITL can be used on Linux or Mac. The use in a virtualization environment such as VirtualBox is possible but generally not recommended for performance reasons. A headless instance can be run inside a Docker container, as explained [here](sitl).

### Installing Gazebo7

#### Mac

Tested on macOS 10.11 and 10.12.

Make sure to have [homebrew](http://brew.sh).

```
brew cask install xquartz
brew tap px4/simulation
brew update
brew install gazebo7
```
OpenCV is required to run the simulation.
```
brew tap homebrew/science
brew install opencv
```

#### Ubuntu

Tested on Ubuntu 16.04.

```
sudo sh -c 'echo "deb http://packages.osrfoundation.org/gazebo/ubuntu-stable `lsb_release -cs` main" > /etc/apt/sources.list.d/gazebo-stable.list'
wget http://packages.osrfoundation.org/gazebo.key -O - | sudo apt-key add -
sudo apt-get update
sudo apt-get install gazebo7 libgazebo7-dev
```

Instructions taken from [gazebosim.org](http://gazebosim.org/tutorials?tut=install_ubuntu&ver=7.0&cat=install).

### Get the SITL simulation

Download the zip containing the SITL simulation for [Mac](https://s3.eu-central-1.amazonaws.com/08f61bbd-8958-433e-8e83-5d79160fa0be/sitl/latest/Yuneec-SITL-Simulation-macOS.zip) or [Linux](https://s3.eu-central-1.amazonaws.com/08f61bbd-8958-433e-8e83-5d79160fa0be/sitl/latest/Yuneec-SITL-Simulation-Linux.zip).

### Run the simulation

Use the included bash script to start the simulation environment:

```
cd Yuneec-SITL-simulation-xxx/
./typhoon_sitl.bash
```

To run it without simulation GUI, use:

```
HEADLESS=1 ./typhoon_sitl.bash
```

Sometimes the simulation hangs on startup. If it happens, press Ctrl+C in the console and try again.

To test if the simulation works, you can type `commander takeoff` or `commander land` in the `pxh>` shell.

### Use the Android emulator

#### Port fowarding

In order for the Android emulator in Android Studio to communicate with SITL, you need to redirect the UDP port between the host and the emulator.


First, get the auth token:
```
cat ~/.emulator_console_auth_token
```

Then use telnet to configure the emulator:
```
telnet localhost 5554
```

Copy the token found above it and paste it authenticate the telnet session:
```
auth <paste your token>
```

Then, set up the redirect:
```
redir add udp:14540:14540
```

You can now leave telnet:
```
quit
```

### Use a real Android device

By default, the SITL simulation will only try to connect on localhost but not to a phone/tablet on the network.

There are two options to connect to another IP:

1. Use broadcasting: This has the advantage that you don't need to determine the IP of the iOS device because it will pick it up automatically in the network. However, this can be a race if to devices are listening on the network.

2. Set the IP: This let's you choose the IP and avoid races for who gets to be the client.

#### Use broadcasting:

Start the SITL simuation and type the following in the `pxh>`:

```
param set MAV_BROADCAST 1
param save
```

#### Set IP:

Before starting the SITL simulation, open the file `posix-configs/SITL/init/ekf2/typhoon_h480` and look for this line:

```
mavlink start -u 14557 -r 4000000 -m custom -o 14540
```

then add the IP of the Android device in the local network with `-t`:
```
mavlink start -u 14557 -r 4000000 -m custom -o 14540 -t 192.168.0.X
```

## License

This example app is published under the [BSD 3-Clause license](LICENSE).

