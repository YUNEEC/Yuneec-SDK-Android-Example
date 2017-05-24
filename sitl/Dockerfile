FROM ubuntu:16.04

RUN apt-get update
RUN apt-get install -y apt-utils
RUN apt-get install -y wget
RUN apt-get install -y unzip
RUN apt-get install -y lsb-release

RUN echo "deb http://packages.osrfoundation.org/gazebo/ubuntu-stable `lsb_release -cs` main" > /etc/apt/sources.list.d/gazebo-stable.list
RUN wget http://packages.osrfoundation.org/gazebo.key -O - | apt-key add -

RUN apt-get update
RUN apt-get install -y gazebo7 libgazebo7-dev

RUN wget https://s3.eu-central-1.amazonaws.com/08f61bbd-8958-433e-8e83-5d79160fa0be/sitl/latest/Yuneec-SITL-Simulation-Linux.zip -P /root/
RUN unzip /root/Yuneec-SITL-Simulation-Linux.zip -d /root/
RUN mkdir /root/posix-configs/SITL/init/ekf2
RUN cp /root/posix-configs/SITL/init/default/typhoon_h480 /root/posix-configs/SITL/init/ekf2/typhoon_h480

EXPOSE 14540
EXPOSE 14550

WORKDIR /root
CMD HEADLESS=1 /root/typhoon_sitl.bash
