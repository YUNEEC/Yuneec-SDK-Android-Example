# Running SITL in Docker
## Description
This is an example of running SITL headless in a Docker container.

## Building the docker image
From this directory, run:

    $ docker build -t yuneec-sitl .

An image called `yuneec-sitl` should be created. Note that the latest version of the SITL simulator is downloaded at that point.

## Running a SITL container
Run the following command:

    $ docker run -it yuneec-sitl

More details on how to use the SITL simulation can be found [here](https://github.com/YUNEEC/Yuneec-SDK-iOS-Example-prerelease#run-the-simulation).
