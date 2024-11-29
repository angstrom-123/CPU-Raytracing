# CPU Raytracing
Toy CPU path tracer written entirely in Java.

## Features
- Physically based path tracing
- BVH optimisation using axis-aligned bounding boxes
- support for Lambertian diffuse, metallic, glass, and emissive materials
- .obj file format model importing (model and normals only)
- texture support for spheres (including custom images)

## Running
Requires an installation of at least JDK-21.

Executable .jar file can be found in the target directory.

**IMPORTANT : By default, renders are saved in the renders folder. For renders to be saved there you must be connected to the main project directory (not target) when running the .jar.**

## Config
**Samples:**
Samples per pixel shouldn't be increased past ~800 as there are diminishing returns beyond this. Even this is extremely high. 

**Max bounces:**
Maximum bounces experience diminishing returns above ~20. 50 is enough for high-res renders with little perceivable difference from 20.

**Multithreading:**
Preliminary tests have shown that, on powerful computers, thread count should be equal to the number of high-power CPU cores (such as Intel's "performance cores"). Weaker systems and laptops could experience best performance with just one core. 

**Tiling:**
Tile width should be left at auto to take up whole image as this improves performance due to better cache efficiency. Tile height has little effect on rendering speed.

![configMenu](https://github.com/user-attachments/assets/6a593dda-9089-4953-b2d3-25a90194f1bb)
## Demos
**Note: times to render are taken from my computer and are only useful as a comparison between demos.**

Samples per pixel: 500, Maximum bounces: 50, Time to render: 664s
![2702551944162722](https://github.com/user-attachments/assets/1ed8d316-9d30-4ce9-9dde-9c8fecdf50c8)

Samples per pixel: 500, Maximum bounces: 50, Time to render: 2854s
![35633903981623816](https://github.com/user-attachments/assets/300d88aa-fd66-4d28-b89b-0046dde0452d)

Samples per pixel: 500, Maximum bounces: 50, Time to render: 65s
![7559715668385661](https://github.com/user-attachments/assets/2d54ef4c-551b-4be9-a45a-6506f0882a25)

Samples per pixel: 500, Maximum bounces: 50, Time to render: 1279s
![9419044670518447](https://github.com/user-attachments/assets/1abba150-245b-4290-883f-dbf3e6c06671)

Samples per pixel: 500, Maximum bounces: 50, Time to render: 7245s
![6321357860854423](https://github.com/user-attachments/assets/0eb75c96-76ee-4b43-8adc-e7294201a02c)

## References
"Ray tracing in one weekend" and "Ray tracing the next week" : https://raytracing.github.io/
