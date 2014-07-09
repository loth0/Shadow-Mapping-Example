Shadow-Mapping-Example
======================

Shadow Mapping using LWJGL and shaders

I've been looking for a working shadow mapping example in java with LWJGL and shaders for days without luck. Now, when I finally managed to make it using c++ tutorials I thought I could share the code. Here's an image of the result:
![shadow mapping](https://raw.githubusercontent.com/loth0/Shadow-Mapping-Example/master/shadow-example.png)

Used external libraries: LWJGL and slick-util (not sure if slick-util is necessary)

Disclamer: I'm an openGL beginner and the code probably isn't the best written but it works and could be used as a guideline.

Notice that the light is a point light and the shadows are made with orthogonal projection (used for directional light).


