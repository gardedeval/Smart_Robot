# Smart_Robot
Module 1
Getting direction from smartphone:
To control the orientation of robot we must have accurate orientation sense in smartphone.
To get orientation we make use of accelerometer and gyroscope which are built in Smart phones.
Gyroscopre gives rate of change of angle with respect to North and accelerometer gives change in
velocity i.e when phone is moving while controlling the robot. Generally Kalaman filter(hardware)
is used to take out exact position with respect to north from these two sensor values. But here as we
have android oprating system in smartphone many API's are available which make use of these
sensors and produce angle with respect to north. So have send this value to robot via socket
programmimg.
Module 2
Sending orientaion to robot.
For sending data from smartphone to robot we use socket programming. Robot has
raspberry Pi on it, which supports connection wifi. User Datagram Protocol(UDP) is used to
transfer orintation values of smartphone to raspberry pi. Raspberry Pi has UDP server running
which listens on port 22 and the UDP client which smartphone will connect to this server via port
22.
Module 3
Mapping orientation of smartphone to robot
Since robot has raspberry Pi, it supports GPIO programming. Raspberry Pi controlls motors
through motor driver IC ie. LM293. LM 293 provide power required to drive motors which
raspberry pi can't provide. On robot Gyroscope is connected to get values of robots actual
orientation. Which will later be compared with smartphone orientation and robot will be moved
accourdingly to match. On robot side raspberry pi can be programmed using python. Python
provide level of abstraction so that we need not work on hardware lavel to get values from
gyroscope. Gyroscope support I2C protocol and provides 3 values i.e Axis X, Y ,Z. Each one has 16
bits. Now there are 2 important parts: 1. Coverting X,Y,Z values to gyroscope angle with respect to
North.
2. Comparing this value and smartphone value to move robot in correct direction.
We calulate first part on some formule depending upon X and Y values.
After comparing values from robot and smartphone one important problem must be taken care of
i.e. When robot angle changes from 2->1->0 to 359-> 358->257 and vice versa. With conventional
algo robot will move in wrong direction as it produces false direction change. Here we make use of
the fact that robot will take atleast one reading while changing its direction by 180 and the
algorithm is modified accourdingly. If robot is made to match exactly with smartphone orientation
then it oscillates back and forth at that angle, so the window of 2-5 degrees is given, so that the
movement is smooth.
